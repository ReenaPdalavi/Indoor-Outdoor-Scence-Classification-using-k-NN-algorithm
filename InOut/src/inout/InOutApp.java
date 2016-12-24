/*
 * InOutApp.java
 */

package inout;
import javax.swing.JDesktopPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


/**
 * The main class of the application.
 */
public class InOutApp extends SingleFrameApplication {
    /**
     * At startup create and show the main frame of the application.
     */
    String name="Indoor Outdoor";
    @Override protected void startup() {
        
        show(new InOutView(this));
    //    this.getApplication().name=name;
      
       // show(new Login());
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
       
        //root.setBounds(10,10, 400, 400);
        root.setSize(400, 400);
        
        
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of InOutApp
     */
    public static InOutApp getApplication() {
        
        return Application.getInstance(InOutApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        args[0]="HHHHH";
        launch(InOutApp.class, args);
    }
}
