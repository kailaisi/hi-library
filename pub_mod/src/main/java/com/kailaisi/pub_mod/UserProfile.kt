package com.kailaisi.pub_mod

import java.io.Serializable

data class UserProfile(
    val isLogin: Boolean,
    val favoriteCount: Int,
    val brrowseCount: Int,
    val learnMinutes: Int,
    val userName: String,
    val avatar: String,
    val bannerNoticeList: List<NoticeInfo>?,
) : Serializable


data class NoticeInfo(
    val id: String,
    val stick: Int,
    val type: String,
    val title: String,
    val subtitle: String,
    val url: String,
    val cover: String,
    val createTime: String,
) : java.io.Serializable