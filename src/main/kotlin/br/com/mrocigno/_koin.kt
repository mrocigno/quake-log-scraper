package br.com.mrocigno

import br.com.mrocigno.common.ArgsValidationHelper
import br.com.mrocigno.common.FileScanner
import br.com.mrocigno.quake.QuakeFileScanner
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {

    startKoin {
        modules(
            module {
                factory { (args: Array<String>) -> ArgsValidationHelper(*args) }
                factory<FileScanner> { QuakeFileScanner() }
            }
        )
    }
}