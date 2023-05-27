package br.com.mrocigno

import br.com.mrocigno.helper.GameScrapperHelper
import com.google.gson.GsonBuilder

/***
 * Commit the log lines into a GameScrapperHelper
 *
 * @param logs log lines from a specific game
 */
fun MutableList<GameScrapperHelper>.commit(logs: List<String>) =
    add(GameScrapperHelper(logs))

/***
 * Extract the id with -1 as default value
 *
 * @param groupIndex the groupIndex from MatchResult
 */
fun MatchResult.extractId(groupIndex: Int) =
    runCatching { groupValues[groupIndex].toInt() }.getOrElse { -1 }

/***
 * Find the value with the key and increment by 1
 *
 * @param key key of value that should be incremented
 */
fun <T> MutableMap<T, Int>.increment(key: T) {
    this[key] = (this[key] ?: 0) + 1
}

/***
 * Transform any object in Json using Gson lib
 */
fun Any?.toJson(): String? {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    return gson.toJson(this)
}

/***
 * Sort a map using the value as reference (descending)
 */
fun <K> Map<K, Int>.sortByValue(): Map<K, Int> =
    toList().sortByValue()

/***
 * Sort a list of pair using the value as reference (descending)
 */
fun <K> List<Pair<K, Int>>.sortByValue(): Map<K, Int> =
    sortedByDescending { (_, value) -> value }.toMap()
