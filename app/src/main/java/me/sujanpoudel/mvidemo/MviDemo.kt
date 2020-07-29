package me.sujanpoudel.mvidemo

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.StrictMode
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.schedulers.Schedulers
import me.sujanpoudel.mvidemo.data.Preferences
import me.sujanpoudel.mvidemo.data.RoomDB
import me.sujanpoudel.mvidemo.data.models.Note
import rxdogtag2.RxDogTag
import rxdogtag2.autodispose2.AutoDisposeConfigurer
import javax.inject.Inject

@HiltAndroidApp
class MviDemo : Application() {

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var roomDB: RoomDB

    override fun onCreate() {
        super.onCreate()
        RxDogTag.builder()
            .configureWith(AutoDisposeConfigurer::configure)
            .install()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        AndroidThreeTen.init(this)

//        roomDB.noteDao.insert(mutableListOf<Note>().apply {
//            add(Note("Note 1", "dkjfhjdkhfkdjfhdjkfh"))
//            add(Note("Note 2", "djfhdjf jdfhjdf kdf"))
//            add(Note("Note 3", "klsofd dfj kakdjfdfh"))
//            add(Note("Note 4", "slojshdk kj askjdfh sf"))
//        }).subscribeOn(Schedulers.io())
//            .subscribe()
    }

    fun restartProcess(logOut: Boolean = false) {
        if (logOut)
            preferences.cleanUp()
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            Runtime.getRuntime().exit(0)
        }, 100)
    }
}