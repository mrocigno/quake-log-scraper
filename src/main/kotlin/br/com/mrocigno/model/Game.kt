package br.com.mrocigno.model

import br.com.mrocigno.model.Clients.Companion.shouldCreateClient
import br.com.mrocigno.model.Clients.Companion.shouldDisconnectClient
import br.com.mrocigno.model.Clients.Companion.shouldUpdateClientName
import br.com.mrocigno.model.Death.Companion.shouldComputeDeath

class Game(logs: List<String>) {

    val clients: Clients = Clients()
    val meansOfDeath = hashMapOf<MeanOfDeath, Int>()

    init {
        clients.use {
            logs.forEach logLooper@ { log ->

                shouldCreateClient(log) { id ->
                    clients.create(id)
                    return@logLooper
                }

                shouldUpdateClientName(log) { id, name ->
                    clients.update(id, name)
                    return@logLooper
                }

                shouldDisconnectClient(log) { id, time ->
                    clients.disconnect(id, time)
                    return@logLooper
                }

                shouldComputeDeath(log) { killerId, deadId, meanOfDeath ->
                    clients.computeDeath(killerId, deadId)
                    meansOfDeath[meanOfDeath] = (meansOfDeath[meanOfDeath] ?: 0) + 1
                    return@logLooper
                }
            }
        }
    }

    companion object {

        const val WORLD_KILLER_ID = 1022

        private const val INIT_GAME_TAG = "InitGame"

        // Get first 20 chars to make sure that's not a play named InitGame
        fun isGameInitialization(logLine: String): Boolean =
            logLine.substring(0, 20).contains(INIT_GAME_TAG)
    }
}