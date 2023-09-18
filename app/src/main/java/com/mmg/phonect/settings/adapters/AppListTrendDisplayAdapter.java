package com.mmg.phonect.settings.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
//import com.mmg.phonect.common.basic.models.options.appearance.DailyTrendDisplay;
import com.mmg.phonect.common.basic.models.options.appearance.AppListTrendDisplay;
import com.mmg.phonect.common.ui.widgets.slidingItem.SlidingItemContainerLayout;

import java.util.List;

public class AppListTrendDisplayAdapter extends RecyclerView.Adapter<AppListTrendDisplayAdapter.ViewHolder> {

    private final List<AppListTrendDisplay> mDailyTrendDisplayList;
    private final OnItemRemoveListener mRemoveListener;
    private final OnItemDragListener mDragListener;

    public interface OnItemRemoveListener {
        void onRemoved(AppListTrendDisplay dailyTrendDisplay);
    }

    public interface OnItemDragListener {
        void onDrag(ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SlidingItemContainerLayout container;
        RelativeLayout item;
        TextView title;
        ImageButton sortButton;
        ImageButton deleteButton;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_card_display_container);
            item = itemView.findViewById(R.id.item_card_display);
            title = itemView.findViewById(R.id.item_card_display_title);
            sortButton = itemView.findViewById(R.id.item_card_display_sortButton);
            sortButton.setOnTouchListener((View v, MotionEvent event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDragListener.onDrag(this);
                }
                return false;
            });
            deleteButton = itemView.findViewById(R.id.item_card_display_deleteBtn);
            deleteButton.setOnClickListener(v -> removeItem(getAdapterPosition()));
        }

        void onBindView(AppListTrendDisplay dailyTrendDisplay) {
            title.setText(dailyTrendDisplay.getName(title.getContext()));

            container.swipe(0);
            container.setOnClickListener(v -> {
                // do nothing.
            });
        }
    }

    public AppListTrendDisplayAdapter(List<AppListTrendDisplay> dailyTrendDisplayList,
                                      OnItemRemoveListener removeListener,
                                      OnItemDragListener dragListener) {
        mDailyTrendDisplayList = dailyTrendDisplayList;
        mRemoveListener = removeListener;
        mDragListener = dragListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_card_display, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(mDailyTrendDisplayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDailyTrendDisplayList.size();
    }

    public List<AppListTrendDisplay> getDailyTrendDisplayList() {
        return mDailyTrendDisplayList;
    }

    public void insertItem(AppListTrendDisplay dailyTrendDisplay) {
        mDailyTrendDisplayList.add(dailyTrendDisplay);
        notifyItemInserted(mDailyTrendDisplayList.size() - 1);
    }

    public void removeItem(int adapterPosition) {
        AppListTrendDisplay dailyTrendDisplay = mDailyTrendDisplayList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        mRemoveListener.onRemoved(dailyTrendDisplay);
    }

    public void moveItem(int fromPosition, int toPosition) {
        mDailyTrendDisplayList.add(toPosition, mDailyTrendDisplayList.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }
}
