package com.kailaisi.hiapp.model

import java.io.Serializable

data class TabCategory(val categoryId: String, val categoryName: String, val goodsCount: String): Serializable


data class Subcategory(
    val categoryId: String,
    val subcategoryIcon: String,
    val subcategoryName: String,
    val groupName: String,
): Serializable

class HomeModel {

}