package com.piticlistudio.playednext

import android.app.Activity
import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.piticlistudio.playednext.data.repository.datasource.net.giantbomb.GiantbombServiceFactory
import com.piticlistudio.playednext.ui.injection.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject


class MvpStarterApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    lateinit var connectivity: BehaviorSubject<Connectivity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun onCreate() {
        super.onCreate()
        connectivity = BehaviorSubject.create()
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        val service = GiantbombServiceFactory.makeGameService()
        service.fetchGame(49994)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { Log.e("VIRUTA", "Received error ${it.localizedMessage}") },
                        onSuccess = {
                            Log.d("VIRUTA", it.results?.name)
                        }
                )

        ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onError = { connectivity.onError(it) },
                        onNext = {
                            Log.d("MvpStarterApplication", "onNext (line 47): ${it}")
                            connectivity.onNext(it)
                        }
                )
    }
}
