package com.mmg.phonect.common.basic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class GeoViewModel extends AndroidViewModel {

    private boolean mNewInstance;

    public GeoViewModel(@NonNull Application application) {
        super(application);
        mNewInstance = true;
    }

    public boolean checkIsNewInstance() {
        boolean result = mNewInstance;
        mNewInstance = false;
        return result;
    }
}
