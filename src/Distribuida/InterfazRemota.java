

 package Distribuida;

import Distribuida.Interfaz.InterfazCS;
import javax.swing.*;
        import java.awt.*;
        import java.rmi.RemoteException;
import java.util.ArrayList;

public class InterfazRemota extends JFrame {
    private final InterfazCS obj;

    // Componentes de Resumen Hawkins y Portales
    private JLabel lblTotalHawkins;
    private JLabel[] lblPortales = new JLabel[4];

    // Componentes de Ubicaciones (Niños y Demogorgons)
    private JLabel[] lblNiñosZonas = new JLabel[5]; // Bosque, Lab, Centro, Alcant, Colmena
    private JLabel[] lblDemosZonas = new JLabel[5];

    // Ranking y Eventos
    private JTextArea txtRanking;
    private JLabel lblEventoTipo;
    private JButton btnPausa;
    private boolean programaPausado = false;

    public InterfazRemota(InterfazCS obj) {
        this.obj = obj;
        configurarVentana();
        inicializarComponentes();

        // Timer para actualización automática cada 500ms
        Timer timer = new Timer(200, e -> actualizarDatos());
        timer.start();
    }

    private void configurarVentana() {
        setTitle("STRANGER THINGS - MODULO REMOTO");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(10, 30, 10)); // Estilo oscuro
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 3, 10, 10));
        panelPrincipal.setOpaque(false);

        // --- COLUMNA 1: RESUMEN HAWKINS Y PORTALES ---
        JPanel col1 = crearPanelSeccion("RESUMEN HAWKINS");
        lblTotalHawkins = crearLabel("TOTAL NIÑOS: [0]");
        col1.add(lblTotalHawkins);
        col1.add(new JLabel("<html><br><font color='white'>ESTADO DE PORTALES:</font></html>"));
        for (int i = 0; i < 4; i++) {
            lblPortales[i] = crearLabel("PORTAL " + (i + 1) + ": [0] niños");
            col1.add(lblPortales[i]);
        }
        panelPrincipal.add(col1);

        // --- COLUMNA 2: ESTADO DEL UPSIDE DOWN ---
        JPanel col2 = crearPanelSeccion("ESTADO DEL UPSIDE DOWN");
        String[] nombres = {"BOSQUE", "LABORATORIO", "CENTRO COMERCIAL", "ALCANTARILLADO", "COLMENA"};
        for (int i = 0; i < 5; i++) {
            lblNiñosZonas[i] = crearLabel(nombres[i] + " (N): 0");
            lblDemosZonas[i] = crearLabel(nombres[i] + " (D): 0");
            col2.add(lblNiñosZonas[i]);
            col2.add(lblDemosZonas[i]);
        }
        panelPrincipal.add(col2);

        // --- COLUMNA 3: RANKING Y EVENTO ---
        JPanel col3 = crearPanelSeccion("RANKING & EVENTOS");
        txtRanking = new JTextArea(5, 15);
        txtRanking.setEditable(false);
        txtRanking.setBackground(Color.BLACK);
        txtRanking.setForeground(Color.GREEN);
        col3.add(new JScrollPane(txtRanking));

        lblEventoTipo = crearLabel("EVENTO: NORMALIDAD");
        col3.add(lblEventoTipo);

        btnPausa = new JButton("DETENER PROGRAMA PRINCIPAL");
        btnPausa.addActionListener(e -> alternarSimulacion());
        col3.add(btnPausa);

        panelPrincipal.add(col3);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void actualizarDatos() {
        try {
            // Quitamos el (null) de todas las llamadas
            lblTotalHawkins.setText("TOTAL NIÑOS EN HAWKINS: [" + obj.niños_hawkins() + "]");

            lblPortales[0].setText("PORTAL BOSQUE: [" + obj.niños_portalesBosque() + "] niños");
            lblPortales[1].setText("PORTAL LABORATORIO: [" + obj.niños_portalesLaboratorio() + "] niños");
            lblPortales[2].setText("PORTAL CENTRO COMERCIAL: [" + obj.niños_portalesCentroComercial() + "] niños");
            lblPortales[3].setText("PORTAL ALCANTARILLADO: [" + obj.niños_portalesAlcantarillado() + "] niños");

            // Actualizar Niños y Demogorgons en zonas
            lblNiñosZonas[0].setText("BOSQUE (N): " + obj.niños_Bosque());
            lblNiñosZonas[1].setText("LABORATORIO (N): " + obj.niños_Laboratorio());
            lblNiñosZonas[2].setText("CENTRO COM. (N): " + obj.niños_CentroComercial());
            lblNiñosZonas[3].setText("ALCANTARILLADO (N): " + obj.niños_Alcantarillado());
            lblNiñosZonas[4].setText("[!] COLMENA (N): " + obj.niños_Colmena());

            lblDemosZonas[0].setText("BOSQUE (D): " + obj.demogorgons_Bosque());
            lblDemosZonas[1].setText("LABORATORIO (D): " + obj.demogorgons_Laboratorio());
            lblDemosZonas[2].setText("CENTRO COM. (D): " + obj.demogorgons_CentroComercial());
            lblDemosZonas[3].setText("ALCANTARILLADO (D): " + obj.demogorgons_Alcantarillado());
            lblDemosZonas[4].setText("COLMENA (D): " + obj.demogorgons_Colmena());

            // Ranking (también sin parámetros)
            ArrayList<String> top3 = obj.top3_Demogorgons();
            txtRanking.setText("TOP 3 CAPTURAS:\n1. " + top3.get(0) + "\n2. " + top3.get(1) + "\n3. " + top3.get(2));

            // Evento Global (también sin parámetros)
            lblEventoTipo.setText("EVENTO: " + obj.devolver_evento());

        } catch (RemoteException ex) {
            System.err.println("Error de conexión RMI: " + ex.getMessage());
        }
    }

    private void alternarSimulacion() {
        try {
            obj.start_stop(); // Llama al método remoto para pausar/reanudar [cite: 148]
            programaPausado = !programaPausado;
            btnPausa.setText(programaPausado ? "REANUDAR PROGRAMA" : "DETENER PROGRAMA PRINCIPAL");
            btnPausa.setBackground(programaPausado ? Color.GREEN : Color.RED);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // Métodos auxiliares de UI
    private JPanel crearPanelSeccion(String titulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder(null, titulo, 0, 0, null, Color.GREEN));
        p.setOpaque(false);
        return p;
    }

    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Monospaced", Font.BOLD, 12));
        return l;
    }
}
