package iaplbuwl;

import java.util.Random;
import java.lang.Math;
import java.util.Date;

public class Utils{
  
  public static boolean newMethod = true;
  
  public static boolean naive = false;
  
  public static Random rand = new Random(new Date().getTime());
  
  public static double rssi2mbps(double rssi){
    return 1000 * (11.63 * rssi + 1246) / 20; //http://morse.colorado.edu/~tlen5510/text/classwebch10.html
  }
  
  public static double mbps2rssi(double mbps){
    return ((mbps * 20 / 1000) - 1246) / 11.63;
  }
  
  public static double distance2rssi(double d){
    return 0 - (Math.log10(d) * 20 - 27.55 + 20 * Math.log10(5000));
  }
  
  public static double rssi2distance(double rssi){
    return Math.pow(10, ((0 - rssi) - 20 * Math.log10(5000) + 27.55) / 20);
  }
  
  public static double dither(double n, double d){
    return n * (1 + (rand.nextGaussian() - 0.5) * d);
  }
  
  public static double rand(double max){
    return rand.nextDouble() * max;
  }
}