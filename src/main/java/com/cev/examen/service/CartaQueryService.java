package com.cev.examen.service;

import com.cev.examen.domain.*; // for static metamodels
import com.cev.examen.domain.Carta;
import com.cev.examen.repository.CartaRepository;
import com.cev.examen.service.criteria.CartaCriteria;
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
 * Service for executing complex queries for {@link Carta} entities in the database.
 * The main input is a {@link CartaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Carta} or a {@link Page} of {@link Carta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CartaQueryService extends QueryService<Carta> {

    private final Logger log = LoggerFactory.getLogger(CartaQueryService.class);

    private final CartaRepository cartaRepository;

    public CartaQueryService(CartaRepository cartaRepository) {
        this.cartaRepository = cartaRepository;
    }

    /**
     * Return a {@link List} of {@link Carta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Carta> findByCriteria(CartaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Carta> specification = createSpecification(criteria);
        return cartaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Carta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Carta> findByCriteria(CartaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Carta> specification = createSpecification(criteria);
        return cartaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CartaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Carta> specification = createSpecification(criteria);
        return cartaRepository.count(specification);
    }

    /**
     * Function to convert {@link CartaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Carta> createSpecification(CartaCriteria criteria) {
        Specification<Carta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Carta_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Carta_.nombre));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Carta_.tipo));
            }
            if (criteria.getCosteMana() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCosteMana(), Carta_.costeMana));
            }
            if (criteria.getTexto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTexto(), Carta_.texto));
            }
            if (criteria.getJugadoresId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJugadoresId(), root -> root.join(Carta_.jugadores, JoinType.LEFT).get(Jugador_.id))
                    );
            }
        }
        return specification;
    }
}
