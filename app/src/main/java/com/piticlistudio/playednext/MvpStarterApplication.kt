package com.piticlistudio.playednext

import android.app.Activity
import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.piticlistudio.playednext.data.entity.mapper.datasources.company.GiantbombCompanyMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.franchise.GiantbombCollectionMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.game.GiantbombGameMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.genre.GiantbombGenreMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.image.GiantbombImageMapper
import com.piticlistudio.playednext.data.entity.mapper.datasources.platform.GiantbombPlatformMapper
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.giantbomb.GiantbombGameDatasourceRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.giantbomb.GiantbombServiceFactory
import com.piticlistudio.playednext.data.repository.datasource.room.franchise.RoomCollectionRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.room.game.RoomGameRepositoryImpl
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
