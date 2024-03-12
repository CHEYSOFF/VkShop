package vk.cheysoff.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vkshop")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
)