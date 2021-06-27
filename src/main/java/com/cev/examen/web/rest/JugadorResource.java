package com.cev.examen.web.rest;

import com.cev.examen.domain.Jugador;
import com.cev.examen.repository.JugadorRepository;
import com.cev.examen.service.JugadorQueryService;
import com.cev.examen.service.JugadorService;
import com.cev.examen.service.criteria.JugadorCriteria;
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
 * REST controller for managing {@link com.cev.examen.domain.Jugador}.
 */
@RestController
@RequestMapping("/api")
public class JugadorResource {

    private final Logger log = LoggerFactory.getLogger(JugadorResource.class);

    private static final String ENTITY_NAME = "jugador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JugadorService jugadorService;

    private final JugadorRepository jugadorRepository;

    private final JugadorQueryService jugadorQueryService;

    public JugadorResource(JugadorService jugadorService, JugadorRepository jugadorRepository, JugadorQueryService jugadorQueryService) {
        this.jugadorService = jugadorService;
        this.jugadorRepository = jugadorRepository;
        this.jugadorQueryService = jugadorQueryService;
    }

    /**
     * {@code POST  /jugadors} : Create a new jugador.
     *
     * @param jugador the jugador to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jugador, or with status {@code 400 (Bad Request)} if the jugador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jugadors")
    public ResponseEntity<Jugador> createJugador(@Valid @RequestBody Jugador jugador) throws URISyntaxException {
        log.debug("REST request to save Jugador : {}", jugador);
        if (jugador.getId() != null) {
            throw new BadRequestAlertException("A new jugador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Jugador result = jugadorService.save(jugador);
        return ResponseEntity
            .created(new URI("/api/jugadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jugadors/:id} : Updates an existing jugador.
     *
     * @param id the id of the jugador to save.
     * @param jugador the jugador to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jugador,
     * or with status {@code 400 (Bad Request)} if the jugador is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jugador couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jugadors/{id}")
    public ResponseEntity<Jugador> updateJugador(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Jugador jugador
    ) throws URISyntaxException {
        log.debug("REST request to update Jugador : {}, {}", id, jugador);
        if (jugador.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jugador.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jugadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Jugador result = jugadorService.save(jugador);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jugador.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /jugadors/:id} : Partial updates given fields of an existing jugador, field will ignore if it is null
     *
     * @param id the id of the jugador to save.
     * @param jugador the jugador to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jugador,
     * or with status {@code 400 (Bad Request)} if the jugador is not valid,
     * or with status {@code 404 (Not Found)} if the jugador is not found,
     * or with status {@code 500 (Internal Server Error)} if the jugador couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/jugadors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Jugador> partialUpdateJugador(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Jugador jugador
    ) throws URISyntaxException {
        log.debug("REST request to partial update Jugador partially : {}, {}", id, jugador);
        if (jugador.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jugador.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jugadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Jugador> result = jugadorService.partialUpdate(jugador);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jugador.getId().toString())
        );
    }

    /**
     * {@code GET  /jugadors} : get all the jugadors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jugadors in body.
     */
    @GetMapping("/jugadors")
    public ResponseEntity<List<Jugador>> getAllJugadors(JugadorCriteria criteria) {
        log.debug("REST request to get Jugadors by criteria: {}", criteria);
        List<Jugador> entityList = jugadorQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /jugadors/count} : count all the jugadors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/jugadors/count")
    public ResponseEntity<Long> countJugadors(JugadorCriteria criteria) {
        log.debug("REST request to count Jugadors by criteria: {}", criteria);
        return ResponseEntity.ok().body(jugadorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /jugadors/:id} : get the "id" jugador.
     *
     * @param id the id of the jugador to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jugador, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jugadors/{id}")
    public ResponseEntity<Jugador> getJugador(@PathVariable Long id) {
        log.debug("REST request to get Jugador : {}", id);
        Optional<Jugador> jugador = jugadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jugador);
    }

    /**
     * {@code DELETE  /jugadors/:id} : delete the "id" jugador.
     *
     * @param id the id of the jugador to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jugadors/{id}")
    public ResponseEntity<Void> deleteJugador(@PathVariable Long id) {
        log.debug("REST request to delete Jugador : {}", id);
        jugadorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    
    // CONSULTAS JPA.
    @GetMapping("/jugadores-by-carta/{idCarta}")
    public ResponseEntity<List<Jugador>> getAllJugadorsByCarta(@PathVariable("idCarta") Integer idCarta) {
        log.debug("Buscando listado de jugadores por carta : {}", idCarta);
        List<Jugador> entityList = jugadorService.busarPorCarta(idCarta);
        return ResponseEntity.ok().body(entityList);
    }
}
