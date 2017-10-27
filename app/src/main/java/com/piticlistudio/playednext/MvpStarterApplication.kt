package com.piticlistudio.playednext

import android.app.Activity
import android.app.Application
import com.facebook.stetho.Stetho
import com.piticlistudio.playednext.ui.injection.component.ApplicationComponent
import com.piticlistudio.playednext.ui.injection.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class MvpStarterApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    internal var mApplicationComponent: ApplicationComponent? = null

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
    }

//    // Needed to replace the component with a test specific one
//    var component: ApplicationComponent
//        get() {
//            if (mApplicationComponent == null) {
//                mApplicationComponent = DaggerApplicationComponent.builder()
//                        .applicationModule(ApplicationModule(this))
//                        .build()
//            }
//            return mApplicationComponent as ApplicationComponent
//        }
//        set(applicationComponent) {
//            mApplicationComponent = applicationComponent
//        }
//
//    companion object {
//
//        operator fun get(context: Context): MvpStarterApplication {
//            return context.applicationContext as MvpStarterApplication
//        }
//    }
}
