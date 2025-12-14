package com.project;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Crear carpeta data si no existe
        String basePath = System.getProperty("user.dir") + "/data/";
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Inicializar Hibernate
        Manager.createSessionFactory();

        // =====================================================
        // PUNT 1 - Crear 3 ciutats amb 2 ciutadans cada una
        // =====================================================
        System.out.println("========== PUNT 1: CREACIÓ D'ENTITATS ==========");

        Ciutat c1 = Manager.addCiutat("Vancouver", "Canada", 98661);
        Ciutat c2 = Manager.addCiutat("Växjö", "Suècia", 35220);
        Ciutat c3 = Manager.addCiutat("Kyoto", "Japó", 5200461);

        Ciutada c1a = Manager.addCiutada("Tony", "Happy", 20, c1.getCiutatId());
        Ciutada c1b = Manager.addCiutada("Monica", "Mouse", 22, c1.getCiutatId());

        Ciutada c2a = Manager.addCiutada("Eirika", "Erjo", 44, c2.getCiutatId());
        Ciutada c2b = Manager.addCiutada("Ven", "Enrison", 48, c2.getCiutatId());

        Ciutada c3a = Manager.addCiutada("Akira", "Akiko", 62, c3.getCiutatId());
        Ciutada c3b = Manager.addCiutada("Masako", "Kubo", 66, c3.getCiutatId());

        System.out.println("Ciutats creades:");
        System.out.println(Manager.collectionToString(Manager.listCollection(Ciutat.class)));

        System.out.println("Ciutadans creats:");
        System.out.println(Manager.collectionToString(Manager.listCollection(Ciutada.class)));

        // =====================================================
        // PUNT 2 - Llistar les ciutats i els seus ciutadans
        // =====================================================
        System.out.println("========== PUNT 2: LLISTAT DE CIUTATS I CIUTADANS ==========");
        List<Ciutat> ciutats = Manager.listCollection(Ciutat.class);

        for (Ciutat ciutat : ciutats) {
            System.out.println(ciutat);
            List<Ciutada> ciutadans = Manager.getCiutadansByCiutat(ciutat.getCiutatId());
            for (Ciutada ciutada : ciutadans) {
                System.out.println("  - " + ciutada);
            }
        }

        // =====================================================
        // PUNT 3 - Esborrar el segon ciutadà de cada ciutat
        // =====================================================
        System.out.println("========== PUNT 3: ESBORRAT DEL SEGON CIUTADÀ DE CADA CIUTAT ==========");
        for (Ciutat ciutat : ciutats) {
            List<Ciutada> ciutadans = Manager.getCiutadansByCiutat(ciutat.getCiutatId());
            if (ciutadans.size() >= 2) {
                Ciutada segon = ciutadans.get(1);
                System.out.println("Esborrant ciutadà: " + segon);
                Manager.delete(Ciutada.class, segon.getCiutadaId());
            }
        }

        System.out.println("Ciutadans després de l'esborrat:");
        System.out.println(Manager.collectionToString(Manager.listCollection(Ciutada.class)));

        // =====================================================
        // PUNT 4 - Esborrar la segona ciutat
        // =====================================================
        System.out.println("========== PUNT 4: ESBORRAT DE LA SEGONA CIUTAT ==========");
        if (ciutats.size() >= 2) {
            Ciutat ciutatEsborrada = ciutats.get(1);
            System.out.println("Esborrant ciutat: " + ciutatEsborrada);
            Manager.delete(Ciutat.class, ciutatEsborrada.getCiutatId());
        }

        // =====================================================
        // PUNT 5 - Llistar les ciutats
        // =====================================================
        System.out.println("========== PUNT 5: LLISTAT FINAL DE CIUTATS ==========");
        System.out.println(Manager.collectionToString(Manager.listCollection(Ciutat.class)));

        // =====================================================
        // PUNT 6 - Llistar els ciutadans
        // =====================================================
        System.out.println("========== PUNT 6: LLISTAT FINAL DE CIUTADANS ==========");
        System.out.println(Manager.collectionToString(Manager.listCollection(Ciutada.class)));

        // Cerrar Hibernate
        Manager.close();
    }
}
