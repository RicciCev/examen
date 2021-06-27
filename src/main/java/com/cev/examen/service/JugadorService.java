package com.cev.examen.service;

import com.cev.examen.domain.Jugador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Jugador}.
 */
public interface JugadorService {
    /**
     * Save a jugador.
     *
     * @param jugador the entity to save.
     * @return the persisted entity.
     */
    Jugador save(Jugador jugador);

    /**
     * Partially updates a jugador.
     *
     * @param jugador the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Jugador> partialUpdate(Jugador jugador);

    /**
     * Get all the jugadors.
     *
     * @return the list of entities.
     */
    List<Jugador> findAll();

    /**
     * Get all the jugadors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Jugador> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" jugador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Jugador> findOne(Long id);

    /**
     * Delete the "id" jugador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
