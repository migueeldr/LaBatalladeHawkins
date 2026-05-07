package Distribuida.Interfaz;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfazCS extends Remote {
    // El cliente solo pide el número, no envía el mapa
    int niños_hawkins() throws RemoteException;
    int niños_portalesBosque() throws RemoteException;
    int niños_portalesAlcantarillado() throws RemoteException;
    int niños_portalesCentroComercial() throws RemoteException;
    int niños_portalesLaboratorio() throws RemoteException;
    int niños_Bosque() throws RemoteException;
    int niños_Alcantarillado() throws RemoteException;
    int niños_CentroComercial() throws RemoteException;
    int niños_Laboratorio() throws RemoteException;
    int niños_Colmena() throws RemoteException;
    int demogorgons_Bosque() throws RemoteException;
    int demogorgons_Alcantarillado() throws RemoteException;
    int demogorgons_CentroComercial() throws RemoteException;
    int demogorgons_Laboratorio() throws RemoteException;
    int demogorgons_Colmena() throws RemoteException;
    ArrayList<String> top3_Demogorgons() throws RemoteException;
    String devolver_evento() throws RemoteException;
    void start_stop() throws RemoteException;
}