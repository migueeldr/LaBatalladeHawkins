package Concurrente;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InterfazHawkins extends JFrame {
    private Ciudad mapa;
    private Eventos eventos;

    // Componentes de la UI
    private JTextArea[] txtZonas = new JTextArea[8];
    private JLabel lblSangre, lblCapturas, lblEventoActual;
    private JTextArea logArea;

    public InterfazHawkins(Ciudad mapa, Eventos eventos) {
        this.mapa = mapa;
        this.eventos = eventos;

        setTitle("Hawkins Monitoring System - Upside Down Project");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Panel Superior: Status General ---
        JPanel panelStatus = new JPanel(new GridLayout(1, 3));
        panelStatus.setBorder(BorderFactory.createTitledBorder("Estado Global"));
        lblSangre = new JLabel("Sangre disponible: 0", SwingConstants.CENTER);
        lblCapturas = new JLabel("Niños capturados: 0", SwingConstants.CENTER);
        lblEventoActual = new JLabel("Evento: NORMALIDAD", SwingConstants.CENTER);
        lblEventoActual.setForeground(Color.BLUE);
        panelStatus.add(lblSangre);
        panelStatus.add(lblCapturas);
        panelStatus.add(lblEventoActual);
        add(panelStatus, BorderLayout.NORTH);

        // --- Panel Central: Zonas ---
        JPanel panelZonas = new JPanel(new GridLayout(2, 4, 10, 10));
        String[] nombresZonas = {
                "Calle Principal", "Sótano Byers", "Radio WSQK", "Bosque",
                "Laboratorio", "Centro Comercial", "Alcantarillado", "Colmena (HIVE)"
        };

        for (int i = 0; i < 8; i++) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(BorderFactory.createTitledBorder(nombresZonas[i]));
            txtZonas[i] = new JTextArea();
            txtZonas[i].setEditable(false);
            txtZonas[i].setBackground(i < 3 ? new Color(230, 255, 230) : new Color(255, 230, 230));
            p.add(new JScrollPane(txtZonas[i]), BorderLayout.CENTER);
            panelZonas.add(p);
        }
        add(panelZonas, BorderLayout.CENTER);

        // --- Panel Inferior: Log ---
        logArea = new JTextArea(8, 20);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Timer para refrescar la interfaz cada 200ms
        Timer timer = new Timer(200, e -> refrescarDatos());
        timer.start();
    }

    private void refrescarDatos() {
        // Actualizar contadores
        lblSangre.setText("Sangre disponible: " + mapa.getContador_sangre());
        lblCapturas.setText("Niños capturados: " + mapa.getContador_capturas());

        // Actualizar Evento
        String[] nombresEventos = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN INTERVIENE", "RED MENTAL"};
        lblEventoActual.setText("Evento: " + nombresEventos[eventos.getEventoActual()]);
        lblEventoActual.setForeground(eventos.getEventoActual() == 0 ? Color.BLUE : Color.RED);

        // Actualizar Listado de Niños y Demogorgons por zona
        for (int i = 0; i < 8; i++) {
            StringBuilder sb = new StringBuilder();

            // Obtener niños en la zona
            List<Niño> niños = mapa.getListaUbicacionN(i);
            synchronized (niños) {
                for (Niño n : niños) {
                    sb.append(" [").append(n.getIdNiño()).append(n.getLleva_sangre() ? "🩸" : "").append("]");
                }
            }

            // Obtener demogorgons (solo en zonas 3 a 7)
            if (i >= 3) {
                List<Demogorgon> demos = mapa.getListaUbicacionD(i);
                synchronized (demos) {
                    for (Demogorgon d : demos) {
                        sb.append("\n 👾 ").append(d.getIdDemogorgon());
                    }
                }
            }
            txtZonas[i].setText(sb.toString());
        }
    }
}



