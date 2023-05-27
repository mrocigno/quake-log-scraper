package br.com.mrocigno

import br.com.mrocigno.helper.GameScrapperHelper
import com.google.gson.GsonBuilder



fun MutableList<GameScrapperHelper>.commit(logs: List<String>) =
    add(GameScrapperHelper(logs))

fun MatchResult.extractId(groupIndex: Int) =
    runCatching { groupValues[groupIndex].toInt() }.getOrElse { -1 }

fun <T> MutableMap<T, Int>.increment(key: T) {
    this[key] = (this[key] ?: 0) + 1
}

fun Any?.toJson(): String? {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    return gson.toJson(this)
}

fun <K> Map<K, Int>.sortByValue(): Map<K, Int> =
    toList().sortedByDescending { (_, value) -> value }.toMap()