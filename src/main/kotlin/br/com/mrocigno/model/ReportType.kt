package br.com.mrocigno.model

import br.com.mrocigno.helper.table
import br.com.mrocigno.sortByValue
import br.com.mrocigno.toJson
import javax.swing.GroupLayout.Alignment

enum class ReportType(val transform: (Games) -> String) {
    JSON({ games ->
        games.toJson().orEmpty()
    }),
    REPORT({ games ->
        val builder = StringBuilder()

        builder.appendLine("result of ${games.total} game(s)")
        builder.appendLine("total kills: ${games.list.sumOf { it.totalKills }}")
        builder.appendLine()
        builder.appendLine(
            table {
                header {
                    line {
                        cell(
                            content = "Leaderboard",
                            span = 3,
                            alignment = Alignment.CENTER
                        )
                    }
                }
                header {
                    line {
                        cell("")
                        cell("Player")
                        cell(
                            content = "Score",
                            alignment = Alignment.CENTER
                        )
                    }
                }
                var position = 1
                games.leaderboard().forEach { (key, value) ->
                    line {
                        cell(
                            content = (position++).toString(),
                            alignment = Alignment.TRAILING
                        )
                        cell(key)
                        cell(value.toString(), alignment = Alignment.CENTER)
                    }
                }
            }
        )

        builder.appendLine("=========  score individual game =========")
        builder.appendLine()

        games.list.forEachIndexed { index, game ->
            builder.appendLine(
                table {
                    header {
                        line {
                            cell(
                                content = "Game number ${index + 1}",
                                span = 3,
                                alignment = Alignment.CENTER
                            )
                        }
                    }
                    header {
                        line {
                            cell("")
                            cell("Player")
                            cell(
                                content = "Score",
                                alignment = Alignment.CENTER
                            )
                        }
                    }
                    var position = 1
                    game.kills.sortByValue().forEach { (player, score) ->
                        line {
                            cell(
                                content = (position++).toString(),
                                alignment = Alignment.TRAILING
                            )
                            cell(player)
                            cell(
                                content = score.toString(),
                                alignment = Alignment.CENTER
                            )
                        }
                    }
                }
            )
        }

        builder.toString()
    });

    companion object {

        fun get(name: String): ReportType? =
            ReportType.values().find { it.name.lowercase() == name.lowercase() }
    }
}