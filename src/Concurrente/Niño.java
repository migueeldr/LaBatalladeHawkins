package Concurrente;

import java.util.concurrent.Semaphore;

public class Niño extends Thread{
    private String id;
    private boolean lleva_sangre;
    private int ubicacion;
    private boolean esta_atacado;
    private boolean capturado;
    private final Ciudad mapa;
    private final LogHawkins log;
    private Eventos eventos;

    private final Semaphore semaforo_ataque=new Semaphore(0);

    //zonas seguras
    private final int calle_principal=0;
    private final int sotano_byers=1;
    private final int radio_wsqk=2;
    //zonas inseguras
    private final int bosque=3;
    private final int laboratorio=4;
    private final int centro_comercial=5;
    private final int alcantarillado=6;
    private final int colmena=7;




    public Niño(int n_id, Ciudad mapa, LogHawkins log, Eventos eventos) {
        this.id = String.format("N%04d", n_id);
        this.mapa = mapa;
        this.log = log;
        this.eventos=eventos;
        this.lleva_sangre = false;
        this.ubicacion = 0;
        this.mapa.addNiñoCallePrincipal(this);
        esta_atacado=false;
        capturado=false;

        mapa.registrarHilo(this);

    }
    public String getIdNiño(){
        return id;
    }
    public synchronized boolean getCapturado(){
        return capturado;
    }
    public synchronized void setCapturado(boolean capturado){
        this.capturado=capturado;
    }
    public synchronized boolean getEsta_atacado(){
        return esta_atacado;
    }
    public synchronized void setEsta_atacado(boolean esta_atacado){
        this.esta_atacado = esta_atacado;
    }
    //usaremos synchronized para que no nos devuelva datos incorrectos en los atributos que pueden cambiar
    public synchronized boolean getLleva_sangre(){
        return lleva_sangre;
    }
    public synchronized void setLleva_sangre(boolean lleva_sangre){
        this.lleva_sangre = lleva_sangre;
    }
    public synchronized int getUbicacion(){
        return ubicacion;
    }
    public synchronized void setUbicacion(int ubicacion){
        this.ubicacion = ubicacion;    }

    public Ciudad.Portal elegirPortal(){
        int portal_elegido= (int) (Math.random() * 4+3);
        if(portal_elegido==3){return mapa.getPortalBosque();}
        if(portal_elegido==4){return mapa.getPortaLaboratorio();}
        if(portal_elegido==5){return mapa.getPortaCentroComercial();}
        else {return mapa.getPortaAlcantarillado();}
    }
    public void liberarDeAtaque(){
        semaforo_ataque.release();
    }
    public Semaphore getSemaphore_ataque(){
        return semaforo_ataque;
    }

    public void run(){
        mapa.comprobarPausa();
        log.escribirEvento("El niño " + id + " ha nacido.");
        while(true){
            mapa.comprobarPausa();
            mapa.moverNiño(this, mapa.getZonaCallePrincipal(), mapa.getZonaSotanoByers());
            mapa.comprobarPausa();

            try{
                mapa.getSotano_byersPreparacion().add(this);
                long aleatorio= (long) (Math.random() * 1000+1000);
                sleep(aleatorio);
                mapa.getSotano_byersPreparacion().remove(this);
                mapa.comprobarPausa();
            }
            catch(Exception e){
                mapa.comprobarPausa();
            }
            Ciudad.Portal portal_elegido=elegirPortal();

            try {
                mapa.comprobarPausa();
                portal_elegido.cruzarHabitual(this);
                log.escribirEvento("El niño " + id + " ha cruzado el portal hacia " + this.getUbicacion());
                mapa.comprobarPausa();
            } catch (InterruptedException e) {
                mapa.comprobarPausa();
            }
            try{
                long aleatorio= (long) (Math.random() * 2000+3000);
                mapa.comprobarPausa();
                if (eventos.getEventoActual() == 2) {
                    aleatorio= aleatorio * 2;
                    mapa.comprobarPausa();
                }
                mapa.comprobarPausa();
                sleep(aleatorio);
                mapa.comprobarPausa();
            }
            catch(Exception e){
            {try{semaforo_ataque.acquire();}
                catch(Exception e2){
                    mapa.comprobarPausa();
                }}

            }
            mapa.comprobarPausa();
            if (getCapturado()){
                mapa.comprobarPausa();
                log.escribirEvento("El niño " + id + " ha sido capturado.");
                mapa.comprobarPausa();
                mapa.esperar_rescate();
                mapa.comprobarPausa();
                mapa.moverNiño(this, mapa.getZonaColmena(), mapa.getZonaCallePrincipal());
                mapa.comprobarPausa();
                setCapturado(false);
                log.escribirEvento("El niño " + id + " ha sido rescatado.");
            }
            else{
                mapa.comprobarPausa();
            setLleva_sangre(true);
                    mapa.comprobarPausa();
                    portal_elegido.cruzarContrario(this);
                    mapa.comprobarPausa();
                    log.escribirEvento("El niño " + id + " ha cruzado el portal contrario hacia " + this.getUbicacion());

            mapa.entregar_sangre(this);
            log.escribirEvento("El niño " + id + " ha entregado sangre.");
            try{
                sleep((long)(Math.random() * 2000+2000));
            }
            catch(Exception e){
                mapa.comprobarPausa();
            }
            mapa.comprobarPausa();
            mapa.descanso(this);
            log.escribirEvento("El niño " + id + " esta descansando.");
            try{
                sleep((long)(Math.random() * 2000+3000));
            }
            catch(Exception e){
                mapa.comprobarPausa();
            }}

    }

    }
}


