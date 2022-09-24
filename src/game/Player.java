package game;

import java.util.ArrayList;

public class Player {
    ArrayList<String> keyBag = new ArrayList<String>();

    public Player(){
        
    }
    
    public void addKey(String key) {
        this.keyBag.add(key);
    }

    public boolean checkKey(String checkKey){
        for (String key : this.keyBag){
            if(checkKey.equals(key)){
                return true;
            }
        }
        return false;
    }
}
