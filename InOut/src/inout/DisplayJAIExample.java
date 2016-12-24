/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inout;

/**
 *
 * @author reena
 */
import java.awt.BorderLayout;
import java.awt.Container;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import com.sun.media.jai.widget.DisplayJAI;

/**
 * This application shows how one can use the DisplayJAI class with a
 * JScrollPane to display images.
 */
public class DisplayJAIExample
  {
  public static void main(String[] args)
    {
    // Load the image which file name was passed as the first argument to the
    // application.
      String str="c:\\33.jpg";
    PlanarImage image = JAI.create("fileload", str);
    // Get some information about the image
    String imageInfo = "Dimensions: "+image.getWidth()+"x"+image.getHeight()+
                       " Bands:"+image.getNumBands();
    // Create a frame for display.
    JFrame frame = new JFrame();
    frame.setTitle("DisplayJAI: "+str);
    
    // Get the JFrame's ContentPane.
    Container contentPane = frame.getContentPane();
    contentPane.setLayout(new BorderLayout());
    // Create an instance of DisplayJAI.
    DisplayJAI dj = new DisplayJAI(image);
    // Add to the JFrame's ContentPane an instance of JScrollPane containing the
    // DisplayJAI instance.
    contentPane.add(new JScrollPane(dj),BorderLayout.CENTER);
    // Add a text label with the image information.
    contentPane.add(new JLabel(imageInfo),BorderLayout.SOUTH);
    // Set the closing operation so the application is finished.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400,400); // adjust the frame size.
    frame.setVisible(true); // show the frame.
    }

  }