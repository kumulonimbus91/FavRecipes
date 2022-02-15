package com.nenad.favrecipes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nenad.favrecipes.MyApplication
import com.nenad.favrecipes.R
import com.nenad.favrecipes.adapter.RecipesAdapter
import com.nenad.favrecipes.data.RemoteDataSource
import com.nenad.favrecipes.data.Repository
import com.nenad.favrecipes.databinding.FragmentSearchBinding
import com.nenad.favrecipes.network.ApiServiceInt
import com.nenad.favrecipes.utils.NetworkResult
import com.nenad.favrecipes.viewmodels.MainViewModel
import com.nenad.favrecipes.viewmodels.RecipeViewModel
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var mBinding: FragmentSearchBinding

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel

    private val mAdapter by lazy {
        RecipesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)


        mBinding.shimmerRv.showShimmer()

        setHasOptionsMenu(true)
        setupRecyclerView()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mainViewModel

        mBinding.lifecycleOwner = this





    }

    private fun setupRecyclerView() {
        mBinding.shimmerRv.adapter = mAdapter
        mBinding.shimmerRv.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    /** show shimmer recycler effect */
    private fun showShimmerEffect() {
       mBinding.shimmerRv.showShimmer()
    }

    /** hide shimmer recycler effect */
    private fun hideShimmerEffect() {
        mBinding.shimmerRv.hideShimmer()
    }
    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipeViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }
    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { rows ->
                if (rows.isNotEmpty()) {
                    mAdapter.setData(rows[0].foodRecipe)
                }
            })
        }
    }


}