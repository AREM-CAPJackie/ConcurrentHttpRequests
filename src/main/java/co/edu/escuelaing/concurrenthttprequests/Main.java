package co.edu.escuelaing.concurrenthttprequests;


import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String argv[]) throws Exception{

        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(new Integer(System.getenv("PORT")));
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 3, TimeUnit.SECONDS, 
                new ArrayBlockingQueue<>(10));
        while(true){
            executor.execute(new ServerThread(serverSocket.accept()));
        }
    }
}