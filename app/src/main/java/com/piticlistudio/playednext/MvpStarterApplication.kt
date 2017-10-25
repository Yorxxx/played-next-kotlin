package com.piticlistudio.playednext

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import android.util.Log
import com.facebook.stetho.Stetho
import com.piticlistudio.playednext.data.AppDatabase
import com.piticlistudio.playednext.data.entity.mapper.datasources.*
import com.piticlistudio.playednext.data.entity.net.CompanyDTO
import com.piticlistudio.playednext.data.repository.CompanyRepositoryImpl
import com.piticlistudio.playednext.data.repository.GameRepositoryImpl
import com.piticlistudio.playednext.data.repository.GenreRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.CompanyDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GameLocalImpl
import com.piticlistudio.playednext.data.repository.datasource.dao.GenreDaoRepositoryImpl
import com.piticlistudio.playednext.data.repository.datasource.net.CompanyRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameRemoteImpl
import com.piticlistudio.playednext.data.repository.datasource.net.GameServiceFactory
import com.piticlistudio.playednext.data.repository.datasource.net.GenreRemoteImpl
import com.piticlistudio.playednext.domain.interactor.game.LoadGameUseCase
import com.piticlistudio.playednext.domain.interactor.game.SaveGameUseCase
import com.piticlistudio.playednext.ui.injection.component.ApplicationComponent
import com.piticlistudio.playednext.ui.injection.component.DaggerApplicationComponent
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

        val database = Room.databaseBuilder(this.applicationContext, AppDatabase::class.java, "my-todo-db")
                .fallbackToDestructiveMigration().build()
        val service = GameServiceFactory.makeGameService()
        val gamesDao = database.gamesDao()

        val localRepository = GameLocalImpl(gamesDao, GameDaoMapper())
        val companyDTOMapper = CompanyDTOMapper()
        val genreDTOMapper = GenreDTOMapper()
        val gameMapper = GameDTOMapper(companyDTOMapper, genreDTOMapper)
        val repository = GameRepositoryImpl(GameRemoteImpl(service, gameMapper), localRepository)
        val localCompRepository = CompanyDaoRepositoryImpl(database.companyDao(), CompanyDaoMapper())
        val comp_repository = CompanyRepositoryImpl(localCompRepository, CompanyRemoteImpl(service, companyDTOMapper))
        val localGenRepository = GenreDaoRepositoryImpl(database.genreDao(), GenreDaoMapper())
        val gen_repository = GenreRepositoryImpl(localGenRepository, GenreRemoteImpl(service, genreDTOMapper))
        val load = LoadGameUseCase(repository, comp_repository, gen_repository)
        val save = SaveGameUseCase(repository, comp_repository, gen_repository)
        load.execute(657)
                .flatMap { save.execute(it).andThen(Single.just(it)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribeBy(
                        onNext = {
                            Log.d("LoadGameUseCase", "Retrieved game ${it}")
                            it.developers?.forEach {
                                Log.d("LoadGameUseCase", "Retrieved developer ${it}")
                            }
                            it.publishers?.forEach {
                                Log.d("LoadGameUseCase", "Retrieved publisher ${it}")
                            }
                            it.genres?.forEach {
                                Log.d("LoadGameUseCase", "Retrieved genre ${it}")
                            }
                        },
                        onError = { Log.e("LoadGameUseCase", "Failed loading game ${it}") },
                        onComplete = { println("LoadGameUseCase completed") }
                )
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
