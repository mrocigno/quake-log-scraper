package br.com.mrocigno.helper

import br.com.mrocigno.extractId
import br.com.mrocigno.helper.ClientScrapperHelper.Companion.shouldUpdateClientName
import br.com.mrocigno.increment
import br.com.mrocigno.model.MeanOfDeath

/***
 * Create a game container, here will contain the info about one game
 *
 * @param logs list of strings read from log file
 */
class GameScrapperHelper(logs: List<String>) {

    val clients: ClientScrapperHelper = ClientScrapperHelper()
    val meansOfDeath: MutableMap<MeanOfDeath, Int> = MeanOfDeath.values().associateWith { 0 }.toMutableMap()
    val totalKill: Int get() = meansOfDeath.entries.sumOf { (_, value) -> value }

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

        const val WORLD_KILLER_ID: Int = 1022
        const val KILL_TAG: String = "Kill"

        private const val INIT_GAME_TAG: String = "InitGame"

        // Get first 20 chars to make sure that's not a play named InitGame
        fun isGameInitialization(logLine: String): Boolean =
            runCatching { logLine.substring(0, 20).contains(INIT_GAME_TAG) }.getOrElse { false }

        /***
         * Check if the current line is a Kill log
         * if the current line actually is a Kill log, will run the callback with extracted data
         *
         * @param log the current line
         * @param action extracted data callback
         */
        inline fun shouldComputeDeath(log: String, action: (killerId: Int, deadId: Int, meanOfDeath: MeanOfDeath) -> Unit) {
            val shouldComputeDeath = runCatching { log.substring(0, 20).contains(KILL_TAG) }.getOrElse { false }
            if (!shouldComputeDeath) return

            """$KILL_TAG: (\d+) (\d+) (\d+)"""
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