import java.util.ArrayList;

public class MySimulator{
  public ArrayList<AccessPoint> aps;
  public ArrayList<Client> devices;
  
  public int room_size = 500;
  
  
  public MySimulator(){
    aps = new ArrayList<AccessPoint>();
    devices = new ArrayList<Client>();
  }
  
  public void updateAll(){
    double client_sat = 0;
    for(Client c : devices){
      c.update();
      client_sat += c.sat;
    }
    client_sat = client_sat / devices.size();
    
    double ap_demand = 0;
    for(AccessPoint ap : aps){
      ap.update();
      ap_demand += ap.demand * 1.0 / ap.bandwidth;
    }
    ap_demand = ap_demand / aps.size();
    
    System.out.println("Avg. AccessPoint Demand: " + ap_demand + "\nAvg. Client Satisfaction: " + client_sat);
  }
  public static void main(String[] args){
    MySimulator god = new MySimulator();
    
    for(int i = 0; i < 10; i++){
      god.aps.add(new AccessPoint(god));
    }
    
    for(int i = 0; i < 200; i++){
      god.devices.add(new Client(god));
    }
    god.updateAll();
  }
}