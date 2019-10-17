package com.dollareuro.rates.module

import com.dollareuro.rates.BuildConfig
import com.dollareuro.rates.model.remote.RatesApi
import com.dollareuro.rates.model.repository.RatesRepository
import com.dollareuro.rates.model.repository.RatesRepositoryImpl
import com.dollareuro.rates.view.model.RatesViewModel
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModules = module {
    single {
        createApi<RatesApi>(
            okHttpClient = createHttpClient(),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_URL
        )
    }
    factory<RatesRepository> { RatesRepositoryImpl(api = get()) }
    viewModel { RatesViewModel(ratesRepository = get()) }
}

fun createHttpClient(): OkHttpClient {
    val client = OkHttpClient.Builder()
    client.readTimeout(20, TimeUnit.SECONDS)
    return client.addInterceptor {
        val original = it.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        var request = requestBuilder.method(original.method(), original.body()).build()
        val url =
            request.url().newBuilder().addQueryParameter("access_key", BuildConfig.TOKEN).build()
        request = request.newBuilder().url(url).build()
        return@addInterceptor it.proceed(request)
    }.build()
}


inline fun <reified T> createApi(
    okHttpClient: OkHttpClient,
    factory: CallAdapter.Factory, baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addCallAdapterFactory(factory)
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}
