package br.com.mrocigno.quake.helper

import br.com.mrocigno.extractId
import br.com.mrocigno.increment
import br.com.mrocigno.quake.model.Constants
import br.com.mrocigno.quake.model.MeanOfDeath
import org.koin.core.component.KoinComponent

/***
 * Create a game container, here will contain the info about one game
 *
 * @param logs list of strings read from log file
 */
class GameScrapperHelper(logs: List<String>) : KoinComponent {

    val meansOfDeath: MutableMap<MeanOfDeath, Int> = MeanOfDeath.values().associateWith { 0 }.toMutableMap()
    val totalKill: Int get() = meansOfDeath.entries.sumOf { (_, value) -> value }
    val playersList: List<String> get() = clients.playersList
    val playersRank: Map<String, Int> get() = clients.playersRank

    // here there is no need to be injected because they are dependent classes
    private val clients: ClientScrapperHelper = ClientScrapperHelper()

    init {
         for (log in logs) {
            if (log.updateClient()) continue
            if (log.computeDeath()) continue
        }
    }

    /***
     * Check if the current line is a Kill log
     * if the current line actually is a Kill log, compute score with extracted data
     */
    private fun String.computeDeath(): Boolean {
        val shouldComputeDeath = runCatching { substring(0, 20).contains(Constants.KILL_TAG) }.getOrElse { false }
        if (!shouldComputeDeath) return false

        return """${Constants.KILL_TAG}: (\d+) (\d+) (\d+)"""
            .toRegex()
            .find(this)
            ?.let {
                val killerId = it.extractId(1)
                val deadId = it.extractId(2)
                val meanOfDeath = it.extractId(3).let(MeanOfDeath::findById)

                clients.computeDeath(killerId, deadId)
                meansOfDeath.increment(meanOfDeath)

                true
            } ?: false
    }

    /***
     * Check if the current line is a ClientUserinfoChanged log
     * if the current line actually is a ClientUserinfoChanged log, update client with extracted data
     */
    private fun String.updateClient(): Boolean {
        if (!contains(Constants.CLIENT_INFO_TAG)) return false

        return """${Constants.CLIENT_INFO_TAG}:\s(\d+)\sn\\(.+?)[!\t\\]"""
            .toRegex()
            .find(this)
            ?.let {
                val id = it.extractId(1)
                val name = it.groupValues[2]

                clients.update(id, name)

                true
            } ?: false
    }
}