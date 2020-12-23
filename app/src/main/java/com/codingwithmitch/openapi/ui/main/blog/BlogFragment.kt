package com.codingwithmitch.openapi.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.models.BlogPost
import com.codingwithmitch.openapi.ui.DataState
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState
import com.codingwithmitch.openapi.ui.main.blog.viewmodel.*
import com.codingwithmitch.openapi.util.ErrorHandling
import com.codingwithmitch.openapi.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*
import javax.inject.Inject

class BlogFragment : BaseBlogFragment(), BlogListAdapter.Interaction {


    lateinit var recyclerAdapter: BlogListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goViewBlogFragment.setOnClickListener {
            findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
        }

        subscribeObserver()
        initRecyclerView()

        if (savedInstanceState == null) {
            viewModel.loadFirstPage()
        }

    }

    private fun subscribeObserver() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "BlogFragment: subscribeObserver: viewState ${it}")
            if (it != null) {
                recyclerAdapter.submitList(
                    list = it.blogFields.blogList,
                    isQueryExhausted = it.blogFields.isQueryExhausted
                )
            }
        })


    }

    private fun handlePagination(dataState: DataState<BlogViewState>) {

        //handle incomming data from dataState

        dataState.data?.let {
            it.data?.let { event ->
                event.getContentIfNotHandled()?.let {
                    Log.d(TAG, "BlogFragment: subscribeObserver: dataState ${dataState}")
                    // viewModel.setBlogListData(it.blogFields.blogList)

                    viewModel.handleIncommingBlogListData(it)

                }
            }
        }

        //check paginatin for end(ex:"no nore results")
        //must do this b/c server will return Api Error Response if page is not valid
        //-> mean no more data1!

        dataState.error?.let { event ->
            event.peekContent().response.message?.let {
                if (ErrorHandling.isPaginationDone(it)) {
                    //handle the error  message event so it doesn't play on ui
                    event.getContentIfNotHandled()

                    //setQueryExhausted to update recyclerview with
                    //no more results
                    viewModel.setQueryExhausted(true)
                }
            }


        }

    }


    private fun initRecyclerView() {

        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingItemDecoration)
            addItemDecoration(topSpacingItemDecoration)
            recyclerAdapter = BlogListAdapter(
                requestManager,
                this@BlogFragment
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting toload next pae... ")
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }

    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_blogFragment_to_viewBlogFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        blog_post_recyclerview.adapter = null
    }

}