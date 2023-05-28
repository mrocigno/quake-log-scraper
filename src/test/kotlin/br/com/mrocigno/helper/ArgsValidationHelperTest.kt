package br.com.mrocigno.helper

import br.com.mrocigno.model.ReportType
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ArgsValidationHelperTest {

    @Test
    fun `check if return file with two args mapped`() {
        val helper1 = ArgsValidationHelper("-f", "/x/")
        assertNotNull(helper1.file)

        val helper2 = ArgsValidationHelper("--file", "/x/")
        assertNotNull(helper2.file)
    }

    @Test
    fun `check type from args, if not defined, should return JSON as default`() {
        val helper1 = ArgsValidationHelper("--type", "report")
        val helper2 = ArgsValidationHelper("-t", "json")
        val helper3 = ArgsValidationHelper()

        assertEquals(ReportType.REPORT, helper1.type)
        assertEquals(ReportType.JSON, helper2.type)
        assertEquals(ReportType.JSON, helper3.type)
    }

    @Test
    fun `check if has invalid args, if have, should run callback`() {
        val helperInvalidArgs = ArgsValidationHelper("-g", "topper", "-f", "/x/")
        val callbackInvalid: (invalidArg: String) -> Unit = mockk()
        every { callbackInvalid.invoke(any()) } answers { }
        helperInvalidArgs.hasInvalidArg(callbackInvalid)
        verify { callbackInvalid.invoke("-g") }

        val helperValidArgs = ArgsValidationHelper("-f", "/x/", "-t", "json", "-h")
        val callbackValid: (invalidArg: String) -> Unit = mockk()
        every { callbackValid.invoke(any()) } answers { }
        helperValidArgs.hasInvalidArg(callbackValid)
        verify { callbackValid wasNot Called }
    }

    @Test
    fun `check if required file arg, if not defined, should run callback`() {
        val helperInvalid = ArgsValidationHelper()
        val callbackInvalid: () -> Unit = mockk()
        every { callbackInvalid.invoke() } answers { }
        helperInvalid.hasFileArg(callbackInvalid)
        verify { callbackInvalid.invoke() }

        val helperValid = ArgsValidationHelper("-f", "/x/")
        val callbackValid: () -> Unit = mockk()
        every { callbackValid.invoke() } answers { }
        helperValid.hasFileArg(callbackValid)
        verify { callbackValid wasNot Called }
    }

    @Test
    fun `check if has help arg, if have, should run callback`() {
        val helperInvalid = ArgsValidationHelper()
        val callbackInvalid: () -> Unit = mockk()
        every { callbackInvalid.invoke() } answers { }
        helperInvalid.shouldShowHelp(callbackInvalid)
        verify { callbackInvalid wasNot Called }

        val helperValid = ArgsValidationHelper("-h")
        val callbackValid: () -> Unit = mockk()
        every { callbackValid.invoke() } answers { }
        helperValid.shouldShowHelp(callbackValid)
        verify { callbackValid.invoke() }
    }
}