package br.com.mrocigno.helper

import br.com.mrocigno.helper.AllowedTags.shouldComputeDeath
import br.com.mrocigno.helper.AllowedTags.shouldUpdateClientName
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
}