package server;

public class ContentChecker {

    private String[] words = new String[]{"Sponge Bob", "Britney Spears", "Paris Hilton", "Norrk%C3%B6ping"};

    public ContentChecker() {
    }
    
    public ContentChecker(String[] words) {
        this.words = words;
    }
    
    /** Check string to find inappropriate contains.
     *  @param content Content to check
     *  @param delimiters Delimiters used to split string into words
     *  @return 
     *  <ul>
     *    <li> true - if string contain inappropriate contains </li>
     *    <li> false - if string is save</li>
     *  </ul>
     */
    public boolean checkContent(String content, String delimiters) {
        for(int i=0;i<words.length;i++){
            if(!checkWord(content, words[i], delimiters))
                return false;
        }
        return true;
    }
    /** Check byte array to find inappropriate contains.
     *  @param bufor The bytes to be decoded into characters offset
     *  @param off The index of the first byte to decode length
     *  @param len The number of bytes to decode
     *  @param delimiters Delimiters used to split string into words
     *  @return 
     *  <ul>
     *    <li> true - if string contain inappropriate contains </li>
     *    <li> false - if string is save</li>
     *  </ul>
     */    
    public boolean checkContent(byte[] bufor,int off,int len,String delimiters){
        if(!checkContent(new String(bufor, off, len), delimiters)){
            return false;
        }
        return true;
    }
    
    /** Check single word to find undesirable content.
     *  @param content Content to check
     *  @param word_to_check Word to be compared with content.
     *  @param delimiters Delimiters used to split string into words
     *  @return 
     *  <ul>
     *    <li> true - if string contain inappropriate contains </li>
     *    <li> false - if string is save</li>
     *  </ul>
     */    
    private boolean checkWord(String content, String word_to_check, String delimiters) {
        for(int i=0;i<delimiters.length();i++){
            content = content.replace(delimiters.substring(i, i+1),"");
            word_to_check = word_to_check.replace(delimiters.substring(i, i+1),"");
        }
        content=content.toLowerCase();
        word_to_check=word_to_check.toLowerCase();
        if(content.contains(word_to_check)){
            return false;
        }
        return true;
    }

}
