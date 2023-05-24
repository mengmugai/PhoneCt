package com.mmg.phonect.common.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.common.ui.widgets.TagView;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final List<Tag> mTagList;
    private final @ColorInt int mCheckedTitleColor;
    private final @ColorInt int mUncheckedTitleColor;
    private final @ColorInt int mCheckedBackgroundColor;
    private final @ColorInt int mUncheckedBackgroundColor;
    private final OnTagCheckedListener mListener;
    private int mCheckedIndex;

    public static final int UNCHECKABLE_INDEX = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TagView mTagView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTagView = itemView.findViewById(R.id.item_tag);
            mTagView.setOnClickListener(v -> {
                boolean consumed = false;
                if (mListener != null) {
                    consumed = mListener.onItemChecked(!mTagView.isChecked(), mCheckedIndex, getAdapterPosition());
                }
                if (!consumed && mCheckedIndex != getAdapterPosition()) {
                    int i = mCheckedIndex;
                    mCheckedIndex = getAdapterPosition();
                    notifyItemChanged(i);
                    notifyItemChanged(mCheckedIndex);
                }
            });
        }

        void onBindView(Tag tag, boolean checked) {
            mTagView.setText(tag.getName());

            mTagView.setCheckedBackgroundColor(mCheckedBackgroundColor);
            mTagView.setUncheckedBackgroundColor(mUncheckedBackgroundColor);

            setChecked(checked);
        }

        public void setChecked(boolean checked) {
            mTagView.setTextColor(checked ? mCheckedTitleColor : mUncheckedTitleColor);
            mTagView.setChecked(checked);
        }
    }

    public interface OnTagCheckedListener {
        boolean onItemChecked(boolean checked, int oldPosition, int newPosition);
    }

    public interface Tag {
        String getName();
    }

    public TagAdapter(List<Tag> tagList,
                      @ColorInt int checkedTitleColor,
                      @ColorInt int uncheckedTitleColor,
                      @ColorInt int checkedBackgroundColor,
                      @ColorInt int uncheckedBackgroundColor,
                      OnTagCheckedListener listener) {
        this(
                tagList,
                checkedTitleColor,
                uncheckedTitleColor,
                checkedBackgroundColor,
                uncheckedBackgroundColor,
                listener,
                UNCHECKABLE_INDEX
        );
    }

    public TagAdapter(List<Tag> tagList,
                      @ColorInt int checkedTitleColor,
                      @ColorInt int uncheckedTitleColor,
                      @ColorInt int checkedBackgroundColor,
                      @ColorInt int uncheckedBackgroundColor,
                      OnTagCheckedListener listener,
                      int checkedIndex) {
        mTagList = tagList;
        mCheckedTitleColor = checkedTitleColor;
        mUncheckedTitleColor = uncheckedTitleColor;
        mCheckedBackgroundColor = checkedBackgroundColor;
        mUncheckedBackgroundColor = uncheckedBackgroundColor;
        mListener = listener;
        mCheckedIndex = checkedIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tag, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(mTagList.get(position), position == mCheckedIndex);
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }

    public void insertItem(Tag tag) {
        mTagList.add(tag);
        notifyItemInserted(mTagList.size() - 1);
    }

    public Tag removeItem(int position) {
        Tag tag = mTagList.remove(position);
        notifyItemRemoved(position);
        return tag;
    }
}
