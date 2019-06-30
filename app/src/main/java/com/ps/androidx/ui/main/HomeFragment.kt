package com.ps.androidx.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ps.androidx.R
import com.ps.androidx.base.BaseFragment
import com.ps.androidx.data.model.unsplash.Unsplash
import com.ps.androidx.databinding.FragmentHomeBinding
import com.ps.androidx.di.ViewModelFactory
import com.ps.androidx.network.NetworkState
import com.ps.androidx.ui.main.adapter.UnsplashAdapter
import javax.inject.Inject


/**
 * Created by Prokash Sarkar on 5/20/2019.
 * https://github.com/prokash-sarkar
 */

class HomeFragment : BaseFragment(), UnsplashAdapter.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentHomeBinding

    private lateinit var movieAdapter: UnsplashAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViews()
        subscribeObservables()
    }

    override fun onItemClick(unsplash: Unsplash) {
        view?.findNavController()?.navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                        unsplash.id,
                        unsplash.likes.toString(),
                        unsplash.urls.regular
                )
        )
    }

    private fun prepareViews() {
        activity?.let {
            binding.rvImages.layoutManager = GridLayoutManager(activity, 2)
            movieAdapter = UnsplashAdapter(it.applicationContext, this)
            binding.rvImages.adapter = movieAdapter
        }
    }

    private fun subscribeObservables() {
        viewModel.observePagedUnplashData().removeObservers(viewLifecycleOwner)
        viewModel.observeNetworkState().removeObservers(viewLifecycleOwner)

        viewModel.observePagedUnplashData().observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.observeNetworkState().observe(this, Observer {
            movieAdapter.updateNetworkState(it.state)
        })

        binding.btnRetry.setOnClickListener { viewModel.retryFetchingUnsplashData() }
    }

    override fun onClickRetry() {
        viewModel.retryFetchingUnsplashData()
    }

    override fun onListUpdated(size: Int, networkState: NetworkState.State?) {
        updateProgressView(size, networkState)
        updateErrorsView(size, networkState)
    }

    private fun updateProgressView(size: Int, networkState: NetworkState.State?) {
        binding.pbLoading.visibility = if (size == 0 && networkState == NetworkState.State.LOADING)
            View.VISIBLE else View.GONE
    }

    private fun updateErrorsView(size: Int, networkState: NetworkState.State?) {
        binding.ivInfoImage.visibility = View.GONE
        binding.tvInfoText.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE

        if (size == 0) {
            when (networkState) {
                NetworkState.State.LOADING -> {
                    //
                }
                NetworkState.State.SUCCESS -> {
                    binding.ivInfoImage.visibility = View.GONE
                    binding.tvInfoText.visibility = View.VISIBLE
                    binding.tvInfoText.text = resources.getString(R.string.hint_no_result)
                    binding.btnRetry.visibility = View.GONE
                }
                NetworkState.State.ERROR -> {
                    binding.ivInfoImage.visibility = View.VISIBLE
                    binding.tvInfoText.visibility = View.VISIBLE
                    binding.tvInfoText.text = resources.getString(R.string.error_processing_request)
                    binding.btnRetry.visibility = View.VISIBLE
                }
            }
        }

    }

}
