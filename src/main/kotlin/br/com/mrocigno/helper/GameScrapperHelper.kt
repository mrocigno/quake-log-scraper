package br.com.mrocigno.helper

import br.com.mrocigno.extractId
import br.com.mrocigno.helper.ClientScrapperHelper.Companion.shouldUpdateClientName
import br.com.mrocigno.increment
import br.com.mrocigno.model.MeanOfDeath

class GameScrapperHelper(logs: List<String>) {

    val clients: ClientScrapperHelper = ClientScrapperHelper()
    val meansOfDeath: MutableMap<MeanOfDeath, Int> = MeanOfDeath.values().associateWith { 0 }.toMutableMap()
    val totalKill get() = meansOfDeath.entries.sumOf { (_, value) -> value }

    init {
        logs.forEach logLooper@ { log ->

            shouldUpdateClientName(log) { id, name ->
                clients.update(id, name)
                return@logLooper
            }

            shouldComputeDeath(log) { killerId, deadId, meanOfDeath ->
                clients.computeDeath(killerId, deadId)
                meansOfDeath.increment(meanOfDeath)
                return@logLooper
            }
        }

    }

    companion object {

        const val WORLD_KILLER_ID = 1022
        const val KILL_TAG = "Kill"

        private const val INIT_GAME_TAG = "InitGame"

        // Get first 20 chars to make sure that's not a play named InitGame
        fun isGameInitialization(logLine: String): Boolean =
            logLine.substring(0, 20).contains(INIT_GAME_TAG)

        inline fun shouldComputeDeath(log: String, action: (killerId: Int, deadId: Int, meanOfDeath: MeanOfDeath) -> Unit) {
            if (!log.substring(0, 20).contains(KILL_TAG)) return

            """Kill: (\d+) (\d+) (\d+)"""
                .toRegex()
                .find(log)
                ?.let {
                    val killerId = it.extractId(1)
                    val deadId = it.extractId(2)
                    val meanOfDeath = it.extractId(3).let(MeanOfDeath::findById)

                    action.invoke(killerId, deadId, meanOfDeath)
                }
        }
    }
}