package server;

public class Main {

    public static void main(String[] args) {
        Integer port = null;
        //Reads port from argument, if it is no port in argument - set default
        try{
            port = new Integer(args[0]);
        }catch(Exception e){
            port = new Integer(Server.DEFAULT_PORT);
        }
        //start new server
        new Server(port);
    }
}
