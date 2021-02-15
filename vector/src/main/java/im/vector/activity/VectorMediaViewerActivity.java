/*
 * Copyright 2014 OpenMarket Ltd
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonElement;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.MXMediaDownloadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import im.vector.BuildConfig;
import im.vector.Matrix;
import im.vector.R;
import im.vector.VectorApp;
import im.vector.adapters.VectorMediaViewerAdapter;
import im.vector.adapters.VectorRoomsSelectionAdapter;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.SlidableMediaInfo;

import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_CANONICAL_ALIAS;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_PINNED_EVENT;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_RELATED_GROUPS;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_ALIASES;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_AVATAR;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_CREATE;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_GUEST_ACCESS;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_MEMBER;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_NAME;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_TOMBSTONE;
import static org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_STATE_ROOM_TOPIC;

/**
 * Display a medias list.
 */
public class VectorMediaViewerActivity extends MXCActionBarActivity {
    private static final String LOG_TAG = VectorMediaViewerActivity.class.getSimpleName();

    public static final String KEY_INFO_LIST = "ImageSliderActivity.KEY_INFO_LIST";
    public static final String KEY_INFO_LIST_INDEX = "ImageSliderActivity.KEY_INFO_LIST_INDEX";

    public static final String KEY_THUMBNAIL_WIDTH = "ImageSliderActivity.KEY_THUMBNAIL_WIDTH";
    public static final String KEY_THUMBNAIL_HEIGHT = "ImageSliderActivity.KEY_THUMBNAIL_HEIGHT";

    public static final String EXTRA_MATRIX_ID = "ImageSliderActivity.EXTRA_MATRIX_ID";
    public static final String SELECTED_EVENT = "SELECTED_EVENT";
    public static final String SELECTED_ROOM_TO_FORWARD = "SELECTED_ROOM_TO_FORWARD";
    public static final String SELECTED_EVENT_TO_TAG = "SELECTED_EVENT_TO_TAG";

    // session
    private MXSession mSession;

    // the pager
    private ViewPager mViewPager;

    // the pager adapter
    private VectorMediaViewerAdapter mAdapter;

    // the medias list
    private List<SlidableMediaInfo> mMediasList;

    // Pending data during permission request
    private int mPendingPosition;
    private int mPendingAction;

