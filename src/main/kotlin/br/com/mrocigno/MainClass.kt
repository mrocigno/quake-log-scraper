package br.com.mrocigno

import br.com.mrocigno.common.ArgsValidationHelper
import br.com.mrocigno.common.Cell
import br.com.mrocigno.common.FileScanner
import br.com.mrocigno.common.ReportType.JSON
import br.com.mrocigno.common.ReportType.REPORT
import br.com.mrocigno.common.table
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import javax.swing.GroupLayout.Alignment.CENTER
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

    val report = MainClass(*args).process()
    print(report)
}

class MainClass(private vararg val args: String) : KoinComponent {

    private val argsHelper: ArgsValidationHelper = get { parametersOf(args) }
    private val fileScanner: FileScanner = get()

    fun process(): String {
        argsHelper.hasInvalidArg {
            println("Unrecognized option: $it")
            println("Use --help (-h) to see all options")
            exitProcess(1)
        }

        argsHelper.shouldShowHelp {
            println(
                table {
                    header {
                        line {
                            cell("COMMANDS", 2, CENTER)
                        }
                    }

                    it.forEach { (key, value) ->
                        line {
                            cell(key)
                            cell(value)
                        }
                        if (key.contains("--")) {
                            line { cells.add(Cell(isSeparator = true, span = 2)) }
                        }
                    }
                }
            )
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

        val report = fileScanner.scan(file)
        return when (argsHelper.type) {
            JSON -> report.asJson()
            REPORT -> report.asReport()
        }
    }
}
