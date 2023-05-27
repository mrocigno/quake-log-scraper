package br.com.mrocigno.model

import br.com.mrocigno.extractId

class Death(
    val killerId: Int,
    val deadId: Int,
    val meanOfDeath: MeanOfDeath
) {

    companion object {

        const val KILL_TAG = "Kill"

        inline fun shouldComputeDeath(log: String, action: (killerId: Int, deadId: Int, meanOfDeath: MeanOfDeath) -> Unit) {
            if (!log.substring(0, 20).contains(KILL_TAG)) return

            """Kill: (\d+) (\d+) (\d+)"""
                .toRegex()
                .find(log)
                ?.let {
                    val killerId = it.extractId(1)
                    val deadId = it.extractId(2)
                    val meanOfDeath = it.extractId(3).let(MeanOfDeath::findById)

                    action.invoke(killerId, deadId, meanOfDeath)
                }
        }
    }
}