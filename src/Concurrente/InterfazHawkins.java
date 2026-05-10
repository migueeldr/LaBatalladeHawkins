package Concurrente;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class InterfazHawkins extends JFrame {
    private Ciudad mapa;
    private Eventos eventos;

    // colores
    private final Color bgPrincipal = new Color(35, 35, 35);
    private final Color bgPaneles = new Color(45, 45, 45);
    private final Color bgTexto = new Color(30, 30, 30);

    private final Color verde = new Color(106, 243, 106); // DarkSeaGreen
    private final Color amarillo = new Color(237, 221, 72); // Khaki
    private final Color azul = new Color(49, 168, 218); // SkyBlue
    private final Color rojo = new Color(205, 92, 92); // IndianRed
    private final Color naranja = new Color(244, 164, 96); // SandyBrown
    private final Color magenta = new Color(186, 47, 191, 255); // Gainsboro

    // contadores
    private JLabel lblSangre, lblCapturas, lblEventoActual;

    // texto zonas
    private JTextArea txtCallePrincipal, txtRadioWsqk, txtColmena;

    // filas cola poertal ud
    private JTextArea[] txtEsperaPortal = new JTextArea[4];
    private JTextArea[] txtGrupoFormado = new JTextArea[4];
    private JTextArea[] txtCruzando = new JTextArea[4];
    private JTextArea[] txtNiñosUD = new JTextArea[4];
    private JTextArea[] txtDemosUD = new JTextArea[4];

    public InterfazHawkins(Ciudad mapa, Eventos eventos) {
        this.mapa = mapa;
        this.eventos = eventos;

        setTitle("Interfaz Grafica Hawkins");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bgPrincipal);

        // area contadores
        JPanel panelStatus = new JPanel(new GridLayout(1, 3));
        panelStatus.setBackground(bgPaneles);
        panelStatus.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblSangre = crearLabelStatus("🩸 SANGRE: 0", rojo);
        lblCapturas = crearLabelStatus("👤 CAPTURAS: 0", naranja);
        lblEventoActual = crearLabelStatus("EVENTO: NORMALIDAD", azul);
        panelStatus.add(lblSangre);
        panelStatus.add(lblCapturas);
        panelStatus.add(lblEventoActual);
        add(panelStatus, BorderLayout.NORTH);

        // zonas hawkins
        JPanel panelOeste = new JPanel(new GridLayout(2, 1, 10, 10));
        panelOeste.setOpaque(false);
        panelOeste.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        txtCallePrincipal = crearTextArea();
        txtRadioWsqk = crearTextArea();

        panelOeste.add(crearPanelScroll("CALLE PRINCIPAL", txtCallePrincipal, verde, new Dimension(220, 0)));
        panelOeste.add(crearPanelScroll("RADIO WSQK ", txtRadioWsqk, verde, new Dimension(220, 0)));
        add(panelOeste, BorderLayout.WEST);

        // colmena
        JPanel panelEste = new JPanel(new BorderLayout());
        panelEste.setOpaque(false);
        panelEste.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        txtColmena = crearTextAreaUD();
        panelEste.add(crearPanelScroll("COLMENA ", txtColmena, rojo, new Dimension(220, 0)));
        add(panelEste, BorderLayout.EAST);

        // portales
        JPanel panelEje = new JPanel();
        panelEje.setLayout(new BoxLayout(panelEje, BoxLayout.Y_AXIS));
        panelEje.setOpaque(false);

        String[] nombresZonas = {"BOSQUE", "LABORATORIO", "CENTRO COMERCIAL", "ALCANTARILLADO"};
        int[] capacidades = {2, 3, 4, 2};

        for (int i = 0; i < 4; i++) {
            panelEje.add(crearFilaPortal(i, nombresZonas[i], capacidades[i]));
            if (i < 3) panelEje.add(Box.createRigidArea(new Dimension(0, 15))); // Espaciado fijo entre filas
        }

        JScrollPane scrollCentral = new JScrollPane(panelEje);
        scrollCentral.setOpaque(false);
        scrollCentral.getViewport().setOpaque(false);
        scrollCentral.setBorder(null);
        add(scrollCentral, BorderLayout.CENTER);

        Timer timer = new Timer(200, e -> refrescarDatos());
        timer.start();
    }

    private JPanel crearFilaPortal(int i, String nombre, int cap) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(bgPaneles);
        fila.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));


        Dimension dimSotano = new Dimension(160, 130);
        Dimension dimGrupo = new Dimension(160, 130);
        Dimension dimCruzando = new Dimension(160, 130);
        Dimension dimUDNiños = new Dimension(220, 130);
        Dimension dimUDDemos = new Dimension(160, 130);

        // colas sotano
        txtEsperaPortal[i] = crearTextArea();
        txtEsperaPortal[i].setForeground(amarillo);
        fila.add(Box.createRigidArea(new Dimension(5, 0)));
        fila.add(crearPanelScroll("SÓTANO: Cola ("+cap+")", txtEsperaPortal[i], amarillo, dimSotano));

        // grupos
        txtGrupoFormado[i] = crearTextArea();
        txtGrupoFormado[i].setForeground(naranja);
        fila.add(Box.createRigidArea(new Dimension(5, 0)));
        fila.add(crearPanelScroll("GRUPO FORMADO", txtGrupoFormado[i], naranja, dimGrupo));

        // dentro portal
        txtCruzando[i] = crearTextArea();
        txtCruzando[i].setForeground(azul);
        txtCruzando[i].setFont(new Font("Monospaced", Font.BOLD, 14));
        fila.add(Box.createRigidArea(new Dimension(5, 0)));
        fila.add(crearPanelScroll("ATRAVESANDO PORTAL", txtCruzando[i], azul, dimCruzando));

        // niños ud
        txtNiñosUD[i] = crearTextArea();
        txtNiñosUD[i].setForeground(magenta);
        fila.add(Box.createRigidArea(new Dimension(10, 0)));
        fila.add(crearPanelScroll(nombre + " (Niños)", txtNiñosUD[i], magenta, dimUDNiños));

        // dem ud
        txtDemosUD[i] = crearTextArea();
        txtDemosUD[i].setForeground(rojo);
        fila.add(Box.createRigidArea(new Dimension(5, 0)));
        fila.add(crearPanelScroll(nombre + " (Demogogorgons)", txtDemosUD[i], rojo, dimUDDemos));

        fila.add(Box.createRigidArea(new Dimension(5, 0)));

        return fila;
    }

    private void refrescarDatos() {
        lblSangre.setText("🩸 SANGRE: " + mapa.getContador_sangre());
        lblCapturas.setText("👤 CAPTURAS: " + mapa.getContador_capturas());
        String[] evs = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN", "RED MENTAL"};
        lblEventoActual.setText("EVENTO: " + evs[eventos.getEventoActual()]);

        txtCallePrincipal.setText(listarHilos(mapa.getZonaCallePrincipal()));
        txtRadioWsqk.setText(listarHilos(mapa.getZonaRadioWsqk()));
        txtColmena.setText(listarHilos(mapa.getZonaColmena()));

        Ciudad.Portal[] portals = {mapa.getPortalBosque(), mapa.getPortaLaboratorio(), mapa.getPortaCentroComercial(), mapa.getPortaAlcantarillado()};
        for (int i = 0; i < 4; i++) {
            Ciudad.Portal portalActual = portals[i];

            txtEsperaPortal[i].setText(listarHilos(portalActual.getColaEspera()));

            txtGrupoFormado[i].setText(listarHilos(portalActual.getGrupoFormado()));

            Niño cruzando = portalActual.getNiñoCruzando();
            if (cruzando != null) {
                txtCruzando[i].setText("[" + cruzando.getIdNiño() + "]");
            } else {
                txtCruzando[i].setText("---");
            }

            txtNiñosUD[i].setText(listarHilos(mapa.getListaUbicacionN(i + 3)));

            StringBuilder sbDemos = new StringBuilder();
            List<Demogorgon> demos = mapa.getListaUbicacionD(i + 3);
            synchronized (demos) {
                for (Demogorgon d : demos) {
                    sbDemos.append("[").append(d.getIdDemogorgon()).append("]\n");
                }
            }
            txtDemosUD[i].setText(sbDemos.toString());
        }
    }

    private String listarHilos(List<Niño> lista) {
        StringBuilder sb = new StringBuilder();
        synchronized (lista) {
            for (Niño n : lista) {
                sb.append("[").append(n.getIdNiño()).append("]");
                if (n.getLleva_sangre()) sb.append("🩸");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private JLabel crearLabelStatus(String t, Color c) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setForeground(c);
        l.setFont(new Font("SansSerif", Font.BOLD, 18));
        return l;
    }

    private JTextArea crearTextArea() {
        JTextArea a = new JTextArea();
        a.setBackground(bgTexto);
        a.setForeground(verde);
        a.setEditable(false);
        a.setFont(new Font("Monospaced", Font.PLAIN, 12));
        a.setMargin(new Insets(5, 5, 5, 5));
        return a;
    }
    private JTextArea crearTextAreaUD() {
        JTextArea a = new JTextArea();
        a.setBackground(bgTexto);
        a.setForeground(rojo);
        a.setEditable(false);
        a.setFont(new Font("Monospaced", Font.PLAIN, 12));
        a.setMargin(new Insets(5, 5, 5, 5));
        return a;
    }

    private JScrollPane crearPanelScroll(String tit, JTextArea a, Color borderColor, Dimension fixedSize) {
        JScrollPane s = new JScrollPane(a);
        s.setBackground(bgPaneles);
        s.getViewport().setBackground(bgPaneles);

        s.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                tit,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.ITALIC, 11),
                borderColor)
        );

        s.setPreferredSize(fixedSize);
        s.setMinimumSize(fixedSize);
        s.setMaximumSize(fixedSize);

        return s;
    }
}

