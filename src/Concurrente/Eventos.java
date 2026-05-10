package Concurrente;

public class Eventos extends Thread {
    private Ciudad mapa;
    private int eventoactual;
    private final LogHawkins log;
    private static final int normalidad=0;
    private static final int apagon=1;
    private static final int tormenta=2;
    private static final int eleven=3;
    private static final int red=4;
    public Eventos(Ciudad mapa, LogHawkins log){
        this.mapa=mapa;
        eventoactual=0;
        this.log=log;

        mapa.registrarHilo(this);

    }
    public int getEventoActual(){
        return eventoactual;
    }
    public void setEventoActual(int eventoactual){
        this.eventoactual=eventoactual;
    }

    public void run(){
        mapa.comprobarPausa();
        while(true){
            try{
                long pausa = 30000 + (long)(Math.random() * 30000);
                mapa.comprobarPausa();
                sleep(pausa);
                mapa.comprobarPausa();
                int evento_elegido= (int) (Math.random() * 4 +1);
                mapa.comprobarPausa();
                if (evento_elegido==1){
                    mapa.comprobarPausa();
                    setEventoActual(apagon);

                    mapa.getPortaAlcantarillado().setApagon_activo(true);
                    mapa.getPortalBosque().setApagon_activo(true);
                    mapa.getPortaCentroComercial().setApagon_activo(true);
                    mapa.getPortaLaboratorio().setApagon_activo(true);

                    mapa.comprobarPausa();
                    log.escribirEvento("El evento de APAGÓN DEL LABORATORIO ha comenzado.");
                    mapa.comprobarPausa();
                    long duracion= (long) (Math.random() * 5000+5000);
                    mapa.comprobarPausa();
                    sleep(duracion);
                    mapa.comprobarPausa();

                    mapa.getPortaAlcantarillado().setApagon_activo(false);
                    mapa.getPortalBosque().setApagon_activo(false);
                    mapa.getPortaCentroComercial().setApagon_activo(false);
                    mapa.getPortaLaboratorio().setApagon_activo(false);

                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de APAGÓN DEL LABORATORIO ha terminado.");
                    mapa.comprobarPausa();
                }
                else if (evento_elegido==2){
                    mapa.comprobarPausa();
                    setEventoActual(tormenta);

                    log.escribirEvento("El evento de TORMENTA DEL UPSIDE DOWN ha comenzado.");
                    mapa.comprobarPausa();
                    long duracion= (long) (Math.random() * 5000+5000);
                    mapa.comprobarPausa();
                    sleep(duracion);
                    mapa.comprobarPausa();
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de TORMENTA DEL UPSIDE DOWN ha terminado.");
                    mapa.comprobarPausa();
                }
                else if (evento_elegido==3){
                    mapa.comprobarPausa();
                    setEventoActual(eleven);
                    log.escribirEvento("El evento de INTERVENCION DE ELEVEN ha comenzado.");
                    mapa.comprobarPausa();
                    mapa.setElevenActiva(true);
                    mapa.comprobarPausa();
                    mapa.incrementar_contador_sangre();
                    mapa.comprobarPausa();
                    mapa.esperar_rescate();
                    mapa.comprobarPausa();
                    long duracion= (long) (Math.random() * 5000+5000);
                    mapa.comprobarPausa();
                    sleep(duracion);
                    mapa.comprobarPausa();
                    mapa.setElevenActiva(false);
                    mapa.comprobarPausa();
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de INTERVENCION DE ELEVEN ha terminado.");
                    mapa.comprobarPausa();
                }
                else if (evento_elegido==4){
                    mapa.comprobarPausa();
                    setEventoActual(red);
                    log.escribirEvento("El evento de LA RED MENTAL ha comenzado.");
                    mapa.comprobarPausa();
                    long duracion= (long) (Math.random() * 5000+5000);
                    mapa.comprobarPausa();
                    sleep(duracion);
                    mapa.comprobarPausa();
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de LA RED MENTAL ha terminado.");
                    mapa.comprobarPausa();
                }

            }
            catch(Exception e){mapa.comprobarPausa();}
        }
    }
}
