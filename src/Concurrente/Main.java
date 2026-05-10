package Concurrente;

public class Main {
    public static void main(String[] args) {
        Ciudad mapaHawkins = new Ciudad();
        LogHawkins logHawkins = new LogHawkins();
        Eventos eventosHawkins = new Eventos(mapaHawkins, logHawkins);

        java.awt.EventQueue.invokeLater(() -> {
            InterfazHawkins gui = new InterfazHawkins(mapaHawkins, eventosHawkins);
            gui.setVisible(true);
        });

        eventosHawkins.start();

        Demogorgon demogorgonAlfa = new Demogorgon(0, mapaHawkins, logHawkins, eventosHawkins);

        demogorgonAlfa.start();

        for (int i = 0; i < 1500; i++) {
            try {
                Thread.sleep((long) (Math.random() * 1000 + 1000));
                Niño n = new Niño(i, mapaHawkins, logHawkins, eventosHawkins);
                n.start();
            } catch (Exception e) {}
        }
    }
}