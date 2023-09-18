package com.mmg.phonect.device.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.mmg.phonect.BuildConfig;
//import com.mmg.phonect.device.apis.AccuWeatherApi;
import com.mmg.phonect.device.apis.OwmApi;
import com.mmg.phonect.device.apis.CaiYunApi;

@InstallIn(SingletonComponent.class)
@Module
public class ApiModule {


    @Provides
    public OwmApi provideOpenWeatherMapApi(OkHttpClient client,
                                           GsonConverterFactory converterFactory,
                                           RxJava2CallAdapterFactory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.OWM_BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
                .create((OwmApi.class));
    }

    @Provides
    public CaiYunApi provideCaiYunApi(OkHttpClient client,
                                      GsonConverterFactory converterFactory,
                                      RxJava2CallAdapterFactory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl("https://weatherapi.market.xiaomi.com/")
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
                .create((CaiYunApi.class));
    }







}
