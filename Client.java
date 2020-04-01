public class Client{
  public double x;
  public double y;
  public AccessPoint connectTo;
  public int baseData;
  public double sat;
  
  protected MySimulator god;
  
  public Client(MySimulator god){
    this.god = god;
    connectTo = null;
    x = Utils.rand(god.room_size);
    y = Utils.rand(god.room_size);
    baseData = (int)(Utils.rand(50)) + 1;
  }
  
  //connect to ap if not connected, or reconnect when the connected one too low, then use data
  public void update(){
    if(connectTo == null){
      connectAP();
    }else{
      if(connectTo.getRssi(x, y) < -100){
        connectAP();
      }
    }
    sat = 0;
    if(connectTo != null){
      useData();
    }
  }
  
  //connect to ap if rssi is greater than -100dbm
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
    sat = 0;
    if(connectTo != null){
      int d = (int)(Utils.dither(baseData, 0.5));
      sat = connectTo.requestData(d, x, y) * 1.0 / d;
      sat = d == 0 ? 0 : sat;
    }
  }
}