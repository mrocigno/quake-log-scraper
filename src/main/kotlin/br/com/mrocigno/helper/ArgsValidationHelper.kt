package br.com.mrocigno.helper

import br.com.mrocigno.model.ReportType
import java.io.File

class ArgsValidationHelper(private vararg val args: String) {

    val file: File get() = args.getArg("-f", "--file")!!.run {
        File(this)
    }

    val type: ReportType get() = args.getArg("-t", "--type")?.let(ReportType::get) ?: ReportType.JSON

    private val validArgs: MutableMap<String, String> = mutableMapOf(
        "-h" to "",
        "--help" to "print help message\n",
        "-f" to "",
        "--file" to "path to file that should be read\n",
        "-t" to "",
        "--type" to "indicate the print type [json | report], the application uses json by default\n"
    )

    fun hasInvalidArg(action: (invalidArg: String) -> Unit) {
        for (key in args.filter { it.startsWith("-") }) {
            if (!validArgs.contains(key)) action.invoke(key)
        }
    }

    fun hasFileArg(action: () -> Unit) {
        args.getArg("-f", "--file") ?: action.invoke()
    }

    fun shouldShowHelp(action: () -> Unit) {
        if (args.contains("-h") || args.contains("--help")) {
            validArgs.toList().forEach { (key, value) ->
                println("$key\t\t\t$value")
            }
            action.invoke()
        }
    }

    private fun Array<out String>.getArg(key: String, variant: String): String? {
        indices.forEach { index ->
            if ((this[index] == key || this[index] == variant) && index + 1 < size) {
                return this[index + 1]
            }
        }
        return null
    }
}