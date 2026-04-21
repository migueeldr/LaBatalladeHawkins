public class Niño extends Thread{
    private String id;
    private boolean lleva_sangre;
    private int ubicacion;
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
    }
    public String getIdNiño(){
        return id;
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
            log.escribir(this.id + " ha entrado en el Sótano Byers.");
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
                mapa.ataque_niño();
            }
            setLleva_sangre(true);
            mapa.entregar_sangre(portal_elegido);
            setUbicacion(radio_wsqk);
            try{
                sleep((long)(Math.random() * 2000+2000));
            }
            catch(Exception e){}
            setUbicacion(calle_principal);
            try{
                sleep((long)(Math.random() * 2000+3000));
            }
            catch(Exception e){}

    }}


}
//Alternativa de chatgpt para el run
//            if(mapa.sobreviveAlAtaque(this)) {
//                setLleva_sangre(true);
//                mapa.regresar_y_entregar(this, elegido);
//
//                // --- DESCANSO ---
//                setUbicacion(radio_wsqk);
//                Thread.sleep((long) (Math.random() * 2000 + 2000));
//
//                setUbicacion(calle_principal);
//                Thread.sleep((long) (Math.random() * 2000 + 3000));
//            } else {
//                // Si es capturado, el Mapa lo mandará a la Colmena
//                mapa.ir_a_colmena(this);
//            }