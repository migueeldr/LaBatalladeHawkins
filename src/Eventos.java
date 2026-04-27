public class Eventos extends Thread {
    private Ciudad mapa;
    private int eventoactual;

    private static final int normalidad=0;
    private static final int apagon=1;
    private static final int tormenta=2;
    private static final int eleven=3;
    private static final int red=4;
//hd
    public Eventos(Ciudad mapa){
        this.mapa=mapa;
        eventoactual=0;
    }
    public int getEventoActual(){
        return eventoactual;
    }
    public void setEventoActual(int eventoactual){
        this.eventoactual=eventoactual;
    }

    public void run(){
        while(true){
            try{
                long pausa = 30000 + (long)(Math.random() * 30000);
                sleep(pausa);
                int evento_elegido= (int) (Math.random() * 5);
                if (evento_elegido==0){
                    setEventoActual(normalidad);
                }
                else if (evento_elegido==1){
                    setEventoActual(apagon);
                }
                else if (evento_elegido==2){
                    setEventoActual(tormenta);
                }
                else if (evento_elegido==3){
                    setEventoActual(eleven);
                }
                else if (evento_elegido==4){
                    setEventoActual(red);
                }
                long duracion= (long) (Math.random() * 5000+5000);
                sleep(duracion);
                setEventoActual(normalidad);
            }
            catch(Exception e){}
        }
    }
}
