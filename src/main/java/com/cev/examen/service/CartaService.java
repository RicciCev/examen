package com.cev.examen.service;

import com.cev.examen.domain.Carta;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Carta}.
 */
public interface CartaService {
    /**
     * Save a carta.
     *
     * @param carta the entity to save.
     * @return the persisted entity.
     */
    Carta save(Carta carta);

    /**
     * Partially updates a carta.
     *
     * @param carta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Carta> partialUpdate(Carta carta);

    /**
     * Get all the cartas.
     *
     * @return the list of entities.
     */
    List<Carta> findAll();
    
    // CONSULTAS JPA.
    List<Carta> busarPorJugador(String jugador);

    /**
     * Get the "id" carta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Carta> findOne(Long id);

    /**
     * Delete the "id" carta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
