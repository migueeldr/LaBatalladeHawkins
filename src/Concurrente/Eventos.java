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
                sleep(pausa);
                int evento_elegido= (int) (Math.random() * 4 +1);
                if (evento_elegido==1){
                    setEventoActual(apagon);
                    log.escribirEvento("El evento de APAGÓN DEL LABORATORIO ha comenzado.");
                    long duracion= (long) (Math.random() * 5000+5000);
                    sleep(duracion);
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de APAGÓN DEL LABORATORIO ha terminado.");
                }
                else if (evento_elegido==2){
                    setEventoActual(tormenta);

                    log.escribirEvento("El evento de TORMENTA DEL UPSIDE DOWN ha comenzado.");
                    long duracion= (long) (Math.random() * 5000+5000);
                    sleep(duracion);
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de TORMENTA DEL UPSIDE DOWN ha terminado.");
                }
                else if (evento_elegido==3){
                    setEventoActual(eleven);
                    log.escribirEvento("El evento de INTERVENCION DE ELEVEN ha comenzado.");
                    mapa.setElevenActiva(true);
                    mapa.incrementar_contador_sangre();
                    mapa.esperar_rescate();
                    long duracion= (long) (Math.random() * 5000+5000);
                    sleep(duracion);
                    mapa.setElevenActiva(false);
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de INTERVENCION DE ELEVEN ha terminado.");
                }
                else if (evento_elegido==4){
                    setEventoActual(red);
                    log.escribirEvento("El evento de LA RED MENTAL ha comenzado.");
                    long duracion= (long) (Math.random() * 5000+5000);
                    sleep(duracion);
                    setEventoActual(normalidad);
                    log.escribirEvento("El evento de LA RED MENTAL ha terminado.");
                }

            }
            catch(Exception e){mapa.comprobarPausa();}
        }
    }
}
