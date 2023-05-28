package br.com.mrocigno.quake.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MeanOfDeathTest {

    @Test
    fun `check mean of death id transform, if not found, MOD_UNKNOWN should return`() {
        val validId1 = 0
        val validId2 = 1
        val validId3 = 2
        assertEquals(MeanOfDeath.MOD_UNKNOWN, MeanOfDeath.findById(validId1))
        assertEquals(MeanOfDeath.MOD_SHOTGUN, MeanOfDeath.findById(validId2))
        assertEquals(MeanOfDeath.MOD_GAUNTLET, MeanOfDeath.findById(validId3))

        val invalidId1 = 123
        val invalidId2 = 124
        val invalidId3 = 125
        assertEquals(MeanOfDeath.MOD_UNKNOWN, MeanOfDeath.findById(invalidId1))
        assertEquals(MeanOfDeath.MOD_UNKNOWN, MeanOfDeath.findById(invalidId2))
        assertEquals(MeanOfDeath.MOD_UNKNOWN, MeanOfDeath.findById(invalidId3))
    }
}