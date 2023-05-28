package br.com.mrocigno.quake.model

import br.com.mrocigno.common.ReportModel
import br.com.mrocigno.common.table
import br.com.mrocigno.quake.helper.GameScrapperHelper
import br.com.mrocigno.sortByValue
import br.com.mrocigno.toJson
import com.google.gson.annotations.SerializedName
import javax.swing.GroupLayout.Alignment.CENTER
import javax.swing.GroupLayout.Alignment.TRAILING

class QuakeGameReportModel(
    @SerializedName("total") val total: Int,
    @SerializedName("list") val list: List<Game>
) : ReportModel {

    constructor(games: List<GameScrapperHelper>) : this(
        total = games.size,
        list = games.map(::Game)
    )

    override fun asJson(): String = this.toJson().orEmpty()

    override fun asReport(): String {
        val builder = StringBuilder()

        builder.appendLine("result of $total game(s)")
        builder.appendLine("total kills: ${list.sumOf { it.totalKills }}")
        builder.appendLine()
        builder.appendLine(
            table {
                header {
                    line {
                        cell(
                            content = "Leaderboard",
                            span = 3,
                            alignment = CENTER
                        )
                    }
                }
                header {
                    line {
                        cell("")
                        cell("Player")
                        cell(
                            content = "Score",
                            alignment = CENTER
                        )
                    }
                }
                var position = 1
                leaderboard().forEach { (key, value) ->
                    line {
                        cell(
                            content = (position++).toString(),
                            alignment = TRAILING
                        )
                        cell(key)
                        cell(value.toString(), alignment = CENTER)
                    }
                }
            }
        )

        builder.appendLine("=========  score individual game =========")
        builder.appendLine()

        list.forEachIndexed { index, game ->
            builder.appendLine(
                table {
                    header {
                        line {
                            cell(
                                content = "Game number ${index + 1}",
                                span = 3,
                                alignment = CENTER
                            )
                        }
                    }
                    header {
                        line {
                            cell("")
                            cell("Player")
                            cell(
                                content = "Score",
                                alignment = CENTER
                            )
                        }
                    }
                    var position = 1
                    game.kills.sortByValue().forEach { (player, score) ->
                        line {
                            cell(
                                content = (position++).toString(),
                                alignment = TRAILING
                            )
                            cell(player)
                            cell(
                                content = score.toString(),
                                alignment = CENTER
                            )
                        }
                    }
                }
            )
        }

        return builder.toString()
    }

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
        players = helper.playersList,
        kills = helper.playersRank.sortByValue(),
        killsByMeans = helper.meansOfDeath.sortByValue()
    )
}