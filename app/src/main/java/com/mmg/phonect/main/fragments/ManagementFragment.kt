package com.mmg.phonect.main.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import androidx.core.graphics.ColorUtils
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.models.Location
import com.mmg.phonect.common.utils.DisplayUtils
import com.mmg.phonect.databinding.FragmentManagementBinding
import com.mmg.phonect.main.MainActivityViewModel
import com.mmg.phonect.main.adapters.LocationAdapterAnimWrapper
import com.mmg.phonect.main.adapters.location.LocationAdapter
import com.mmg.phonect.main.utils.MainThemeColorProvider
//import com.mmg.phonect.main.widgets.LocationItemTouchCallback.TouchReactor
import kotlin.math.max
import kotlin.math.min

class PushedManagementFragment: ManagementFragment() {

    companion object {
        @JvmStatic
        fun getInstance() = PushedManagementFragment()
    }

    override fun setSystemBarStyle() {
        DisplayUtils.setSystemBarStyle(
            requireContext(),
            requireActivity().window,
            false,
            !DisplayUtils.isDarkMode(requireContext()),
            true,
            !DisplayUtils.isDarkMode(requireContext())
        )
    }
}

open class ManagementFragment : MainModuleFragment() {

    private lateinit var binding: FragmentManagementBinding
    protected lateinit var viewModel: MainActivityViewModel

    private lateinit var layout: LinearLayoutManager
    private lateinit var adapter: LocationAdapter
    private var adapterAnimWrapper: LocationAdapterAnimWrapper? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    private var scrollOffset = 0f
    private var callback: Callback? = null

    interface Callback {
//        fun onSearchBarClicked(searchBar: View)
        fun onSelectProviderActivityStarted()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentManagementBinding.inflate(layoutInflater, container, false)

        initModel()
        initView()
        setCallback(requireActivity() as Callback)

        return binding.root
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
//        if (enter && nextAnim != 0 && adapterAnimWrapper != null) {
//            adapterAnimWrapper!!.setLastPosition(-1)
//        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun setSystemBarStyle() {
        // do nothing.
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateDayNightColors()

        val firstHolderPosition = layout.findFirstVisibleItemPosition()
        adapter.notifyItemRangeChanged(
            firstHolderPosition,
            layout.findLastVisibleItemPosition() - firstHolderPosition + 1
        )
    }

    private fun initModel() {
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
    }

    private fun initView() {
        updateDayNightColors()

//        binding.searchBar.setOnClickListener {
//            if (callback != null) {
//                callback!!.onSearchBarClicked(binding.searchBar)
//            }
//        }


//        binding.currentLocationButton.setOnClickListener {
//            viewModel.addLocation(buildLocal(), null)
//            SnackbarHelper.showSnackbar(getString(R.string.feedback_collect_succeed))
//        }

//        adapter =
//            PhoneAdapter(
//                requireActivity(),
//                ArrayList(),
//                null,
//                { _, formattedId ->  // on click.
//                    viewModel.setLocation(formattedId)
//                    parentFragmentManager.popBackStack()
//                }
//            ) { holder ->
//                itemTouchHelper.startDrag(holder)
//            }
//        adapterAnimWrapper = LocationAdapterAnimWrapper(requireContext(), adapter)
//        adapterAnimWrapper!!.setLastPosition(Int.MAX_VALUE)
//        binding.recyclerView.adapter = adapterAnimWrapper
//        binding.recyclerView.layoutManager = LinearLayoutManager(
//            requireActivity(),
//            RecyclerView.VERTICAL,
//            false
//        ).also { layout = it }
//        while (binding.recyclerView.itemDecorationCount > 0) {
//            binding.recyclerView.removeItemDecorationAt(0)
//        }
//        binding.recyclerView.addItemDecoration(Material3ListItemDecoration(requireContext()))
//        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                scrollOffset = recyclerView.computeVerticalScrollOffset().toFloat()
//                updateAppBarColor()
//
//                if (dy != 0) {
//                    adapterAnimWrapper!!.setScrolled()
//                }
//            }
//        })
//
//        itemTouchHelper = ItemTouchHelper(
//            LocationItemTouchCallback(
//                requireActivity() as GeoActivity,
//                viewModel,
//                this
//            )
//        )
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
//
//        viewModel.totalLocationList.observe(viewLifecycleOwner) {
//            adapter.update(it.locationList, it.selectedId)
//            setCurrentLocationButtonEnabled(it.locationList)
//        }
    }

    private fun updateDayNightColors() {
        val lightTheme = !DisplayUtils.isDarkMode(requireContext())

        updateAppBarColor()

        binding.recyclerView.setBackgroundColor(
            MainThemeColorProvider.getColor(
                lightTheme = lightTheme,
                id = R.attr.colorSurfaceVariant
            )
        )
        binding.searchBar.setCardBackgroundColor(
            MainThemeColorProvider.getColor(
                lightTheme = lightTheme,
                id = R.attr.colorSurface
            )
        )

//        ImageViewCompat.setImageTintList(
//            binding.searchIcon,
//            ColorStateList.valueOf(
//                MainThemeColorProvider.getColor(
//                    lightTheme = lightTheme,
//                    id = R.attr.colorBodyText
//                )
//            )
//        )
        ImageViewCompat.setImageTintList(
            binding.currentLocationButton,
            ColorStateList.valueOf(
                MainThemeColorProvider.getColor(
                    lightTheme = lightTheme,
                    id = R.attr.colorPrimary
                )
            )
        )
//        binding.title.setTextColor(
//            MainThemeColorProvider.getColor(
//                lightTheme = lightTheme,
//                id = R.attr.colorBodyText
//            )
//        )
    }

    private fun updateAppBarColor() {
        val lightTheme = !DisplayUtils.isDarkMode(requireContext())
        val ratio = max(
            0f,
            min(
                scrollOffset / binding.appBar.height,
                1f
            )
        )
        binding.appBar.setBackgroundColor(
            DisplayUtils.blendColor(
                ColorUtils.setAlphaComponent(
                    MainThemeColorProvider.getColor(
                        lightTheme = lightTheme,
                        id = R.attr.colorPrimary
                    ),
                    (255 * 0.2 * ratio).toInt()
                ),
                MainThemeColorProvider.getColor(
                    lightTheme = lightTheme,
                    id = R.attr.colorSurfaceVariant
                )
            )
        )
    }

    private fun setCurrentLocationButtonEnabled(list: List<Location>) {
        var enabled = list.isNotEmpty()
        for (i in list.indices) {
            if (list[i].isCurrentPosition) {
                enabled = false
                break
            }
        }

        binding.currentLocationButton.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    fun prepareReenterTransition() {
        postponeEnterTransition()

        binding.searchBar.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.searchBar.viewTreeObserver.removeOnPreDrawListener(this)
                    startPostponedEnterTransition()
                    return true
                }
            }
        )
    }

    // interface.
    private fun setCallback(l: Callback?) {
        callback = l
    }

    // location item touch reactor.
//    override fun resetViewHolderAt(position: Int) {
//        adapter.notifyItemChanged(position)
//    }
//
//    override fun reorderByDrag(from: Int, to: Int) {
//        adapter.update(from, to)
//    }
//
//    override fun startSelectProviderActivityBySwipe() {
//        if (callback != null) {
//            callback!!.onSelectProviderActivityStarted()
//        }
//    }
}