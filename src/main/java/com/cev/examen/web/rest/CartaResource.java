package com.cev.examen.web.rest;

import com.cev.examen.domain.Carta;
import com.cev.examen.repository.CartaRepository;
import com.cev.examen.service.CartaQueryService;
import com.cev.examen.service.CartaService;
import com.cev.examen.service.criteria.CartaCriteria;
import com.cev.examen.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cev.examen.domain.Carta}.
 */
@RestController
@RequestMapping("/api")
public class CartaResource {

    private final Logger log = LoggerFactory.getLogger(CartaResource.class);

    private static final String ENTITY_NAME = "carta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartaService cartaService;

    private final CartaRepository cartaRepository;

    private final CartaQueryService cartaQueryService;

    public CartaResource(CartaService cartaService, CartaRepository cartaRepository, CartaQueryService cartaQueryService) {
        this.cartaService = cartaService;
        this.cartaRepository = cartaRepository;
        this.cartaQueryService = cartaQueryService;
    }

    /**
     * {@code POST  /cartas} : Create a new carta.
     *
     * @param carta the carta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carta, or with status {@code 400 (Bad Request)} if the carta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cartas")
    public ResponseEntity<Carta> createCarta(@Valid @RequestBody Carta carta) throws URISyntaxException {
        log.debug("REST request to save Carta : {}", carta);
        if (carta.getId() != null) {
            throw new BadRequestAlertException("A new carta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Carta result = cartaService.save(carta);
        return ResponseEntity
            .created(new URI("/api/cartas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cartas/:id} : Updates an existing carta.
     *
     * @param id the id of the carta to save.
     * @param carta the carta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carta,
     * or with status {@code 400 (Bad Request)} if the carta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cartas/{id}")
    public ResponseEntity<Carta> updateCarta(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Carta carta)
        throws URISyntaxException {
        log.debug("REST request to update Carta : {}, {}", id, carta);
        if (carta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Carta result = cartaService.save(carta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cartas/:id} : Partial updates given fields of an existing carta, field will ignore if it is null
     *
     * @param id the id of the carta to save.
     * @param carta the carta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carta,
     * or with status {@code 400 (Bad Request)} if the carta is not valid,
     * or with status {@code 404 (Not Found)} if the carta is not found,
     * or with status {@code 500 (Internal Server Error)} if the carta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cartas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Carta> partialUpdateCarta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Carta carta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Carta partially : {}, {}", id, carta);
        if (carta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Carta> result = cartaService.partialUpdate(carta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carta.getId().toString())
        );
    }

    /**
     * {@code GET  /cartas} : get all the cartas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartas in body.
     */
    @GetMapping("/cartas")
    public ResponseEntity<List<Carta>> getAllCartas(CartaCriteria criteria) {
        log.debug("REST request to get Cartas by criteria: {}", criteria);
        List<Carta> entityList = cartaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cartas/count} : count all the cartas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cartas/count")
    public ResponseEntity<Long> countCartas(CartaCriteria criteria) {
        log.debug("REST request to count Cartas by criteria: {}", criteria);
        return ResponseEntity.ok().body(cartaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cartas/:id} : get the "id" carta.
     *
     * @param id the id of the carta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cartas/{id}")
    public ResponseEntity<Carta> getCarta(@PathVariable Long id) {
        log.debug("REST request to get Carta : {}", id);
        Optional<Carta> carta = cartaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carta);
    }

    /**
     * {@code DELETE  /cartas/:id} : delete the "id" carta.
     *
     * @param id the id of the carta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cartas/{id}")
    public ResponseEntity<Void> deleteCarta(@PathVariable Long id) {
        log.debug("REST request to delete Carta : {}", id);
        cartaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    
    // CONSULTAS JPA.
    @GetMapping("/cartas-jugador/{jugador}")
    public ResponseEntity<List<Carta>> getAllCartasByJugador(@PathVariable("jugador") String jugador) {
        log.debug("Buscando listado de cartas por : {}", jugador);
        List<Carta> entityList = cartaService.busarPorJugador(jugador);
        return ResponseEntity.ok().body(entityList);
    }
}
