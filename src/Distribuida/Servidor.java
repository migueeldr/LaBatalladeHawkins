package Distribuida;

import Concurrente.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

public class Servidor {
    public static void main(String args[]) {
        try {
            // 1. Inicializar los objetos compartidos de la simulación
            Ciudad mapa = new Ciudad();
            LogHawkins log = new LogHawkins();
            Eventos eventos = new Eventos(mapa, log);

            // 2. LANZAR LA INTERFAZ PRINCIPAL (El mapa de Hawkins)
            // Sin esto, el servidor no mostrará nada en pantalla
            java.awt.EventQueue.invokeLater(() -> {
                InterfazHawkins guiPrincipal = new InterfazHawkins(mapa, eventos);
                guiPrincipal.setVisible(true);
            });

            // 3. Iniciar la lógica de hilos (Simulación)
            eventos.start();
            new Demogorgon(0, mapa, log, eventos).start(); // Demogorgon Alpha [cite: 44]

            // Lanzador de niños paulatinos (0.5 a 2 segundos) [cite: 36]
            // Definimos el hilo generador de niños
            Thread hiloGenerador = new Thread(() -> {
                // 1. Muy importante: registrar este hilo para que reciba interrupciones de pausa
                mapa.registrarHilo(Thread.currentThread());

                for (int i = 0; i < 1500; i++) {
                    try {
                        // 2. Comprobar si está pausado antes de esperar
                        mapa.comprobarPausa();

                        // Tiempo de espera entre niños (0.5 a 2 segundos) [cite: 14]
                        Thread.sleep((long) (Math.random() * 1500 + 500));

                        // 3. Comprobar de nuevo tras el sleep por si se pausó mientras dormía
                        mapa.comprobarPausa();

                        Niño n = new Niño(i, mapa, log, eventos);
                        n.start();

                    } catch (InterruptedException e) {
                        // Si el hilo es interrumpido por el botón de pausa durante el sleep,
                        // comprobarPausa() lo bloqueará hasta que se pulse reanudar.
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