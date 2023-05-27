package br.com.mrocigno.helper

import javax.swing.GroupLayout.Alignment
import kotlin.math.max

@DslMarker
annotation class TableMarker

/***
 * Start a table constructor helper
 *
 * @sample br.com.mrocigno.model.ReportType
 */
@TableMarker
fun table(action: TableHelper.() -> Unit): String {
    return TableHelper().apply(action).toString()
}

class TableHelper {

    private val lines: MutableList<LineHelper> = mutableListOf()

    /***
     * Add a header if is wanted a line separation
     */
    @TableMarker
    fun header(action: HeaderHelper.() -> Unit) {
        val headerHelper = HeaderHelper().apply(action)

        lines.addAll(headerHelper.getLines())
    }

    /***
     * Add a simple line in the table
     */
    @TableMarker
    fun line(action: LineHelper.() -> Unit) {
        lines.add(LineHelper().apply(action))
    }

    // Print the table!!
    override fun toString(): String {
        val build = StringBuilder()

        val columnsSize = IntArray(lines.maxBy { it.columnCount }.columnCount)
        lines.forEach {
            var column = 0
            it.cells.forEach cellLooper@ { cell ->
                if (cell.span > 1) return@cellLooper
                repeat(cell.span) {
                    columnsSize[column] = max(columnsSize[column], cell.size + 2)
                    column++
                }
            }
        }

        build.close(columnsSize)
        lines.forEach {
            build.append("|")

            var column = 0
            it.cells.forEach { cell ->
                var size = 0
                repeat(cell.span) {
                    size += columnsSize[column++]
                }
                size += cell.span - 1

                build.append(cell.getContent(size))
                build.append("|")
            }
            build.appendLine()
        }
        build.close(columnsSize)

        return build.toString()
    }

    private fun StringBuilder.close(columnSize: IntArray) {
        append("|")
        append("".padEnd(columnSize.lastIndex + columnSize.sum(), '-'))
        appendLine("|")
    }
}

class HeaderHelper {

    private val lines: MutableList<LineHelper> = mutableListOf()
    private val columnCount: Int get() = lines.maxOf { it.columnCount }

    /***
     * Add a simple line in the header (this will not add a line separation)
     */
    @TableMarker
    fun line(action: LineHelper.() -> Unit) {
        val lineHelper = LineHelper().apply(action)
        lines.add(lineHelper)
    }

    fun getLines() = lines + LineHelper().also {
        it.cells.addAll(List(columnCount) { Cell(isSeparator = true) })
    }
}

class LineHelper {

    val cells: MutableList<Cell> = mutableListOf()
    val columnCount: Int get() = cells.sumOf { it.span }

    @TableMarker
    fun cell(content: String, span: Int = 1, alignment: Alignment = Alignment.BASELINE) =
        cells.add(Cell(span, " $content ", alignment = alignment))
}

class Cell(
    val span: Int = 1,
    private val content: String? = null,
    val size: Int = content?.length ?: 0,
    val isSeparator: Boolean = false,
    private val alignment: Alignment = Alignment.BASELINE
) {

    fun getContent(size: Int) =
        if (isSeparator) {
            "".padEnd(size, '-')
        } else {
            getAlignment(size)
        }

    private fun getAlignment(columnSize: Int) = when (alignment) {
        Alignment.BASELINE,
        Alignment.LEADING -> content.orEmpty().padEnd(columnSize)
        Alignment.TRAILING -> content.orEmpty().padStart(columnSize)
        Alignment.CENTER -> {
            val padSize = (columnSize - size) / 2
            content.orEmpty().padStart(padSize + size).padEnd(columnSize)
        }
    }
}