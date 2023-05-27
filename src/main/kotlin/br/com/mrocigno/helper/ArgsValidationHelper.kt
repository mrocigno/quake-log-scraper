package br.com.mrocigno.helper

import java.io.File

class ArgsValidationHelper(val args: Array<String>) {

    val validArgs: MutableMap<String, String> = mutableMapOf(
        "-h" to "",
        "--help" to "print help message\n",
        "-f" to "",
        "--file" to "path to file that should be read\n",
        "-t" to "",
        "--type" to "indicate the print type [json | report]\n"
    )

    val file: File get() = args.getArg("-f", "--file")!!.run {
        File(this)
    }

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

    fun Array<String>.getArg(key: String, variant: String): String? {
        indices.forEach { index ->
            if ((this[index] == key || this[index] == variant) && index + 1 < size) {
                return this[index + 1]
            }
        }
        return null
    }
}