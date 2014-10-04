package server;

import java.io.*;

public class RequestHeader extends Header{

    public String Method,Adress;
    public float HTTP_Version;
    public RequestHeader(InputStream in) throws NullPointerException, IOException {
        super(in);
    }
    
    @Override
    protected void getFirstLine(String firstline) throws NullPointerException{
        String[] tab = firstline.split(" ",3);
        if(tab.length>=3){
            Method = tab[0];
            Adress = tab[1];
            //changes: "http://###.com/***" into -----> "/***"
            try{
                Adress = "/"+Adress.split("/",4)[3];
            }catch(Exception e){
                throw new NullPointerException();
            }
            HTTP_Version = Float.parseFloat(tab[2].split("/")[1]);
        }else{
            throw new Error("Error read RequestHeader - firstline");
        }
    }

    @Override
    protected String getFirstLine() {
        return Method+" "+Adress+" HTTP/"+HTTP_Version;
    }
}
