/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inout;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.JAI.*;
import javax.media.jai.PlanarImage;
import javax.swing.JOptionPane;

/**
 *
 * @author reena
 */
public class Checkfeature {
public Checkfeature(int b1,int b2,int c1,int c2)
{
       // FileOutputStream fout = null;
        try {
            int diff = b1 - b2;
            File fname = Features.file;
            FileInputStream fin = new FileInputStream(fname);
            String name=fname.toString();
            System.out.println("FileNAme::"+name+"=======================");
             RenderedImage p1 = JAI.create("fileload",fname.toString());
             File ff=new File("OutDoor");
             ff.mkdir();
             File ff2=new File("InDoor");
             ff2.mkdir();
            System.out.println("CONstructor Calling------------------------" + diff);
            if (diff == 0) {
                JOptionPane.showMessageDialog(null, "INDOOR!!!!");
                 FileOutputStream fout=new FileOutputStream(ff2+"/"+fname.getName());
                    ImageIO.write(p1, "png", fout);
              ///  File foutd=new File("C:/Documents and Settings/nilesh/My Documents/NetBeansProjects/InOut");
              //  System.out.println("FILE PATH:::"+foutd.getPath());
               // foutd.mkdir();
                //ImageIO.write(p1, "png", new File(""+name+".png"));
            } else if (diff > 100 || b2==c2) {
                JOptionPane.showMessageDialog(null, "OUTDOOR!!!!");
                 FileOutputStream fout=new FileOutputStream(ff+"/"+fname.getName());
                    ImageIO.write(p1, "png", fout);
                //ImageIO.write(p1, "png", new File(""+name));
            } else if (b1 >= b2 && c1 > c2) {
                //JOptionPane.showMessageDialog(null,"OUTDOOR");
                System.out.println("LOOP1------------------------------------");
                int f = 0;
                f = checkDevPro(name);
                if (f == 2) {
                    JOptionPane.showMessageDialog(null, "OUTDOOR!!!!!");
                    FileOutputStream fout=new FileOutputStream(ff+"/"+fname.getName());
                    ImageIO.write(p1, "png", fout);
                   
                } else if (f == 1) {
                    JOptionPane.showMessageDialog(null, "INDOOR!!!!");
                     FileOutputStream fout=new FileOutputStream(ff2+"/"+fname.getName());
                    ImageIO.write(p1, "png", fout);
                } else {
                    JOptionPane.showMessageDialog(null, "NOT Recognized!!!!");
                }
            } else {
                int f = 0;
                System.out.println("LOOP2-----------------------");
                f = checkDevPro(name);
                if (f == 1) {
                    JOptionPane.showMessageDialog(null, "INDOOR!!!!!");
                      FileOutputStream fout=new FileOutputStream(ff2+"/"+fname.getName());
                    ImageIO.write(p1, "png", fout);
                } else {
                    JOptionPane.showMessageDialog(null, "OUTDOOR!!!!");
                    FileOutputStream fout=new FileOutputStream(ff+"/"+fname.getName());
                    ImageIO.write(p1, "png", fout);
                   // ImageIO.write(p1, "png", new File(""+name));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Checkfeature.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           /*try {
               // fout.close();
            } catch (IOException ex) {
                Logger.getLogger(Checkfeature.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
}
public int  checkDevPro(String file)
{
        PlanarImage src = JAI.create("fileload", file+"");
        javax.media.jai.Histogram histogram = (javax.media.jai.Histogram)JAI.create("histogram", src).getProperty("histogram");
        double entropy[] = histogram.getEntropy();
        double maxVarianceTh[] = histogram.getMaxVarianceThreshold();
        double mean[] = histogram.getMean();
        double stdDev[] = histogram.getStandardDeviation();
     // double me[]=histogram.getMoment(10,true,true);
    int flag=0;
      double a,b,c;
      
    for(int j=0; j < stdDev.length; j++)
    { //j=j+1;
        a=stdDev[j];
        
        b=stdDev[j+1];
        c=stdDev[j+2];
        if(a<b)
        {
            flag=2;
            break;
        }
        else if(a<c && a>b )
        {
            flag=2;
            break;
        }
        else if(a>b ||a>c)
        {
            flag=1;
            System.out.println("FLAG111");
            break;
        }
        else
        {
            flag=0;
            break;
        }
    }
      //  JFrame f = new JFrame();
         Vector v= new Vector();
        double en = 0;
        v.add("Entropy");
        for(int i=0; i < entropy.length; i++)
            en+=entropy[i];
             en=en/3;
            v.add(en);
        v.add("----------------");
        v.add("Max Variance Threshold");
        double mth=0;
        for(int i=0; i < maxVarianceTh.length; i++)
            mth+=maxVarianceTh[i];
        mth=mth/3;
            v.add(mth);
        v.add("----------------");
        v.add("Mean");
        double mn=0;
        for(int i=0; i < mean.length; i++)
            mn+=mean[i];
        mn=mn/3;
            v.add(mn);
        v.add("----------------");
        v.add("Standard Deviation");
        for(int i=0; i < stdDev.length; i++)
            v.add(stdDev[i]);
        v.add("----------------");
        System.out.println("Properties of image:::::"+v.toString());
        return flag;
}
}
