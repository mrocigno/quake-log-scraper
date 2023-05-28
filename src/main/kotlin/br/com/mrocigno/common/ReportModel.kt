package br.com.mrocigno.common

interface ReportModel {

    fun asJson(): String
    fun asReport(): String
}