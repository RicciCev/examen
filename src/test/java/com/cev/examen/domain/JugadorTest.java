package com.cev.examen.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cev.examen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JugadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jugador.class);
        Jugador jugador1 = new Jugador();
        jugador1.setId(1L);
        Jugador jugador2 = new Jugador();
        jugador2.setId(jugador1.getId());
        assertThat(jugador1).isEqualTo(jugador2);
        jugador2.setId(2L);
        assertThat(jugador1).isNotEqualTo(jugador2);
        jugador1.setId(null);
        assertThat(jugador1).isNotEqualTo(jugador2);
    }
}
