package br.com.mrocigno

import br.com.mrocigno.model.Game
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val file = File("C:\\Users\\Matheus\\Documents\\projects\\quake-log-scrapper\\assets\\qgames.log")

    if (!file.exists()) {
        println("O arquivo ${file.name} não encontrado")
        // Ending program with status 1 to indicate error
        exitProcess(1)
    }

    val games = mutableListOf<Game>()

    // Read line by line and close on end of lambda
    file.bufferedReader().useLines {
        var creator: MutableList<String>? = null

        it.forEach { line ->

            when {
                Game.isGameInitialization(line) -> {
                    creator?.run {
                        games.add(Game(this))
                    }
                    creator = mutableListOf()
                }
                else -> creator?.add(line)
            }
        }
    }


    println("Total games: ${games.size}")
    games.forEachIndexed { index, game ->
        println("===================")
        println("Game number $index")
        game.clients.print()
    }
}