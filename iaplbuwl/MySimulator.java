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
    
    //not in use
    for(Client c : devices){
      //c.preUpdate();
    }
    
    //device update connection and request data
    for(Client c : devices){
      c.update();
    }
    
    //device use data
    double client_sat = 0;
    for(Client c : devices){
      c.postUpdate();
      client_sat += c.sat;
    }
    client_sat = client_sat / devices.size();
    
    double ap_demand = 0;
    double max = 0;
    ArrayList<Double> loads = new ArrayList<Double>();
    
    //ap reset load
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
    p.println(ap_demand + "," + client_sat + "," + max + "," + sd);
  }
  
  public static void oneRound(PrintWriter p){
    MySimulator god = new MySimulator();
    
    //create 50 random APs
    for(int i = 0; i < 50; i++){
      god.aps.add(new AccessPoint(god));
    }
    
    //create 50000 random users
    for(int i = 0; i < 5000; i++){
      god.devices.add(new Client(god));
    }
    
    //run for 100 round
    for(int i = 0; i < (Utils.naive ? 2000 : 200); i++){
      god.updateAll(p);
    }
  }
  
  public static void main(String[] args){
    try{
      //use naive way
      Utils.naive = true;
      FileWriter fileWriter = new FileWriter("naive.csv");
      PrintWriter printWriter = new PrintWriter(fileWriter);
      printWriter.println("ap_load,client_sat,max_ap_load,ap_load_sd");
      for(int i = 0; i < 10; i++){
        oneRound(printWriter);
      }
      
      //use our frist paper method
      Utils.naive = false;
      Utils.newMethod = false;
      fileWriter = new FileWriter("paper.csv");
      printWriter = new PrintWriter(fileWriter);
      printWriter.println("ap_load,client_sat,max_ap_load,ap_load_sd");
      for(int i = 0; i < 20; i++){
        oneRound(printWriter);
      }
      printWriter.close();
      
      //use our improved method
      Utils.newMethod = true;
      fileWriter = new FileWriter("ours.csv");
      printWriter = new PrintWriter(fileWriter);
      printWriter.println("ap_load,client_sat,max_ap_load,ap_load_sd");
      for(int i = 0; i < 20; i++){
        oneRound(printWriter);
      }
      printWriter.close();
    }catch(Exception e){
      e.printStackTrace(System.out);
    }
  }
}