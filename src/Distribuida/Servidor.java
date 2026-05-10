package Distribuida;

import Concurrente.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

public class Servidor {
    public static void main(String args[]) {
        try {

            Ciudad mapa = new Ciudad();
            LogHawkins log = new LogHawkins();
            Eventos eventos = new Eventos(mapa, log);


            java.awt.EventQueue.invokeLater(() -> {
                InterfazHawkins guiPrincipal = new InterfazHawkins(mapa, eventos);
                guiPrincipal.setVisible(true);
            });

            eventos.start();
            new Demogorgon(0, mapa, log, eventos).start();

            Thread hiloGenerador = new Thread(() -> {
                mapa.registrarHilo(Thread.currentThread());

                for (int i = 0; i < 1500; i++) {
                    try {
                        mapa.comprobarPausa();

                        Thread.sleep((long) (Math.random() * 1500 + 500));

                        mapa.comprobarPausa();

                        Niño n = new Niño(i, mapa, log, eventos);
                        n.start();

                    } catch (InterruptedException e) {

                        mapa.comprobarPausa();
                    }
                }
            });

// Iniciamos la generación
            hiloGenerador.start();

            // 4. Configurar RMI para el acceso remoto
            try {
                LocateRegistry.createRegistry(1099); // Crea el registro en el puerto por defecto
            } catch (Exception e) {
                // Si ya existe un registro activo, continuamos
            }

            ImpletancionMetodosR obj = new ImpletancionMetodosR(mapa, eventos);
            Naming.rebind("//127.0.0.1/ObjetoSaluda", obj);

            System.out.println(">>> Servidor en ejecución: Simulación iniciada y RMI registrado.");

        } catch (Exception e) {
            System.err.println("Error crítico en el Servidor:");
            e.printStackTrace();
        }
    }
}