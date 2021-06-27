package com.cev.examen.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cev.examen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carta.class);
        Carta carta1 = new Carta();
        carta1.setId(1L);
        Carta carta2 = new Carta();
        carta2.setId(carta1.getId());
        assertThat(carta1).isEqualTo(carta2);
        carta2.setId(2L);
        assertThat(carta1).isNotEqualTo(carta2);
        carta1.setId(null);
        assertThat(carta1).isNotEqualTo(carta2);
    }
}
