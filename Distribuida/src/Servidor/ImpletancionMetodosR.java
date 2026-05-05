package Servidor;

import Interfaz.InterfazCS;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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

    public String[] top3_Demogorgons() throws RemoteException{
        return  null;
    }

    public String devolver_evento(Eventos evento) throws RemoteException{
        String[] nombresEventos = {"NORMALIDAD", "APAGÓN", "TORMENTA", "ELEVEN INTERVIENE", "RED MENTAL"};
        return ("Evento: " + nombresEventos[evento.getEventoActual()]);
    }

    public void start_stop() throws RemoteException{

    }

}

