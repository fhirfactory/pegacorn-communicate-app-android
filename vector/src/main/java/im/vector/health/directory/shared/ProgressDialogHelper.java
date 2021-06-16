package im.vector.health.directory.shared;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import javax.inject.Inject;

import im.vector.R;

public class ProgressDialogHelper {
    private Dialog overlayDialog;

    @Inject
    public ProgressDialogHelper() {

    }

    public void showDialog(Context context) {
        if (overlayDialog == null) {
            overlayDialog = new Dialog(context, android.R.style.Theme_Panel);
            overlayDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            View v = overlayDialog.getLayoutInflater().inflate(R.layout.dialog_progress_view,null);
            overlayDialog.addContentView(v,lp);
        }
        overlayDialog.show();
    }

    public void hideDialog() {
        if (overlayDialog == null) {
            return;
        }
        overlayDialog.cancel();
    }
}
