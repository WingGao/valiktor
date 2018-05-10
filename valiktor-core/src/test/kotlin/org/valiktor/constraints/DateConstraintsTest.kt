package org.valiktor.constraints

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.jupiter.api.Test
import org.valiktor.i18n.Locales
import org.valiktor.i18n.interpolatedMessages

class TodayTest {

    @Test
    fun `should validate messages`() {
        assertThat(Today.interpolatedMessages()).containsExactly(
                entry(Locales.DEFAULT, "Must be today"),
                entry(Locales.EN, "Must be today"),
                entry(Locales.PT_BR, "Deve ser hoje"))
    }
}