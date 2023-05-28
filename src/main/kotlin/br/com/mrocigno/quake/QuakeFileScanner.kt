package br.com.mrocigno.quake

import br.com.mrocigno.common.FileScanner
import br.com.mrocigno.common.ReportModel
import br.com.mrocigno.quake.helper.GameScrapperHelper
import br.com.mrocigno.quake.model.Constants
import br.com.mrocigno.quake.model.QuakeGameReportModel
import org.jetbrains.annotations.TestOnly
import java.io.File

class QuakeFileScanner : FileScanner {

    private val gamesHelper = mutableListOf<GameScrapperHelper>()

    override fun scan(file: File): ReportModel {
        // Read line by line and close buffer on end of lambda
        file.bufferedReader().useLines(::transform)
        return QuakeGameReportModel(gamesHelper)
    }

    private fun transform(logs: Sequence<String>) {
        var creator: MutableList<String>? = null

        logs.forEach { line ->
            when {
                line.isTagAllowed() -> creator?.add(line)
                line.isGameInitialization() -> {
                    creator?.run { gamesHelper.commit(this) }
                    creator = mutableListOf()
                }
            }
        }

        if (!creator.isNullOrEmpty()) gamesHelper.commit(creator!!)
    }

    /***
     * Check if is a game initialization log line
     */
    private fun String.isGameInitialization(): Boolean =
        // Get first 20 chars to make sure that's not a play named InitGame
        runCatching { substring(0, 20).contains(Constants.INIT_GAME_TAG) }.getOrElse { false }

    /***
     * Check if log line is relevant
     */
    private fun String.isTagAllowed(): Boolean =
        contains(Constants.CLIENT_INFO_TAG) || contains(Constants.KILL_TAG)

    /***
     * Commit the log lines into a GameScrapperHelper
     *
     * @param logs log lines from a specific game
     */
    private fun MutableList<GameScrapperHelper>.commit(logs: List<String>) =
        add(GameScrapperHelper(logs))

    @TestOnly
    fun exposedTransform(logs: Sequence<String>) = transform(logs)

    @TestOnly
    fun exposedGamesHelper() = gamesHelper
}