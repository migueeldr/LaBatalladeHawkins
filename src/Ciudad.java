import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

import static java.lang.Thread.sleep;

public class Ciudad {
    private Niño niño;
    private Demogorgon demogorgon;
    private int contador_sangre;
    private AtomicInteger contador_capturas;
    private List<Niño> zona_calle_principal = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zona_sotano_byers = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zona_radio_wsqk = Collections.synchronizedList(new ArrayList<>());

    private List<Niño> zonaBosque = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaLaboratorio = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaCentroComercial = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaAlcantarillado = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaColmena = Collections.synchronizedList(new ArrayList<>());
//
    //creo que esto deberia ser private y hacer getters
    public Portal portalBosque = new Portal(zona_sotano_byers,zonaBosque);
    public Portal portaLaboratorio = new Portal(zona_sotano_byers,zonaLaboratorio);
    public Portal portaCentroComercial = new Portal(zona_sotano_byers,zonaCentroComercial);
    public Portal portaAlcantarillado = new Portal(zona_sotano_byers,zonaAlcantarillado);

    private List<Demogorgon> dem_Bosque=Collections.synchronizedList(new ArrayList<>());;
    private List<Demogorgon> dem_Laboratorio=Collections.synchronizedList(new ArrayList<>());;
    private List<Demogorgon> dem_CentroComercial=Collections.synchronizedList(new ArrayList<>());;
    private List<Demogorgon> dem_Alcantarillado=Collections.synchronizedList(new ArrayList<>());;
    private List<Demogorgon> dem_Colmena=Collections.synchronizedList(new ArrayList<>());;


    private Semaphore semaforo_Contador= new Semaphore(1);

    public class Portal {
        private List<Niño> origen;
        private List<Niño> destino;

        private final ReentrantLock lock = new ReentrantLock(true);

        private final Condition condHabitual = lock.newCondition();
        private final Condition condContrario = lock.newCondition();

        private int esperandoHawkins = 0;
        private int esperndoUpsideDown = 0;

        private int restantesGrupo = 0;   // hilos del grupo que quedan por cruzar
        private boolean portalOcupado = false;

        public Portal(List<Niño> origen,List<Niño> destino) { //no tengo claro como se manejan los lugares
            this.origen = origen;
            this.destino = destino;
        }
        public void cruzarHabitual(Niño n) throws InterruptedException {
            lock.lock();
            try {
                esperandoHawkins++;


                while (restantesGrupo == 0) {  //lo comprueban los hilos que entran despues de que pase un grupo
                    if (esperandoHawkins >= 3 && esperndoUpsideDown == 0) {  //forma grupo cuando toca
                        restantesGrupo = 3;
                        condHabitual.signalAll();
                    } else {
                        condHabitual.await();
                    }
                }

                esperandoHawkins--;

            } finally {
                lock.unlock();
            }

            // pasan de uno en uno
            lock.lock();
            try {
                while (portalOcupado || esperndoUpsideDown > 0) {
                    condHabitual.await();
                }
                portalOcupado = true;
            } finally {
                lock.unlock();
            }

            moverNiño(n, origen, destino);
            n.setUbicacion(zona_GetId(destino));
            cruzar(n);

            lock.lock();
            try {
                portalOcupado = false;
                restantesGrupo--;

                condContrario.signalAll();
                condHabitual.signalAll();

            } finally {
                lock.unlock();
            }
        }



        public void cruzarContrario(Niño n) throws InterruptedException {
            lock.lock();
            try {
                esperndoUpsideDown++;

                while (portalOcupado) {
                    condContrario.await();
                }

                esperndoUpsideDown--;
                portalOcupado = true;

            } finally {
                lock.unlock();
            }
            moverNiño(n, destino, origen);
            n.setUbicacion(zona_GetId(origen)); //me parece una mala decison lo de los numeros de las ubicaciones
            cruzar(n);

            lock.lock();
            try {
                portalOcupado = false;

                // Prioridad contrarios
                condContrario.signalAll();
                condHabitual.signalAll();

            } finally {
                lock.unlock();
            }
        }

        //quitar print antes de entregar
        private void cruzar(Niño  n) throws InterruptedException {
            System.out.println("Hilo " + n.getIdNiño() + " cruzando...");
            Thread.sleep(1000);
            System.out.println("Hilo " + n.getIdNiño() + " ha cruzado");
        }
    }



    public Ciudad(){
        contador_capturas=new AtomicInteger(0);
        contador_sangre=0;
    }
    public int getContador_capturas(){
        return contador_capturas.get();
    }
    public synchronized int getContador_sangre(){
        return contador_sangre;
    }
    public synchronized void incrementar_contador_sangre(){
        contador_sangre++;
    }
    public synchronized void decrementar_contador_sangre(){
        contador_sangre--;
    }
    public void incrementar_contador_capturas(){
        contador_capturas.incrementAndGet();
    }
    public void decrementar_contador_capturas(){
        contador_capturas.decrementAndGet();
    }
    public synchronized List<Niño> getZonaCallePrincipal() { return zona_calle_principal; }
    public synchronized void addNiñoCallePrincipal(Niño n) { zona_calle_principal.add(n); }
    public synchronized void removeNiñoCallePrincipal(Niño n) { zona_calle_principal.remove(n); }

