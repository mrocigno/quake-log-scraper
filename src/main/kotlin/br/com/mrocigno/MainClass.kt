package br.com.mrocigno

import br.com.mrocigno.helper.AllowedTags
import br.com.mrocigno.helper.ArgsValidationHelper
import br.com.mrocigno.helper.GameScrapperHelper
import br.com.mrocigno.model.Games
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
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
    initKoin()

    MainClass(*args).print()
}

class MainClass(private vararg val args: String) : KoinComponent {

    private val argsHelper: ArgsValidationHelper = get { parametersOf(args) }

    fun print() = apply {
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

        print(argsHelper.type.transform.invoke(file.scan()))
    }

    private fun File.scan(): Games {
        val gamesHelper = mutableListOf<GameScrapperHelper>()

        // Read line by line and close buffer on end of lambda
        bufferedReader().useLines {
            var creator: MutableList<String>? = null

            it.forEach { line ->

                when {
                    AllowedTags.isTagAllowed(line) -> creator?.add(line)
                    AllowedTags.isGameInitialization(line) -> {
                        creator?.run(gamesHelper::commit)
                        creator = mutableListOf()
                    }
                }
            }

            if (!creator.isNullOrEmpty()) gamesHelper.commit(creator!!)
        }

        return Games(gamesHelper)
    }
}