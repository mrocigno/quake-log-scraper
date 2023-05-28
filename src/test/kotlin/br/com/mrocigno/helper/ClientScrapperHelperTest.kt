package br.com.mrocigno.helper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ClientScrapperHelperTest {

    private lateinit var clientScrapperHelper: ClientScrapperHelper

    @BeforeEach
    fun setUp() {
        clientScrapperHelper = ClientScrapperHelper()

        clientScrapperHelper.update(1, "loki")
        clientScrapperHelper.update(2, "lola")
    }

    @Test
    fun `check if player list return single names`() {
        assertEquals(listOf("loki", "lola"), clientScrapperHelper.playersList)
    }

    @Test
    fun `checks players after adding duplicity`() {
        clientScrapperHelper.update(3, "roberta")
        clientScrapperHelper.update(3, "roberta")

        assertEquals(listOf("loki", "lola", "roberta"), clientScrapperHelper.playersList)
    }

    @Test
    fun `check score after kill computed`() {
        clientScrapperHelper.computeDeath(1, 2)

        assertEquals(mapOf(
            "loki" to 1,
            "lola" to 0
        ), clientScrapperHelper.playersRank)
    }
}