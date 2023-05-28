package br.com.mrocigno

import com.google.gson.GsonBuilder

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
    toList().sortedByDescending { (_, value) -> value }.toMap()
