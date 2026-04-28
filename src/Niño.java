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
        mapa.addNiñoCallePrincipal(this);
        esta_atacado=false;
        capturado=false;
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
        if(portal_elegido==3){return mapa.portalBosque;}
        if(portal_elegido==4){return mapa.portaLaboratorio;}
        if(portal_elegido==5){return mapa.portaCentroComercial;}
        else {return mapa.portaAlcantarillado;}
    }
    public void liberarDeAtaque(){
        semaforo_ataque.release();
    }

    public void run(){
        log.escribirEvento("El niño " + id + " ha nacido.");
        while(true){
            mapa.moverNiño(this, mapa.getZonaCallePrincipal(), mapa.getZonaSotanoByers());

            try{
                long aleatorio= (long) (Math.random() * 1000+1000);
                sleep(aleatorio);
            }
            catch(Exception e){}
            Ciudad.Portal portal_elegido=elegirPortal();
            try {
                portal_elegido.cruzarHabitual(this);
                log.escribirEvento("El niño " + id + " ha cruzado el portal hacia " + this.getUbicacion());
            } catch (InterruptedException e) {

            }
            try{
                long aleatorio= (long) (Math.random() * 2000+3000);
                if (eventos.getEventoActual() == 2) {
                    aleatorio= aleatorio * 2;
                }
                sleep(aleatorio);
            }
            catch(Exception e){
                try{semaforo_ataque.acquire();}
                catch(Exception e2){}
            }

            if (getCapturado()){
                log.escribirEvento("El niño " + id + " ha sido capturado.");
                mapa.esperar_rescate();
                mapa.moverNiño(this, mapa.getZonaColmena(), mapa.getZonaCallePrincipal());
                setCapturado(false);
                log.escribirEvento("El niño " + id + " ha sido rescatado.");
            }
            else{
            setLleva_sangre(true);
                try {
                    portal_elegido.cruzarContrario(this);
                    log.escribirEvento("El niño " + id + " ha cruzado el portal contrario hacia " + this.getUbicacion());
                } catch (InterruptedException e) {}

            mapa.entregar_sangre(this);
            log.escribirEvento("El niño " + id + " ha entregado sangre.");
            try{
                sleep((long)(Math.random() * 2000+2000));
            }
            catch(Exception e){}
            mapa.descanso(this);
            log.escribirEvento("El niño " + id + " esta descansando.");
            try{
                sleep((long)(Math.random() * 2000+3000));
            }
            catch(Exception e){}}

    }

    }
}


