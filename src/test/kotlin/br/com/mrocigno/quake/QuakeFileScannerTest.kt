package br.com.mrocigno.quake

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class QuakeFileScannerTest {

    private val logs = sequenceOf(
        "20:37 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate",
        "20:34 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian",
        "20:37 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\uriel/zael\\hmodel\\uriel/",
        "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
        "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
        "21:53 ClientUserinfoChanged: 3 n\\Mocinha\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0",
        "22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH",
        "20:37 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate",
        "20:34 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian",
        "20:37 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\uriel/zael\\hmodel\\uriel/",
        "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
        "20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT",
        "21:53 ClientUserinfoChanged: 3 n\\Mocinha\\t\\0\\model\\sarge\\hmodel\\sarge\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0",
        "22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH"
    )

    @Test
    fun `check log transformation`() {
        val fileScanner = QuakeFileScanner()
        fileScanner.exposedTransform(logs)

        assertEquals(2, fileScanner.exposedGamesHelper().size)
    }
}