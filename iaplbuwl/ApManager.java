package iaplbuwl;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class ApManager{
  HashMap<Client, HashMap<AccessPoint, Double>> db;
  HashMap<Client, Double> db2;
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
    
    if(Utils.newMethod){
      ap = bestSuitedAp3(c, db.get(c));
      if(ap == null){
        return -1000;
      }
      rssi = db.get(c).get(ap);
    }else{
      for(Map.Entry<AccessPoint, Double> pair : db.get(c).entrySet()){
        if(pair.getValue() > rssi){
          ap = pair.getKey();
          rssi = pair.getValue();
        }
      }
    }
    return x == ap ? rssi : -1000;
  }
  public AccessPoint bestSuitedAp1(Client c, HashMap<AccessPoint, Double> m){
    double needs = c.baseData;
    List<AccessPoint> ls = new ArrayList<AccessPoint>();
    ls.addAll(m.keySet());
    //ls.sort((AccessPoint ap1, AccessPoint ap2) -> (int)(Math.abs(m.get(ap1) - Utils.mbps2rssi(needs)) - Math.abs(m.get(ap2) - Utils.mbps2rssi(needs))));
    ls.sort((AccessPoint ap1, AccessPoint ap2) -> (int)(Math.abs(m.get(ap2) - Utils.mbps2rssi(needs))) - (int)(Math.abs(m.get(ap1) - Utils.mbps2rssi(needs))));
    AccessPoint ap = ls.get(0);
    double min = ap.throughput / ap.bandwidth;
    for(int i = 0; i < ls.size(); i++){
      double curr = ls.get(i).throughput / ls.get(i).bandwidth;
      if(curr < min){
        min = curr;
        ap = ls.get(i);
      }
      if(ls.get(i).throughput + needs <= ls.get(i).bandwidth){
        return ls.get(i);
      }
    }
    return ap;
  }
  public AccessPoint bestSuitedAp2(Client c, HashMap<AccessPoint, Double> m){
    double needs = c.baseData;
    List<AccessPoint> ls = new ArrayList<AccessPoint>();
    ls.addAll(m.keySet());
    //ls.sort((AccessPoint ap1, AccessPoint ap2) -> (int)(Math.abs(m.get(ap1) - Utils.mbps2rssi(needs)) - Math.abs(m.get(ap2) - Utils.mbps2rssi(needs))));
    ls.sort((AccessPoint ap1, AccessPoint ap2) -> ap1.throughput - ap2.throughput);
    AccessPoint ap = ls.get(0);
    double min = Math.abs(m.get(ap) - Utils.mbps2rssi(needs));
    for(int i = 0; i < ls.size(); i++){
      double curr = Math.abs(m.get(ls.get(i)) - Utils.mbps2rssi(needs));
      if(curr < min){
        min = curr;
        ap = ls.get(i);
      }
      if(Math.abs(m.get(ls.get(i)) - Utils.mbps2rssi(needs)) < 10){
        return ls.get(i);
      }
    }
    return ap;
  }
  public AccessPoint bestSuitedAp3(Client c, HashMap<AccessPoint, Double> m){
    double needs = Utils.mbps2rssi(c.baseData);
    ArrayList<AccessPoint> ls = new ArrayList<AccessPoint>();
    
    for(Map.Entry<AccessPoint, Double> pair : db.get(c).entrySet()){
      if(pair.getValue() > needs - 5){
        ls.add(pair.getKey());
      }
    }
    if(ls.size() == 0){
      ls = new ArrayList<AccessPoint>();
      for(Map.Entry<AccessPoint, Double> pair : db.get(c).entrySet()){
        if(pair.getValue() > 100){
          ls.add(pair.getKey());
        }
      }
    }
    AccessPoint out = null;
    double load = 100;
    for(AccessPoint ap : ls){
      double curr = 1.0 * ap.prev_demand / ap.bandwidth;
      if(curr < load){
        out = ap;
        load = curr;
      }
    }
    return out;
  }
}