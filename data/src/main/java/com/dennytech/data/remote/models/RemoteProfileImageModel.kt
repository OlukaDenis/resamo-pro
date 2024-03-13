package com.dennytech.data.remote.models

data class RemoteProfileImageModel(
    val sizes: List<ImageSizeModel>?
)

data class ImageSizeModel(
    val _id: String?,
    val type: String?,
    val url: String?
)