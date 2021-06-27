package com.cev.examen.repository;

import com.cev.examen.domain.Carta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Carta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartaRepository extends JpaRepository<Carta, Long>, JpaSpecificationExecutor<Carta> {
	Optional<List<Carta>> findAllByJugadores_Apodo(String apodo);
}
