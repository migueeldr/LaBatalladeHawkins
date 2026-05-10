package Concurrente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ciudad {
    private int contador_sangre;
    private final LogHawkins log;
    private AtomicInteger contador_capturas;
    private List<Niño> zona_calle_principal = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zona_sotano_byers = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> sotano_byersPreparacion = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zona_radio_wsqk = Collections.synchronizedList(new ArrayList<>());

    private List<Niño> zonaBosque = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaLaboratorio = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaCentroComercial = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaAlcantarillado = Collections.synchronizedList(new ArrayList<>());
    private List<Niño> zonaColmena = Collections.synchronizedList(new ArrayList<>());

    private Portal portalBosque = new Portal(2, zona_sotano_byers,zonaBosque);
    private Portal portaLaboratorio = new Portal(3, zona_sotano_byers,zonaLaboratorio);
    private Portal portaCentroComercial = new Portal(4,zona_sotano_byers,zonaCentroComercial);
    private Portal portaAlcantarillado = new Portal(2, zona_sotano_byers,zonaAlcantarillado);

    private List<Demogorgon> dem_Bosque=Collections.synchronizedList(new ArrayList<>());
    private List<Demogorgon> dem_Laboratorio=Collections.synchronizedList(new ArrayList<>());
    private List<Demogorgon> dem_CentroComercial=Collections.synchronizedList(new ArrayList<>());
    private List<Demogorgon> dem_Alcantarillado=Collections.synchronizedList(new ArrayList<>());
    private List<Demogorgon> dem_Colmena=Collections.synchronizedList(new ArrayList<>());

    private List<Demogorgon> dem_Todos=Collections.synchronizedList(new ArrayList<>());

    private boolean elevenActiva = false;
    private final ReentrantLock lockEleven = new ReentrantLock();
    private final Condition condElevenTermina = lockEleven.newCondition();

    private Semaphore semaforo_Contador= new Semaphore(1);

    private AtomicBoolean pausado= new AtomicBoolean(false);
    private final ReentrantLock lockPausa = new ReentrantLock();
    private final Condition condReanudar = lockPausa.newCondition();
    private List<Thread> todosLosHilos = Collections.synchronizedList(new ArrayList<>());

    public boolean getPausado() {
        return pausado.get();
    }

    public void registrarHilo(Thread t) {
        todosLosHilos.add(t);
    }

    public void alternarPausa() {
        lockPausa.lock();
        try {
            pausado.set(!(pausado.get()));
            if (!pausado.get()) {
                condReanudar.signalAll();
            }
        } finally {
            lockPausa.unlock();
        }
    }

    public void comprobarPausa() {
        lockPausa.lock();
        try {
            while (pausado.get()) {
                try {
                    condReanudar.await();
                } catch (InterruptedException e) {
                }
            }
        } finally {
            lockPausa.unlock();
        }
    }

    public synchronized void esperar_rescate() {
        while (!isElevenActiva() || getContador_sangre()==0){
            try{this.wait();}catch(InterruptedException e){}
        }
        decrementar_contador_sangre();
        notifyAll();
    }

    public List<Niño> getSotano_byersPreparacion() {
        return sotano_byersPreparacion;
    }

    public class Portal {
        private int tGrupo;
        private int pEsperando;
        private List<Niño> origen;
        private List<Niño> destino;

        private final ReentrantLock lock = new ReentrantLock(true);
        private final Condition condHabitual = lock.newCondition();
        private final Condition condContrario = lock.newCondition();

        private int esperandoHawkins = 0;
        private int esperndoUpsideDown = 0;
        private boolean apagon_activo;

        private int restantesGrupo = 0;   // hilos del grupo que quedan por cruzar
        private boolean portalOcupado = false;

        private List<Niño> colaEspera = new ArrayList<>();
        private List<Niño> grupoFormado = new ArrayList<>();
        private Niño niñoCruzando = null;
        private List<Niño> colaEsperaContrario = new ArrayList<>();

        public Portal(int tGrupo , List<Niño> origen, List<Niño> destino) {
            this.tGrupo=tGrupo;
            this.origen = origen;
            this.destino = destino;
        }

        public List<Niño> getColaEspera() {
            lock.lock();
            try { return new ArrayList<>(colaEspera); } finally { lock.unlock(); }
        }

        public List<Niño> getGrupoFormado() {
            lock.lock();
            try { return new ArrayList<>(grupoFormado); } finally { lock.unlock(); }
        }

        public Niño getNiñoCruzando() {
            lock.lock();
            try { return niñoCruzando; } finally { lock.unlock(); }
        }

        public List<Niño> getColaEsperaContrario() {
            lock.lock();
            try { return new ArrayList<>(colaEsperaContrario); } finally { lock.unlock(); }
        }

        public int getNiños_Portal() {
            lock.lock();
            try { return pEsperando; } finally { lock.unlock(); }
        }

        public void setApagon_activo(boolean apagon_activo) {
            lock.lock();
            try {
                this.apagon_activo = apagon_activo;
                if (!this.apagon_activo) {
                    condContrario.signalAll();
                    condHabitual.signalAll();
                }
            } catch (Exception e) {}
            finally { lock.unlock(); }
        }

        public void cruzarHabitual(Niño n) throws InterruptedException {
            comprobarPausa();
            lock.lock();
            try {
                colaEspera.add(n);
                esperandoHawkins++;
                pEsperando++;

                while (restantesGrupo == 0) {
                    if (esperandoHawkins >= tGrupo && esperndoUpsideDown == 0) {
                        restantesGrupo = tGrupo;

                        for (int i = 0; i < tGrupo; i++) {
                            if (!colaEspera.isEmpty()) {
                                grupoFormado.add(colaEspera.remove(0));
                            }
                        }
                        condHabitual.signalAll();
                        log.escribirEvento("El niño " + n.getIdNiño() + " ha intentado formar grupo");
                    } else {
                        condHabitual.await();
                    }
                }
                esperandoHawkins--;
            } finally {
                lock.unlock();
                comprobarPausa();
            }

            comprobarPausa();
            lock.lock();
            try {
                while (portalOcupado || esperndoUpsideDown > 0 || apagon_activo || !grupoFormado.contains(n)) {
                    condHabitual.await();
                }
                portalOcupado = true;

                niñoCruzando = n;
                grupoFormado.remove(n);
            } finally {
                lock.unlock();
                comprobarPausa();
            }


            cruzar(n);
            moverNiño(n, origen, destino);
            log.escribirEvento("El niño " + n.getIdNiño() + " ha cruzado hacia " + destino);

            comprobarPausa();
            lock.lock();
            try {
                portalOcupado = false;
                niñoCruzando = null; // [REALIDAD] Ya terminó de cruzar
                restantesGrupo--;

                condContrario.signalAll();
                condHabitual.signalAll();
            } finally {
                pEsperando--;
                lock.unlock();
                comprobarPausa();
            }
        }

        public void cruzarContrario(Niño n)  {
            comprobarPausa();
            lock.lock();
            try {
                colaEsperaContrario.add(n);
                esperndoUpsideDown++;
                pEsperando++;

                while (portalOcupado  || apagon_activo) {
                    condContrario.await();
                }

                esperndoUpsideDown--;
                colaEsperaContrario.remove(n);
                portalOcupado = true;
                niñoCruzando = n;
            }
            catch(Exception e){
                esperndoUpsideDown--;
                colaEsperaContrario.remove(n);
                try { n.getSemaphore_ataque().acquire(); } catch(Exception e2) {}
            } finally {
                lock.unlock();
                comprobarPausa();
            }


            cruzar(n);
            log.escribirEvento("El niño " + n.getIdNiño() + " HA CRUZADO EN SENTIDO CONTRARIO");
            moverNiño(n, destino, origen);

            comprobarPausa();
            lock.lock();
            try {
                portalOcupado = false;
                niñoCruzando = null;

                condContrario.signalAll();
                condHabitual.signalAll();
            } finally {
                pEsperando--;
                lock.unlock();
                comprobarPausa();
            }
        }

        private void cruzar(Niño n) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }



    public Ciudad(){
        contador_capturas=new AtomicInteger(0);
        contador_sangre=0;
        log=new LogHawkins();
    }

    //COSAS NIÑO
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

    public synchronized void moverNiño(Niño n, List<Niño> origen, List<Niño> destino) {
        if (origen.remove(n)) { // devuelve true si todavia no lo han movido
            destino.add(n);
            if (destino ==zona_calle_principal ) {n.setUbicacion(0);}
            if (destino ==zona_sotano_byers ) {n.setUbicacion(1);}
            if (destino ==zona_radio_wsqk ) {n.setUbicacion(2);}
            if (destino ==zonaBosque ) {n.setUbicacion(3);}
            if (destino ==zonaLaboratorio ) {n.setUbicacion(4);}
            if (destino ==zonaCentroComercial ) {n.setUbicacion(5);}
            if (destino ==zonaAlcantarillado ) {n.setUbicacion(6);}
            if (destino ==zonaColmena ) {n.setUbicacion(7);}

        }
    }
    public synchronized List<Niño> getListaUbicacionN(int n) {
        if (n == 0) {
            return getZonaCallePrincipal();
        }
        if (n == 1) {
            return getZonaSotanoByers();
        }
        if (n == 2) {
            return getZonaRadioWsqk();
        }
        if (n == 3) {
            return getZonaBosque();
        }
        if (n == 4) {
            return getZonaLaboratorio();
        }
        if (n == 5) {
            return getZonaCentroComercial();
        }
        if (n == 6) {
            return getZonaAlcantarillado();
        } else {
            return getZonaColmena();
        }
    }
    public void entregar_sangre(Niño n){
        try{
            moverNiño(n, zona_sotano_byers, zona_radio_wsqk);
            semaforo_Contador.acquire();
            contador_sangre++;
            n.setLleva_sangre(false);
            semaforo_Contador.release();
        }
        catch(Exception e){}

    }
    public void descanso(Niño n){
        comprobarPausa();
        moverNiño(n, zona_radio_wsqk, zona_calle_principal);
        comprobarPausa();

    }

    public int zona_niños(){
        List<Niño> devolver = zonaBosque;
        int zona=3;

        if (zonaLaboratorio.size() > devolver.size()) {devolver = zonaLaboratorio; zona=4;}
        if (zonaCentroComercial.size() > devolver.size()) {devolver = zonaCentroComercial; zona=5;}
        if (zonaAlcantarillado.size() > devolver.size()) {devolver = zonaAlcantarillado; zona=6;}
        return zona;

    }
    //COSAS DEMOGORGON
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


    public synchronized List<Demogorgon> getListaUbicacionD(int n) {

        if (n ==3 ) {return  getDemBosque();}
        if (n ==4 ) {return  getDemLaboratorio();}
        if (n ==5 ) {return  getDemCentroComercial();}
        if (n ==6 ) {return  getDemAlcantarillado();}
        else  {return  getDemColmena();}
    }
    public synchronized void moverDemogorgon(Demogorgon d, List<Demogorgon> origen, List<Demogorgon> destino) {
        origen.remove(d);
        destino.add(d);
        if (destino ==dem_Bosque ) {d.setUbicacion(3);}
        if (destino ==dem_Laboratorio ) {d.setUbicacion(4);}
        if (destino ==dem_CentroComercial ) {d.setUbicacion(5);}
        if (destino ==dem_Alcantarillado ) {d.setUbicacion(6);}
        if (destino ==dem_Colmena ) {d.setUbicacion(7);}
    }
    public synchronized Niño obtener_niño(int ubicacion){
        int longitud= (getListaUbicacionN(ubicacion)).size();
        int aleatorio= (int) (Math.random()*longitud);
        if (longitud==0){
            return null;
        }
        if(getListaUbicacionN(ubicacion).get(aleatorio).getEsta_atacado()){
            return null;
        }
        else{
            getListaUbicacionN(ubicacion).get(aleatorio).setEsta_atacado(true);
            return getListaUbicacionN(ubicacion).get(aleatorio);
            }
    }

    public boolean ataque_niño(Demogorgon d, Niño n) {
        n.interrupt();
        long tiempo_ataque= (long) (Math.random() * 1000+500);
        try{d.sleep(tiempo_ataque);}
        catch (Exception e){}
        int aleatorio= (int) (Math.random()*3);
        boolean capturado=(aleatorio==0);
        if (capturado){
            n.setCapturado(true);
            moverDemogorgon(d, getListaUbicacionD(d.getUbicacion()), dem_Colmena);
            moverNiño(n, getListaUbicacionN(n.getUbicacion()), zonaColmena);
            long tiempo_depositar= (long) (Math.random()*500+500);
            try{d.sleep(tiempo_depositar);}
            catch (Exception e){}
            incrementar_contador_capturas(d);
            n.setEsta_atacado(false);
        }
        else{
            n.setCapturado(false);
            n.setEsta_atacado(false);
        }
        n.liberarDeAtaque();
        return capturado;
    }








    public void setElevenActiva(boolean activa) {
        lockEleven.lock();
        try {
            this.elevenActiva = activa;
            if (!activa) {
                condElevenTermina.signalAll();
            }
        } finally {
            lockEleven.unlock();
        }
    }
    public boolean isElevenActiva() {
        return elevenActiva;
    }

    public void esperarSiElevenEstaActiva()  {
        lockEleven.lock();
        try {
            while (elevenActiva) {
                condElevenTermina.await();
            }

        }
        catch(Exception e) {}
        finally {
            lockEleven.unlock();
        }
    }
    public Portal getPortalBosque() {
        return portalBosque;
    }

    public Portal getPortaLaboratorio() {
        return portaLaboratorio;
    }

    public Portal getPortaCentroComercial() {
        return portaCentroComercial;
    }

    public Portal getPortaAlcantarillado() {
        return portaAlcantarillado;
    }
    public List<Demogorgon> getDem_Todos() {
        return dem_Todos;
    }

    public void setDem_Todos(List<Demogorgon> dem_Todos) {
        this.dem_Todos = dem_Todos;
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
    public void incrementar_contador_capturas(Demogorgon d){
        contador_capturas.incrementAndGet();
        if (getContador_capturas()%8==0){
            d.crearDemogorgon();
        }
    }
    public void decrementar_contador_capturas(){
        contador_capturas.decrementAndGet();
    }
}
