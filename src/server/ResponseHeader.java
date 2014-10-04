/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author piotr-admin
 */
public class ResponseHeader extends Header{
    public float HTTP_Version;
    public int Status_Code;
    public String Status_Message;
    public ResponseHeader(InputStream in) throws IOException,NullPointerException{
        super(in);
    }

    
    @Override
    protected void getFirstLine(String firstline) throws NullPointerException{                
        String[] tab = firstline.split(" ",3);        
        if(tab.length>=3){
            HTTP_Version = Float.parseFloat(tab[0].split("/")[1]);
            Status_Code = Integer.parseInt(tab[1]);
            Status_Message = tab[2];
        }else{            
            System.out.println(firstline);
            throw new Error("Error read ResponsetHeader - firstline");
        }
        
    }

    @Override
    protected String getFirstLine() {
        return "HTTP/"+HTTP_Version+" "+Status_Code+" "+Status_Message;
    }
    
}
