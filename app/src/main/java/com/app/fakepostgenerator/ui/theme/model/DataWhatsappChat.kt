package com.app.fakepostgenerator.ui.theme.model

data class DataWhatsappChat(
    val text: String,
    val time: String,
    val day: String,
    val type: Int,
    val image: String,
    var isEditCheck: Boolean = true,
    var isSeen: Boolean,
    var device: String = "ANDROID",
    var msgType: Int,
    var isFirst: Boolean = true,
)
