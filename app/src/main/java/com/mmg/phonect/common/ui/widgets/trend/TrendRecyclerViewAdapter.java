package com.mmg.phonect.common.ui.widgets.trend;

import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.common.basic.models.Location;

public abstract class TrendRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private Location mLocation;

    public TrendRecyclerViewAdapter(Location location) {
        mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
        notifyDataSetChanged();
    }
}
