package server;

import java.io.*;
import java.net.*;

public class Server{
    private static ServerSocket server;
    public static boolean activ = true;
    public static final int DEFAULT_PORT = 10000;
    public Server(final int port) {           
        try {
            server = new ServerSocket(port);
        } catch (Exception ex) {// if port is unavailable
            try {
                System.err.println(ex.getMessage());
                System.err.println("Opening server at port: "+DEFAULT_PORT);
                server = new ServerSocket(DEFAULT_PORT); //then set default port
            } catch (IOException ex1) {
                System.err.println(ex1.getMessage()); //if default is also unavailable
                System.err.println("Try again");
                System.exit(0);
            }
        }
        System.out.println("Server run at port: "+server.getLocalPort());
        while(activ){ //while port is active
            Socket client=null;
            try {
                client = server.accept();
            } catch (IOException ex) {
                System.err.println("Error in opening new Socket connection ("+ex.getMessage()+")");
            }
                new Thread(new Client(client)).start();
        }
        try {
            server.close();
        } catch (IOException ex) {
            System.err.println("Error in closeing server");
        }                       
    }        
}
