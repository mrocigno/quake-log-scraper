package br.com.mrocigno.model

import br.com.mrocigno.extractId
import java.io.Closeable

class Clients : Closeable {

    private val clients = mutableListOf<Client>()

    fun create(id: Int) {
        clients.add(Client(id))
    }

    fun update(id: Int, name: String) {
        this[name]?.run {
            this.id = id
            this.disconnected = false
            return
        }

        this[id]?.run {
            this.name = name
            return
        }
    }

    fun disconnect(id: Int, time: String) {
        this[id]?.run {
            disconnected = true
            disconnectionTime = time
        }
    }

    fun computeDeath(killerId: Int, deadId: Int) {
        this[killerId]?.run { killCount++ }
        this[deadId]?.run {
            deathCount++
            if (killerId == Game.WORLD_KILLER_ID) killCount--
        }
    }

    operator fun get(id: Int): Client? =
        clients.firstOrNull { it.id == id && !it.disconnected }

    operator fun get(name: String): Client? =
        clients.firstOrNull { it.name == name }

    fun print() {
        clients.forEach {
            println("${it.id}: ${it.name} ${it.killCount}/${it.deathCount}")
//            println("${it.id}: ${it.name} ${if (it.disconnected) " -- disconnected at: ${it.disconnectionTime}" else ""}")
        }
    }

    override fun close() {
        clients.removeAll { it.name == "" }
    }

    companion object {

        // Must be public to use inside inline functions
        const val CLIENT_CONNECT_TAG = "ClientConnect"
        const val CLIENT_INFO_TAG = "ClientUserinfoChanged"
        const val CLIENT_DISCONNECT_TAG = "ClientDisconnect"

        inline fun shouldCreateClient(log: String, action: (id: Int) -> Unit): Unit? {
            if (!log.contains(CLIENT_CONNECT_TAG)) return null

            return """$CLIENT_CONNECT_TAG:\s(\d+)"""
                .toRegex()
                .find(log)
                ?.let {
                    val id = it.extractId(1)
                    action.invoke(id)
                }
        }

        inline fun shouldUpdateClientName(log: String, action: (id: Int, name: String) -> Unit) {
            if (!log.contains(CLIENT_INFO_TAG)) return

            """$CLIENT_INFO_TAG:\s(\d+)\sn\\(.+?)\\t\\"""
                .toRegex()
                .find(log)
                ?.let {
                    val id = it.extractId(1)
                    val name = it.groupValues[2]

                    action.invoke(id, name)
                }
        }

        inline fun shouldDisconnectClient(log: String, action: (id: Int, time: String) -> Unit) {
            if (!log.contains(CLIENT_DISCONNECT_TAG)) return

            """(\d+:\d+)\s$CLIENT_DISCONNECT_TAG:\s(\d+)"""
                .toRegex()
                .find(log)
                ?.let {
                    val time = it.groupValues[1]
                    val id = it.extractId(2)

                    action.invoke(id, time)
                }
        }
    }
}

class Client(
    var id: Int,
    var name: String = "",
    var disconnected: Boolean = false,
    var disconnectionTime: String? = null,
    val deaths: MutableList<Death> = mutableListOf()
) {

    var killCount: Int = 0
    var deathCount: Int = 0

    override fun toString() = "$id: $name"
}
