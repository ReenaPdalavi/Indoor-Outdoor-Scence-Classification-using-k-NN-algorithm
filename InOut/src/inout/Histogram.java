
package inout;
//package operators;

import java.awt.GridLayout;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;


/**
 * This class demonstrates the usage of the histogram operator.
 */
public class Histogram 
  {
 /**
  * The entry point for the application.
  * @param args
  */
  //public static void main(String[] args)
    
/*    public Histogram ()
    {
        
    }*/
    
    public  Histogram(String path)
    {
    // Read the image.
    //PlanarImage image = JAI.create("fileload", "c:\\44.bmp");
    PlanarImage image = JAI.create("fileload", path);
    double dd[]={120,170,20,30};
     int[] bins = {256, 256, 256};             // The number of bins.
     double[] low = {0.0D, 0.0D, 0.0D};        // The low value.
     double[] high = {256.0D, 256.0D, 256.0D}; // The high value.


//-----------------------------------------------------------------------------------------    
    // Create one histogram with 256 bins.  
   //    Histogram hist= new Histogram(bins,low,high);
    ParameterBlock pb1 = new ParameterBlock();
    pb1.addSource(image);
    pb1.add(null); // The ROI
    pb1.add(1); pb1.add(1); // Sampling
    pb1.add(new int[]{256}); // Bins
    
    pb1.add(new double[]{0});  pb1.add(new double[]{256}); // Range for inclusion
    //pb1.add(dd);
   
     PlanarImage dummyImage1 = JAI.create("histogram", pb1);
    // Gets the histogram.
     
    javax.media.jai.Histogram histo1 = (javax.media.jai.Histogram)dummyImage1.getProperty("histogram");
    dummyImage1.setProperty("CCCC", histo1);
    // Create one histogram with 64 bins.  


//-----------------------------------------------------------------------------------------        
    ParameterBlock pb2 = new ParameterBlock();
    pb2.addSource(image);
    pb2.add(null); // The ROI
    pb2.add(1); pb2.add(1); // Sampling
    pb2.add(new int[]{64}); // Bins
    pb2.add(new double[]{0});  pb2.add(new double[]{256}); // Range for inclusion    
    PlanarImage dummyImage2 = JAI.create("histogram", pb2);
    // Gets the histogram.
    javax.media.jai.Histogram histo2 = (javax.media.jai.Histogram)dummyImage2.getProperty("histogram");
    // Create one histogram with 8 bins.  


//-----------------------------------------------------------------------------------------    
    ParameterBlock pb3 = new ParameterBlock();
    pb3.addSource(image);
    pb3.add(null); // The ROI
    pb3.add(1); pb3.add(1); // Sampling
    pb3.add(new int[]{8}); // Bins
    pb3.add(new double[]{0});  pb3.add(new double[]{256}); // Range for inclusion
    PlanarImage dummyImage3 = JAI.create("histogram", pb3);
    // Gets the histogram.
    javax.media.jai.Histogram histo3 = (javax.media.jai.Histogram)dummyImage3.getProperty("histogram");




    // Show those histograms in a GUI application. Set some parameters on the 
    // DisplayHistogram components to adjust the 
    JFrame f = new JFrame("Histograms");
    DisplayHistogram dh1 = new DisplayHistogram(histo1,"256 bins");
   
    dh1.setBinWidth(2); 
    dh1.setHeight(160); 
    dh1.setIndexMultiplier(1);
    
    DisplayHistogram dh2 = new DisplayHistogram(histo2,"64 bins");
    dh2.setBinWidth(8); 
    dh2.setHeight(160); 
    dh2.setIndexMultiplier(4); 
    dh2.setSkipIndexes(2);
    
    DisplayHistogram dh3 = new DisplayHistogram(histo3,"8 bins");
    dh3.setBinWidth(64); 
    dh3.setHeight(160); 
    dh3.setIndexMultiplier(32); 
    dh3.setSkipIndexes(1);
 
    
    f.getContentPane().setLayout(new GridLayout(1,1));
    f.getContentPane().add(dh1);
  //  f.getContentPane().add(dh2);
  //  f.getContentPane().add(dh3);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.pack();
    f.setLocation(200, 150);
    f.setVisible(true);
    }

    private Histogram(int[] bins, double[] low, double[] high) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
  }
