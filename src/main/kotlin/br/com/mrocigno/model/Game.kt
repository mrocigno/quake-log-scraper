package br.com.mrocigno.model

import br.com.mrocigno.helper.GameScrapperHelper
import br.com.mrocigno.sortByValue
import com.google.gson.annotations.SerializedName

data class Games(
    @SerializedName("total") val total: Int,
    @SerializedName("list") val list: List<Game>
) {

    constructor(games: List<GameScrapperHelper>) : this(
        total = games.size,
        list = games.map(::Game)
    )

    fun leaderboard(): Map<String, Int> {
        val result = mutableMapOf<String, Int>()

        list.forEach {
            it.kills.forEach { (key, value) ->
                result.merge(key, value) { old, new -> old + new }
            }
        }

        return result.sortByValue()
    }
}

data class Game(
    @SerializedName("total_kills") val totalKills: Int,
    @SerializedName("players") val players: List<String>,
    @SerializedName("kills") val kills: Map<String, Int>,
    @SerializedName("kills_by_means") val killsByMeans: Map<MeanOfDeath, Int>
) {

    constructor(helper: GameScrapperHelper) : this(
        totalKills = helper.totalKill,
        players = helper.clients.playersList,
        kills = helper.clients.playerRank.sortByValue(),
        killsByMeans = helper.meansOfDeath.sortByValue()
    )
}