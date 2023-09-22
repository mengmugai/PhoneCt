//package com.mmg.phonect.daily.adapter.holder;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.ColorStateList;
//
//import com.mmg.phonect.R;
//import com.mmg.phonect.common.basic.models.options.unit.PollenUnit;
//import com.mmg.phonect.common.basic.models.weather.Pollen;
//import com.mmg.phonect.daily.adapter.DailyWeatherAdapter;
//import com.mmg.phonect.daily.adapter.model.DailyPollen;
//import com.mmg.phonect.databinding.ItemWeatherDailyPollenBinding;
//
//public class PollenHolder extends DailyWeatherAdapter.ViewHolder {
//
//    private final ItemWeatherDailyPollenBinding mBinding;
//    private final PollenUnit mPollenUnit;
//
//    public PollenHolder(ItemWeatherDailyPollenBinding binding) {
//        super(binding.getRoot());
//        mBinding = binding;
//        mPollenUnit = PollenUnit.PPCM;
//    }
//
//    @SuppressLint({"SetTextI18n", "RestrictedApi"})
//    @Override
//    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
//        Context context = itemView.getContext();
//        Pollen pollen = ((DailyPollen) model).getPollen();
//
//        mBinding.grassIcon.setSupportImageTintList(ColorStateList.valueOf(
//                Pollen.getPollenColor(itemView.getContext(), pollen.getGrassLevel())
//        ));
//        mBinding.grassTitle.setText(context.getString(R.string.grass));
//        mBinding.grassValue.setText(
//                mPollenUnit.getValueText(
//                        context, pollen.getGrassIndex() != null ? pollen.getGrassIndex() : 0
//                ) + " - " + pollen.getGrassDescription()
//        );
//
//        mBinding.ragweedIcon.setSupportImageTintList(ColorStateList.valueOf(
//                Pollen.getPollenColor(itemView.getContext(), pollen.getRagweedLevel())
//        ));
//        mBinding.ragweedTitle.setText(context.getString(R.string.ragweed));
//        mBinding.ragweedValue.setText(
//                mPollenUnit.getValueText(
//                        context, pollen.getRagweedIndex() != null ? pollen.getRagweedIndex() : 0
//                ) + " - " + pollen.getRagweedDescription()
//        );
//
//        mBinding.treeIcon.setSupportImageTintList(ColorStateList.valueOf(
//                Pollen.getPollenColor(itemView.getContext(), pollen.getTreeLevel())
//        ));
//        mBinding.treeTitle.setText(context.getString(R.string.tree));
//        mBinding.treeValue.setText(
//                mPollenUnit.getValueText(
//                        context, pollen.getTreeIndex() != null ? pollen.getTreeIndex() : 0
//                ) + " - " + pollen.getTreeDescription()
//        );
//
//        mBinding.moldIcon.setSupportImageTintList(ColorStateList.valueOf(
//                Pollen.getPollenColor(itemView.getContext(), pollen.getMoldLevel())
//        ));
//        mBinding.moldTitle.setText(context.getString(R.string.mold));
//        mBinding.moldValue.setText(
//                mPollenUnit.getValueText(
//                        context, pollen.getMoldIndex() != null ? pollen.getMoldIndex() : 0
//                ) + " - " + pollen.getMoldDescription()
//        );
//    }
//}