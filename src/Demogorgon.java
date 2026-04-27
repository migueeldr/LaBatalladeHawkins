public class Demogorgon extends Thread{
    private Niño niño;
    private String id;
    private boolean lleva_niño;
    private int ubicacion;
    private int capturas;
    private final Ciudad mapa;
    private final LogHawkins log;
    private Eventos eventos;

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
            mapa.esperarSiElevenEstaActiva();
            if (eventos.getEventoActual() != 1) {
                int zona=elegirZona();
                if (eventos.getEventoActual() == 4) {
                    zona= mapa.zona_niños();
                }
                mapa.moverDemogorgon(this,mapa.getListaUbicacionD(getUbicacion()), mapa.getListaUbicacionD(zona));
            }

            mapa.esperarSiElevenEstaActiva();
            Niño victima=mapa.obtener_niño(ubicacion);
            if(victima!=null){
                try{
                    long tiempo_ataque= (long) (Math.random() * 1000+500);
                    sleep(tiempo_ataque);
                    if (mapa.ataque_niño(this, victima)){
                        incrementarCapturas();
                    }


                }
                catch(Exception e){}
            }
            else{
                try{
                    long espera=(long) (Math.random() * 1000 + 4000);
                    if (eventos.getEventoActual() == 2) {
                        espera = espera / 2;
                    }
                    sleep(espera);
                }
                catch(Exception e){}
            }



        }
    }
}
