package br.com.mrocigno.helper

import br.com.mrocigno.helper.AllowedTags.WORLD_KILLER_ID

/***
 * A helper class that concentrate the business logic of score points
 */
class ClientScrapperHelper {

    val playersList: List<String> get() = clients.map { it.name }
    val playerRank: Map<String, Int> get() = clients.associate { it.name to it.killCount }

    private val currentPlayerId: HashMap<Int, Client> = hashMapOf()
    private val clients: MutableSet<Client> = mutableSetOf()

    /***
     * Check if the client is already registered, if it is then update de id, if not then create new client
     *
     * @param id client id
     * @param name client name
     */
    fun update(id: Int, name: String) {
        val client = this[name] ?: Client(name)
        currentPlayerId[id] = client

        // A Set collection does not allow duplicate elements
        clients.add(client)
    }

    /***
     * Find the killer and the dead models to increment 1 to the score
     * If the killer was the <world> so we take 1 point
     *
     * @param killerId client id that score
     * @param deadId client id that was killed
     */
    fun computeDeath(killerId: Int, deadId: Int) {
        currentPlayerId[killerId]?.run { killCount++ }
        currentPlayerId[deadId]?.run {
            deathCount++
            if (killerId == WORLD_KILLER_ID) killCount--
        }
    }

    operator fun get(name: String): Client? =
        clients.firstOrNull { it.name == name }
}

class Client(
    var name: String = "",
    var killCount: Int = 0,
    var deathCount: Int = 0
)
