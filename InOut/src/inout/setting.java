package inout;




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.JMenu.*;
import javax.swing.KeyStroke.*;
import org.jdesktop.application.Application;



 public class setting  extends JFrame implements ActionListener
   {
    JDesktopPane jdp;
	static JPanel panel;
	static JFrame fr;
	JLabel lid,lpassword;
	JTextField tid;
        JPasswordField tpassword;
	JButton add,cancle;
	ResultSet rs;
        String id;
	String password;
        static int flag=0;
       // Password password;
	public static void  main(String arg[])
	{
		 setting lo=new setting();
	}
	
	public setting()
	  {
	  	super("Login");
	  	jdp = new JDesktopPane();
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds((int)d.width/4,(int)d.height/4,(int)d.width/2,(int)d.height/2);
		
		panel=new JPanel();
		panel.setLayout(null);
				
		lid=new JLabel("adminId");
		lpassword=new JLabel("Password");
		
		lid.setFont(new java.awt.Font("Vardana", 1, 12));
		lpassword.setFont(new java.awt.Font("Vardana", 1, 12));
		
		tid=new JTextField(30);
		tpassword=new JPasswordField(30);
		
		add=new JButton("ok");
		cancle=new JButton("Cancle");
		
	
		
		
		lid.setBounds(50,30,90,20);
		lpassword.setBounds(50,70,90,20);
		
		
		tid.setBounds(150,30,100,20);
		tpassword.setBounds(150,70,100,20);
		
		
		add=new JButton("Login");
		cancle=new JButton("Cancle");
		
			add.setMnemonic('A');
		cancle.setMnemonic('C');
		
		add.setBounds(50,120,80,25);
		cancle.setBounds(150,120,80,25);
	
		
		panel.add(lid);
		panel.add(lpassword);
		
		
		panel.add(tid);
		panel.add(tpassword);
		
		panel.add(add);
		panel.add(cancle);
		
				
		
		getContentPane().add(panel);
		setResizable(false);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		//this.setClosable(true);
		
		setSize(300,200);
		setVisible(true);
		
			
		add.addActionListener(this);
		cancle.addActionListener(this);
	}
	public void actionPerformed(ActionEvent av)	
	{
		    id=tid.getText().trim();
		     password=tpassword.getText().trim();
                    
					
	
	if(av.getActionCommand().equals("Cancle"))
			   {
			   	
			  dispose();
			}
   if(av.getActionCommand().equals("Login"))
			{
				
			 String aid=id;
                        String apass=password;
			  dispose(); 	
	        try
				{
				if(tid.getText().length()==0)
				 {
			     JOptionPane.showMessageDialog(null,"Enter the adminid");
			     System.out.println("rrr");
				 }
				else
			     {
			      
                 if(aid.equals("user")&& apass.equals("user"))
                    {
                        System.out.println("WELCOME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        flag=1;
                         dispose();   
                        InOutApp app=new InOutApp();
                      InOutApp.getApplication().getMainView().getResourceMap().getParent();
                       Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane()); 
                       String args[] = null;
                        InOutApp.launch(InOutApp.class, args);
                       
                        
                            
             	             //setVisible(false);
                      
             	      
                       }
                      else if(aid.equals("admin") && apass.equals("admin")) 
                      {
                      flag=2;
                        dispose();  
                       InOutApp app=new InOutApp();
                      InOutApp.getApplication().getMainView().getResourceMap().getParent();
                       Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane()); 
                       String args[] = null;
                        InOutApp.launch(InOutApp.class, args);
                        
                       }
                      else
                      {
                      JOptionPane.showMessageDialog(null,"Enter correct username or password");
                        tid.setText("");
					    tpassword.setText("");
                                            tid.grabFocus();
                                            
                      }
                   } 
					       
                }
				catch(Exception e)
					{
						System.out.println("erroe"+e);
					}
                         
                         	      
			}
			
                       
					
   }
}
					