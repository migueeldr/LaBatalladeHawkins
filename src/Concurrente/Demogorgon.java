package Concurrente;

import java.util.concurrent.Semaphore;

public class Demogorgon extends Thread{
    private Niño niño;
    private String id;
    private boolean lleva_niño;
    private int ubicacion;
    private int capturas;
    private final Ciudad mapa;
    private final LogHawkins log;
    private Eventos eventos;
    private static  int creador_Demogorgon = 1;
    private Semaphore semaforo_crearDemogorgon=new Semaphore(1);
    private final int bosque=3;
    private final int laboratorio=4;
    private final int centro_comercial=5;
    private final int alcantarillado=6;
    private final int colmena=7;

    public Demogorgon(int n_Id, Ciudad mapa, LogHawkins log, Eventos eventos) {
        this.id = String.format("D%04d", n_Id);
        this.mapa=mapa;
        this.log=log;
        this.eventos=eventos;
        this.lleva_niño=false;
        this.ubicacion=7;
        this.capturas=0;

        mapa.registrarHilo(this);
    }
    public void crearDemogorgon(){
        try {
            semaforo_crearDemogorgon.acquire();
            Demogorgon d = new Demogorgon(creador_Demogorgon, mapa, log, eventos);
            creador_Demogorgon++;
            semaforo_crearDemogorgon.release();
            d.start();
        }
        catch (Exception e){}
    }
    public String getIdDemogorgon(){
        return id;
    }
    public synchronized int getUbicacion(){
        return ubicacion;
    }
    public synchronized int getCapturas(){
        return capturas;
    }
    public synchronized boolean getLleva_niño(){
        return lleva_niño;
    }
    public synchronized void setLleva_niño(boolean lleva_niño){
        this.lleva_niño=lleva_niño;
    }
    public synchronized void setUbicacion(int ubicacion){
        this.ubicacion=ubicacion;
    }
    public synchronized void incrementarCapturas(){
        capturas++;
    }
    public int elegirZona(){
        int zona_elegida= (int) (Math.random() * 4+3);
        return zona_elegida;

    }


    public void run(){
        mapa.comprobarPausa();
        log.escribirEvento("El demogorgon " + id + " ha nacido.");
        mapa.getDem_Todos().add(this);
        while(true){
            mapa.esperarSiElevenEstaActiva();
            mapa.comprobarPausa();
            if (eventos.getEventoActual() != 1) {
                int zona=elegirZona();
                if (eventos.getEventoActual() == 4) {
                    zona= mapa.zona_niños();
                }
                mapa.comprobarPausa();
                mapa.moverDemogorgon(this,mapa.getListaUbicacionD(getUbicacion()), mapa.getListaUbicacionD(zona));
            }

            mapa.esperarSiElevenEstaActiva();
            mapa.comprobarPausa();
            Niño victima=mapa.obtener_niño(ubicacion);
            if(victima!=null){
                try{
                    long tiempo_ataque= (long) (Math.random() * 1000+500);
                    sleep(tiempo_ataque);
                    mapa.comprobarPausa();
                    if (mapa.ataque_niño(this, victima)){
                        log.escribirEvento("El demogorgon " + id + " ha capturado a " + victima.getIdNiño());
                        incrementarCapturas();
                    }


                }
                catch(Exception e){mapa.comprobarPausa();}
            }
            else{
                try{
                    long espera=(long) (Math.random() * 1000 + 4000);
                    if (eventos.getEventoActual() == 2) {
                        espera = espera / 2;
                    }
                    sleep(espera);
                }
                catch(Exception e){mapa.comprobarPausa();}
            }



        }
    }
}
