package com.mmg.phonect.settings.dialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.R;
import com.mmg.phonect.common.utils.DisplayUtils;
import com.mmg.phonect.common.utils.helpers.AsyncHelper;
import com.mmg.phonect.common.utils.helpers.IntentHelper;
import com.mmg.phonect.settings.adapters.IconProviderAdapter;
import com.mmg.phonect.theme.resource.ResourcesProviderFactory;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;

public class ProvidersPreviewerDialog {

    public static final String ACTION_RESOURCE_PROVIDER_CHANGED
            = "com.mmg.phonect.RESOURCE_PROVIDER_CHANGED";
    public static final String KEY_PACKAGE_NAME = "package_name";

    public interface OnProviderSelectedCallback {
        void onProviderSelected(String packageName);
    }

    public static void show(
            Activity activity,
            OnProviderSelectedCallback callback
    ) {
        View view = LayoutInflater
                .from(activity)
                .inflate(R.layout.dialog_providers_previewer, null, false);
        initWidget(
                activity,
                view,
                new MaterialAlertDialogBuilder(activity)
                        .setTitle(R.string.settings_title_icon_provider)
                        .setView(view)
                        .show(),
                callback
        );
    }

    private static void initWidget(
            Activity activity,
            View view,
            AlertDialog dialog,
            OnProviderSelectedCallback callback
    ) {
        CircularProgressView progressView = view.findViewById(R.id.dialog_providers_previewer_progress);
        progressView.setVisibility(View.VISIBLE);

        RecyclerView listView = view.findViewById(R.id.dialog_providers_previewer_list);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            final float elevation = DisplayUtils.dpToPx(activity, 2);

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!listView.canScrollVertically(-1)) {
                    listView.setTranslationZ(0);
                } else {
                    listView.setTranslationZ(elevation);
                }
            }
        });
        listView.setVisibility(View.GONE);

        AsyncHelper.runOnIO(emitter -> emitter.send(
                ResourcesProviderFactory.getProviderList(PhoneCt.getInstance()),
                true
        ), (AsyncHelper.Callback<List<ResourceProvider>>) (resourceProviders, done) -> bindAdapter(
                activity,
                listView,
                progressView,
                resourceProviders,
                dialog,
                callback
        ));
    }

    private static void bindAdapter(Activity activity,
                                    RecyclerView listView,
                                    CircularProgressView progressView,
                                    List<ResourceProvider> providerList,
                                    AlertDialog dialog,
                                    OnProviderSelectedCallback callback) {
        listView.setAdapter(new IconProviderAdapter(
                activity,
                providerList,
                new IconProviderAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(ResourceProvider provider, int adapterPosition) {
                        callback.onProviderSelected(provider.getPackageName());
                        dialog.dismiss();
                    }

                    @Override
                    public void onAppStoreItemClicked(String query) {
                        IntentHelper.startAppStoreSearchActivity(activity, query);
                        dialog.dismiss();
                    }

                    @Override
                    public void onGitHubItemClicked(String query) {
                        IntentHelper.startWebViewActivity(activity, query);
                        dialog.dismiss();
                    }
                }
        ));

        Animation show = new AlphaAnimation(0f, 1f);
        show.setDuration(300);
        show.setInterpolator(new FastOutSlowInInterpolator());
        listView.startAnimation(show);
        listView.setVisibility(View.VISIBLE);

        Animation out = new AlphaAnimation(1f, 0f);
        show.setDuration(300);
        show.setInterpolator(new FastOutSlowInInterpolator());
        progressView.startAnimation(out);
        progressView.setVisibility(View.GONE);
    }
}
