package com.cev.examen.service.impl;

import com.cev.examen.domain.Carta;
import com.cev.examen.repository.CartaRepository;
import com.cev.examen.service.CartaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

/**
 * Service Implementation for managing {@link Carta}.
 */
@Service
@Transactional
public class CartaServiceImpl implements CartaService {

    private final Logger log = LoggerFactory.getLogger(CartaServiceImpl.class);

    private final CartaRepository cartaRepository;

    public CartaServiceImpl(CartaRepository cartaRepository) {
        this.cartaRepository = cartaRepository;
    }

    @Override
    public Carta save(Carta carta) {
        log.debug("Request to save Carta : {}", carta);
        return cartaRepository.save(carta);
    }

    @Override
    public Optional<Carta> partialUpdate(Carta carta) {
        log.debug("Request to partially update Carta : {}", carta);

        return cartaRepository
            .findById(carta.getId())
            .map(
                existingCarta -> {
                    if (carta.getNombre() != null) {
                        existingCarta.setNombre(carta.getNombre());
                    }
                    if (carta.getTipo() != null) {
                        existingCarta.setTipo(carta.getTipo());
                    }
                    if (carta.getCosteMana() != null) {
                        existingCarta.setCosteMana(carta.getCosteMana());
                    }
                    if (carta.getTexto() != null) {
                        existingCarta.setTexto(carta.getTexto());
                    }

                    return existingCarta;
                }
            )
            .map(cartaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Carta> findAll() {
        log.debug("Request to get all Cartas");
        return cartaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Carta> findOne(Long id) {
        log.debug("Request to get Carta : {}", id);
        return cartaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Carta : {}", id);
        cartaRepository.deleteById(id);
    }
    
    // CONSULTAS JPA.
    public List<Carta> busarPorJugador(String jugador) {
    	return this.cartaRepository.findAllByJugadores_Apodo(jugador).orElse(new ArrayList<>());
    }
}
