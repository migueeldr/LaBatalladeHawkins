public class Niño extends Thread{
    private String id;
    private boolean lleva_sangre;
    private int ubicacion;
    private boolean esta_atacado;
    private boolean capturado;
    private final Ciudad mapa;
    private final LogHawkins log;

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




    public Niño(String id, Ciudad mapa, LogHawkins log) {
        this.id = id;
        this.mapa = mapa;
        this.log = log;
        this.lleva_sangre = false;
        this.ubicacion = 0;
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

    public int elegirPortal(){
        int portal_elegido= (int) (Math.random() * 4+3);
        return portal_elegido;

    }

    public void run(){
        while(true){

            setUbicacion(sotano_byers);

            try{
                long aleatorio= (long) (Math.random() * 1000+1000);
                sleep(aleatorio);
            }
            catch(Exception e){}
            int portal_elegido=elegirPortal();
            mapa.entrar_portal(portal_elegido);
            setUbicacion(portal_elegido);
            try{
                long aleatorio= (long) (Math.random() * 2000+3000);
                sleep(aleatorio);
            }
            catch(Exception e){
            }
            if (capturado){
                setUbicacion(colmena);
                mapa.esperar_rescate(this);
            }
            else{
            setLleva_sangre(true);
            mapa.entrar_portal_vuelta(portal_elegido);
            mapa.entregar_sangre();
            setUbicacion(radio_wsqk);
            try{
                sleep((long)(Math.random() * 2000+2000));
            }
            catch(Exception e){}
            setUbicacion(calle_principal);
            try{
                sleep((long)(Math.random() * 2000+3000));
            }
            catch(Exception e){}}

    }}
}