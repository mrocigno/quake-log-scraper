package br.com.mrocigno.helper

import br.com.mrocigno.model.MeanOfDeath
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AllowedTagsTest {

    @Test
    fun `check if first 20 chars has InitGame`() {
        val validLogLine = "20:37 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate"
        assertTrue {
            AllowedTags.isGameInitialization(validLogLine)
        }

        val invalidLogLine = "20:38 ClientUserinfoChanged: 2 n\\InitGame\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael"
        assertFalse {
            AllowedTags.isGameInitialization(invalidLogLine)
        }
    }

    @Test
    fun `check if expected tag is on current line`() {
        val validLogLine1 = "20:38 ClientUserinfoChanged: 2 n\\InitGame\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael"
        val validLogLine2 = "21:07 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT"

        assertTrue { AllowedTags.isTagAllowed(validLogLine1) }
        assertTrue { AllowedTags.isTagAllowed(validLogLine2) }

        val invalidLogLine1 = "20:40 Item: 2 ammo_rockets"
        val invalidLogLine2 = "20:38 ClientConnect: 2"

        assertFalse { AllowedTags.isTagAllowed(invalidLogLine1) }
        assertFalse { AllowedTags.isTagAllowed(invalidLogLine2) }
    }

    @Test
    fun `check if current log line is a kill log and then run the callback`() {
        val callback: (killerId: Int, deadId: Int, meanOfDeath: MeanOfDeath) -> Unit = mockk()
        val validLogLine = "21:07 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT"
        every { callback.invoke(any(), any(), any()) } answers { }
        AllowedTags.shouldComputeDeath(validLogLine, callback)
        verify { callback.invoke(any(), any(), any()) }

        val callback2: (killerId: Int, deadId: Int, meanOfDeath: MeanOfDeath) -> Unit = mockk()
        val invalidLogLine = "20:40 Item: 2 ammo_rockets"
        every { callback2.invoke(any(), any(), any()) } answers { }
        AllowedTags.shouldComputeDeath(invalidLogLine, callback2)
        verify { callback2 wasNot Called }
    }

    @Test
    fun `check if current log line is a client update log and then run the callback`() {
        val callback: (id: Int, name: String) -> Unit = mockk()
        val validLogLine = "20:38 ClientUserinfoChanged: 2 n\\InitGame\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael"
        every { callback.invoke(any(), any()) } answers { }
        AllowedTags.shouldUpdateClientName(validLogLine, callback)
        verify { callback.invoke(any(), any()) }

        val callback2: (id: Int, name: String) -> Unit = mockk()
        val invalidLogLine = "21:07 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT"
        every { callback2.invoke(any(), any()) } answers { }
        AllowedTags.shouldUpdateClientName(invalidLogLine, callback2)
        verify { callback2 wasNot Called }
    }
}