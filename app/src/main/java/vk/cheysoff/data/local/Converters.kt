package vk.cheysoff.data.local

import androidx.room.TypeConverter


object Converters {
    @TypeConverter
    fun fromStringArrayList(images: List<String>): String {
        return images.joinToString(", ")
    }

    @TypeConverter
    fun toStringArrayList(imagesString: String): List<String> {
        return imagesString.split(", ")
    }
}
