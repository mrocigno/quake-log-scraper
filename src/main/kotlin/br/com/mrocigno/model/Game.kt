package br.com.mrocigno.model

import br.com.mrocigno.helper.GameScrapperHelper
import br.com.mrocigno.sortByValue
import com.google.gson.annotations.SerializedName

data class Games(
    @SerializedName("total_games") val totalGames: Int,
    @SerializedName("games") val games: List<Game>
) {

    constructor(games: List<GameScrapperHelper>) : this(
        totalGames = games.size,
        games = games.map(::Game)
    )
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