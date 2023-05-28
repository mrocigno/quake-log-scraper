package br.com.mrocigno.common

import org.junit.jupiter.api.Test
import javax.swing.GroupLayout.Alignment.BASELINE
import javax.swing.GroupLayout.Alignment.CENTER
import javax.swing.GroupLayout.Alignment.TRAILING
import kotlin.test.assertEquals

class TableHelperTest {

    @Test
    fun `check start alignment`() {
        val table = table {
            line {
                cell("loki", alignment = BASELINE)
                cell("lola", alignment = BASELINE)
            }
        }

        assertEquals("""
            |-----------------|
            | loki   | lola   |
            |-----------------|
            
        """.trimIndent(), table)
    }

    @Test
    fun `check center alignment`() {
        val table = table {
            line {
                cell("loki", alignment = CENTER)
                cell("lola", alignment = CENTER)
            }
        }

        assertEquals("""
            |-----------------|
            |  loki  |  lola  |
            |-----------------|
            
        """.trimIndent(), table)
    }

    @Test
    fun `check end alignment`() {
        val table = table {
            line {
                cell("loki", alignment = TRAILING)
                cell("lola", alignment = TRAILING)
            }
        }

        assertEquals("""
            |-----------------|
            |   loki |   lola |
            |-----------------|

        """.trimIndent(), table)
    }

    @Test
    fun `check cell with span 2`() {
        val table = table {
            line {
                cell("loki")
                cell("lola")
            }
            line {
                cell("merged", 2, CENTER)
            }
        }

        assertEquals("""
            |-----------------|
            | loki   | lola   |
            |     merged      |
            |-----------------|

        """.trimIndent(), table)
    }

    @Test
    fun `check table with header`() {
        val table = table {
            header {
                line {
                    cell("header", 2, CENTER)
                }
            }
            line {
                cell("loki")
                cell("lola")
            }
        }

        assertEquals("""
            |-----------------|
            |     header      |
            |--------|--------|
            | loki   | lola   |
            |-----------------|

        """.trimIndent(), table)
    }
}