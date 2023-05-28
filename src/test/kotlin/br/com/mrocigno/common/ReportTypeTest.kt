package br.com.mrocigno.common

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class ReportTypeTest {

    @Test
    fun `check string transformation, should ignore case, should return null on unknown type`() {
        assertNotNull(ReportType.get("json"))
        assertNotNull(ReportType.get("JSON"))
        assertNotNull(ReportType.get("report"))
        assertNotNull(ReportType.get("REPORT"))

        assertNull(ReportType.get("nvm"))
    }
}