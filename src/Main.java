import static java.lang.Thread.sleep;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Ciudad mapaHawkins = new Ciudad();
        LogHawkins logHawkins = new LogHawkins();
        Eventos eventosHawkins = new Eventos(mapaHawkins, logHawkins);
        Demogorgon demogorgonAlfa = new Demogorgon( 0, mapaHawkins, logHawkins, eventosHawkins);
        demogorgonAlfa.start();
        for (int i=0; i<1500; i++){
            try {
                sleep((long) (Math.random()*1500 +500));
                Niño n = new Niño(i,mapaHawkins, logHawkins, eventosHawkins);
                n.start();
            }
            catch (Exception e){}
        }
        }
    }
