package iaplbuwl;

import java.lang.Math;

public class AccessPoint{
  public double x;
  public double y;
  public MySimulator god;
  public int bandwidth; //max
  public int throughput; //curr
  public int demand; //actual requested;
  public int prev_demand;
  
  public AccessPoint(MySimulator god){
    this.god = god;
    bandwidth = 500000;
    throughput = bandwidth;
    x = Utils.rand(god.room_size);
    y = Utils.rand(god.room_size);
  }
  
  public void update(){
    throughput = bandwidth;
    prev_demand = demand;
    demand = 0;
  }
  
  //get rssi and update entry in ap manager
  public void requestRssi(Client c){
    //distance = 10^((27.55-(20*log10(freq))+signalLevel)/20)
    double d = Utils.dither(Math.sqrt(Math.pow(x - c.x, 2) + Math.pow(x - c.x, 2)), 0.25);
    double rssi = (demand < 0 && !Utils.newMethod) ? 0 : Utils.distance2rssi(d);
    god.manager.addEntry(c, this, rssi);
  }
  
  public double getRssi(Client c){
    if(Utils.naive){
      return Utils.distance2rssi(Utils.dither(Math.sqrt(Math.pow(x - c.x, 2) + Math.pow(x - c.x, 2)), 0.25));
    }
    requestRssi(c);
    return god.manager.apRssiFor(this, c);
  }
  
  //process request, used to estimate demand and average out the use
  public int requestData(int request, Client c){
    
    double rssi = getRssi(c);
    
    if(rssi < -100){
      return 0;
    }
    
    double mbps = Utils.rssi2mbps(rssi);
    
    int use = (int)Math.floor(request < mbps ? request : mbps);
    
    demand += use;
    
    if(throughput <= 0){
      return 0;
    }
    return use;
  }
  
  //process the data transmision and return actual given
  public int useData(int request, Client c){
    
    double rssi = getRssi(c);
    
    if(rssi < -100){
      return 0;
    }
    
    double mbps = Utils.rssi2mbps(rssi);
    
    int use = (int)(Math.floor(request < mbps ? request : mbps) * (bandwidth > demand ? 1 : bandwidth / demand));
    
    if(throughput <= 0){
      return 0;
    }
    throughput -= use;
    return throughput > 0 ? use : use + throughput;
  }
  
}