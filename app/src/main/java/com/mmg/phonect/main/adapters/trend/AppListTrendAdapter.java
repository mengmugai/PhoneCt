package com.mmg.phonect.main.adapters.trend;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.unit.PrecipitationUnit;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.options.unit.TemperatureUnit;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.main.adapters.AppListAdapter;
import com.mmg.phonect.main.adapters.trend.applist.AbsAppListTrendAdapter;
import com.mmg.phonect.main.adapters.trend.applist.AppListHookFrameworkAdapter;
import com.mmg.phonect.main.adapters.trend.applist.AppListXposedAdapter;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;

public class AppListTrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private @Nullable AbsAppListTrendAdapter mAdapter;

    public AppListTrendAdapter() {
        mAdapter = null;
    }

    public void xposedModule(Context context, RecyclerView parent, Location location,
                            ResourceProvider provider, TemperatureUnit unit) {
        mAdapter = new AppListXposedAdapter(context, parent, location);
    }

    public void hookFramework(Context context, RecyclerView parent, Location location,
                              ResourceProvider provider, PrecipitationUnit unit) {
        mAdapter = new AppListHookFrameworkAdapter(context,parent, location);
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
        mAdapter.onBindViewHolder((AbsAppListTrendAdapter.ViewHolder)holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter == null ? 0 : mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdapter == null) {
            return 0;
        } else if (mAdapter instanceof AppListXposedAdapter) {
            return 1;
        } else if (mAdapter instanceof AppListHookFrameworkAdapter) {
            return 2;

        }
        return -1;
    }
}
