package com.nenad.favrecipes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nenad.favrecipes.R
import com.nenad.favrecipes.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

  private lateinit var mBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)


       mBinding.shimmerRv.showShimmer()

       return mBinding.root
    }


}