    public synchronized List<Niño> getZonaSotanoByers() { return zona_sotano_byers; }
    public synchronized void addNiñoSotanoByers(Niño n) { zona_sotano_byers.add(n); }
    public synchronized void removeNiñoSotanoByers(Niño n) { zona_sotano_byers.remove(n); }

    public synchronized List<Niño> getZonaRadioWsqk() { return zona_radio_wsqk; }
    public synchronized void addNiñoRadioWsqk(Niño n) { zona_radio_wsqk.add(n); }
    public synchronized void removeNiñoRadioWsqk(Niño n) { zona_radio_wsqk.remove(n); }

    public synchronized List<Niño> getZonaBosque() { return zonaBosque; }
    public synchronized void addNiñoBosque(Niño n) { zonaBosque.add(n); }
    public synchronized void removeNiñoBosque(Niño n) { zonaBosque.remove(n); }

    public synchronized List<Niño> getZonaLaboratorio() { return zonaLaboratorio; }
    public synchronized void addNiñoLaboratorio(Niño n) { zonaLaboratorio.add(n); }
    public synchronized void removeNiñoLaboratorio(Niño n) { zonaLaboratorio.remove(n); }

    public synchronized List<Niño> getZonaCentroComercial() { return zonaCentroComercial; }
    public synchronized void addNiñoCentroComercial(Niño n) { zonaCentroComercial.add(n); }
    public synchronized void removeNiñoCentroComercial(Niño n) { zonaCentroComercial.remove(n); }

    public synchronized List<Niño> getZonaAlcantarillado() { return zonaAlcantarillado; }
    public synchronized void addNiñoAlcantarillado(Niño n) { zonaAlcantarillado.add(n); }
    public synchronized void removeNiñoAlcantarillado(Niño n) { zonaAlcantarillado.remove(n); }

    public synchronized List<Niño> getZonaColmena() { return zonaColmena; }
    public synchronized void addNiñoColmena(Niño n) { zonaColmena.add(n); }
    public synchronized void removeNiñoColmena(Niño n) { zonaColmena.remove(n); }

    public synchronized List<Demogorgon> getDemBosque() { return dem_Bosque; }
    public synchronized void addDemBosque(Demogorgon d) { dem_Bosque.add(d); }
    public synchronized void removeDemBosque(Demogorgon d) { dem_Bosque.remove(d); }

    public synchronized List<Demogorgon> getDemLaboratorio() { return dem_Laboratorio; }
    public synchronized void addDemLaboratorio(Demogorgon d) { dem_Laboratorio.add(d); }
    public synchronized void removeDemLaboratorio(Demogorgon d) { dem_Laboratorio.remove(d); }

    public synchronized List<Demogorgon> getDemCentroComercial() { return dem_CentroComercial; }
    public synchronized void addDemCentroComercial(Demogorgon d) { dem_CentroComercial.add(d); }
    public synchronized void removeDemCentroComercial(Demogorgon d) { dem_CentroComercial.remove(d); }

    public synchronized List<Demogorgon> getDemAlcantarillado() { return dem_Alcantarillado; }
    public synchronized void addDemAlcantarillado(Demogorgon d) { dem_Alcantarillado.add(d); }
    public synchronized void removeDemAlcantarillado(Demogorgon d) { dem_Alcantarillado.remove(d); }

    public synchronized List<Demogorgon> getDemColmena() { return dem_Colmena; }
    public synchronized void addDemColmena(Demogorgon d) { dem_Colmena.add(d); }
    public synchronized void removeDemColmena(Demogorgon d) { dem_Colmena.remove(d); }

    public synchronized void moverNiño(Niño n, List<Niño> origen, List<Niño> destino) {
        origen.remove(n);
        destino.add(n);
    }
    public synchronized void moverDemogorgon(Demogorgon d, List<Demogorgon> origen, List<Demogorgon> destino) {
        origen.remove(d);
        destino.add(d);
    }




    public void entregar_sangre(Niño n){
        try{
            moverNiño(n, zona_sotano_byers, zona_radio_wsqk);
            n.setUbicacion(2);
            semaforo_Contador.acquire();
            contador_sangre++;
            n.setLleva_sangre(false);
            semaforo_Contador.release();
        }
        catch(Exception e){}

    }
    public void descanso(Niño n){
        moverNiño(n, zona_radio_wsqk, zona_calle_principal);
        n.setUbicacion(0);
    }

}
