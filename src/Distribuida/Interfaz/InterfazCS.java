package Distribuida.Interfaz;
import Concurrente.Ciudad;
import Concurrente.Eventos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface  InterfazCS extends Remote {
//mapa de parametro de entrada?
    public int niños_hawkins(Ciudad mapa) throws RemoteException;

    public int niños_portalesBosque(Ciudad mapa) throws RemoteException;
    public int niños_portalesAlcantarillado(Ciudad mapa) throws RemoteException;
    public int niños_portalesCentroComercial(Ciudad mapa) throws RemoteException;
    public int niños_portalesLaboratorio(Ciudad mapa) throws RemoteException;

    public int niños_Bosque(Ciudad mapa) throws RemoteException;
    public int niños_Alcantarillado(Ciudad mapa) throws RemoteException;
    public int niños_CentroComercial(Ciudad mapa) throws RemoteException;
    public int niños_Laboratorio(Ciudad mapa) throws RemoteException;
    public int niños_Colmena(Ciudad mapa) throws RemoteException;

    public int demogorgons_Bosque(Ciudad mapa) throws RemoteException;
    public int demogorgons_Alcantarillado(Ciudad mapa) throws RemoteException;
    public int demogorgons_CentroComercial(Ciudad mapa) throws RemoteException;
    public int demogorgons_Laboratorio(Ciudad mapa) throws RemoteException;
    public int demogorgons_Colmena(Ciudad mapa) throws RemoteException;

    public ArrayList<String> top3_Demogorgons() throws RemoteException;

    public String devolver_evento(Eventos evento) throws RemoteException;

    public void start_stop() throws RemoteException;
}
