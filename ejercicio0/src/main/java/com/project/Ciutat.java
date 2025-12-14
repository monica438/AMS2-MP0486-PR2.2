package com.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe que representa una ciutat.
 * 
 * Implementa Serializable per permetre persistència i transport d'objectes.
 */
public class Ciutat implements Serializable {

    // Identificador únic de la ciutat (clau primària)
    private long ciutatId;

    private String nom;
    private String pais;

    // El codi postal
    private int poblacio;

    /**
     * Col·lecció de ciutadans ASSOCIADA LÒGICAMENT (la relació no apareix al hbm.xml).
     */
    private Set<Ciutada> ciutadans = new HashSet<>();

    // Constructor buit requerit per Hibernate
    public Ciutat() {}

    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    public long getCiutatId() {
        return ciutatId;
    }

    public void setCiutatId(long ciutatId) {
        this.ciutatId = ciutatId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(int poblacio) {
        this.poblacio = poblacio;
    }

    /**
     * Retorna els ciutadans associats lògicament a la ciutat.
     */
    public Set<Ciutada> getCiutadans() {
        return ciutadans;
    }

    public void setCiutadans(Set<Ciutada> ciutadans) {
        this.ciutadans = ciutadans;
    }

    @Override
    public String toString() {
        String llistaCiutadans = "[]";

        if (ciutadans != null && !ciutadans.isEmpty()) {
            llistaCiutadans = ciutadans.stream()
                    .map(c -> c.getNom() + " " + c.getCognom())
                    .collect(Collectors.joining(", ", "[", "]"));
        }

        return String.format(
                "Ciutat [ID=%d, Nom=%s, País=%s, Població=%d, Ciutadans=%s]",
                ciutatId, nom, pais, poblacio, llistaCiutadans
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciutat ciutat = (Ciutat) o;
        if (ciutatId == 0 || ciutat.ciutatId == 0) return this == ciutat;
        return ciutatId == ciutat.ciutatId;
    }

    @Override
    public int hashCode() {
        return (ciutatId > 0) ? Objects.hash(ciutatId) : super.hashCode();
    }
}
