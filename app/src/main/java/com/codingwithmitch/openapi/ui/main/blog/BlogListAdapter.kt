package com.codingwithmitch.openapi.ui.main.blog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.*
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.util.DateUtils.Companion.convertLongToStringDate
import com.codingwithmitch.openapi.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*

class BlogListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TAG = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0
    private val NO_MORE_RESULTE_MARKER = BlogPost(
        NO_MORE_RESULTS,
        "",
        "",
        "",
        "",
        0,
        ""
    )


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {

        override fun areItemsTheSame(
            oldItem: BlogPost,
            newItem: BlogPost
        ): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(
            oldItem: BlogPost,
            newItem: BlogPost
        ): Boolean {
            return oldItem == newItem
        }
    }


    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    private inner class BlogRecyclerChangeCallback(
        private val adapter: BlogListAdapter
    ) : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyDataSetChanged()
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            notifyItemRangeChanged(position, count, payload)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {

            NO_MORE_RESULTS -> {
                return GenericViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(
                            R.layout.layout_no_more_results, parent, false
                        )
                )
            }

            BLOG_ITEM -> {
                return BogViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_blog_list_item, parent, false),
                    requestManager = requestManager,
                    interation = interaction
                )
            }

            else -> {
                return BogViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_blog_list_item, parent, false),
                    requestManager = requestManager,
                    interation = interaction
                )
            }

        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BogViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return BLOG_ITEM
        }

        return differ.currentList.get(position).pk //-1
    }


    fun submitList(list: List<BlogPost>?, isQueryExhausted: Boolean) {
        val newList = list?.toMutableList()
        if (isQueryExhausted) {
            newList?.add(NO_MORE_RESULTE_MARKER)
        }
        differ.submitList(list)
    }

    class BogViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interation: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlogPost) = with(itemView) {
            itemView.setOnClickListener {
                interation?.onItemSelected(adapterPosition, item)
            }

            requestManager.load(item.image)
                .transition(withCrossFade())
                .into(itemView.blog_image)

            itemView.blog_title.text = item.title
            itemView.blog_author.text = item.username
            itemView.blog_update_date.text = convertLongToStringDate(item.date_updated)

        }

    }


    interface Interaction {
        fun onItemSelected(position: Int, item: BlogPost)
    }


}