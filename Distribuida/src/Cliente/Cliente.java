package Cliente;

import Interfaz.InterfazCS;

import java.rmi.*;


public class Cliente
{
    public static void main(String args[])
    {
        try
        {
            InterfazCS obj = (InterfazCS) Naming.lookup("//127.0.0.1/ObjetoSaluda"); // Localiza el objeto distribuido
            //logica de la gui
        }
        catch (Exception e)
        {
            System.out.println("Excepción : " + e.getMessage());
        }
    }
}
