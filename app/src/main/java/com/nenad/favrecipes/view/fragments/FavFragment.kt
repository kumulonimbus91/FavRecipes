package com.nenad.favrecipes.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nenad.favrecipes.R
import com.nenad.favrecipes.databinding.FragmentFavBinding

class FavFragment : Fragment() {

    private lateinit var mBinding: FragmentFavBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fav, container, false)


        return mBinding.root

    }


}