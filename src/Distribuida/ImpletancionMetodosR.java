package Distribuida;

import Concurrente.Ciudad;
import Concurrente.Demogorgon;
import Concurrente.Eventos;

import Distribuida.Interfaz.InterfazCS;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ImpletancionMetodosR extends UnicastRemoteObject implements InterfazCS {
     private Ciudad mapa;
    public ImpletancionMetodosR() throws RemoteException {

    }

    public int niños_hawkins(Ciudad mapa) throws RemoteException{
        return mapa.getZonaRadioWsqk().size() + mapa.getZonaSotanoByers().size() + mapa.getZonaCallePrincipal().size();
    }

    public int niños_portalesBosque(Ciudad mapa) throws RemoteException{
        return mapa.portalBosque.getNiños_Portal();
    }
    public int niños_portalesAlcantarillado(Ciudad mapa) throws RemoteException{
        return mapa.portaAlcantarillado.getNiños_Portal();
    }
    public int niños_portalesCentroComercial(Ciudad mapa) throws RemoteException{
        return mapa.portaCentroComercial.getNiños_Portal();
    }
    public int niños_portalesLaboratorio(Ciudad mapa) throws RemoteException{
        return mapa.portaLaboratorio.getNiños_Portal();
    }

    public int niños_Bosque(Ciudad mapa) throws RemoteException{
        return mapa.getZonaBosque().size();
    }
    public int niños_Alcantarillado(Ciudad mapa) throws RemoteException{
        return mapa.getZonaAlcantarillado().size();
    }
    public int niños_CentroComercial(Ciudad mapa) throws RemoteException{
        return mapa.getZonaCentroComercial().size();
    }
    public int niños_Laboratorio(Ciudad mapa) throws RemoteException{
        return mapa.getZonaLaboratorio().size();
    }
    public int niños_Colmena(Ciudad mapa) throws RemoteException{
        return mapa.getZonaColmena().size();
    }

    public int demogorgons_Bosque(Ciudad mapa) throws RemoteException{
        return mapa.getDemBosque().size();
    }
    public int demogorgons_Alcantarillado(Ciudad mapa) throws RemoteException{
        return mapa.getDemAlcantarillado().size();
    }
    public int demogorgons_CentroComercial(Ciudad mapa) throws RemoteException{
        return mapa.getDemCentroComercial().size();
    }
    public int demogorgons_Laboratorio(Ciudad mapa) throws RemoteException{
        return mapa.getDemLaboratorio().size();
    }
    public int demogorgons_Colmena(Ciudad mapa) throws RemoteException{
        return mapa.getDemColmena().size();
    }

    //seria mejor ordenar la lista sync demTodos??
    public ArrayList<String> top3_Demogorgons() throws RemoteException{
        ArrayList <Demogorgon> dem;
        dem = (ArrayList<Demogorgon>) mapa.getDem_Todos();
        dem.sort(Comparator.comparing(Demogorgon::getCapturas).reversed());

        ArrayList <String> demT3 = new ArrayList<>();
        demT3.add(dem.get(0).getIdDemogorgon());
        demT3.add(dem.get(1).getIdDemogorgon());
        demT3.add(dem.get(2).getIdDemogorgon());

        return demT3;
    }

    public String devolver_evento(Eventos evento) throws RemoteException{
        String[] nombresEventos = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN INTERVIENE", "RED MENTAL"};
        return ("Evento: " + nombresEventos[evento.getEventoActual()]);
    }

    public void start_stop() throws RemoteException{

    }

}

