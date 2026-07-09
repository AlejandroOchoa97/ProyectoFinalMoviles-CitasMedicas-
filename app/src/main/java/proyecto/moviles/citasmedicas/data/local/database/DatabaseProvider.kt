package proyecto.moviles.citasmedicas.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var database: MediCitasDatabase? = null

    fun getDatabase(context: Context): MediCitasDatabase {
        return database ?: Room.databaseBuilder(
            context.applicationContext,
            MediCitasDatabase::class.java,
            "medicitas_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
            .also {
                database = it
            }
    }
}
