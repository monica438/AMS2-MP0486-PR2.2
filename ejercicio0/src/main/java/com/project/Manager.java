package com.project;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe Manager:
 * Gestiona totes les operacions amb la base de dades mitjançant Hibernate.
 */
public class Manager {

    /**
     * SessionFactory:
     * Objecte principal d'Hibernate. És thread-safe i s'ha de crear un sol cop.
     */
    private static SessionFactory factory;

    /**
     * Inicialitza Hibernate llegint hibernate.cfg.xml
     */
    public static void createSessionFactory() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error creating SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void close() {
        if (factory != null) factory.close();
    }

    // ============================================================
    // GESTIÓ DE TRANSACCIONS (PATRÓ DRY)
    // ============================================================

    private static void executeInTransaction(Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }

    private static <T> T executeInTransactionWithResult(Function<Session, T> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }

    // ============================================================
    // CREATE
    // ============================================================

    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = new Ciutat(nom, pais, poblacio);
            session.persist(ciutat);
            return ciutat;
        });
    }

    public static Ciutada addCiutada(String nom, String cognom, int edat, long ciutatId) {
        return executeInTransactionWithResult(session -> {
            Ciutada ciutada = new Ciutada(nom, cognom, edat, ciutatId);
            session.persist(ciutada);
            return ciutada;
        });
    }

    // ============================================================
    // UPDATE
    // ============================================================

    /**
     * Actualitza una ciutat i assigna lògicament els ciutadans.
     * NO és una relació Hibernate, només s'actualitza el ciutatId
     * dels ciutadans.
     */
    public static void updateCiutat(long ciutatId, String nom, String pais,
                                    int poblacio, Set<Ciutada> ciutadans) {

        executeInTransaction(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat == null) return;

            ciutat.setNom(nom);
            ciutat.setPais(pais);
            ciutat.setPoblacio(poblacio);

            if (ciutadans != null) {
                for (Ciutada c : ciutadans) {
                    Ciutada managed = session.get(Ciutada.class, c.getCiutadaId());
                    if (managed != null) {
                        managed.setCiutatId(ciutatId);
                        session.merge(managed);
                    }
                }
            }

            session.merge(ciutat);
        });
    }

    public static void updateCiutada(long ciutadaId, String nom, String cognom, int edat) {
        executeInTransaction(session -> {
            Ciutada ciutada = session.get(Ciutada.class, ciutadaId);
            if (ciutada != null) {
                ciutada.setNom(nom);
                ciutada.setCognom(cognom);
                ciutada.setEdat(edat);
                session.merge(ciutada);
            }
        });
    }

    // ============================================================
    // READ
    // ============================================================

    public static <T> T getById(Class<T> clazz, long id) {
        return executeInTransactionWithResult(session -> session.get(clazz, id));
    }

    /**
     * Recupera una ciutat i carrega els seus ciutadans
     * mitjançant una consulta HQL (relació lògica).
     */
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat != null) {
                List<Ciutada> llista = session.createQuery(
                        "FROM Ciutada WHERE ciutatId = :id", Ciutada.class)
                        .setParameter("id", ciutatId)
                        .list();
                ciutat.setCiutadans(new HashSet<>(llista));
            }
            return ciutat;
        });
    }

    /**
     * Retorna tots els ciutadans d'una ciutat (relació lògica).
     */
    public static List<Ciutada> getCiutadansByCiutat(long ciutatId) {
        return executeInTransactionWithResult(session ->
                session.createQuery(
                        "FROM Ciutada WHERE ciutatId = :id", Ciutada.class)
                        .setParameter("id", ciutatId)
                        .list()
        );
    }

    public static <T> List<T> listCollection(Class<T> clazz, String whereClause) {
        return executeInTransactionWithResult(session -> {
            String hql = "FROM " + clazz.getName();
            if (whereClause != null && !whereClause.trim().isEmpty()) {
                hql += " WHERE " + whereClause;
            }
            return session.createQuery(hql, clazz).list();
        });
    }

    public static <T> List<T> listCollection(Class<T> clazz) {
        return listCollection(clazz, "");
    }

    // ============================================================
    // DELETE
    // ============================================================

    public static <T> void delete(Class<T> clazz, Serializable id) {
        executeInTransaction(session -> {
            T obj = session.get(clazz, id);
            if (obj != null) {
                session.remove(obj);
            }
        });
    }

    // ============================================================
    // UTILITATS
    // ============================================================

    public static <T> String collectionToString(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }

    public static <T> String collectionToString(Class<T> clazz, Collection<T> collection) {
        if (collection == null || collection.isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder();
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }
}
