package com.project;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe que representa un ciutadà.
 */
public class Ciutada implements Serializable {

    // Identificador únic del ciutadà (clau primària)
    private long ciutadaId;

    private String nom;
    private String cognom;
    private int edat;

    /**
     * RELACIÓ LÒGICA: només l'ID de la ciutat a la que pertany.
     */
    private long ciutatId;

    // Constructor buit requerit per Hibernate
    public Ciutada() {}

    public Ciutada(String nom, String cognom, int edat, long ciutatId) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
        this.ciutatId = ciutatId;
    }

    public long getCiutadaId() {
        return ciutadaId;
    }

    public void setCiutadaId(long ciutadaId) {
        this.ciutadaId = ciutadaId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public int getEdat() {
        return edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    public long getCiutatId() {
        return ciutatId;
    }

    public void setCiutatId(long ciutatId) {
        this.ciutatId = ciutatId;
    }

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
        if (o == null || getClass() != o.getClass()) return false;
        Ciutada ciutada = (Ciutada) o;
        if (ciutadaId == 0 || ciutada.ciutadaId == 0) return this == ciutada;
        return ciutadaId == ciutada.ciutadaId;
    }

    @Override
    public int hashCode() {
        return (ciutadaId > 0) ? Objects.hash(ciutadaId) : super.hashCode();
    }
}
