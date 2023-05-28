package br.com.mrocigno

import br.com.mrocigno.common.ArgsValidationHelper
import br.com.mrocigno.common.FileScanner
import br.com.mrocigno.common.ReportModel
import br.com.mrocigno.common.ReportType.JSON
import br.com.mrocigno.common.ReportType.REPORT
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.io.File
import kotlin.test.assertEquals

class MainClassTest : KoinComponent {

    private val args: ArgsValidationHelper by inject()

    @BeforeEach
    fun setUp() {
        startKoin {
            modules(module {
                single<ArgsValidationHelper> { mockk() }
                single<FileScanner> { TestFileScanner() }
            })
        }

        val file: File = mockk()
        every { args.hasInvalidArg(any()) } answers { }
        every { args.shouldShowHelp(any()) } answers { }
        every { args.hasFileArg(any()) } answers { }
        every { args.file } returns file
        every { file.exists() } returns true
    }

    @Test
    fun `check return when report type is JSON`() {
        every { args.type } returns JSON

        val mainClass = MainClass()
        assertEquals("Json", mainClass.process())
    }

    @Test
    fun `check return when report type is REPORT`() {
        every { args.type } returns REPORT

        val mainClass = MainClass()
        assertEquals("Report", mainClass.process())
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }
}

class TestFileScanner : FileScanner {

    override fun scan(file: File) = TestReportModel()
}

class TestReportModel : ReportModel {

    override fun asJson() = "Json"
    override fun asReport() = "Report"
}