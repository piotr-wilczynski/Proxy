
package server;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public abstract class Header {

    public LinkedHashMap<String,String> fields = new LinkedHashMap<>();    
    public Header(InputStream in) throws IOException,NullPointerException{
        String line;        
        getFirstLine(readLine(in));
        while((line=readLine(in))!=null){
            String[] tab = line.split(":", 2);
            fields.put(tab[0], tab[1].substring(1));
        }
    }
    /** Get main header.
     *  @param firstline String which represent first line of header
     */    
    protected abstract void getFirstLine(String firstline);
    
    /** Gets first line of header
     *  @return Main header 
     */    
    protected abstract  String getFirstLine();
    public List<String> getList(){
        List<String> lista = new ArrayList<>();
        lista.add(getFirstLine());
        Iterator<Entry<String,String>> set=fields.entrySet().iterator();   
        while(set.hasNext()){
            Entry<String,String> i = set.next();
            lista.add(i.getKey()+": "+i.getValue());            
        }        
        return lista;
    }
    
    /** Read one line of input stream.
     *  @param in Input stream which contain header
     *  @return One line of stream.
     */    
    private String readLine(InputStream in) throws IOException{
        String text = "";
        char c;
        int a;
        try{
            while(true){
                if((a=in.read())!=-1){
                    c=(char) a;
                    if(c=='\n'){
                        text+='\n';
                        if(text.length()==2){
                            return null;
                        }else{
                            text=text.substring(0, text.length()-2);
                        }
                        break;
                    }else{
                        text+=c;
                    }
                }else{
                    return null;
                }
            }
        }catch (Exception e){
            throw new NullPointerException();
        }
        if(!text.matches("\r\n")){
            return text;
        }else{
            return null;            
        }
    }
    
    /** Gets value of header.
     *  @param key Header name
     *  @return Value of header with key property 
     */  
    public String get(String key){
        return fields.get(key);
    }
    
    /** Sets value of header.
     *  @param key Header name
     *  @param value Value of header
     */  
    public void put(String key,String value){
        fields.put(key, value);
    }    
    /** Check if header exist
     *  @param key Header name
     *  @return 
     * <ul>
     *   <li> true - if header exist</li>
     *   <li> false -  if header not exist</li>
     * </ul>
     */  
    public boolean containsKey(Object Key){
        return fields.containsKey(Key);
    }
    
    /** Remove header.
     *  @param key Name of header to remove
     */  
    public void remove(String key){
        fields.remove(key);
    }
    
    /** Sends headers to the specified stream.
     *  @param out The stream to which the data will be sent.
     */  
    public void sendHeader(OutputStream out){
        List lista = getList();
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
            for(int i=0;i<lista.size();i++){
                bw.write(lista.get(i)+ "\r\n");                
            }            
            bw.write("\r\n");
            bw.flush();            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
