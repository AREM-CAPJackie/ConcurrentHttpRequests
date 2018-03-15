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
            //System.out.println(clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String pathSource = in.readLine();
            System.out.println("path-----------------------> "+pathSource);
            String outputFormat;
            String dataLength;
            byte[] bytesSource = null;
            if(pathSource != null){
                pathSource = pathSource.split(" ")[1];
                if(pathSource.contains(".html")){
                    bytesSource = Files.readAllBytes(new File("./" + pathSource).toPath());
                    dataLength = "" + bytesSource.length;
                    outputFormat = "text/html";
                }
                
                else if(pathSource.contains(".jpg")){
                    bytesSource = Files.readAllBytes(new File("./" + pathSource).toPath());
                    dataLength = "" + bytesSource.length;
                    outputFormat = "image/jpg";
                }
                
                else{
                    bytesSource = Files.readAllBytes(new File("./index.html").toPath());
                    dataLength = "" + bytesSource.length;
                    outputFormat = "text/html";
                }
                
            }
            else{
                bytesSource = Files.readAllBytes(new File("./index.html").toPath());
                dataLength = "" + bytesSource.length;
                outputFormat = "text/html";
            }
            
            
            
            String output = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: " + outputFormat + "\r\n"+"Content-Length: " + dataLength;   
            
            byte [] hByte = output.getBytes();
            byte[] rta = new byte[bytesSource.length + hByte.length];
            for (int i = 0; i < hByte.length; i++) {rta[i] = hByte[i];}
            for (int i = hByte.length; i < hByte.length + bytesSource.length; i++) {
                rta[i] = bytesSource[i - hByte.length];
            }
            clientSocket.getOutputStream().write(rta);
            
            //out.write(output);
            
            //out.close();
            //in.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}