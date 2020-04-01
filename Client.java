public class Client{
  public int x;
  public int y;
  public AccessPoint connectTo;
  public int baseData;
  
  protected MySimulator god;
  
  public Client(MySimulator god){
    this.god = god;
    connectTo = null;
  }
  
  public void update(){
    if(connectTo == null){
      connectAP();
    }else{
      if(connectTo.getRssi(x, y) < -100){
        connectAP();
      }
      if(connectTo != null){
        useData();
      }
      
    }
  }
  
  public void connectAP(){
    AccessPoint max = god.aps.get(0);
    double rssi = -1000;
    for(AccessPoint ap : god.aps){
      if(ap.getRssi(x, y) > rssi){
        max = ap;
        rssi = ap.getRssi(x, y);
      }
    }
    if(rssi > -100){
      connectTo = max;
    }else{
      connectTo = null;
    }
  }
  
  public void useData(){
    if(connectTo != null){
      connectTo.requestData(100, x, y);
    }
  }
}