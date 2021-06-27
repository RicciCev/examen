package com.cev.examen.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cev.examen.IntegrationTest;
import com.cev.examen.domain.Carta;
import com.cev.examen.domain.Jugador;
import com.cev.examen.repository.JugadorRepository;
import com.cev.examen.service.JugadorService;
import com.cev.examen.service.criteria.JugadorCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JugadorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JugadorResourceIT {

    private static final String DEFAULT_APODO = "AAAAAAAAAA";
    private static final String UPDATED_APODO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/jugadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JugadorRepository jugadorRepository;

    @Mock
    private JugadorRepository jugadorRepositoryMock;

    @Mock
    private JugadorService jugadorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJugadorMockMvc;

    private Jugador jugador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jugador createEntity(EntityManager em) {
        Jugador jugador = new Jugador()
            .apodo(DEFAULT_APODO)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        return jugador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jugador createUpdatedEntity(EntityManager em) {
        Jugador jugador = new Jugador()
            .apodo(UPDATED_APODO)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        return jugador;
    }

    @BeforeEach
    public void initTest() {
        jugador = createEntity(em);
    }

    @Test
    @Transactional
    void createJugador() throws Exception {
        int databaseSizeBeforeCreate = jugadorRepository.findAll().size();
        // Create the Jugador
        restJugadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isCreated());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeCreate + 1);
        Jugador testJugador = jugadorList.get(jugadorList.size() - 1);
        assertThat(testJugador.getApodo()).isEqualTo(DEFAULT_APODO);
        assertThat(testJugador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testJugador.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testJugador.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void createJugadorWithExistingId() throws Exception {
        // Create the Jugador with an existing ID
        jugador.setId(1L);

        int databaseSizeBeforeCreate = jugadorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJugadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJugadors() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList
        restJugadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jugador.getId().intValue())))
            .andExpect(jsonPath("$.[*].apodo").value(hasItem(DEFAULT_APODO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJugadorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jugadorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJugadorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jugadorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJugadorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jugadorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJugadorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jugadorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get the jugador
        restJugadorMockMvc
            .perform(get(ENTITY_API_URL_ID, jugador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jugador.getId().intValue()))
            .andExpect(jsonPath("$.apodo").value(DEFAULT_APODO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getJugadorsByIdFiltering() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        Long id = jugador.getId();

        defaultJugadorShouldBeFound("id.equals=" + id);
        defaultJugadorShouldNotBeFound("id.notEquals=" + id);

        defaultJugadorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJugadorShouldNotBeFound("id.greaterThan=" + id);

        defaultJugadorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJugadorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJugadorsByApodoIsEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apodo equals to DEFAULT_APODO
        defaultJugadorShouldBeFound("apodo.equals=" + DEFAULT_APODO);

        // Get all the jugadorList where apodo equals to UPDATED_APODO
        defaultJugadorShouldNotBeFound("apodo.equals=" + UPDATED_APODO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApodoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apodo not equals to DEFAULT_APODO
        defaultJugadorShouldNotBeFound("apodo.notEquals=" + DEFAULT_APODO);

        // Get all the jugadorList where apodo not equals to UPDATED_APODO
        defaultJugadorShouldBeFound("apodo.notEquals=" + UPDATED_APODO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApodoIsInShouldWork() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apodo in DEFAULT_APODO or UPDATED_APODO
        defaultJugadorShouldBeFound("apodo.in=" + DEFAULT_APODO + "," + UPDATED_APODO);

        // Get all the jugadorList where apodo equals to UPDATED_APODO
        defaultJugadorShouldNotBeFound("apodo.in=" + UPDATED_APODO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApodoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apodo is not null
        defaultJugadorShouldBeFound("apodo.specified=true");

        // Get all the jugadorList where apodo is null
        defaultJugadorShouldNotBeFound("apodo.specified=false");
    }

    @Test
    @Transactional
    void getAllJugadorsByApodoContainsSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apodo contains DEFAULT_APODO
        defaultJugadorShouldBeFound("apodo.contains=" + DEFAULT_APODO);

        // Get all the jugadorList where apodo contains UPDATED_APODO
        defaultJugadorShouldNotBeFound("apodo.contains=" + UPDATED_APODO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApodoNotContainsSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apodo does not contain DEFAULT_APODO
        defaultJugadorShouldNotBeFound("apodo.doesNotContain=" + DEFAULT_APODO);

        // Get all the jugadorList where apodo does not contain UPDATED_APODO
        defaultJugadorShouldBeFound("apodo.doesNotContain=" + UPDATED_APODO);
    }

    @Test
    @Transactional
    void getAllJugadorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where nombre equals to DEFAULT_NOMBRE
        defaultJugadorShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the jugadorList where nombre equals to UPDATED_NOMBRE
        defaultJugadorShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllJugadorsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where nombre not equals to DEFAULT_NOMBRE
        defaultJugadorShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the jugadorList where nombre not equals to UPDATED_NOMBRE
        defaultJugadorShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllJugadorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultJugadorShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the jugadorList where nombre equals to UPDATED_NOMBRE
        defaultJugadorShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllJugadorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where nombre is not null
        defaultJugadorShouldBeFound("nombre.specified=true");

        // Get all the jugadorList where nombre is null
        defaultJugadorShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllJugadorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where nombre contains DEFAULT_NOMBRE
        defaultJugadorShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the jugadorList where nombre contains UPDATED_NOMBRE
        defaultJugadorShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllJugadorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where nombre does not contain DEFAULT_NOMBRE
        defaultJugadorShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the jugadorList where nombre does not contain UPDATED_NOMBRE
        defaultJugadorShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllJugadorsByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apellido equals to DEFAULT_APELLIDO
        defaultJugadorShouldBeFound("apellido.equals=" + DEFAULT_APELLIDO);

        // Get all the jugadorList where apellido equals to UPDATED_APELLIDO
        defaultJugadorShouldNotBeFound("apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apellido not equals to DEFAULT_APELLIDO
        defaultJugadorShouldNotBeFound("apellido.notEquals=" + DEFAULT_APELLIDO);

        // Get all the jugadorList where apellido not equals to UPDATED_APELLIDO
        defaultJugadorShouldBeFound("apellido.notEquals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apellido in DEFAULT_APELLIDO or UPDATED_APELLIDO
        defaultJugadorShouldBeFound("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO);

        // Get all the jugadorList where apellido equals to UPDATED_APELLIDO
        defaultJugadorShouldNotBeFound("apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apellido is not null
        defaultJugadorShouldBeFound("apellido.specified=true");

        // Get all the jugadorList where apellido is null
        defaultJugadorShouldNotBeFound("apellido.specified=false");
    }

    @Test
    @Transactional
    void getAllJugadorsByApellidoContainsSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apellido contains DEFAULT_APELLIDO
        defaultJugadorShouldBeFound("apellido.contains=" + DEFAULT_APELLIDO);

        // Get all the jugadorList where apellido contains UPDATED_APELLIDO
        defaultJugadorShouldNotBeFound("apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllJugadorsByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where apellido does not contain DEFAULT_APELLIDO
        defaultJugadorShouldNotBeFound("apellido.doesNotContain=" + DEFAULT_APELLIDO);

        // Get all the jugadorList where apellido does not contain UPDATED_APELLIDO
        defaultJugadorShouldBeFound("apellido.doesNotContain=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento not equals to DEFAULT_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.notEquals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento not equals to UPDATED_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.notEquals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento is not null
        defaultJugadorShouldBeFound("fechaNacimiento.specified=true");

        // Get all the jugadorList where fechaNacimiento is null
        defaultJugadorShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadorList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultJugadorShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the jugadorList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultJugadorShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllJugadorsByCartasIsEqualToSomething() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);
        Carta cartas = CartaResourceIT.createEntity(em);
        em.persist(cartas);
        em.flush();
        jugador.addCartas(cartas);
        jugadorRepository.saveAndFlush(jugador);
        Long cartasId = cartas.getId();

        // Get all the jugadorList where cartas equals to cartasId
        defaultJugadorShouldBeFound("cartasId.equals=" + cartasId);

        // Get all the jugadorList where cartas equals to (cartasId + 1)
        defaultJugadorShouldNotBeFound("cartasId.equals=" + (cartasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJugadorShouldBeFound(String filter) throws Exception {
        restJugadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jugador.getId().intValue())))
            .andExpect(jsonPath("$.[*].apodo").value(hasItem(DEFAULT_APODO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));

        // Check, that the count call also returns 1
        restJugadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJugadorShouldNotBeFound(String filter) throws Exception {
        restJugadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJugadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJugador() throws Exception {
        // Get the jugador
        restJugadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // Update the jugador
        Jugador updatedJugador = jugadorRepository.findById(jugador.getId()).get();
        // Disconnect from session so that the updates on updatedJugador are not directly saved in db
        em.detach(updatedJugador);
        updatedJugador.apodo(UPDATED_APODO).nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restJugadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJugador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJugador))
            )
            .andExpect(status().isOk());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
        Jugador testJugador = jugadorList.get(jugadorList.size() - 1);
        assertThat(testJugador.getApodo()).isEqualTo(UPDATED_APODO);
        assertThat(testJugador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testJugador.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testJugador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void putNonExistingJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();
        jugador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJugadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jugador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jugador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();
        jugador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJugadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jugador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();
        jugador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJugadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJugadorWithPatch() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // Update the jugador using partial update
        Jugador partialUpdatedJugador = new Jugador();
        partialUpdatedJugador.setId(jugador.getId());

        partialUpdatedJugador.apodo(UPDATED_APODO).nombre(UPDATED_NOMBRE).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restJugadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJugador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJugador))
            )
            .andExpect(status().isOk());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
        Jugador testJugador = jugadorList.get(jugadorList.size() - 1);
        assertThat(testJugador.getApodo()).isEqualTo(UPDATED_APODO);
        assertThat(testJugador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testJugador.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testJugador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void fullUpdateJugadorWithPatch() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // Update the jugador using partial update
        Jugador partialUpdatedJugador = new Jugador();
        partialUpdatedJugador.setId(jugador.getId());

        partialUpdatedJugador
            .apodo(UPDATED_APODO)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restJugadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJugador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJugador))
            )
            .andExpect(status().isOk());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
        Jugador testJugador = jugadorList.get(jugadorList.size() - 1);
        assertThat(testJugador.getApodo()).isEqualTo(UPDATED_APODO);
        assertThat(testJugador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testJugador.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testJugador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void patchNonExistingJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();
        jugador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJugadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jugador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jugador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();
        jugador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJugadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jugador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJugador() throws Exception {
        int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();
        jugador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJugadorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jugador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jugador in the database
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        int databaseSizeBeforeDelete = jugadorRepository.findAll().size();

        // Delete the jugador
        restJugadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, jugador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Jugador> jugadorList = jugadorRepository.findAll();
        assertThat(jugadorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
