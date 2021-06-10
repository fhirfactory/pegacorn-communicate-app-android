package im.vector.health.directory.shared

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.*
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

    fun firstLoadListSetup() {
        (listView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        listView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listView.layoutManager = LinearLayoutManager(requireContext())
        listView.adapter = listItemAdapter
        listView.setHasFixedSize(true)
        initializeList()
    }

    fun initializeList() {
        listView.scrollToPosition(0)

        loadNumber = 0
        listItemAdapter.setData(ArrayList())
        loading = false
        page = -1
        paginate()
        setupScrollListener()
    }

    override fun onResume() {
        super.onResume()
        listView.invalidate()
        listView.adapter = listItemAdapter
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
        val selectable = arguments?.getBoolean(SELECTABLE, false) ?: false
        listItemAdapter = constructAdapter(requireContext(),selectable)
        firstLoadListSetup()
    }

    var page = -1;
    var loading = false;
    var pageSize = 20;
    var loadNumber = 0

    fun paginate() {
        if (!loading) {

            val idx = listView.getChildAdapterPosition(listView.getChildAt(0))
            val overHalfway: Boolean = page < 0 || (idx >= (page * pageSize) / 2) || (idx == -1)

            if (overHalfway || listView.height == 0) {
                page += 1
                loading = true;
                loadNumber += 1
                val request = loadNumber
                context?.let {
                    if (!favourites) {
                        getData(page, pageSize, filter) { res, count ->
                            if (loadNumber != request || header == null) return@getData
                            res?.let {
                                setHeader(header, getHeaderText(count,favourites))
                                if (page == 0) listItemAdapter.setData(it) else listItemAdapter.addPage(it)
                                loading = false
                            }
                        }
                    } else {
                        getDataFavourites(page, pageSize, filter) { res, count ->
                            if (loadNumber != request || header == null) return@getDataFavourites
                            res?.let {
                                setHeader(header, getHeaderText(count,favourites))
                                if (page == 0) listItemAdapter.setData(it) else listItemAdapter.addPage(it)
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