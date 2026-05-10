package Concurrente;

import javax.swing.*;
import javax.swing.border.Border;
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

    // NUEVO: campo visual de preparacion en el sótano
    private JTextArea txtSotanoByersPreparacion;

    // filas cola portal ud
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

        // portales (centro)
        JPanel panelEje = new JPanel();
        panelEje.setLayout(new BoxLayout(panelEje, BoxLayout.X_AXIS));
        panelEje.setOpaque(false);

        JPanel panelSotano = new JPanel(new BorderLayout());
        panelSotano.setOpaque(false);

        Border borderSotano = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(amarillo, 1),
                "Sótano Byers",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                amarillo
        );
        panelSotano.setBorder(borderSotano);


        txtSotanoByersPreparacion = crearTextArea();
        txtSotanoByersPreparacion.setForeground(amarillo);
        JScrollPane panelPrep = crearPanelScroll("PREPARACIÓN", txtSotanoByersPreparacion, amarillo, new Dimension(330, 80));

        JPanel wrapperPrep = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        wrapperPrep.setOpaque(false);
        wrapperPrep.add(panelPrep);
        panelSotano.add(wrapperPrep, BorderLayout.NORTH);

        JPanel panelSotanoFilas = new JPanel();
        panelSotanoFilas.setLayout(new BoxLayout(panelSotanoFilas, BoxLayout.Y_AXIS));
        panelSotanoFilas.setOpaque(false);


        JPanel panelResto = new JPanel(new BorderLayout());
        panelResto.setOpaque(false);

        // alineacion
        Insets insetsSotano = borderSotano.getBorderInsets(panelSotano);
        panelResto.setBorder(BorderFactory.createEmptyBorder(insetsSotano.top, 5, insetsSotano.bottom, 5));

        JPanel dummyPrep = new JPanel();
        dummyPrep.setPreferredSize(new Dimension(560, 80)); // mismo alto que PREPARACIÓN
        dummyPrep.setOpaque(false);

        JPanel wrapperSpacer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        wrapperSpacer.setOpaque(false);
        wrapperSpacer.add(dummyPrep);
        panelResto.add(wrapperSpacer, BorderLayout.NORTH);

        JPanel panelRestoFilas = new JPanel();
        panelRestoFilas.setLayout(new BoxLayout(panelRestoFilas, BoxLayout.Y_AXIS));
        panelRestoFilas.setOpaque(false);

        String[] nombresZonas = {"BOSQUE", "LABORATORIO", "CENTRO COMERCIAL", "ALCANTARILLADO"};
        int[] capacidades = {2, 3, 4, 2};

        Dimension dimCola = new Dimension(160, 130);
        Dimension dimGrupo = new Dimension(160, 130);
        Dimension dimCruzando = new Dimension(160, 130);
        Dimension dimUDNiños = new Dimension(220, 130);
        Dimension dimUDDemos = new Dimension(160, 130);

        for (int i = 0; i < 4; i++) {
            // 1. Lado Izquierdo (Dentro del Sótano Byers)
            JPanel filaS = new JPanel();
            filaS.setLayout(new BoxLayout(filaS, BoxLayout.X_AXIS));
            filaS.setOpaque(false);
            filaS.setAlignmentX(Component.LEFT_ALIGNMENT);

            txtEsperaPortal[i] = crearTextArea();
            txtEsperaPortal[i].setForeground(amarillo);
            filaS.add(crearPanelScroll("Cola (" + capacidades[i] + ")", txtEsperaPortal[i], amarillo, dimCola));
            filaS.add(Box.createRigidArea(new Dimension(10, 0)));

            txtGrupoFormado[i] = crearTextArea();
            txtGrupoFormado[i].setForeground(naranja);
            filaS.add(crearPanelScroll("GRUPO FORMADO", txtGrupoFormado[i], naranja, dimGrupo));

            panelSotanoFilas.add(filaS);

            // 2. Lado Derecho (Cruzando el Portal y UD)
            JPanel filaR = new JPanel();
            filaR.setLayout(new BoxLayout(filaR, BoxLayout.X_AXIS));
            filaR.setOpaque(false);
            filaR.setAlignmentX(Component.LEFT_ALIGNMENT);

            txtCruzando[i] = crearTextArea();
            txtCruzando[i].setForeground(azul);
            txtCruzando[i].setFont(new Font("Monospaced", Font.BOLD, 14));
            filaR.add(crearPanelScroll("ATRAVESANDO PORTAL", txtCruzando[i], azul, dimCruzando));
            filaR.add(Box.createRigidArea(new Dimension(10, 0)));

            txtNiñosUD[i] = crearTextArea();
            txtNiñosUD[i].setForeground(magenta);
            filaR.add(crearPanelScroll(nombresZonas[i] + " (Niños)", txtNiñosUD[i], magenta, dimUDNiños));
            filaR.add(Box.createRigidArea(new Dimension(10, 0)));

            txtDemosUD[i] = crearTextArea();
            txtDemosUD[i].setForeground(rojo);
            filaR.add(crearPanelScroll(nombresZonas[i] + " (Demos)", txtDemosUD[i], rojo, dimUDDemos));

            panelRestoFilas.add(filaR);

            // Espaciado vertical entre filas
            if (i < 3) {
                panelSotanoFilas.add(Box.createRigidArea(new Dimension(0, 15)));
                panelRestoFilas.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        panelSotano.add(panelSotanoFilas, BorderLayout.CENTER);
        panelResto.add(panelRestoFilas, BorderLayout.CENTER);

        // Ensamblar todo en el Eje Central
        panelEje.add(Box.createHorizontalGlue());
        panelEje.add(panelSotano);
        panelEje.add(Box.createRigidArea(new Dimension(15, 0))); // Separación Sótano - Exterior
        panelEje.add(panelResto);
        panelEje.add(Box.createHorizontalGlue());

        JScrollPane scrollCentral = new JScrollPane(panelEje);
        scrollCentral.setOpaque(false);
        scrollCentral.getViewport().setOpaque(false);
        scrollCentral.setBorder(null);
        add(scrollCentral, BorderLayout.CENTER);

        Timer timer = new Timer(200, e -> refrescarDatos());
        timer.start();
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
                    sbDemos.append("[").append(d.getIdDemogorgon()).append("] ");
                }
            }
            txtDemosUD[i].setText(sbDemos.toString());
        }


         txtSotanoByersPreparacion.setText(listarHilos(mapa.getSotano_byersPreparacion()));
    }

    private String listarHilos(List<Niño> lista) {
        StringBuilder sb = new StringBuilder();
        synchronized (lista) {
            for (Niño n : lista) {
                sb.append("[").append(n.getIdNiño()).append("]");
                if (n.getLleva_sangre()) sb.append("🩸");
                sb.append(" ");
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
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        return a;

    }

    private JTextArea crearTextAreaUD() {
        JTextArea a = new JTextArea();
        a.setBackground(bgTexto);
        a.setForeground(rojo);
        a.setEditable(false);
        a.setFont(new Font("Monospaced", Font.PLAIN, 12));
        a.setMargin(new Insets(5, 5, 5, 5));
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
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
