import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;

public class MySimulator{
  public ArrayList<AccessPoint> aps;
  public ArrayList<Client> devices;
  
  public int room_size = 1000;
  
  
  public MySimulator(){
    aps = new ArrayList<AccessPoint>();
    devices = new ArrayList<Client>();
  }
  
  public void updateAll(PrintWriter p){
    double client_sat = 0;
    for(Client c : devices){
      c.update();
      client_sat += c.sat;
    }
    client_sat = client_sat / devices.size();
    
    double ap_demand = 0;
    for(AccessPoint ap : aps){
      ap.update();
      ap_demand += ap.prev_demand * 1.0 / ap.bandwidth;
    }
    ap_demand = ap_demand / aps.size();
    
    //System.out.println("Avg. AccessPoint Demand: " + ap_demand + "\nAvg. Client Satisfaction: " + client_sat);
    p.println(ap_demand + "," + client_sat);
  }
  public static void main(String[] args){
    MySimulator god = new MySimulator();
    
    for(int i = 0; i < 50; i++){
      god.aps.add(new AccessPoint(god));
    }
    
    for(int i = 0; i < 1000; i++){
      god.devices.add(new Client(god));
    }
    try{
      FileWriter fileWriter = new FileWriter("out1.csv");
      PrintWriter printWriter = new PrintWriter(fileWriter);
      for(int i = 0; i < 2000; i++){
        god.updateAll(printWriter);
      }
      printWriter.close();
    }catch(Exception e){
    }
  }
}
