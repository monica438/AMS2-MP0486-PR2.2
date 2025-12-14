package com.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe MANAGER: Patró DAO per a Ciutat i Ciutada amb Hibernate + JPA Annotations
 */
public class Manager {

    private static SessionFactory factory;

    // ────────────────
    // Inicialització
    // ────────────────
    public static void createSessionFactory() {
        createSessionFactory("hibernate.properties");
    }

    public static void createSessionFactory(String propertiesFileName) {
        try {
            Configuration configuration = new Configuration();

            // Registrar les classes anotades
            configuration.addAnnotatedClass(Ciutat.class);
            configuration.addAnnotatedClass(Ciutada.class);

            // Carregar propietats
            Properties properties = new Properties();
            try (InputStream input = Manager.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                if (input == null) throw new IOException("No s'ha pogut trobar " + propertiesFileName);
                properties.load(input);
            }
            configuration.addProperties(properties);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            factory = configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            System.err.println("Error en crear sessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void close() {
        if (factory != null) factory.close();
    }

    // ────────────────
    // CREATE
    // ────────────────
    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Ciutat ciutat = new Ciutat(nom, pais, poblacio);
            session.persist(ciutat);
            tx.commit();
            return ciutat;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    public static Ciutada addCiutada(String nom, String cognom, int edat, Long ciutatId) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Ciutada ciutada = new Ciutada(nom, cognom, edat, ciutatId);
            session.persist(ciutada);
            tx.commit();
            return ciutada;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    // ────────────────
    // READ
    // ────────────────
    public static <T> List<T> listCollection(Class<T> clazz) {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM " + clazz.getName(), clazz).list();
        }
    }

    public static List<Ciutada> getCiutadansByCiutat(Long ciutatId) {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Ciutada WHERE ciutatId = :cid", Ciutada.class)
                    .setParameter("cid", ciutatId)
                    .list();
        }
    }

    // ────────────────
    // DELETE
    // ────────────────
    public static <T> void delete(Class<T> clazz, Serializable id) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T obj = session.get(clazz, id);
            if (obj != null) {
                session.remove(obj);
                tx.commit();
                System.out.println("Eliminat " + clazz.getSimpleName() + " amb id " + id);
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    // ────────────────
    // UTILITATS
    // ────────────────
    public static <T> String collectionToString(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }
}
