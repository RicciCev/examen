package com.cev.examen.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cev.examen.IntegrationTest;
import com.cev.examen.domain.Carta;
import com.cev.examen.domain.Jugador;
import com.cev.examen.repository.CartaRepository;
import com.cev.examen.service.criteria.CartaCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CartaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Integer DEFAULT_COSTE_MANA = 1;
    private static final Integer UPDATED_COSTE_MANA = 2;
    private static final Integer SMALLER_COSTE_MANA = 1 - 1;

    private static final String DEFAULT_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cartas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CartaRepository cartaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartaMockMvc;

    private Carta carta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carta createEntity(EntityManager em) {
        Carta carta = new Carta().nombre(DEFAULT_NOMBRE).tipo(DEFAULT_TIPO).costeMana(DEFAULT_COSTE_MANA).texto(DEFAULT_TEXTO);
        return carta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carta createUpdatedEntity(EntityManager em) {
        Carta carta = new Carta().nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).costeMana(UPDATED_COSTE_MANA).texto(UPDATED_TEXTO);
        return carta;
    }

    @BeforeEach
    public void initTest() {
        carta = createEntity(em);
    }

    @Test
    @Transactional
    void createCarta() throws Exception {
        int databaseSizeBeforeCreate = cartaRepository.findAll().size();
        // Create the Carta
        restCartaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carta)))
            .andExpect(status().isCreated());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeCreate + 1);
        Carta testCarta = cartaList.get(cartaList.size() - 1);
        assertThat(testCarta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCarta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCarta.getCosteMana()).isEqualTo(DEFAULT_COSTE_MANA);
        assertThat(testCarta.getTexto()).isEqualTo(DEFAULT_TEXTO);
    }

    @Test
    @Transactional
    void createCartaWithExistingId() throws Exception {
        // Create the Carta with an existing ID
        carta.setId(1L);

        int databaseSizeBeforeCreate = cartaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carta)))
            .andExpect(status().isBadRequest());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCartas() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList
        restCartaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].costeMana").value(hasItem(DEFAULT_COSTE_MANA)))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)));
    }

    @Test
    @Transactional
    void getCarta() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get the carta
        restCartaMockMvc
            .perform(get(ENTITY_API_URL_ID, carta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.costeMana").value(DEFAULT_COSTE_MANA))
            .andExpect(jsonPath("$.texto").value(DEFAULT_TEXTO));
    }

    @Test
    @Transactional
    void getCartasByIdFiltering() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        Long id = carta.getId();

        defaultCartaShouldBeFound("id.equals=" + id);
        defaultCartaShouldNotBeFound("id.notEquals=" + id);

        defaultCartaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCartaShouldNotBeFound("id.greaterThan=" + id);

        defaultCartaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCartaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCartasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where nombre equals to DEFAULT_NOMBRE
        defaultCartaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the cartaList where nombre equals to UPDATED_NOMBRE
        defaultCartaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCartasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where nombre not equals to DEFAULT_NOMBRE
        defaultCartaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the cartaList where nombre not equals to UPDATED_NOMBRE
        defaultCartaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCartasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultCartaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the cartaList where nombre equals to UPDATED_NOMBRE
        defaultCartaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCartasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where nombre is not null
        defaultCartaShouldBeFound("nombre.specified=true");

        // Get all the cartaList where nombre is null
        defaultCartaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCartasByNombreContainsSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where nombre contains DEFAULT_NOMBRE
        defaultCartaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the cartaList where nombre contains UPDATED_NOMBRE
        defaultCartaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCartasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where nombre does not contain DEFAULT_NOMBRE
        defaultCartaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the cartaList where nombre does not contain UPDATED_NOMBRE
        defaultCartaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCartasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where tipo equals to DEFAULT_TIPO
        defaultCartaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the cartaList where tipo equals to UPDATED_TIPO
        defaultCartaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCartasByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where tipo not equals to DEFAULT_TIPO
        defaultCartaShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the cartaList where tipo not equals to UPDATED_TIPO
        defaultCartaShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCartasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultCartaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the cartaList where tipo equals to UPDATED_TIPO
        defaultCartaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCartasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where tipo is not null
        defaultCartaShouldBeFound("tipo.specified=true");

        // Get all the cartaList where tipo is null
        defaultCartaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllCartasByTipoContainsSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where tipo contains DEFAULT_TIPO
        defaultCartaShouldBeFound("tipo.contains=" + DEFAULT_TIPO);

        // Get all the cartaList where tipo contains UPDATED_TIPO
        defaultCartaShouldNotBeFound("tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCartasByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where tipo does not contain DEFAULT_TIPO
        defaultCartaShouldNotBeFound("tipo.doesNotContain=" + DEFAULT_TIPO);

        // Get all the cartaList where tipo does not contain UPDATED_TIPO
        defaultCartaShouldBeFound("tipo.doesNotContain=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana equals to DEFAULT_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.equals=" + DEFAULT_COSTE_MANA);

        // Get all the cartaList where costeMana equals to UPDATED_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.equals=" + UPDATED_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana not equals to DEFAULT_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.notEquals=" + DEFAULT_COSTE_MANA);

        // Get all the cartaList where costeMana not equals to UPDATED_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.notEquals=" + UPDATED_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsInShouldWork() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana in DEFAULT_COSTE_MANA or UPDATED_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.in=" + DEFAULT_COSTE_MANA + "," + UPDATED_COSTE_MANA);

        // Get all the cartaList where costeMana equals to UPDATED_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.in=" + UPDATED_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana is not null
        defaultCartaShouldBeFound("costeMana.specified=true");

        // Get all the cartaList where costeMana is null
        defaultCartaShouldNotBeFound("costeMana.specified=false");
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana is greater than or equal to DEFAULT_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.greaterThanOrEqual=" + DEFAULT_COSTE_MANA);

        // Get all the cartaList where costeMana is greater than or equal to UPDATED_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.greaterThanOrEqual=" + UPDATED_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana is less than or equal to DEFAULT_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.lessThanOrEqual=" + DEFAULT_COSTE_MANA);

        // Get all the cartaList where costeMana is less than or equal to SMALLER_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.lessThanOrEqual=" + SMALLER_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsLessThanSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana is less than DEFAULT_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.lessThan=" + DEFAULT_COSTE_MANA);

        // Get all the cartaList where costeMana is less than UPDATED_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.lessThan=" + UPDATED_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByCosteManaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where costeMana is greater than DEFAULT_COSTE_MANA
        defaultCartaShouldNotBeFound("costeMana.greaterThan=" + DEFAULT_COSTE_MANA);

        // Get all the cartaList where costeMana is greater than SMALLER_COSTE_MANA
        defaultCartaShouldBeFound("costeMana.greaterThan=" + SMALLER_COSTE_MANA);
    }

    @Test
    @Transactional
    void getAllCartasByTextoIsEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where texto equals to DEFAULT_TEXTO
        defaultCartaShouldBeFound("texto.equals=" + DEFAULT_TEXTO);

        // Get all the cartaList where texto equals to UPDATED_TEXTO
        defaultCartaShouldNotBeFound("texto.equals=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllCartasByTextoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where texto not equals to DEFAULT_TEXTO
        defaultCartaShouldNotBeFound("texto.notEquals=" + DEFAULT_TEXTO);

        // Get all the cartaList where texto not equals to UPDATED_TEXTO
        defaultCartaShouldBeFound("texto.notEquals=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllCartasByTextoIsInShouldWork() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where texto in DEFAULT_TEXTO or UPDATED_TEXTO
        defaultCartaShouldBeFound("texto.in=" + DEFAULT_TEXTO + "," + UPDATED_TEXTO);

        // Get all the cartaList where texto equals to UPDATED_TEXTO
        defaultCartaShouldNotBeFound("texto.in=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllCartasByTextoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where texto is not null
        defaultCartaShouldBeFound("texto.specified=true");

        // Get all the cartaList where texto is null
        defaultCartaShouldNotBeFound("texto.specified=false");
    }

    @Test
    @Transactional
    void getAllCartasByTextoContainsSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where texto contains DEFAULT_TEXTO
        defaultCartaShouldBeFound("texto.contains=" + DEFAULT_TEXTO);

        // Get all the cartaList where texto contains UPDATED_TEXTO
        defaultCartaShouldNotBeFound("texto.contains=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllCartasByTextoNotContainsSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        // Get all the cartaList where texto does not contain DEFAULT_TEXTO
        defaultCartaShouldNotBeFound("texto.doesNotContain=" + DEFAULT_TEXTO);

        // Get all the cartaList where texto does not contain UPDATED_TEXTO
        defaultCartaShouldBeFound("texto.doesNotContain=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllCartasByJugadoresIsEqualToSomething() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);
        Jugador jugadores = JugadorResourceIT.createEntity(em);
        em.persist(jugadores);
        em.flush();
        carta.addJugadores(jugadores);
        cartaRepository.saveAndFlush(carta);
        Long jugadoresId = jugadores.getId();

        // Get all the cartaList where jugadores equals to jugadoresId
        defaultCartaShouldBeFound("jugadoresId.equals=" + jugadoresId);

        // Get all the cartaList where jugadores equals to (jugadoresId + 1)
        defaultCartaShouldNotBeFound("jugadoresId.equals=" + (jugadoresId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartaShouldBeFound(String filter) throws Exception {
        restCartaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].costeMana").value(hasItem(DEFAULT_COSTE_MANA)))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)));

        // Check, that the count call also returns 1
        restCartaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartaShouldNotBeFound(String filter) throws Exception {
        restCartaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarta() throws Exception {
        // Get the carta
        restCartaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCarta() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();

        // Update the carta
        Carta updatedCarta = cartaRepository.findById(carta.getId()).get();
        // Disconnect from session so that the updates on updatedCarta are not directly saved in db
        em.detach(updatedCarta);
        updatedCarta.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).costeMana(UPDATED_COSTE_MANA).texto(UPDATED_TEXTO);

        restCartaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCarta))
            )
            .andExpect(status().isOk());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
        Carta testCarta = cartaList.get(cartaList.size() - 1);
        assertThat(testCarta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCarta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCarta.getCosteMana()).isEqualTo(UPDATED_COSTE_MANA);
        assertThat(testCarta.getTexto()).isEqualTo(UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void putNonExistingCarta() throws Exception {
        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();
        carta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarta() throws Exception {
        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();
        carta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarta() throws Exception {
        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();
        carta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartaWithPatch() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();

        // Update the carta using partial update
        Carta partialUpdatedCarta = new Carta();
        partialUpdatedCarta.setId(carta.getId());

        partialUpdatedCarta.tipo(UPDATED_TIPO);

        restCartaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarta))
            )
            .andExpect(status().isOk());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
        Carta testCarta = cartaList.get(cartaList.size() - 1);
        assertThat(testCarta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCarta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCarta.getCosteMana()).isEqualTo(DEFAULT_COSTE_MANA);
        assertThat(testCarta.getTexto()).isEqualTo(DEFAULT_TEXTO);
    }

    @Test
    @Transactional
    void fullUpdateCartaWithPatch() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();

        // Update the carta using partial update
        Carta partialUpdatedCarta = new Carta();
        partialUpdatedCarta.setId(carta.getId());

        partialUpdatedCarta.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).costeMana(UPDATED_COSTE_MANA).texto(UPDATED_TEXTO);

        restCartaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarta))
            )
            .andExpect(status().isOk());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
        Carta testCarta = cartaList.get(cartaList.size() - 1);
        assertThat(testCarta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCarta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCarta.getCosteMana()).isEqualTo(UPDATED_COSTE_MANA);
        assertThat(testCarta.getTexto()).isEqualTo(UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void patchNonExistingCarta() throws Exception {
        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();
        carta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarta() throws Exception {
        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();
        carta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarta() throws Exception {
        int databaseSizeBeforeUpdate = cartaRepository.findAll().size();
        carta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carta in the database
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarta() throws Exception {
        // Initialize the database
        cartaRepository.saveAndFlush(carta);

        int databaseSizeBeforeDelete = cartaRepository.findAll().size();

        // Delete the carta
        restCartaMockMvc
            .perform(delete(ENTITY_API_URL_ID, carta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Carta> cartaList = cartaRepository.findAll();
        assertThat(cartaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
