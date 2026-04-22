public class Demogorgon extends Thread{
    private Niño niño;
    private String id;
    private boolean lleva_niño;
    private int ubicacion;
    private int capturas;
    private final Ciudad mapa;
    private final LogHawkins log;

    private final int bosque=3;
    private final int laboratorio=4;
    private final int centro_comercial=5;
    private final int alcantarillado=6;
    private final int colmena=7;

    public Demogorgon(String id, Ciudad mapa, LogHawkins log) {
        this.id=id;
        this.mapa=mapa;
        this.log=log;
        this.lleva_niño=false;
        this.ubicacion=7;
        this.capturas=0;
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
        while(true){
            int zona=elegirZona();
            setUbicacion(zona);
            Niño victima=mapa.obtener_niño(ubicacion);
            if(victima!=null){
                try{
                    synchronized (victima) {
                    long tiempo_ataque= (long) (Math.random() * 1000+500);
                    sleep(tiempo_ataque);
                    if (mapa.ataque_niño(victima)){
                        setUbicacion(colmena);
                        sleep((long) (Math.random() * 500 + 500));
                        incrementarCapturas();
                    }


                }}
                catch(Exception e){}
            }
            else{
                try{
                    sleep((long) (Math.random() * 1000 + 4000));
                }
                catch(Exception e){}
            }

        }
    }
}
