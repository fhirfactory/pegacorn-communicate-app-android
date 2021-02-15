package im.vector.gallery

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.fragments.AbsHomeFragment
import im.vector.ui.themes.ThemeUtils
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.matrix.androidsdk.data.Room

class GalleryFragment : AbsHomeFragment(), GalleryItemClickListener {

    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var viewModel: GalleryFragmentViewModel
    override fun getLayoutResId() = R.layout.fragment_gallery

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            mPrimaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home_secondary)

            mFabColor = ContextCompat.getColor(activity, R.color.tab_people)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_people_secondary)
        }

        viewModel = ViewModelProviders.of(this).get(GalleryFragmentViewModel::class.java)
        subscribeUI()

        galleryAdapter = GalleryAdapter(requireContext(), this)
        (galleryRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        galleryRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        galleryRecyclerview.adapter = galleryAdapter
        galleryRecyclerview.setHasFixedSize(true)
    }

    private fun subscribeUI() {
        viewModel.galleryItems.observe(viewLifecycleOwner, Observer {
            galleryAdapter.setData(it)
        })
        viewModel.getData()
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    override fun onRoleClick(galleryItem: DummyGalleryItem) {

    }
}