package com.piticlistudio.playednext

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import android.util.Log
import com.facebook.stetho.Stetho
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.game.mapper.GameEntityMapper
import com.piticlistudio.playednext.data.game.mapper.local.GameDaoMapper
import com.piticlistudio.playednext.data.game.mapper.remote.IGDBGameMapper
import com.piticlistudio.playednext.data.game.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.game.repository.local.GameLocalImpl
import com.piticlistudio.playednext.data.game.repository.remote.GameRemoteImpl
import com.piticlistudio.playednext.data.remote.GameServiceFactory
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SaveGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SearchGamesUseCase
import com.piticlistudio.playednext.injection.component.ApplicationComponent
import com.piticlistudio.playednext.injection.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
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

        val database = Room.databaseBuilder(this.applicationContext, AppDatabase::class.java, "my-todo-db").build()
        val service = GameServiceFactory.makeGameService()
        val gamesDao = database.gamesDao()

        val localRepository = GameLocalImpl(gamesDao, GameDaoMapper())
        val repository = GameRepositoryImpl(GameRemoteImpl(service, IGDBGameMapper()), localRepository, GameEntityMapper())
        val search = SearchGamesUseCase(repository)
        /*search.execute("zelda")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            it.map {
                                Log.d("SearchGamesUseCase", it.toString())
                            }
                        },
                        onError = { Log.e("SearchGamesUseCase", it.toString()) },
                        onComplete = { println("SearchGamesUseCase completed!") }
                )

        gamesDao.getAllGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            Log.d("gamesDao", "Number of games stored: ${it.size}")
                        },
                        onError = { Log.e("gamesDao", it.toString()) },
                        onComplete = { println("gamesDao completed") }
                )

        val save = SaveGameUseCase(repository)
        val load = LoadGameUseCase(repository)
        load.execute(250)
                .flatMap { save.execute(it).andThen(Single.just(it)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            Log.d("LoadGameUseCase", "foo")
                        },
                        onError = { Log.e("LoadGameUseCase", it.toString()) },
                        onComplete = { println("LoadGameUseCase completed") }
                )
*/

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
