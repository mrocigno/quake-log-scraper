package br.com.mrocigno

import br.com.mrocigno.helper.ArgsValidationHelper
import br.com.mrocigno.helper.ClientScrapperHelper
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {

    startKoin {
        modules(
            module {
                factory { (args: Array<String>) -> ArgsValidationHelper(*args) }
                factory { ClientScrapperHelper() }
            }
        )
    }
}