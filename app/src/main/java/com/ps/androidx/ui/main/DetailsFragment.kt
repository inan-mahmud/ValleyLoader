package com.ps.androidx.ui.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.ps.androidx.R
import com.ps.androidx.base.BaseFragment
import com.ps.androidx.databinding.FragmentDetailsBinding
import com.ps.valleyloader.listener.ValleyLoaderListener
import com.ps.valleyloader.utils.ValleyLoader
import timber.log.Timber


/**
 * Created by Prokash Sarkar on 5/20/2019.
 * https://github.com/prokash-sarkar
 */

class DetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsBinding

    private lateinit var args: DetailsFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = DetailsFragmentArgs.fromBundle(arguments!!)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.tvLikes.text = "${args.likes} Likes"

        /*Glide.with(context!!)
                .load(args.url)
                .into(binding.ivPreview)*/

        ValleyLoader.getInstance(context!!)
                .download(args.url, object : ValleyLoaderListener {
                    override fun onByteSuccess(loader: ValleyLoader, byteArray: ByteArray, url: String) {
                        binding.pbLoading.visibility = View.GONE

                        val bitmap = BitmapFactory.decodeStream(byteArray.inputStream())
                        binding.ivPreview.setImageBitmap(bitmap)
                    }

                    override fun onByteFailure(loader: ValleyLoader, url: String) {
                        Timber.e("Failed to load $url")
                        binding.pbLoading.visibility = View.GONE
                    }
                })
    }

}
