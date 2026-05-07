package Distribuida;

import Concurrente.Ciudad;
import Concurrente.Demogorgon;
import Concurrente.Eventos;
import Distribuida.Interfaz.InterfazCS;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;

public class ImpletancionMetodosR extends UnicastRemoteObject implements InterfazCS {
    private Ciudad mapa;
    private Eventos eventos;

    public ImpletancionMetodosR(Ciudad mapa, Eventos eventos) throws RemoteException {
        super();
        this.mapa = mapa;
        this.eventos = eventos;
    }

    @Override
    public int niños_hawkins() throws RemoteException {
        return mapa.getZonaRadioWsqk().size() + mapa.getZonaSotanoByers().size() + mapa.getZonaCallePrincipal().size();
    }

    @Override
    public int niños_portalesBosque() throws RemoteException { return mapa.portalBosque.getNiños_Portal(); }
    @Override
    public int niños_portalesAlcantarillado() throws RemoteException { return mapa.portaAlcantarillado.getNiños_Portal(); }
    @Override
    public int niños_portalesCentroComercial() throws RemoteException { return mapa.portaCentroComercial.getNiños_Portal(); }
    @Override
    public int niños_portalesLaboratorio() throws RemoteException { return mapa.portaLaboratorio.getNiños_Portal(); }

    @Override
    public int niños_Bosque() throws RemoteException { return mapa.getZonaBosque().size(); }
    @Override
    public int niños_Alcantarillado() throws RemoteException { return mapa.getZonaAlcantarillado().size(); }
    @Override
    public int niños_CentroComercial() throws RemoteException { return mapa.getZonaCentroComercial().size(); }
    @Override
    public int niños_Laboratorio() throws RemoteException { return mapa.getZonaLaboratorio().size(); }
    @Override
    public int niños_Colmena() throws RemoteException { return mapa.getZonaColmena().size(); }

    @Override
    public int demogorgons_Bosque() throws RemoteException { return mapa.getDemBosque().size(); }
    @Override
    public int demogorgons_Alcantarillado() throws RemoteException { return mapa.getDemAlcantarillado().size(); }
    @Override
    public int demogorgons_CentroComercial() throws RemoteException { return mapa.getDemCentroComercial().size(); }
    @Override
    public int demogorgons_Laboratorio() throws RemoteException { return mapa.getDemLaboratorio().size(); }
    @Override
    public int demogorgons_Colmena() throws RemoteException { return mapa.getDemColmena().size(); }

    @Override
    public ArrayList<String> top3_Demogorgons() throws RemoteException {
        ArrayList<Demogorgon> dem = new ArrayList<>(mapa.getDem_Todos());
        dem.sort(Comparator.comparing(Demogorgon::getCapturas).reversed());
        ArrayList<String> top3 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, dem.size()); i++) {
            top3.add(dem.get(i).getIdDemogorgon() + " (" + dem.get(i).getCapturas() + ")");
        }
        while (top3.size() < 3) top3.add("---");
        return top3;
    }

    @Override
    public String devolver_evento() throws RemoteException {
        String[] nombres = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN", "RED MENTAL"};
        return nombres[eventos.getEventoActual()];
    }

    @Override
    public void start_stop() throws RemoteException {
        mapa.alternarPausa();
    }
}