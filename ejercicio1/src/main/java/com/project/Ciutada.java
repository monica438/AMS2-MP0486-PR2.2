package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase Ciutada con anotaciones JPA.
 * Relación lógica con Ciutat mediante el campo ciutatId.
 */
@Entity
@Table(name = "Ciutada")
public class Ciutada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ciutadaId;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String cognom;

    @Column(nullable = false)
    private int edat;

    /**
     * Relación lógica con Ciutat
     */
    @Column(nullable = false)
    private long ciutatId;

    public Ciutada() {}

    public Ciutada(String nom, String cognom, int edat, long ciutatId) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
        this.ciutatId = ciutatId;
    }

    // Getters y setters
    public long getCiutadaId() { return ciutadaId; }
    public void setCiutadaId(long ciutadaId) { this.ciutadaId = ciutadaId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }

    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }

    @Override
    public String toString() {
        return String.format(
                "Ciutada [ID=%d, Nom=%s, Cognom=%s, Edat=%d, CiutatID=%d]",
                ciutadaId, nom, cognom, edat, ciutatId
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutada)) return false;
        Ciutada ciutada = (Ciutada) o;
        return ciutadaId == ciutada.ciutadaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ciutadaId);
    }
}
