package Concurrente;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHawkins {
        private final String nombreArchivo = "hawkins.txt";
        private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public synchronized void escribirEvento(String mensaje) {
            String timestamp = LocalDateTime.now().format(formateador);
            String lineaLog = "[" + timestamp + "] " + mensaje;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
                writer.write(lineaLog);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error escribiendo en el log: " + e.getMessage());
            }
        }
    }

