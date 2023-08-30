package com.mmg.phonect.main.adapters.trend;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.unit.PrecipitationUnit;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.options.unit.TemperatureUnit;
import com.mmg.phonect.main.adapters.trend.daily.DailyTemperatureAdapter;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.main.adapters.trend.daily.AbsDailyTrendAdapter;
import com.mmg.phonect.main.adapters.trend.daily.DailyAirQualityAdapter;
import com.mmg.phonect.main.adapters.trend.daily.DailyPrecipitationAdapter;
import com.mmg.phonect.main.adapters.trend.daily.DailyUVAdapter;
import com.mmg.phonect.main.adapters.trend.daily.DailyWindAdapter;

public class DailyTrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable private AbsDailyTrendAdapter mAdapter;

    public DailyTrendAdapter() {
        mAdapter = null;
    }

    public void temperature(GeoActivity activity, TrendRecyclerView parent, Location location,
                            ResourceProvider provider, TemperatureUnit unit) {
        mAdapter = new DailyTemperatureAdapter(activity, parent, location, provider, unit);
    }

    public void airQuality(GeoActivity activity, TrendRecyclerView parent, Location location) {
        mAdapter = new DailyAirQualityAdapter(activity, parent, location);
    }

    public void wind(GeoActivity activity, TrendRecyclerView parent, Location location, SpeedUnit unit) {
        mAdapter = new DailyWindAdapter(activity, parent, location, unit);
    }

    public void uv(GeoActivity activity, TrendRecyclerView parent, Location location) {
        mAdapter = new DailyUVAdapter(activity, parent, location);
    }

    public void precipitation(GeoActivity activity, TrendRecyclerView parent, Location location,
                              ResourceProvider provider, PrecipitationUnit unit) {
        mAdapter = new DailyPrecipitationAdapter(activity, parent, location, provider, unit);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        assert mAdapter != null;
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        assert mAdapter != null;
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter == null ? 0 : mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdapter == null) {
            return 0;
        } else if (mAdapter instanceof DailyTemperatureAdapter) {
            return 1;
        } else if (mAdapter instanceof DailyAirQualityAdapter) {
            return 2;
        } else if (mAdapter instanceof DailyWindAdapter) {
            return 3;
        } else if (mAdapter instanceof DailyUVAdapter) {
            return 4;
        } else if (mAdapter instanceof DailyPrecipitationAdapter) {
            return 5;
        }
        return -1;
    }
}