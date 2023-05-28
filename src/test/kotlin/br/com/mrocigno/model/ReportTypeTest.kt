package br.com.mrocigno.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ReportTypeTest {

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
            )
        )
    )

    @Test
    fun `check json transformation`() {
        val json = ReportType.JSON.transform(games)
        assertEquals(json, """
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
                }
              ]
            }
        """.trimIndent())
    }

    @Test
    fun `check report transformation`() {
        val report = ReportType.REPORT.transform(games)
        println(report)
        assertEquals(report, """
            result of 1 game(s)
            total kills: 5
            
            |--------------------------|
            |       Leaderboard        |
            |-----|----------|---------|
            |     | Player   |  Score  |
            |-----|----------|---------|
            |   1 | lola     |    3    |
            |   2 | loki     |    2    |
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
            

        """.trimIndent())
    }
}