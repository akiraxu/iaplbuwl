package iaplbuwl;

import java.util.Random;
import java.lang.Math;
import java.util.Date;

public class Utils{
  public static Random rand = new Random(new Date().getTime());
  
  public static double rssi2mbps(double rssi){
    return 1000 * (11.63 * rssi + 1246) / 20; //http://morse.colorado.edu/~tlen5510/text/classwebch10.html
  }
  
  public static double distance2rssi(double d){
    return Math.log10(d) * 20 - 27.55 + 20 * Math.log10(5000);
  }
  
  public static double dither(double n, double d){
    return n * (1 + (rand.nextGaussian() - 0.5) * d);
  }
  
  public static double rand(double max){
    return rand.nextDouble() * max;
  }
}