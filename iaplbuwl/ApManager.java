package iaplbuwl;

import java.util.Map;
import java.util.HashMap;

public class ApManager{
  HashMap<Client, HashMap<AccessPoint, Double>> db;
  public ApManager(){
    db = new HashMap<Client, HashMap<AccessPoint, Double>>();
  }
  public void addEntry(Client c, AccessPoint ap, double rssi){
    HashMap<AccessPoint, Double> ls = db.get(c);
    if(ls == null){
      ls = new HashMap<AccessPoint, Double>();
    }
    ls.put(ap, rssi);
    db.put(c, ls);
  }
  public double apRssiFor(AccessPoint x, Client c){
    AccessPoint ap = null;
    double rssi = -1000;
    for(Map.Entry<AccessPoint, Double> pair : db.get(c).entrySet()){
      if(pair.getValue() > rssi){
        ap = pair.getKey();
        rssi = pair.getValue();
      }
    }
    return x == ap ? rssi : -1000;
  }
}