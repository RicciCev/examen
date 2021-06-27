package com.cev.examen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Carta.
 */
@Entity
@Table(name = "carta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Carta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true)
    private String nombre;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "coste_mana")
    private Integer costeMana;

    @Column(name = "texto")
    private String texto;

    @ManyToMany(mappedBy = "cartas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cartas" }, allowSetters = true)
    private Set<Jugador> jugadores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carta id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Carta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Carta tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCosteMana() {
        return this.costeMana;
    }

    public Carta costeMana(Integer costeMana) {
        this.costeMana = costeMana;
        return this;
    }

    public void setCosteMana(Integer costeMana) {
        this.costeMana = costeMana;
    }

    public String getTexto() {
        return this.texto;
    }

    public Carta texto(String texto) {
        this.texto = texto;
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Set<Jugador> getJugadores() {
        return this.jugadores;
    }

    public Carta jugadores(Set<Jugador> jugadors) {
        this.setJugadores(jugadors);
        return this;
    }

    public Carta addJugadores(Jugador jugador) {
        this.jugadores.add(jugador);
        jugador.getCartas().add(this);
        return this;
    }

    public Carta removeJugadores(Jugador jugador) {
        this.jugadores.remove(jugador);
        jugador.getCartas().remove(this);
        return this;
    }

    public void setJugadores(Set<Jugador> jugadors) {
        if (this.jugadores != null) {
            this.jugadores.forEach(i -> i.removeCartas(this));
        }
        if (jugadors != null) {
            jugadors.forEach(i -> i.addCartas(this));
        }
        this.jugadores = jugadors;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Carta)) {
            return false;
        }
        return id != null && id.equals(((Carta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Carta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", costeMana=" + getCosteMana() +
            ", texto='" + getTexto() + "'" +
            "}";
    }
}
