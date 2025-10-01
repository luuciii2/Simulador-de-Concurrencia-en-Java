/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Lucía
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogApocalipsis {
    

    
    // Lock para proteger acceso concurrente al log.
    private final Lock lock = new ReentrantLock();
    
    // Formato para la marca de tiempo.
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // Writer para escribir en "apocalipsis.txt".
    private PrintWriter writer;
    

    
    LogApocalipsis() {
        try {
            
            writer = new PrintWriter(
                       new BufferedWriter(
                       new FileWriter("apocalipsis.txt")));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // Método para escribir un evento en el log.
    public void escribir(String evento) {
        lock.lock();
        try {
            String marcaTiempo = formatoFecha.format(new Date());
            writer.println(marcaTiempo + " " + evento);
            writer.flush();
        } finally {
            lock.unlock();
        }
    }

}

