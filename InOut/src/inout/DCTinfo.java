/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inout;

import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.Vector;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author reena
 */
public class DCTinfo 
{
    public static void main(String arg[])
    {
        new DCTinfo();
    }
    DCTinfo()
    {
         JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int tmp = jfc.showOpenDialog(null);
        if(tmp == JFileChooser.APPROVE_OPTION)
        {
            File file = jfc.getSelectedFile();
            System.out.println(file);           
        
        
            PlanarImage rgbImage = JAI.create("fileload",file+"");
            
            int rgbWidth = rgbImage.getWidth();
            int rgbHeight = rgbImage.getHeight();
            ParameterBlock pbDCT = (new ParameterBlock()).addSource(rgbImage);
            PlanarImage dct = JAI.create("dct", pbDCT, null);
            double[] dctData = dct.getData().getPixels(0, 0, rgbWidth,rgbHeight,(double[]) null);
            
           
           Vector v = new Vector();
             
           double av = 0;
           for(int i=0; i < dctData.length; i++)
            {
                v.add(i+" "+dctData[i]); 
                av += dctData[i];
            }
            JOptionPane.showMessageDialog(null, "Avg: "+av);
            JFrame f = new JFrame();
            JList l = new JList(v);    
            JScrollPane pane =new JScrollPane(l);
            f.setTitle("DCT information");
            f.setVisible(true);
            f.setSize(300,300);
            f.setLocation(200,200);
            f.add(pane);
            f.setVisible(true);
            
       }        
    }
}
