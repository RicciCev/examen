package com.cev.examen.repository;

import com.cev.examen.domain.Carta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Carta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartaRepository extends JpaRepository<Carta, Long>, JpaSpecificationExecutor<Carta> {}
