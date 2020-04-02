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
  
  public double getRssi(double cx, double cy){
    //distance = 10^((27.55-(20*log10(freq))+signalLevel)/20)
    double d = Utils.dither(Math.sqrt(Math.pow(x - cx, 2) + Math.pow(x - cx, 2)), 0.05);
    double rssi = Utils.distance2rssi(d);
    
    return 0 - rssi;
  }
  
  //process request and return actual data given
  public int requestData(int request, double x, double y){
    
    double rssi = getRssi(x, y);
    
    double mbps = Utils.rssi2mbps(rssi);
    
    int use = (int)Math.floor(request < mbps ? request : mbps);
    
    demand += use;
    
    if(throughput <= 0){
      return 0;
    }
    throughput -= use;
    return throughput > 0 ? use : use + throughput;
  }
}
