package com.nenad.favrecipes.view.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nenad.favrecipes.R
import com.nenad.favrecipes.adapter.RecipesAdapter
import com.nenad.favrecipes.databinding.FragmentSearchBinding
import com.nenad.favrecipes.utils.NetworkListener
import com.nenad.favrecipes.utils.NetworkResult
import com.nenad.favrecipes.utils.observeOnce
import com.nenad.favrecipes.viewmodels.MainViewModel
import com.nenad.favrecipes.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment(), android.widget.SearchView.OnQueryTextListener {

    private lateinit var mBinding: FragmentSearchBinding




    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel

    private val args by navArgs<SearchFragmentArgs>()

    private lateinit var networkListener: NetworkListener



    private val mAdapter by lazy {
        RecipesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }



    @InternalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        recipeViewModel.readBackOnlineStatus.observe(viewLifecycleOwner, {
            recipeViewModel.backOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", "$status")
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    loadRecipes()
                }
        }

        mBinding.filterRecipesFab.setOnClickListener {
            if (recipeViewModel.networkStatus) {
                findNavController().navigate(R.id.action_searchFragment_to_recipesBottomSheet)
            } else {
                recipeViewModel.showNetworkStatus()
            }
        }


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
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
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

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { rows ->
                if (rows.isNotEmpty()) {
                    mAdapter.setData(rows[0].foodRecipe)
                }
            })
        }
    }
    private fun loadRecipes() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { rows ->
                if (rows.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "readRecipes() called!")

                    // must have one row only.
                    mAdapter.setData(rows[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }


    /** request api data */
    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData() called!")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                    recipeViewModel.saveMealAndDietType()
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
    override fun onDestroyView() {
        super.onDestroyView()
        // to avoid memory leak


    }



    override fun onQueryTextChange(newText: String?): Boolean {
        // Do not use this function to reduce api request calls.
        return true
    }


}


