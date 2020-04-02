package iaplbuwl;

public class Client{
  public double x;
  public double y;
  public double target_x;
  public double target_y;
  public AccessPoint connectTo;
  public int baseData;
  public double sat;
  
  protected MySimulator god;
  
  public Client(MySimulator god){
    this.god = god;
    connectTo = null;
    x = Utils.rand(god.room_size);
    y = Utils.rand(god.room_size);
    target_x = Utils.rand(god.room_size);
    target_y = Utils.rand(god.room_size);
    baseData = (int)(Utils.rand(50000)) + 1;
  }
  
  //connect to ap if not connected, or reconnect when the connected one too low, then use data
  public void preUpdate(){
    for(AccessPoint ap : god.aps){
      ap.requestRssi(this);
    }
  }
  public void update(){
    if(connectTo == null){
      connectAP();
    }else{
      if(connectTo.getRssi(this) < -100){
        connectAP();
      }
    }
    //connectAP();
    sat = 0;
    if(connectTo != null){
      useData();
    }
    moving();
  }
  
  //connect to ap if rssi is greater than -100dbm
  public void connectAP(){
    AccessPoint max = god.aps.get(0);
    double rssi = -1000;
    for(AccessPoint ap : god.aps){
      if(ap.getRssi(this) > rssi){
        max = ap;
        rssi = ap.getRssi(this);
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
      int d = (int)(Utils.dither(baseData, 0.2));
      sat = connectTo.requestData(d, this) * 1.0 / d;
      sat = d == 0 ? 0 : sat;
    }
  }
  
  //people will move to his/her target location, smaill chance to change target
  public void moving(){
    if(Utils.rand(100) < 1){
      target_x = Utils.rand(god.room_size);
      target_y = Utils.rand(god.room_size);
    }
    double dx = Math.signum(target_x - x) * Utils.dither(2.4, 0.5); // avg walk speed is 1.4m/s
    double dy = Math.signum(target_y - y) * Utils.dither(2.4, 0.5);
    
    x += dx;
    y += dy;
  }
  
}