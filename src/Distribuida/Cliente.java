package Distribuida;

import Distribuida.Interfaz.InterfazCS;
import java.rmi.Naming;

public class Cliente {
    public static void main(String args[]) {
        try {
            InterfazCS obj = (InterfazCS) Naming.lookup("//127.0.0.1/ObjetoSaluda");

            java.awt.EventQueue.invokeLater(() -> {
                InterfazRemota guiRemota = new InterfazRemota(obj);
                guiRemota.setVisible(true);
            });



        } catch (Exception e) {
            System.err.println("No se pudo conectar con el Servidor");
            e.printStackTrace();
        }
    }
}