package Servidor;

import java.rmi.*;

public class Servidor
{
    public static void main(String args[])
    {
        try
        {
            ImpletancionMetodosR obj = new ImpletancionMetodosR(); // Crea una instancia del objeto que implementa la interfaz
            Naming.rebind("//127.0.0.1/ObjetoSaluda",obj);
            System.out.println("El objeto ha quedado registrado");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}