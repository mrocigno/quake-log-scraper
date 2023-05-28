package br.com.mrocigno.quake.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class QuakeGameReportModelTest {

    private val games = QuakeGameReportModel(
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
    fun `check json transformation`() {
        assertEquals("""
            {
              "total": 1,
              "list": [
                {
                  "total_kills": 5,
                  "players": [
                    "lola",
                    "loki"
                  ],
                  "kills": {
                    "lola": 3,
                    "loki": 2
                  },
                  "kills_by_means": {
                    "MOD_SHOTGUN": 3,
                    "MOD_CRUSH": 2
                  }
                },
                {
                  "total_kills": 6,
                  "players": [
                    "lola",
                    "loki"
                  ],
                  "kills": {
                    "loki": 5,
                    "lola": 1
                  },
                  "kills_by_means": {
                    "MOD_SHOTGUN": 6
                  }
                }
              ]
            }
        """.trimIndent(), games.asJson())
    }

    @Test
    fun `check report transformation`() {
        assertEquals("""
            result of 1 game(s)
            total kills: 11

            |--------------------------|
            |       Leaderboard        |
            |-----|----------|---------|
            |     | Player   |  Score  |
            |-----|----------|---------|
            |   1 | loki     |    7    |
            |   2 | lola     |    4    |
            |--------------------------|

            =========  score individual game =========

            |--------------------------|
            |      Game number 1       |
            |-----|----------|---------|
            |     | Player   |  Score  |
            |-----|----------|---------|
            |   1 | lola     |    3    |
            |   2 | loki     |    2    |
            |--------------------------|

            |--------------------------|
            |      Game number 2       |
            |-----|----------|---------|
            |     | Player   |  Score  |
            |-----|----------|---------|
            |   1 | loki     |    5    |
            |   2 | lola     |    1    |
            |--------------------------|


        """.trimIndent(), games.asReport())
    }

    @Test
    fun `check leaderboard sum`() {
        assertEquals(mapOf(
            "loki" to 7,
            "lola" to 4
        ), games.leaderboard())
    }
}