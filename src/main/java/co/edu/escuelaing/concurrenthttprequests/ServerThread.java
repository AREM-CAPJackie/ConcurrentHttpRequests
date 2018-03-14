package co.edu.escuelaing.concurrenthttprequests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread implements Runnable{

    private Socket clientSocket;

    ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            System.out.println(in.readLine().length());
            String pathSource = in.readLine();
            String outputFormat;
            String dataLength;
            if(pathSource != null){
                if(pathSource.contains(".html")){
                    dataLength = "" + Files.readAllBytes(new File("./" + pathSource).toPath()).length;
                    outputFormat = "text/html";
                }
                
                else if(pathSource.contains(".jpg")){
                    dataLength = "" + Files.readAllBytes(new File("./" + pathSource).toPath()).length;
                    outputFormat = "image/jpg";
                }
                
                else{
                    dataLength = "" + Files.readAllBytes(new File("./index.html").toPath()).length;
                    outputFormat = "text/html";
                }
                
            }
            else{
                dataLength = "" + Files.readAllBytes(new File("./index.html").toPath()).length;
                outputFormat = "text/html";
            }
            
            
            
            String output = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: " + outputFormat + "\r\n"+"Content-Length: " + dataLength;   
            
            out.write(output);
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}