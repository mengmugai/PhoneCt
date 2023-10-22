package com.mmg.phonect.main.fragments

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.GeoActivity
import com.mmg.phonect.common.basic.livedata.EqualtableLiveData
import com.mmg.phonect.common.basic.models.Phone
import com.mmg.phonect.common.ui.widgets.SwipeSwitchLayout
import com.mmg.phonect.common.ui.widgets.SwipeSwitchLayout.OnSwitchListener
import com.mmg.phonect.databinding.FragmentHomeBinding
import com.mmg.phonect.main.MainActivityViewModel
import com.mmg.phonect.main.adapters.main.MainAdapter
import com.mmg.phonect.main.layouts.MainLayoutManager
import com.mmg.phonect.main.utils.MainModuleUtils
import com.mmg.phonect.main.utils.MainThemeColorProvider
import com.mmg.phonect.settings.SettingsManager
import com.mmg.phonect.theme.ThemeManager
import com.mmg.phonect.theme.resource.ResourcesProviderFactory
import com.mmg.phonect.theme.resource.providers.ResourceProvider
import com.mmg.phonect.theme.weatherView.WeatherView
import com.mmg.phonect.theme.weatherView.WeatherViewController

class HomeFragment : MainModuleFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var weatherView: WeatherView

    private var adapter: MainAdapter? = null
    private var scrollListener: OnScrollListener? = null
    private var recyclerViewAnimator: Animator? = null
    private var resourceProvider: ResourceProvider? = null

    private val previewOffset = EqualtableLiveData(0)
    private var callback: Callback? = null

    interface Callback {
        fun onManageIconClicked()
        fun onSettingsIconClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initModel()

        // attach weather view.
        weatherView = ThemeManager
            .getInstance(requireContext())
            .phoneCtThemeDelegate
            .getWeatherView(requireContext())
        (binding.switchLayout.parent as CoordinatorLayout).addView(
            weatherView as View,
            0,
            CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        initView()
        setCallback(requireActivity() as Callback)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        weatherView.setDrawable(!isHidden)
    }

    override fun onPause() {
        super.onPause()
        weatherView.setDrawable(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding.recyclerView.clearOnScrollListeners()
        scrollListener = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        weatherView.setDrawable(!hidden)
    }

    override fun setSystemBarStyle() {
        ThemeManager
            .getInstance(requireContext())
            .phoneCtThemeDelegate
            .setSystemBarStyle(
                requireContext(),
                requireActivity().window,
                statusShader = scrollListener?.topOverlap == true,
                lightStatus = false,
                navigationShader = true,
                lightNavigation = false
            )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateDayNightColors()
        updateViews()
    }

    // init.

    private fun initModel() {
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
    }

    @SuppressLint("ClickableViewAccessibility", "NonConstantResourceId", "NotifyDataSetChanged")
    private fun initView() {
        ensureResourceProvider()
        // 启动重力感应
        weatherView.setGravitySensorEnabled(
            SettingsManager.getInstance(requireContext()).isGravitySensorEnabled
        )

        binding.toolbar.setNavigationOnClickListener {
            if (callback != null) {
                callback!!.onManageIconClicked()
            }
        }
        binding.toolbar.inflateMenu(R.menu.activity_main)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.action_manage -> if (callback != null) {
//                    callback!!.onManageIconClicked()
//                }
                R.id.action_settings -> if (callback != null) {
                    callback!!.onSettingsIconClicked()
                }
            }
            true
        }

        binding.switchLayout.setOnSwitchListener(switchListener)
        binding.switchLayout.reset()
        binding.indicator.setSwitchView(binding.switchLayout)
        binding.indicator.setCurrentIndicatorColor(Color.WHITE)
        binding.indicator.setIndicatorColor(
            ColorUtils.setAlphaComponent(Color.WHITE, (0.5 * 255).toInt())
        )

        binding.refreshLayout.setOnRefreshListener {
            viewModel.updateWithUpdatingChecking(
                triggeredByUser = true,
                checkPermissions = true
            )
        }

        val listAnimationEnabled = SettingsManager
            .getInstance(requireContext())
            .isListAnimationEnabled
        val itemAnimationEnabled = SettingsManager
            .getInstance(requireContext())
            .isItemAnimationEnabled
        adapter = MainAdapter(
            (requireActivity() as GeoActivity),
            binding.recyclerView,
            weatherView,
            null,
            resourceProvider!!,
            listAnimationEnabled,
            itemAnimationEnabled
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = MainLayoutManager()
        binding.recyclerView.addOnScrollListener(OnScrollListener().also { scrollListener = it })
        binding.recyclerView.setOnTouchListener(indicatorStateListener)

        viewModel.currentPhone.observe(viewLifecycleOwner) {
            updateViews(it.phone)
        }

        viewModel.loading.observe(viewLifecycleOwner) { setRefreshing(it) }

        viewModel.indicator.observe(viewLifecycleOwner) {
            binding.switchLayout.isEnabled = false

            if (binding.switchLayout.totalCount != 1
                || binding.switchLayout.position != 0) {
                binding.switchLayout.setData(0, 1)
                binding.indicator.setSwitchView(binding.switchLayout)
            }

//            binding.indicator.visibility = if (it.total > 1) View.VISIBLE else View.GONE
            binding.indicator.visibility =  View.GONE
        }

        previewOffset.observe(viewLifecycleOwner) {
            binding.root.post {
                if (isFragmentViewCreated) {
                    updatePreviewSubviews()
                }
            }
        }
    }

    private fun updateDayNightColors() {
        Log.e("tag","-xxx----------------"+MainThemeColorProvider.getColor(
            phone = viewModel.currentPhone.value!!.phone,
            id = R.attr.colorSurface
        ))
        binding.refreshLayout.setProgressBackgroundColorSchemeColor(
            MainThemeColorProvider.getColor(
                phone = viewModel.currentPhone.value!!.phone,
                id = R.attr.colorSurface
            )
        )
    }

    // control.

    @JvmOverloads
    fun updateViews(phone: Phone = viewModel.currentPhone.value!!.phone) {
        ensureResourceProvider()
        updateContentViews(phone = phone)
        binding.root.post {
            if (isFragmentViewCreated) {
                updatePreviewSubviews()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    private fun updateContentViews(phone: Phone) {
        Log.d("mmg","updateContentViews++++++++++")
        if (recyclerViewAnimator != null) {
            recyclerViewAnimator!!.cancel()
            recyclerViewAnimator = null
        }
        Log.d("mmg","updateContentViews++++++++++2")
        updateDayNightColors()
        Log.d("mmg","updateContentViews++++++++++3")
//        binding.switchLayout.reset()
        Log.d("mmg","updateContentViews++++++++++4")
        if (phone.device == null) {
            Log.d("mmg","updateContentViews++++++++++null")
            adapter!!.setNullWeather()
            adapter!!.notifyDataSetChanged()
            binding.recyclerView.setOnTouchListener { _, event ->
                Log.d("mmg","updateContentViews++++++++++action:"+event.action)
                Log.d("mmg","updateContentViews++++++++++isRefreshing:"+!binding.refreshLayout.isRefreshing)
                if (event.action == MotionEvent.ACTION_DOWN
                    && !binding.refreshLayout.isRefreshing) {
                    Log.d("mmg","updateContentViews++++++++++isRefreshing")

                        viewModel.updateWithUpdatingChecking(
                        triggeredByUser = true,
                        checkPermissions = true
                    )
                }
                false
            }
            return
        }
        Log.d("mmg","updateContentViews++++++++++5")
        binding.recyclerView.setOnTouchListener(null)

        val listAnimationEnabled = SettingsManager
            .getInstance(requireContext())
            .isListAnimationEnabled
        val itemAnimationEnabled = SettingsManager
            .getInstance(requireContext())
            .isItemAnimationEnabled
        adapter!!.update(
            (requireActivity() as GeoActivity),
            binding.recyclerView,
            weatherView,
            phone,
            resourceProvider!!,
            listAnimationEnabled,
            itemAnimationEnabled
        )
        adapter!!.notifyDataSetChanged()

        scrollListener!!.postReset(binding.recyclerView)

        if (!listAnimationEnabled) {
            binding.recyclerView.alpha = 0f
            recyclerViewAnimator = MainModuleUtils.getEnterAnimator(
                binding.recyclerView,
                0
            )
            recyclerViewAnimator!!.startDelay = 150
            recyclerViewAnimator!!.start()
        }
    }

    private fun ensureResourceProvider() {
        val iconProvider = SettingsManager
            .getInstance(requireContext())
            .iconProvider
        if (resourceProvider == null
            || resourceProvider!!.packageName != iconProvider) {
            resourceProvider = ResourcesProviderFactory.getNewInstance()
        }
    }

    private fun updatePreviewSubviews() {
        val phone = viewModel.getValidPhone()
        val daylight = phone.isDaylight

//        binding.toolbar.title = phone.getPhoneName(requireContext())
        // 标头赋值给手机型号
        binding.toolbar.title = Build.BRAND + "-" + Build.MODEL
        WeatherViewController.setWeatherCode(
            weatherView,
            phone.device,
            daylight,
            resourceProvider!!
        )
        binding.refreshLayout.setColorSchemeColors(
            ThemeManager
                .getInstance(requireContext())
                .phoneCtThemeDelegate
                .getThemeColors(
                    requireContext(),
                    WeatherViewController.getWeatherKind(phone.device),
                    daylight
                )[0]
        )
    }

    private fun setRefreshing(b: Boolean) {
        binding.refreshLayout.post {
            if (isFragmentViewCreated) {
                binding.refreshLayout.isRefreshing = b
            }
        }
    }

    // interface.

    private fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    // on touch listener.

    @SuppressLint("ClickableViewAccessibility")
    private val indicatorStateListener = OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_MOVE ->
                binding.indicator.setDisplayState(true)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                binding.indicator.setDisplayState(false)
        }
        false
    }

    // on swipe listener (swipe switch layout).
    // 左右滑动更换城市的  old
    private val switchListener: OnSwitchListener = object : OnSwitchListener {

        override fun onSwiped(swipeDirection: Int, progress: Float) {
            binding.indicator.setDisplayState(progress != 0f)

            if (progress >= 1) {
                previewOffset.setValue(
                    if (swipeDirection == SwipeSwitchLayout.SWIPE_DIRECTION_LEFT) 1 else -1
                )
            } else {
                previewOffset.setValue(0)
            }
        }

        override fun onSwitched(swipeDirection: Int) {
            binding.indicator.setDisplayState(false)

//            viewModel.offsetLocation(
//                if (swipeDirection == SwipeSwitchLayout.SWIPE_DIRECTION_LEFT) 1 else -1
//            )
            previewOffset.setValue(0)
        }
    }

    // on scroll changed listener.

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        private var mTopChanged: Boolean? = null
        var topOverlap = false
        private var mFirstCardMarginTop = 0
        private var mScrollY = 0
        private var mLastAppBarTranslationY = 0f

        fun postReset(recyclerView: RecyclerView) {
            recyclerView.post {
                if (!isFragmentViewCreated) {
                    return@post
                }
                mTopChanged = null
                topOverlap = false
                mFirstCardMarginTop = 0
                mScrollY = 0
                mLastAppBarTranslationY = 0f
                onScrolled(recyclerView, 0, 0)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            mFirstCardMarginTop = if (recyclerView.childCount > 0) {
                recyclerView.getChildAt(0).measuredHeight
            } else {
                -1
            }

            mScrollY = recyclerView.computeVerticalScrollOffset()
            mLastAppBarTranslationY = binding.appBar.translationY
            weatherView.onScroll(mScrollY)

            if (adapter != null) {
                adapter!!.onScroll()
            }

            // set translation y of toolbar.
            if (adapter != null && mFirstCardMarginTop > 0) {
                if (mFirstCardMarginTop >= binding.appBar.measuredHeight
                    + adapter!!.currentTemperatureTextHeight) {
                    when {
                        mScrollY < (mFirstCardMarginTop
                                - binding.appBar.measuredHeight
                                - adapter!!.currentTemperatureTextHeight) -> {
                            binding.appBar.translationY = 0f
                        }
                        mScrollY > mFirstCardMarginTop - binding.appBar.y -> {
                            binding.appBar.translationY = -binding.appBar.measuredHeight.toFloat()
                        }
                        else -> {
                            binding.appBar.translationY = (
                                    mFirstCardMarginTop
                                            - adapter!!.currentTemperatureTextHeight
                                            - mScrollY
                                            - binding.appBar.measuredHeight
                            ).toFloat()
                        }
                    }
                } else {
                    binding.appBar.translationY = -mScrollY.toFloat()
                }
            }

            // set system bar style.
            if (mFirstCardMarginTop <= 0) {
                mTopChanged = true
                topOverlap = false
            } else {
                mTopChanged = (binding.appBar.translationY != 0f) != (mLastAppBarTranslationY != 0f)
                topOverlap = binding.appBar.translationY != 0f
            }
            if (mTopChanged!!) {
                checkToSetSystemBarStyle()
            }
        }
    }
}