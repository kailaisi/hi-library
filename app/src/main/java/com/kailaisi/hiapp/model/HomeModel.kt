package com.kailaisi.hiapp.model

data class TabCategory(val categoryId: String, val categoryName: String, val goodsCount: String)


data class Subcategory(
    val categoryId: String,
    val subcategoryIcon: String,
    val subcategoryName: String,
    val groupName: String,
)