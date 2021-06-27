package com.cev.examen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Jugador.
 */
@Entity
@Table(name = "jugador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "apodo", unique = true)
    private String apodo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_jugador__cartas",
        joinColumns = @JoinColumn(name = "jugador_id"),
        inverseJoinColumns = @JoinColumn(name = "cartas_id")
    )
    @JsonIgnoreProperties(value = { "jugadores" }, allowSetters = true)
    private Set<Carta> cartas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jugador id(Long id) {
        this.id = id;
        return this;
    }

    public String getApodo() {
        return this.apodo;
    }

    public Jugador apodo(String apodo) {
        this.apodo = apodo;
        return this;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Jugador nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Jugador apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Jugador fechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<Carta> getCartas() {
        return this.cartas;
    }

    public Jugador cartas(Set<Carta> cartas) {
        this.setCartas(cartas);
        return this;
    }

    public Jugador addCartas(Carta carta) {
        this.cartas.add(carta);
        carta.getJugadores().add(this);
        return this;
    }

    public Jugador removeCartas(Carta carta) {
        this.cartas.remove(carta);
        carta.getJugadores().remove(this);
        return this;
    }

    public void setCartas(Set<Carta> cartas) {
        this.cartas = cartas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jugador)) {
            return false;
        }
        return id != null && id.equals(((Jugador) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Jugador{" +
            "id=" + getId() +
            ", apodo='" + getApodo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            "}";
    }
}
