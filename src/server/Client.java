package server;

import java.io.*;
import java.net.*;

public class Client implements Runnable{
    private Socket client,server;
    private OutputStream server_out,client_out;
    private InputStream client_in,server_in;
    private RequestHeader reqh;
    private ResponseHeader resh;
    private ContentChecker content_checker= new ContentChecker();
    public Client(Socket client){
        this.client = client;        
    }
    
    @Override
    public void run() {          
        try{
            //open client streams
                client_in = client.getInputStream();            
                client_out = client.getOutputStream();     
            //get Request
                reqh = new RequestHeader(client_in);                  
            //open connection to server    
                server = new Socket();
                server.connect(new InetSocketAddress(reqh.get("Host"), 80),5000);
            //open server streams
                server_out = server.getOutputStream();
                server_in = server.getInputStream();       
            //change request
                reqh.put("Connection", "close");
            //chack Adress
                if(checkURL()){
                //send request
                    reqh.sendHeader(server_out);
                //get response     
                    resh = new ResponseHeader(server_in);
                //sendToBrowser
                    sendToBrowser(server_in);
                }
            //close streams
                client_in.close();
                client_out.close();
                client.close();
                server_in.close();
                server_out.close();
                server.close();            
        }catch(IOException e){
            System.err.println(e.getMessage());
        }catch(NullPointerException e){
        }
    }
    /** Sends response data to browser
     *  @param in Browsers input stream 
     */
    private void sendToBrowser(InputStream in){        
        byte[] bufor = new byte[1024];
        int length;  
        boolean contain_text=false;
        //send header
            resh.sendHeader(client_out);    
        //print status and address
        System.out.println(resh.Status_Code+" "+resh.Status_Message+" "+reqh.Adress);
        //check if request header contain text or css or javascript to verify content
        if(resh.containsKey("Content-Type"))
            if(resh.get("Content-Type").contains("text")||resh.get("Content-Type").contains("css")||resh.get("Content-Type").contains("javascript"))
                contain_text = true;            
        try {
            client_out.flush();
            BufferedInputStream bis = new BufferedInputStream(in);
            BufferedOutputStream bos = new BufferedOutputStream(client_out);
            while((length=bis.read(bufor, 0, bufor.length))!=-1){
                if(contain_text)
                    if(!content_checker.checkContent(bufor, 0, length, " !@#$%^&*()_-+=:;\"'<,>.?/~`")){
                        showErrorPage(2);
                        break;
                    }
                bos.write(bufor, 0, length);
            }
            bos.flush();            
            client_out.flush();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /** Sends http error site to browser
     *  @param error tells which error site show:
     * <ul>
     *   <li> 1 - requests for undesirable URLse</li>
     *   <li> 2-  inappropriate content bytes within a Web page</li>
     * </ul>
     */
    private void showErrorPage(int error) {
        server_in = getClass().getResourceAsStream("error"+error+".html");  
        BufferedInputStream bis = new BufferedInputStream(server_in);
        BufferedOutputStream bos = new BufferedOutputStream(client_out);        
        int length;
        byte[] bufor = new byte[1024];
        try {
            while ((length=bis.read(bufor, 0, bufor.length)) != -1) {
                bos.write(bufor, 0, length);
            }
            bis.close();
            bos.flush();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /** Check url for undesirable address
     *  @return 
     * <ul>
     *   <li> true - if site contains undesirable address</li>
     *   <li> false -  if site is save</li>
     * </ul>
     */
    private boolean checkURL(){
        if(!content_checker.checkContent(reqh.get("Host")+reqh.Adress," !@#$%^&*()_-+=:;\"'<,>.?/~`")){
            showErrorPage(1);
            return false;
        }
        return true;                
    }    
    
   
}