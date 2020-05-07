package tech.jour.appupdater

data class UpdaterBean(
    val androidUpdateUrl: String?,
    val minimumVersion: String?,
    val minimumVersionCode: Int,
    val updateUrl: String?,
    val warVersion: String?
)