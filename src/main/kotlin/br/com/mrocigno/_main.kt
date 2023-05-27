package br.com.mrocigno

import br.com.mrocigno.helper.ArgsValidationHelper
import br.com.mrocigno.helper.GameScrapperHelper
import br.com.mrocigno.model.Games
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val argsHelper = ArgsValidationHelper(args)

    argsHelper.hasInvalidArg {
        println("Unrecognized option: $it")
        println("Use --help (-h) to see all options")
        exitProcess(1)
    }

    argsHelper.shouldShowHelp {
        exitProcess(0)
    }

    argsHelper.hasFileArg {
        println("--file (-f) argument is required")
        exitProcess(1)
    }

    val file = argsHelper.file

    if (!file.exists()) {
        println("Arquivo \"${file.name}\" não encontrado")
        // Ending program with status 1 to indicate error
        exitProcess(1)
    }

    println(file.scan().toJson())
}

fun File.scan(): Games {
    val gamesHelper = mutableListOf<GameScrapperHelper>()

    // Read line by line and close on end of lambda
    bufferedReader().useLines {
        var creator: MutableList<String>? = null

        it.forEach { line ->

            when {
                GameScrapperHelper.isGameInitialization(line) -> {
                    creator?.run {
                        gamesHelper.commit(this)
                    }
                    creator = mutableListOf()
                }
                else -> creator?.add(line)
            }
        }

        if (!creator.isNullOrEmpty()) gamesHelper.commit(creator!!)
    }

    return Games(gamesHelper)
}