/*package Concurrente;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class InterfazHawkins extends JFrame {
    private Ciudad mapa;
    private Eventos eventos;

    // Componentes de Status
    private JLabel lblSangre, lblCapturas, lblEventoActual;

    // Paneles Laterales
    private JTextArea txtCallePrincipal, txtRadioWsqk, txtColmena;

    // Filas Centrales (Portales y Zonas UD)
    // Orden: 0:Bosque, 1:Laboratorio, 2:Centro, 3:Alcantarillado
    private JTextArea[] txtEsperaPortal = new JTextArea[4];
    private JTextArea[] txtGrupoFormado = new JTextArea[4];
    private JTextArea[] txtCruzando = new JTextArea[4];
    private JTextArea[] txtNiñosUD = new JTextArea[4];
    private JTextArea[] txtDemosUD = new JTextArea[4];

    public InterfazHawkins(Ciudad mapa, Eventos eventos) {
        this.mapa = mapa;
        this.eventos = eventos;

        setTitle("Hawkins Monitoring System - Portal Alignment View");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(20, 20, 20));

        // --- NORTE: STATUS GLOBAL ---
        JPanel panelStatus = new JPanel(new GridLayout(1, 3));
        panelStatus.setBackground(new Color(40, 40, 40));
        lblSangre = crearLabelStatus("🩸 SANGRE: 0", Color.RED);
        lblCapturas = crearLabelStatus("👤 CAPTURAS: 0", Color.ORANGE);
        lblEventoActual = crearLabelStatus("EVENTO: NORMALIDAD", Color.CYAN);
        panelStatus.add(lblSangre);
        panelStatus.add(lblCapturas);
        panelStatus.add(lblEventoActual);
        add(panelStatus, BorderLayout.NORTH);

        // --- OESTE: HAWKINS LATERAL ---
        JPanel panelOeste = new JPanel(new GridLayout(2, 1, 10, 10));
        panelOeste.setOpaque(false);
        panelOeste.setPreferredSize(new Dimension(250, 0));
        txtCallePrincipal = crearTextArea(new Color(30, 50, 30));
        txtRadioWsqk = crearTextArea(new Color(30, 50, 30));
        panelOeste.add(crearPanelScroll("CALLE PRINCIPAL", txtCallePrincipal, Color.GREEN));
        panelOeste.add(crearPanelScroll("RADIO WSQK (DESCANSO)", txtRadioWsqk, Color.GREEN));
        add(panelOeste, BorderLayout.WEST);

        // --- ESTE: COLMENA LATERAL ---
        JPanel panelEste = new JPanel(new BorderLayout());
        panelEste.setOpaque(false);
        panelEste.setPreferredSize(new Dimension(250, 0));
        txtColmena = crearTextArea(new Color(50, 30, 30));
        panelEste.add(crearPanelScroll("COLMENA (VÍCTIMAS)", txtColmena, Color.RED), BorderLayout.CENTER);
        add(panelEste, BorderLayout.EAST);

        // --- CENTRO: EL EJE DE PORTALES ---
        JPanel panelEje = new JPanel(new GridLayout(4, 1, 0, 15));
        panelEje.setOpaque(false);

        String[] nombresZonas = {"BOSQUE", "LABORATORIO", "CENTRO COMERCIAL", "ALCANTARILLADO"};
        int[] capacidades = {2, 3, 4, 2}; // Según enunciado

        for (int i = 0; i < 4; i++) {
            panelEje.add(crearFilaPortal(i, nombresZonas[i], capacidades[i]));
        }
        add(new JScrollPane(panelEje), BorderLayout.CENTER);

        Timer timer = new Timer(200, e -> refrescarDatos());
        timer.start();
    }

    private JPanel crearFilaPortal(int i, String nombre, int cap) {
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setBackground(new Color(45, 45, 45));
        fila.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 1. Sótano: Espera
        txtEsperaPortal[i] = crearTextArea(new Color(40, 40, 40));
        gbc.weightx = 0.2;
        fila.add(crearPanelScroll("SÓTANO: ESPERA " + nombre + " ("+cap+")", txtEsperaPortal[i], Color.YELLOW), gbc);

        // 2. Grupo Formado
        txtGrupoFormado[i] = crearTextArea(new Color(20, 20, 20));
        txtGrupoFormado[i].setForeground(Color.YELLOW);
        gbc.weightx = 0.15;
        fila.add(crearPanelScroll("GRUPO LISTO", txtGrupoFormado[i], Color.YELLOW), gbc);

        // 3. Cruzando (Espacio dedicado)
        txtCruzando[i] = crearTextArea(Color.BLACK);
        txtCruzando[i].setForeground(Color.CYAN);
        txtCruzando[i].setFont(new Font("Monospaced", Font.BOLD, 14));
        gbc.weightx = 0.15;
        fila.add(crearPanelScroll("⚡ CRUZANDO", txtCruzando[i], Color.CYAN), gbc);

        // 4. Upside Down (Niños)
        txtNiñosUD[i] = crearTextArea(new Color(20, 20, 40));
        gbc.weightx = 0.25;
        fila.add(crearPanelScroll(nombre + " (NIÑOS)", txtNiñosUD[i], Color.WHITE), gbc);

        // 5. Upside Down (Demos)
        txtDemosUD[i] = crearTextArea(new Color(40, 20, 20));
        gbc.weightx = 0.25;
        fila.add(crearPanelScroll(nombre + " (DEMOS)", txtDemosUD[i], Color.RED), gbc);

        return fila;
    }

    private void refrescarDatos() {
        // Status e hilos laterales
        lblSangre.setText("🩸 SANGRE: " + mapa.getContador_sangre());
        lblCapturas.setText("👤 CAPTURAS: " + mapa.getContador_capturas());
        String[] evs = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN", "RED MENTAL"};
        lblEventoActual.setText("EVENTO: " + evs[eventos.getEventoActual()]);

        txtCallePrincipal.setText(listarHilos(mapa.getZonaCallePrincipal()));
        txtRadioWsqk.setText(listarHilos(mapa.getZonaRadioWsqk()));
        txtColmena.setText(listarHilos(mapa.getZonaColmena()));

        // Refrescar Filas de Portales
        Ciudad.Portal[] portals = {mapa.getPortalBosque(), mapa.getPortaLaboratorio(), mapa.getPortaCentroComercial(), mapa.getPortaAlcantarillado()};

        for (int i = 0; i < 4; i++) {
            // Lógica de visualización basada en el estado del objeto Portal
            int esperando = portals[i].getNiños_Portal();
            txtEsperaPortal[i].setText("Niños en cola: " + esperando);

            // Si hay suficientes para un grupo pero aún no han cruzado todos
            // Nota: Usamos la lógica de restantesGrupo y portalOcupado de Ciudad.java [cite: 139]
            // Como la interfaz no tiene acceso directo a los IDs dentro del lock,
            // mostramos el estado de ocupación.

            txtGrupoFormado[i].setText(esperando >= (i==0||i==3?2:i==1?3:4) ? "¡GRUPO CERRADO!" : "Formando...");

            // El niño cruzando se identifica por estar en la transición (aquí simulamos la vista
            // ya que el hilo duerme 1s en cruzar) [cite: 60]
            txtCruzando[i].setText(esperando > 0 ? "TRANSFIRIENDO..." : "--- VACÍO ---");

            // Zonas UD
            txtNiñosUD[i].setText(listarHilos(mapa.getListaUbicacionN(i + 3)));

            StringBuilder sbD = new StringBuilder();
            List<Demogorgon> ds = mapa.getListaUbicacionD(i + 3);
            synchronized (ds) { for (Demogorgon d : ds) sbD.append(d.getIdDemogorgon()).append("\n"); }
            txtDemosUD[i].setText(sbD.toString());
        }
    }

    private String listarHilos(List<Niño> lista) {
        StringBuilder sb = new StringBuilder();
        synchronized (lista) {
            for (Niño n : lista) {
                sb.append(n.getIdNiño()).append(n.getLleva_sangre() ? "🩸" : "").append(" ");
                if (sb.length() % 30 == 0) sb.append("\n");
            }
        }
        return sb.toString();
    }

    private JLabel crearLabelStatus(String t, Color c) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setForeground(c);
        l.setFont(new Font("Arial", Font.BOLD, 18));
        return l;
    }

    private JTextArea crearTextArea(Color bg) {
        JTextArea a = new JTextArea();
        a.setBackground(bg);
        a.setForeground(Color.WHITE);
        a.setEditable(false);
        a.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return a;
    }

    private JScrollPane crearPanelScroll(String tit, JTextArea a, Color c) {
        JScrollPane s = new JScrollPane(a);
        s.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(c), tit, TitledBorder.LEFT, TitledBorder.TOP, null, c));
        return s;
    }
}
/*
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;

public class InterfazHawkins extends JFrame {
    private Ciudad mapa;
    private Eventos eventos;

    // Componentes de Status Global
    private JLabel lblSangre, lblCapturas, lblEventoActual;
    private JTextArea logArea;

    // Componentes de Zonas Hawkins
    private JTextArea txtCallePrincipal, txtSotanoByers, txtRadioWsqk;
    private JLabel lblPortalBosque, lblPortalLab, lblPortalCentro, lblPortalAlcant;

    // Componentes de Zonas Upside Down (Separados)
    private JTextArea[] txtNiñosUD = new JTextArea[5];
    private JTextArea[] txtDemosUD = new JTextArea[5];

    public InterfazHawkins(Ciudad mapa, Eventos eventos) {
        this.mapa = mapa;
        this.eventos = eventos;

        setTitle("Hawkins Monitoring System - The Upside Down");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 30));

        // --- PANEL SUPERIOR: ESTADO GLOBAL ---
        JPanel panelStatus = new JPanel(new GridLayout(1, 3));
        panelStatus.setBackground(new Color(45, 45, 45));
        panelStatus.setBorder(BorderFactory.createTitledBorder(null, "ESTADO GLOBAL", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.GREEN));

        lblSangre = crearLabelStatus("Sangre disponible: 0", Color.RED);
        lblCapturas = crearLabelStatus("Niños capturados: 0", Color.ORANGE);
        lblEventoActual = crearLabelStatus("Evento: NORMALIDAD", Color.CYAN);

        panelStatus.add(lblSangre);
        panelStatus.add(lblCapturas);
        panelStatus.add(lblEventoActual);
        add(panelStatus, BorderLayout.NORTH);

        // --- PANEL CENTRAL: SPLIT HAWKINS VS UPSIDE DOWN ---
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCentral.setOpaque(false);

        // 1. ZONA HAWKINS (Izquierda)
        JPanel panelHawkins = new JPanel(new GridLayout(3, 1, 5, 5));
        panelHawkins.setOpaque(false);
        panelHawkins.setBorder(BorderFactory.createTitledBorder(null, "HAWKINS (ZONA SEGURA)", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.GREEN));

        txtCallePrincipal = crearTextArea(new Color(230, 255, 230));
        panelHawkins.add(crearPanelZona("Calle Principal", txtCallePrincipal));

        // SÓTANO BYERS CON ESTADO DE PORTALES
        JPanel panelSotano = new JPanel(new BorderLayout());
        panelSotano.setOpaque(false);
        txtSotanoByers = crearTextArea(new Color(230, 255, 230));
        panelSotano.add(crearPanelZona("Sótano Byers (Niños Preparándose)", txtSotanoByers), BorderLayout.CENTER);

        JPanel panelPortales = new JPanel(new GridLayout(2, 2, 5, 5));
        panelPortales.setBackground(new Color(60, 60, 60));
        panelPortales.setBorder(BorderFactory.createTitledBorder(null, "ESTADO DE LOS PORTALES (FORMANDO GRUPOS)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.YELLOW));
        lblPortalBosque = crearLabelStatus("Bosque: 0/2", Color.WHITE);
        lblPortalLab = crearLabelStatus("Laboratorio: 0/3", Color.WHITE);
        lblPortalCentro = crearLabelStatus("Centro Comercial: 0/4", Color.WHITE);
        lblPortalAlcant = crearLabelStatus("Alcantarillado: 0/2", Color.WHITE);
        panelPortales.add(lblPortalBosque);
        panelPortales.add(lblPortalLab);
        panelPortales.add(lblPortalCentro);
        panelPortales.add(lblPortalAlcant);
        panelSotano.add(panelPortales, BorderLayout.SOUTH);

        panelHawkins.add(panelSotano);

        txtRadioWsqk = crearTextArea(new Color(230, 255, 230));
        panelHawkins.add(crearPanelZona("Radio WSQK", txtRadioWsqk));

        panelCentral.add(panelHawkins);

        // 2. ZONA UPSIDE DOWN (Derecha)
        JPanel panelUpsideDown = new JPanel(new GridLayout(5, 1, 5, 5));
        panelUpsideDown.setOpaque(false);
        panelUpsideDown.setBorder(BorderFactory.createTitledBorder(null, "THE UPSIDE DOWN (ZONAS INSEGURAS)", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.RED));

        String[] nombresUD = {"Bosque", "Laboratorio", "Centro Comercial", "Alcantarillado", "Colmena (HIVE)"};
        for (int i = 0; i < 5; i++) {
            JPanel panelZonaUD = new JPanel(new GridLayout(1, 2, 5, 0));
            panelZonaUD.setBorder(BorderFactory.createTitledBorder(null, nombresUD[i], TitledBorder.LEFT, TitledBorder.TOP, null, Color.LIGHT_GRAY));
            panelZonaUD.setOpaque(false);

            txtNiñosUD[i] = crearTextArea(new Color(200, 230, 255)); // Azul claro para niños
            txtDemosUD[i] = crearTextArea(new Color(255, 200, 200)); // Rojo claro para demogorgons

            panelZonaUD.add(crearPanelSubZona("Niños (Objetivos)", txtNiñosUD[i]));
            panelZonaUD.add(crearPanelSubZona("Demogorgons (Cazando)", txtDemosUD[i]));

            panelUpsideDown.add(panelZonaUD);
        }

        panelCentral.add(panelUpsideDown);
        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR: LOG ---
        logArea = crearTextArea(Color.BLACK);
        logArea.setForeground(Color.GREEN);
        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setPreferredSize(new Dimension(1400, 150));
        scrollLog.setBorder(BorderFactory.createTitledBorder(null, "LOG DE EVENTOS", TitledBorder.LEFT, TitledBorder.TOP, null, Color.GREEN));
        add(scrollLog, BorderLayout.SOUTH);

        // Timer de refresco (200ms)
        Timer timer = new Timer(200, e -> refrescarDatos());
        timer.start();
    }

    private void refrescarDatos() {
        // 1. Refrescar Contadores Globales
        lblSangre.setText("Sangre disponible: " + mapa.getContador_sangre());
        lblCapturas.setText("Niños capturados: " + mapa.getContador_capturas());
        String[] nombresEventos = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN INTERVIENE", "RED MENTAL"};
        lblEventoActual.setText("Evento: " + nombresEventos[eventos.getEventoActual()]);
        lblEventoActual.setForeground(eventos.getEventoActual() == 0 ? Color.CYAN : Color.RED);

        // 2. Refrescar Estado de los Portales (Sótano Byers)
        lblPortalBosque.setText(formatPortalText("Portal Bosque", mapa.getPortalBosque().getNiños_Portal(), 2));
        lblPortalLab.setText(formatPortalText("Portal Laboratorio", mapa.getPortaLaboratorio().getNiños_Portal(), 3));
        lblPortalCentro.setText(formatPortalText("Portal Centro Com.", mapa.getPortaCentroComercial().getNiños_Portal(), 4));
        lblPortalAlcant.setText(formatPortalText("Portal Alcant.", mapa.getPortaAlcantarillado().getNiños_Portal(), 2));

        // 3. Refrescar Zonas de Hawkins
        txtCallePrincipal.setText(obtenerListaNiños(0));
        txtSotanoByers.setText(obtenerListaNiños(1));
        txtRadioWsqk.setText(obtenerListaNiños(2));

        // 4. Refrescar Zonas del Upside Down (Separando Niños y Demogorgons)
        for (int i = 0; i < 5; i++) {
            int indiceMapa = i + 3; // En Ciudad.java el Bosque empieza en el índice 3

            // Listar Niños en Upside Down
            txtNiñosUD[i].setText(obtenerListaNiños(indiceMapa));

            // Listar Demogorgons en Upside Down
            StringBuilder sbDemos = new StringBuilder();
            List<Demogorgon> demos = mapa.getListaUbicacionD(indiceMapa);
            synchronized (demos) {
                for (Demogorgon d : demos) {
                    sbDemos.append(" 👾 ").append(d.getIdDemogorgon()).append(d.getLleva_niño() ? " [Capturando]" : "").append("\n");
                }
            }
            txtDemosUD[i].setText(sbDemos.toString());
        }
    }

    // --- MÉTODOS AUXILIARES UI ---

    private String formatPortalText(String nombre, int esperando, int capacidad) {
        if (esperando >= capacidad) {
            return nombre + ": " + esperando + "/" + capacidad + " [CRUZANDO... ⏳]";
        }
        return nombre + ": " + esperando + "/" + capacidad + " [Esperando Grupo]";
    }

    private String obtenerListaNiños(int zonaIndex) {
        StringBuilder sb = new StringBuilder();
        List<Niño> niños = mapa.getListaUbicacionN(zonaIndex);
        synchronized (niños) {
            for (Niño n : niños) {
                sb.append(" [").append(n.getIdNiño());
                if (n.getLleva_sangre()) sb.append("🩸");
                if (n.getCapturado() && zonaIndex == 7) sb.append(" 💀");
                if (n.getEsta_atacado()) sb.append(" ⚔️");
                sb.append("]");
            }
        }
        return sb.toString();
    }

    private JLabel crearLabelStatus(String texto, Color color) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTextArea crearTextArea(Color bg) {
        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setBackground(bg);
        txt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return txt;
    }

    private JPanel crearPanelZona(String titulo, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelSubZona(String titulo, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createTitledBorder(null, titulo, TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.ITALIC, 11)));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}
*/

/*
public class InterfazHawkins extends JFrame {
    private Ciudad mapa;
    private Eventos eventos;

    // Componentes de la UI
    private JTextArea[] txtZonas = new JTextArea[8];
    private JLabel lblSangre, lblCapturas, lblEventoActual;

    public InterfazHawkins(Ciudad mapa, Eventos eventos) {
        this.mapa = mapa;
        this.eventos = eventos;

        setTitle("Mapa de Hawkins");
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
                "Laboratorio", "Centro Comercial", "Alcantarillado", "Colmena"
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
*/


