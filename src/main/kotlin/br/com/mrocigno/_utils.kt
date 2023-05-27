package br.com.mrocigno

fun MatchResult.extractId(groupIndex: Int) =
    runCatching { groupValues[groupIndex].toInt() }.getOrElse { -1 }