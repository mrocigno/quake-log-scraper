package br.com.mrocigno

import br.com.mrocigno.helper.ArgsValidationHelper
import br.com.mrocigno.helper.GameScrapperHelper
import br.com.mrocigno.model.Games
import java.io.File
import kotlin.system.exitProcess

/***
 * @author Matheus R.
 *
 * The entry point of application.
 * The process will end with code 0 if it finishes the execution successfully and code 1 for errors
 *
 * @param args expected arguments:
 *          --file (-f) <path/to/log.log>
 *          --type (-t) <JSON/REPORT>
 */
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
        println("File \"${file.name}\" not found")
        exitProcess(1)
    }

    println(argsHelper.type.transform(file.scan()))
}

fun File.scan(): Games {
    val gamesHelper = mutableListOf<GameScrapperHelper>()

    // Read line by line and close buffer on end of lambda
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