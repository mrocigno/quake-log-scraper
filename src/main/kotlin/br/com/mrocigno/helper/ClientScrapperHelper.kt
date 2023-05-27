package br.com.mrocigno.helper

import br.com.mrocigno.extractId

/***
 * A helper class that concentrate the business logic of score points
 */
class ClientScrapperHelper {

    val playersList: List<String> get() = clients.map { it.name }
    val playerRank: Map<String, Int> get() = clients.associate { it.name to it.killCount }

    private val clients: MutableList<Client> = mutableListOf()

    /***
     * Check if the client is already registered, if it is then update de id, if not then create new client
     *
     * @param id client id
     * @param name client name
     */
    fun update(id: Int, name: String) {
        this[name]?.run {
            this.id = id
            return
        }

        clients.add(Client(id, name))
    }

    /***
     * Find the killer and the dead models to increment 1 to the score
     * If the killer was the <world> so we take 1 point
     *
     * @param killerId client id that score
     * @param deadId client id that was killed
     */
    fun computeDeath(killerId: Int, deadId: Int) {
        this[killerId]?.run { killCount++ }
        this[deadId]?.run {
            deathCount++
            if (killerId == GameScrapperHelper.WORLD_KILLER_ID) killCount--
        }
    }

    operator fun get(id: Int): Client? =
        clients.firstOrNull { it.id == id }

    operator fun get(name: String): Client? =
        clients.firstOrNull { it.name == name }

    companion object {

        // Must be public to use inside inline functions
        const val CLIENT_INFO_TAG = "ClientUserinfoChanged"

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
}

class Client(
    var id: Int,
    var name: String = "",
    var killCount: Int = 0,
    var deathCount: Int = 0
)
