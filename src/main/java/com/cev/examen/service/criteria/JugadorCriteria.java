package com.cev.examen.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cev.examen.domain.Jugador} entity. This class is used
 * in {@link com.cev.examen.web.rest.JugadorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jugadors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JugadorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter apodo;

    private StringFilter nombre;

    private StringFilter apellido;

    private LocalDateFilter fechaNacimiento;

    private LongFilter cartasId;

    public JugadorCriteria() {}

    public JugadorCriteria(JugadorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.apodo = other.apodo == null ? null : other.apodo.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.apellido = other.apellido == null ? null : other.apellido.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.cartasId = other.cartasId == null ? null : other.cartasId.copy();
    }

    @Override
    public JugadorCriteria copy() {
        return new JugadorCriteria(this);
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

    public StringFilter getApodo() {
        return apodo;
    }

    public StringFilter apodo() {
        if (apodo == null) {
            apodo = new StringFilter();
        }
        return apodo;
    }

    public void setApodo(StringFilter apodo) {
        this.apodo = apodo;
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

    public StringFilter getApellido() {
        return apellido;
    }

    public StringFilter apellido() {
        if (apellido == null) {
            apellido = new StringFilter();
        }
        return apellido;
    }

    public void setApellido(StringFilter apellido) {
        this.apellido = apellido;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDateFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            fechaNacimiento = new LocalDateFilter();
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LongFilter getCartasId() {
        return cartasId;
    }

    public LongFilter cartasId() {
        if (cartasId == null) {
            cartasId = new LongFilter();
        }
        return cartasId;
    }

    public void setCartasId(LongFilter cartasId) {
        this.cartasId = cartasId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JugadorCriteria that = (JugadorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(apodo, that.apodo) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(cartasId, that.cartasId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, apodo, nombre, apellido, fechaNacimiento, cartasId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JugadorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (apodo != null ? "apodo=" + apodo + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (apellido != null ? "apellido=" + apellido + ", " : "") +
            (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
            (cartasId != null ? "cartasId=" + cartasId + ", " : "") +
            "}";
    }
}
