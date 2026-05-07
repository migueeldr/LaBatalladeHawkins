package Distribuida;

import Distribuida.Interfaz.InterfazCS;
import java.rmi.Naming;

public class Cliente {
    public static void main(String args[]) {
        try {
            // Conectar con el servidor RMI
            InterfazCS stub = (InterfazCS) Naming.lookup("//127.0.0.1/ObjetoSaluda");

            // LANZAR LA INTERFAZ REMOTA (Módulo Remoto)
            java.awt.EventQueue.invokeLater(() -> {
                InterfazRemota guiRemota = new InterfazRemota(stub);
                guiRemota.setVisible(true);
            });

            System.out.println(">>> Cliente conectado con éxito al servidor de Hawkins.");

        } catch (Exception e) {
            System.err.println("No se pudo conectar con el Servidor. Asegúrate de que Servidor.java esté corriendo.");
            e.printStackTrace();
        }
    }
}