package com.cev.examen.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cev.examen.domain.Carta} entity. This class is used
 * in {@link com.cev.examen.web.rest.CartaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cartas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CartaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter tipo;

    private IntegerFilter costeMana;

    private StringFilter texto;

    private LongFilter jugadoresId;

    public CartaCriteria() {}

    public CartaCriteria(CartaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.costeMana = other.costeMana == null ? null : other.costeMana.copy();
        this.texto = other.texto == null ? null : other.texto.copy();
        this.jugadoresId = other.jugadoresId == null ? null : other.jugadoresId.copy();
    }

    @Override
    public CartaCriteria copy() {
        return new CartaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getTipo() {
        return tipo;
    }

    public StringFilter tipo() {
        if (tipo == null) {
            tipo = new StringFilter();
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public IntegerFilter getCosteMana() {
        return costeMana;
    }

    public IntegerFilter costeMana() {
        if (costeMana == null) {
            costeMana = new IntegerFilter();
        }
        return costeMana;
    }

    public void setCosteMana(IntegerFilter costeMana) {
        this.costeMana = costeMana;
    }

    public StringFilter getTexto() {
        return texto;
    }

    public StringFilter texto() {
        if (texto == null) {
            texto = new StringFilter();
        }
        return texto;
    }

    public void setTexto(StringFilter texto) {
        this.texto = texto;
    }

    public LongFilter getJugadoresId() {
        return jugadoresId;
    }

    public LongFilter jugadoresId() {
        if (jugadoresId == null) {
            jugadoresId = new LongFilter();
        }
        return jugadoresId;
    }

    public void setJugadoresId(LongFilter jugadoresId) {
        this.jugadoresId = jugadoresId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CartaCriteria that = (CartaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(costeMana, that.costeMana) &&
            Objects.equals(texto, that.texto) &&
            Objects.equals(jugadoresId, that.jugadoresId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, tipo, costeMana, texto, jugadoresId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (costeMana != null ? "costeMana=" + costeMana + ", " : "") +
            (texto != null ? "texto=" + texto + ", " : "") +
            (jugadoresId != null ? "jugadoresId=" + jugadoresId + ", " : "") +
            "}";
    }
}
