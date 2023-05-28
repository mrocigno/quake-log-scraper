package br.com.mrocigno.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameTest {

    private val games = Games(
        total = 1,
        list = listOf(
            Game(
                totalKills = 5,
                players = listOf("lola", "loki"),
                kills = mapOf(
                    "lola" to 3,
                    "loki" to 2
                ),
                killsByMeans = mapOf(
                    MeanOfDeath.MOD_SHOTGUN to 3,
                    MeanOfDeath.MOD_CRUSH to 2
                )
            ),
            Game(
                totalKills = 6,
                players = listOf("lola", "loki"),
                kills = mapOf(
                    "loki" to 5,
                    "lola" to 1
                ),
                killsByMeans = mapOf(
                    MeanOfDeath.MOD_SHOTGUN to 6
                )
            )
        )
    )

    @Test
    fun `check leaderboard sum`() {
        assertEquals(mapOf(
            "loki" to 7,
            "lola" to 4
        ), games.leaderboard())
    }
}