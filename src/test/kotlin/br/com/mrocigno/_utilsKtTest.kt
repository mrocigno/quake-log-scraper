package br.com.mrocigno

import br.com.mrocigno.helper.AllowedTags
import br.com.mrocigno.helper.ClientScrapperHelper
import br.com.mrocigno.helper.GameScrapperHelper
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertEquals

class _utilsKtTest {

    @BeforeEach
    fun setUp() {
        startKoin {
            modules(module {
                single<ClientScrapperHelper> { mockk() }
            })
        }
    }

    @Test
    fun `check if commit add new object to list`() {
        val gamesHelper: MutableList<GameScrapperHelper> = mutableListOf()
        val logs: List<String> = listOf("log1", "log2")

        gamesHelper.commit(logs)
        assertEquals(1, gamesHelper.size)
    }

    @Test
    fun `check if ids is correctly extracted from string`() {
        val log = "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT"

        """${AllowedTags.KILL_TAG}: (\d+) (\d+) (\d+)"""
            .toRegex()
            .find(log)!!
            .let {
                assertEquals(1022, it.extractId(1))
                assertEquals(2, it.extractId(2))
                assertEquals(22, it.extractId(3))
            }
    }

    @Test
    fun `check map value increment`() {
        val map: MutableMap<String, Int> = mutableMapOf(
            "loki" to 1,
            "lola" to 0
        )

        map.increment("lola")

        assertEquals(mapOf(
            "loki" to 1,
            "lola" to 1
        ), map)
    }

    @Test
    fun `check json transformation`() {
        val map: Map<String, Int> = mapOf(
            "loki" to 1,
            "lola" to 0
        )

        val json = map.toJson()
        assertEquals("""
            {
              "loki": 1,
              "lola": 0
            }
        """.trimIndent(), json)
    }

    @Test
    fun `check sort by value, should be descending order`() {
        val map: Map<String, Int> = mapOf(
            "lola" to 0,
            "loki" to 1
        )

        val sorted = map.sortByValue()
        assertEquals(mapOf(
            "loki" to 1,
            "lola" to 0
        ), sorted)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }
}