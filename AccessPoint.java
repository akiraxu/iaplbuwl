import java.lang.Math;

public class AccessPoint{
  protected int x;
  protected int y;
  protected MySimulator god;
  protected int bandwidth; //max
  protected int throughput; //curr
  
  public AccessPoint(MySimulator god){
    this.god = god;
  }
  
  public double getRssi(int cx, int cy){
    //distance = 10^((27.55-(20*log10(freq))+signalLevel)/20)
    double d = Utils.dither(Math.sqrt(Math.pow(x - cx, 2) + Math.pow(x - cx, 2)), 0.5);
    double rssi = Utils.distance2rssi(d);
    
    return 0 - rssi;
  }
  
  public int requestData(int request, int x, int y){
    
    double rssi = getRssi(x, y);
    
    double mbps = Utils.rssi2mbps(rssi);
    
    int use = (int)Math.floor(request < mbps ? request : mbps);
    
    if(throughput <= 0){
      return 0;
    }
    throughput -= use;
    return throughput > 0 ? use : use + throughput;
  }
}