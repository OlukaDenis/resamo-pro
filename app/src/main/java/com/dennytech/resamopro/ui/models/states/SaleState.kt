package com.dennytech.resamopro.ui.models.states

data class SalesState(
    val error: String = "",
    val loading: Boolean = false,
    val startDate: String = "",
    val endDate: String = "",
    val empty: Boolean = false
)
