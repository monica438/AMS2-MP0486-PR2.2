package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Clase Ciutat con anotaciones JPA.
 * Relación lógica con Ciutada mediante IDs.
 */
@Entity
@Table(name = "Ciutat")
public class Ciutat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ciutatId;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private int poblacio;

    /**
     * Relación lógica con Ciutada
     * No hay OneToMany real, solo mantenemos Set para visualización
     */
    @Transient
    private Set<Ciutada> ciutadans = new HashSet<>();

    public Ciutat() {}

    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    // Getters y setters
    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public int getPoblacio() { return poblacio; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }

    public Set<Ciutada> getCiutadans() { return ciutadans; }
    public void setCiutadans(Set<Ciutada> ciutadans) { this.ciutadans = ciutadans; }

    @Override
    public String toString() {
        return String.format(
                "Ciutat [ID=%d, Nom=%s, País=%s, Població=%d, Ciutadans=%s]",
                ciutatId, nom, pais, poblacio, ciutadans
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutat)) return false;
        Ciutat ciutat = (Ciutat) o;
        return ciutatId == ciutat.ciutatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ciutatId);
    }
}
