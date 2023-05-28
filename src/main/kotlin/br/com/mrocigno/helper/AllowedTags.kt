package br.com.mrocigno.helper

import br.com.mrocigno.extractId
import br.com.mrocigno.model.MeanOfDeath

object AllowedTags {

    const val WORLD_KILLER_ID: Int = 1022
    const val KILL_TAG: String = "Kill"
    const val CLIENT_INFO_TAG: String = "ClientUserinfoChanged"

    private const val INIT_GAME_TAG: String = "InitGame"

    /***
     * Check if is a game initialization log line
     *
     * @param log the current line
     */
    fun isGameInitialization(log: String): Boolean =
        // Get first 20 chars to make sure that's not a play named InitGame
        runCatching { log.substring(0, 20).contains(INIT_GAME_TAG) }.getOrElse { false }

    /***
     * Check if log line is relevant
     *
     * @param log the current line
     */
    fun isTagAllowed(log: String): Boolean =
        log.contains(CLIENT_INFO_TAG) || log.contains(KILL_TAG)

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

    /***
     * Check if the current line is a ClientUserinfoChanged log
     * if the current line actually is a ClientUserinfoChanged log, will run the callback with extracted data
     *
     * @param log the current line
     * @param action extracted data callback
     */
    inline fun shouldUpdateClientName(log: String, action: (id: Int, name: String) -> Unit) {
        if (!log.contains(CLIENT_INFO_TAG)) return

        """$CLIENT_INFO_TAG:\s(\d+)\sn\\(.+?)[!\t\\]"""
            .toRegex()
            .find(log)
            ?.let {
                val id = it.extractId(1)
                val name = it.groupValues[2]

                action.invoke(id, name)
            }
    }
}