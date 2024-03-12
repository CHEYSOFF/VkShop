package vk.cheysoff.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ProductEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ShopDatabase: RoomDatabase() {

    abstract val dao: ShopDao
}