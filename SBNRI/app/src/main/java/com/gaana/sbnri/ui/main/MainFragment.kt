package com.gaana.sbnri.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaana.sbnri.R
import com.gaana.sbnri.data.model.OctokitModel
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


class MainFragment : Fragment() {

    var arrayListOctokitItems: ArrayList<OctokitModel> = ArrayList()

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView;
    private lateinit var containerView: View;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        containerView = inflater.inflate(R.layout.main_fragment, container, false)
        return containerView;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        recyclerView = containerView.findViewById<RecyclerView>(R.id.octokitRecyclerView)
        bindData()
        bindLoadMore()
        bindErrorHandler()
    }

    fun bindLoadMore() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                if(context!!.isConnected) {
                    if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == arrayListOctokitItems.size - 1) {
                        var factor: Int = 1;
                        if (arrayListOctokitItems.size > 0)
                            factor = arrayListOctokitItems.size / 5;

                        Snackbar.make(containerView, "Loading more data.....", Snackbar.LENGTH_LONG)
                            .show()
                        viewModel.loadFromNetwork(Integer(factor + 1))
                    }
                }
            }
        })
    }

    val Context.isConnected: Boolean
        get() {
            return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected == true
        }

    fun bindData() {
        if (context!!.isConnected)
            viewModel.loadFromNetwork(Integer(1))
        else
            viewModel.loadFromDatabase()

        viewModel.octokitList.observe(this, Observer { items ->
            arrayListOctokitItems.addAll(items)
            recyclerView.adapter =
                OctokitAdapter(context as Activity, arrayListOctokitItems as ArrayList<OctokitModel>)
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            (recyclerView.adapter as OctokitAdapter).notifyDataSetChanged()
        })
    }

    fun bindErrorHandler() {
        viewModel.errorData.observe(this, Observer { error ->
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage("API rate limit exceeded for your ip address. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.")
                .setCancelable(false)
            val alert = dialogBuilder.create()
            alert.setTitle("Error")
            alert.show()
        })
    }
}
