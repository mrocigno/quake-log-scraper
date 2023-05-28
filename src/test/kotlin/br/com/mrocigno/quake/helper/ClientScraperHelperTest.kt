package br.com.mrocigno.quake.helper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ClientScraperHelperTest {

    private lateinit var clientScraperHelper: ClientScraperHelper

    @BeforeEach
    fun setUp() {
        clientScraperHelper = ClientScraperHelper()

        clientScraperHelper.update(1, "loki")
        clientScraperHelper.update(2, "lola")
    }

    @Test
    fun `check if player list return single names`() {
        assertEquals(listOf("loki", "lola"), clientScraperHelper.playersList)
    }

    @Test
    fun `checks players after adding duplicity`() {
        clientScraperHelper.update(3, "roberta")
        clientScraperHelper.update(3, "roberta")

        assertEquals(listOf("loki", "lola", "roberta"), clientScraperHelper.playersList)
    }

    @Test
    fun `check score after kill computed`() {
        clientScraperHelper.computeDeath(1, 2)

        assertEquals(mapOf(
            "loki" to 1,
            "lola" to 0
        ), clientScraperHelper.playersRank)
    }
}