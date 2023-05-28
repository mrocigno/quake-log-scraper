package br.com.mrocigno.quake.helper

import br.com.mrocigno.quake.model.MeanOfDeath
import br.com.mrocigno.quake.model.MeanOfDeath.MOD_ROCKET_SPLASH
import br.com.mrocigno.quake.model.MeanOfDeath.MOD_TRIGGER_HURT
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameScrapperHelperTest {

    private lateinit var helper: GameScrapperHelper
    private val logs = listOf(
        "20:34 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian",
        "20:37 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\uriel/zael\\hmodel\\uriel/",
        "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
        "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
        "21:53 ClientUserinfoChanged: 3 n\\Mocinha\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0",
        "22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH"
    )

    @BeforeEach
    fun setUp() {
        helper = GameScrapperHelper(logs)
    }

    @Test
    fun getMeansOfDeath() {
        val expected = MeanOfDeath.values().associateWith { 0 }.toMutableMap()
        expected[MOD_TRIGGER_HURT] = 2
        expected[MOD_ROCKET_SPLASH] = 1

        assertEquals(expected, helper.meansOfDeath)
    }

    @Test
    fun getTotalKill() {
        assertEquals(3, helper.totalKill)
    }
}