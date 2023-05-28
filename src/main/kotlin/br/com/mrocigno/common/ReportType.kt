package br.com.mrocigno.common

enum class ReportType {
    JSON,
    REPORT;

    companion object {

        fun get(name: String): ReportType? =
            ReportType.values().find { it.name.lowercase() == name.lowercase() }
    }
}