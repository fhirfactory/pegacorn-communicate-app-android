package im.vector.health.directory.shared

import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.fragments.AbsHomeFragment
import kotlinx.android.synthetic.main.fragment_directory_list.*
import kotlinx.android.synthetic.main.fragment_directory_service.header
import org.matrix.androidsdk.data.Room


abstract class StandardDirectoryFragment<Adapter,ViewHolder : RecyclerView.ViewHolder?,Item> : BaseDirectoryFragment()
    where
    Adapter: RecyclerView.Adapter<ViewHolder>, Adapter: IStandardDirectoryAdapter<Item>
{
    companion object {
        internal const val SELECTABLE = "SELECTABLE"
    }

    internal lateinit var listItemAdapter: Adapter
    private var filter: String? = null
    private var favourites: Boolean = false

    override fun getLayoutResId() = R.layout.fragment_directory_list

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    override fun onFavorite(enable: Boolean) {
        favourites = enable
        initializeList()
    }

    override fun filter(with: String?) {
        if (filter != with) {
            filter = with
            initializeList()
        }
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: AbsHomeFragment.OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    fun initializeList() {
        val selectable = arguments?.getBoolean(SELECTABLE, false) ?: false
        listItemAdapter = constructAdapter(requireContext(),selectable)
        (listView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        listView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listView.layoutManager = LinearLayoutManager(requireContext())
        listView.adapter = listItemAdapter
        listView.setHasFixedSize(true)

        listView.scrollToPosition(0)

        loading = false
        page = -1
        paginate()
        setupScrollListener()
    }

    abstract fun constructAdapter(context: Context, selectable: Boolean):Adapter

    //Use this to do things like initializing the view model
    open fun completeActivityCreation() {

    }

    open fun getHeaderText(count: Int, favourites: Boolean): String = (if (favourites) "Favourites: " else "Results: ") + count.toString()

    abstract fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<Item>?, Int) -> Unit)
    abstract fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<Item>?, Int) -> Unit)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        completeActivityCreation()
        initializeList()
    }

    var page = -1;
    var loading = false;
    var pageSize = 20;

    fun paginate() {
        if (!loading) {

            val idx = listView.getChildAdapterPosition(listView.getChildAt(0))
            val overHalfway: Boolean = page < 0 || (idx >= (page * pageSize) / 2) || (idx == -1)

            if (overHalfway || listView.height == 0) {
                page += 1
                loading = true;
                context?.let {
                    if (!favourites) {
                        getData(page, pageSize, filter) { res, count ->
                            setHeader(header, getHeaderText(count,favourites))
                            res?.let {
                                listItemAdapter.addPage(it)
                                loading = false
                            }
                        }
                    } else {
                        getDataFavourites(page, pageSize, filter) { res, count ->
                            setHeader(header, getHeaderText(count,favourites))

                            res?.let {
                                listItemAdapter.addPage(it)
                                loading = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun setupScrollListener() {
        listView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                paginate()
            }
        })
    }
}