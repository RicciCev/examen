package com.cev.examen.service;

import com.cev.examen.domain.*; // for static metamodels
import com.cev.examen.domain.Jugador;
import com.cev.examen.repository.JugadorRepository;
import com.cev.examen.service.criteria.JugadorCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Jugador} entities in the database.
 * The main input is a {@link JugadorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Jugador} or a {@link Page} of {@link Jugador} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JugadorQueryService extends QueryService<Jugador> {

    private final Logger log = LoggerFactory.getLogger(JugadorQueryService.class);

    private final JugadorRepository jugadorRepository;

    public JugadorQueryService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Return a {@link List} of {@link Jugador} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Jugador> findByCriteria(JugadorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Jugador> specification = createSpecification(criteria);
        return jugadorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Jugador} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Jugador> findByCriteria(JugadorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Jugador> specification = createSpecification(criteria);
        return jugadorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JugadorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Jugador> specification = createSpecification(criteria);
        return jugadorRepository.count(specification);
    }

    /**
     * Function to convert {@link JugadorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Jugador> createSpecification(JugadorCriteria criteria) {
        Specification<Jugador> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Jugador_.id));
            }
            if (criteria.getApodo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApodo(), Jugador_.apodo));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Jugador_.nombre));
            }
            if (criteria.getApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellido(), Jugador_.apellido));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Jugador_.fechaNacimiento));
            }
            if (criteria.getCartasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCartasId(), root -> root.join(Jugador_.cartas, JoinType.LEFT).get(Carta_.id))
                    );
            }
        }
        return specification;
    }
}
