import java.util.ArrayList;

public class MySimulator{
  public ArrayList<AccessPoint> aps;
  public ArrayList<Client> devices;
  
  public int room_size = 500;
  
  
  public MySimulator(){
    aps = new ArrayList<AccessPoint>();
    devices = new ArrayList<Client>();
  }
  
  public static void main(String[] args){
    MySimulator god = new MySimulator();
    
    for(int i = 0; i < 10; i++){
      god.aps.add(new AccessPoint(god));
    }
    
    for(int i = 0; i < 100; i++){
      god.devices.add(new Client(god));
    }
  }
}