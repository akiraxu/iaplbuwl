package iaplbuwl;

import java.util.ArrayList;
import java.util.Collections;
import java.io.FileWriter;
import java.io.PrintWriter;

public class MySimulator{
  public ArrayList<AccessPoint> aps;
  public ArrayList<Client> devices;
  public ApManager manager;
  
  public int room_size = 1000;
  
  
  public MySimulator(){
    aps = new ArrayList<AccessPoint>();
    devices = new ArrayList<Client>();
    manager = new ApManager();
  }
  
  public void updateAll(PrintWriter p){
    
    Collections.shuffle(devices);
    
    for(Client c : devices){
      //c.preUpdate();
    }
    
    for(Client c : devices){
      c.update();
    }
    
    double client_sat = 0;
    for(Client c : devices){
      c.postUpdate();
      client_sat += c.sat;
    }
    client_sat = client_sat / devices.size();
    
    double ap_demand = 0;
    double max = 0;
    ArrayList<Double> loads = new ArrayList<Double>();
    
    for(AccessPoint ap : aps){
      ap.update();
      double load = ap.prev_demand * 1.0 / ap.bandwidth;
      loads.add(load);
      ap_demand += load;
      max = load > max ? load : max;
    }
    ap_demand = ap_demand / aps.size();
    
    double sd = 0;
    for(Double i : loads){
      sd += Math.pow(i - ap_demand, 2);
    }
    sd = Math.sqrt(sd / aps.size());
    
    //System.out.println("Avg. AccessPoint Demand: " + ap_demand + "\nAvg. Client Satisfaction: " + client_sat);
    p.println(ap_demand + "," + client_sat + "," + sd);
  }
  
  public static void oneRound(PrintWriter p){
    MySimulator god = new MySimulator();
    
    for(int i = 0; i < 50; i++){
      god.aps.add(new AccessPoint(god));
    }
    
    for(int i = 0; i < 5000; i++){
      god.devices.add(new Client(god));
    }
    for(int i = 0; i < 100; i++){
      god.updateAll(p);
    }
  }
  
  public static void main(String[] args){
    try{
      FileWriter fileWriter = new FileWriter("out1.csv");
      PrintWriter printWriter = new PrintWriter(fileWriter);
      for(int i = 0; i < 20; i++){
        oneRound(printWriter);
      }
      printWriter.close();
    }catch(Exception e){
      e.printStackTrace(System.out);
    }
  }
}