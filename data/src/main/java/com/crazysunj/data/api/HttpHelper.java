package com.crazysunj.data.api;

import android.content.Context;
import android.text.TextUtils;

import com.crazysunj.data.logger.HttpLogger;
import com.crazysunj.data.service.WeatherService;
import com.crazysunj.data.util.NetworkUtils;
import com.crazysunj.domain.bus.DownloadEvent;
import com.crazysunj.domain.bus.RxBus;
import com.crazysunj.domain.entity.constant.CacheConstant;
import com.crazysunj.domain.util.LoggerUtil;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class HttpHelper {

    private OkHttpClient mOkHttpClient;
    private WeatherService mWeatherService;

    @Inject
    public HttpHelper(Context context) {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // 设置缓存 20M
            Cache cache = new Cache(new File(context.getExternalCacheDir(), CacheConstant.CACHE_DIR_API), 20 * 1024 * 1024);
            builder.cache(cache);
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
            builder.addInterceptor(new CrazyDailyCacheInterceptor());
            builder.addNetworkInterceptor(new CrazyDailyCacheNetworkInterceptor());
            // 设置Cookie
            builder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)));
            // 设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            // 错误重连
            builder.retryOnConnectionFailure(true);
            mOkHttpClient = builder.build();
        }
    }

    private static class CrazyDailyCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            //Request request = chain.request();
            //CacheControl cacheControl = request.cacheControl();
            ////header可控制不走这个逻辑
            //boolean noCache = cacheControl.noCache() || cacheControl.noStore() || cacheControl.maxAgeSeconds() == 0;
            //if (!noCache && !NetworkUtils.isNetworkAvailable()) {
            //    Request.Builder builder = request.newBuilder();
            //    LoggerUtil.i(LoggerUtil.MSG_HTTP, "CrazyDailyCacheInterceptor---cache---host:" + request.url().host());
            //    CacheControl newCacheControl = new CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build();
            //    request = builder.cacheControl(newCacheControl).build();
            //    return chain.proceed(request);
            //}
            //return chain.proceed(request);

            Request request = chain.request();
            CacheControl cacheControl = request.cacheControl();
            boolean noCache = cacheControl.noCache() || cacheControl.noStore() || cacheControl.maxAgeSeconds() == 0;
            if (!noCache && NetworkUtils.isNetworkAvailable()) {
                Request.Builder builder = request.newBuilder();
                LoggerUtil.i(LoggerUtil.MSG_HTTP, "CrazyDailyCacheInterceptor---cache---host:" + request.url().host());
                CacheControl newCacheControl = new CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build();
                request = builder.cacheControl(newCacheControl).build();
                return chain.proceed(request);
            }

            return chain.proceed(request);
        }
    }






    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    //封装retrofit2
    public WeatherService getWeatherService() {
        if (mWeatherService == null) {
            synchronized (this) {
                if (mWeatherService == null) {
                    mWeatherService = new Retrofit.Builder()
                            .baseUrl(WeatherService.HOST)
                            .client(mOkHttpClient)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(WeatherService.class);
                }
            }
        }
        return mWeatherService;
    }

    /**
     * 有网才会执行哦
     */
    private static class CrazyDailyCacheNetworkInterceptor implements Interceptor {

        private static final String CACHE_CONTROL = "Cache-Control";

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            final Response response = chain.proceed(request);
            final String requestHeader = request.header(CACHE_CONTROL);
            //判断条件最好加上TextUtils.isEmpty(response.header(CACHE_CONTROL))来判断服务器是否返回缓存策略，如果返回，就按服务器的来，我这里全部客户端控制了
            if (!TextUtils.isEmpty(requestHeader)) {
                LoggerUtil.i(LoggerUtil.MSG_HTTP, "CrazyDailyCacheNetworkInterceptor---cache---host:" + request.url().host());
                return response.newBuilder().header(CACHE_CONTROL, requestHeader).removeHeader("Pragma").build();
            }
            return response;
        }
    }



    private static class ProgressInterceptor implements Interceptor {

        private final int taskId;

        private ProgressInterceptor(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
//            Response originalResponse = chain.proceed(chain.request());
//            return originalResponse.newBuilder()
//                    .body(new ProgressResponseBody(taskId, originalResponse.body()))
//                    .build();
            Response originalResponse = chain.proceed(chain.request());
            return  originalResponse.newBuilder()
                    .body(new ProgressResponseBody(taskId,originalResponse.body()))
                    .build();
        }
    }

    private static class ProgressResponseBody extends ResponseBody {
        private ResponseBody responseBody;
        private final int taskId;
        private BufferedSource bufferedSource;

        private ProgressResponseBody(int taskId, ResponseBody responseBody) {
            this.taskId = taskId;
            this.responseBody = responseBody;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(contentLength(), responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(long contentLength, Source source) {
            return new ForwardingSource(source) {
                long bytesReaded = 0;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                    RxBus.getDefault().post(String.valueOf(taskId), new DownloadEvent(taskId, contentLength, bytesReaded));
                    return bytesRead;
                }
            };
        }
    }

}
