package com.cev.examen.repository;

import com.cev.examen.domain.Jugador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Jugador entity.
 */
@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long>, JpaSpecificationExecutor<Jugador> {
    @Query(
        value = "select distinct jugador from Jugador jugador left join fetch jugador.cartas",
        countQuery = "select count(distinct jugador) from Jugador jugador"
    )
    Page<Jugador> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct jugador from Jugador jugador left join fetch jugador.cartas")
    List<Jugador> findAllWithEagerRelationships();

    @Query("select jugador from Jugador jugador left join fetch jugador.cartas where jugador.id =:id")
    Optional<Jugador> findOneWithEagerRelationships(@Param("id") Long id);
}