    // the slide effect
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_vector_media_viewer;
    }

    @Override
    public void initUiAndData() {
        configureToolbar();

        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.d(LOG_TAG, "onCreate : restart the application");
            CommonActivityUtils.restartApp(this);
            return;
        }

        if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
            return;
        }

        String matrixId = null;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MATRIX_ID)) {
            matrixId = intent.getStringExtra(EXTRA_MATRIX_ID);
        }

        mSession = Matrix.getInstance(getApplicationContext()).getSession(matrixId);

        if ((null == mSession) || !mSession.isAlive()) {
            finish();
            Log.d(LOG_TAG, "onCreate : invalid session");
            return;
        }

        mMediasList = (List<SlidableMediaInfo>) intent.getSerializableExtra(KEY_INFO_LIST);

        if ((null == mMediasList) || (0 == mMediasList.size())) {
            finish();
            return;
        }

        mViewPager = findViewById(R.id.view_pager);

        int position = Math.min(intent.getIntExtra(KEY_INFO_LIST_INDEX, 0), mMediasList.size() - 1);
        int maxImageWidth = intent.getIntExtra(KEY_THUMBNAIL_WIDTH, 0);
        int maxImageHeight = intent.getIntExtra(VectorMediaViewerActivity.KEY_THUMBNAIL_HEIGHT, 0);

        mAdapter = new VectorMediaViewerAdapter(this, mSession, mSession.getMediaCache(), mMediasList, maxImageWidth, maxImageHeight);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mAdapter.autoPlayItemAt(position);
        mViewPager.setCurrentItem(position);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(mMediasList.get(position).mFileName);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (null != getSupportActionBar()) {
                    getSupportActionBar().setTitle(mMediasList.get(position).mFileName);
                }

                // disable shared for encrypted files as they are saved in a tmp folder
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop any playing video
        mAdapter.stopPlayingVideo();
    }

    @Override
    public int getMenuRes() {
        return R.menu.vector_medias_viewer;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // the application is in a weird state
        if (CommonActivityUtils.shouldRestartApp(this)) {
            return false;
        }

        MenuItem shareMenuItem = menu.findItem(R.id.ic_action_share);
        if (null != shareMenuItem) {
            // disable shared for encrypted files as they are saved in a tmp folder
            shareMenuItem.setVisible(null == mMediasList.get(mViewPager.getCurrentItem()).mEncryptedFileInfo && getResources().getBoolean(R.bool.show_image_share_items));
        }
        MenuItem deleteMenuItem = menu.findItem(R.id.ic_action_delete);
        if (null != deleteMenuItem) {
            // disable shared for encrypted files as they are saved in a tmp folder
            deleteMenuItem.setVisible(mMediasList.get(mViewPager.getCurrentItem()).event != null && isEventRemovable(mMediasList.get(mViewPager.getCurrentItem()).event));
        }
        menu.findItem(R.id.ic_action_download).setVisible(getResources().getBoolean(R.bool.show_image_share_items));
        return true;
    }

    /**
     * Download the current video file
     */
    private void onAction(final int position, final int action) {
        final MXMediaCache mediasCache = Matrix.getInstance(this).getMediaCache();
        final SlidableMediaInfo mediaInfo = mMediasList.get(position);

        // check if the media has already been downloaded
        if (mediasCache.isMediaCached(mediaInfo.mMediaUrl, mediaInfo.mMimeType)) {
            mediasCache.createTmpDecryptedMediaFile(mediaInfo.mMediaUrl, mediaInfo.mMimeType, mediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                @Override
                public void onSuccess(File file) {
                    // sanity check
                    if (null == file) {
                        return;
                    }

                    if (action == R.id.ic_action_download) {
                        if (checkWritePermission(PermissionsToolsKt.PERMISSION_REQUEST_CODE)) {
                            CommonActivityUtils.saveMediaIntoDownloads(VectorMediaViewerActivity.this,
                                    file, mediaInfo.mFileName, mediaInfo.mMimeType, new SimpleApiCallback<String>() {
                                        @Override
                                        public void onSuccess(String savedMediaPath) {
                                            Toast.makeText(VectorApp.getInstance(), getText(R.string.media_slider_saved), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            mPendingPosition = position;
                            mPendingAction = action;
                        }
                    } else {
                        // Move the file to the Share folder, to avoid it to be deleted because the Activity will be paused while the
                        // user select an application to share the file
                        if (null != mediaInfo.mFileName) {
                            file = mediasCache.moveToShareFolder(file, mediaInfo.mFileName);
                        } else {
                            file = mediasCache.moveToShareFolder(file, file.getName());
                        }

                        // shared / forward
                        Uri mediaUri = null;
                        try {
                            mediaUri = FileProvider.getUriForFile(VectorMediaViewerActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "onMediaAction Selected File cannot be shared " + e.getMessage(), e);
                        }

                        if (null != mediaUri) {
                            try {
                                final Intent sendIntent = new Intent();
                                // Grant temporary read permission to the content URI
                                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.setType(mediaInfo.mMimeType);
                                sendIntent.putExtra(Intent.EXTRA_STREAM, mediaUri);
                                startActivity(sendIntent);
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "## onAction : cannot display the media " + mediaUri + " mimeType " + mediaInfo.mMimeType, e);
                                Toast.makeText(VectorMediaViewerActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        } else {
            // else download it
            final String downloadId = mediasCache.downloadMedia(this,
                    mSession.getHomeServerConfig(),
                    mediaInfo.mMediaUrl,
                    mediaInfo.mMimeType,
                    mediaInfo.mEncryptedFileInfo);

            if (null != downloadId) {
                mediasCache.addDownloadListener(downloadId, new MXMediaDownloadListener() {
                    @Override
                    public void onDownloadError(String downloadId, JsonElement jsonElement) {
                        MatrixError error = JsonUtils.toMatrixError(jsonElement);

                        if ((null != error) && error.isSupportedErrorCode()) {
                            Toast.makeText(VectorMediaViewerActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDownloadComplete(String aDownloadId) {
                        if (aDownloadId.equals(downloadId)) {
                            onAction(position, action);
                        }
                    }
                });
            }
        }
    }

    private boolean isEventRemovable(Event event) {
        // test if the event can be redacted
        boolean canBeRedacted = !TextUtils.equals(event.getType(), Event.EVENT_TYPE_MESSAGE_ENCRYPTION);

        if (canBeRedacted) {
            // oneself message -> can redact it
            if (TextUtils.equals(event.sender, mSession.getMyUserId())) {
                canBeRedacted = true;
            } else {
                // need the minimum power level to redact an event
                Room room = mSession.getDataHandler().getRoom(event.roomId);

                if ((null != room) && (null != room.getState().getPowerLevels())) {
                    PowerLevels powerLevels = room.getState().getPowerLevels();
                    canBeRedacted = powerLevels.getUserPowerLevel(mSession.getMyUserId()) >= powerLevels.redact;
                }
            }
        }

        //Hiding the "Remove" option from the system messages
        if (getResources().getBoolean(R.bool.hide_remove_from_system_message) && (event.getType().equals(EVENT_TYPE_STATE_ROOM_NAME) || event.getType().equals(EVENT_TYPE_STATE_ROOM_TOPIC) || event.getType().equals(EVENT_TYPE_STATE_ROOM_AVATAR)
                || event.getType().equals(EVENT_TYPE_STATE_ROOM_MEMBER) || event.getType().equals(EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE) || event.getType().equals(EVENT_TYPE_STATE_ROOM_CREATE)
                || event.getType().equals(EVENT_TYPE_STATE_ROOM_JOIN_RULES) || event.getType().equals(EVENT_TYPE_STATE_ROOM_GUEST_ACCESS) || event.getType().equals(EVENT_TYPE_STATE_ROOM_POWER_LEVELS)
                || event.getType().equals(EVENT_TYPE_STATE_ROOM_ALIASES) || event.getType().equals(EVENT_TYPE_STATE_ROOM_TOMBSTONE) || event.getType().equals(EVENT_TYPE_STATE_CANONICAL_ALIAS)
                || event.getType().equals(EVENT_TYPE_STATE_HISTORY_VISIBILITY) || event.getType().equals(EVENT_TYPE_STATE_RELATED_GROUPS) || event.getType().equals(EVENT_TYPE_STATE_PINNED_EVENT))) {
            canBeRedacted = false;
        }
        return canBeRedacted;
    }

    private void showRoomListToForward() {
        // sanity check
        if ((null == mSession) || !mSession.isAlive() || isFinishing()) {
            return;
        }

        List<RoomSummary> mergedSummaries = new ArrayList<>(mSession.getDataHandler().getStore().getSummaries());

        // keep only the joined room
        for (int index = 0; index < mergedSummaries.size(); index++) {
            RoomSummary summary = mergedSummaries.get(index);
            Room room = mSession.getDataHandler().getRoom(summary.getRoomId());

            if (room.isInvited() || room.isConferenceUserRoom()) {
                mergedSummaries.remove(index);
                index--;
            }
        }

        Collections.sort(mergedSummaries, new Comparator<RoomSummary>() {
            @Override
            public int compare(RoomSummary lhs, RoomSummary rhs) {
                if (lhs == null || lhs.getLatestReceivedEvent() == null) {
                    return 1;
                } else if (rhs == null || rhs.getLatestReceivedEvent() == null) {
                    return -1;
                }

                if (lhs.getLatestReceivedEvent().getOriginServerTs() > rhs.getLatestReceivedEvent().getOriginServerTs()) {
                    return -1;
                } else if (lhs.getLatestReceivedEvent().getOriginServerTs() < rhs.getLatestReceivedEvent().getOriginServerTs()) {
                    return 1;
                }
                return 0;
            }
        });

        VectorRoomsSelectionAdapter adapter = new VectorRoomsSelectionAdapter(this, R.layout.adapter_item_vector_recent_room, mSession);
        adapter.addAll(mergedSummaries);

        final List<RoomSummary> fMergedSummaries = mergedSummaries;

        new AlertDialog.Builder(this)
                .setTitle(R.string.send_files_in)
                .setNegativeButton(R.string.cancel, null)
                .setAdapter(adapter,
                        (dialog, which) -> {
                            dialog.dismiss();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RoomSummary summary = fMergedSummaries.get(which);
                                    Intent intent = new Intent();
                                    intent.putExtra(SELECTED_EVENT, mMediasList.get(mViewPager.getCurrentItem()).event);
                                    intent.putExtra(SELECTED_ROOM_TO_FORWARD, summary);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_action_share:
            case R.id.ic_action_download:
                onAction(mViewPager.getCurrentItem(), item.getItemId());
                return true;
            case R.id.ic_action_delete:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.redact) + " ?")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok,
                                (dialog, id) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra(SELECTED_EVENT, mMediasList.get(mViewPager.getCurrentItem()).event);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            case R.id.ic_action_forward:
                showRoomListToForward();
                return true;
            case R.id.ic_action_tag:
                Intent intent = new Intent();
                intent.putExtra(SELECTED_EVENT, mMediasList.get(mViewPager.getCurrentItem()).event);
                intent.putExtra(SELECTED_EVENT_TO_TAG, true);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkWritePermission(int requestCode) {
        return PermissionsToolsKt.checkPermissions(PermissionsToolsKt.PERMISSIONS_FOR_WRITING_FILES, this, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionsToolsKt.allGranted(grantResults)) {
            if (requestCode == PermissionsToolsKt.PERMISSION_REQUEST_CODE) {
                // Request comes from here
                onAction(mPendingPosition, mPendingAction);
            }
        }
    }
}
