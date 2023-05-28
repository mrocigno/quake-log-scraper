package br.com.mrocigno.common

import java.io.File

interface FileScanner {

    fun scan(file: File): ReportModel
}