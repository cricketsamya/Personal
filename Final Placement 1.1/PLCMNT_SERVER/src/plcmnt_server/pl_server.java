/*
 * pl_server.java
 *
 * Created on July 27, 2006, 5:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package plcmnt_server;

/**
 *
 * @author user
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class pl_server
{
    private ServerSocket server;
    private Socket client;
    Connection conn;
    FileWriter LogFile;
    
    public pl_server()
    {
        try
        {
            server = new ServerSocket(2108);
            System.out.println("Server Started");
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conn = DriverManager.getConnection("jdbc:odbc:jdbcOracle","scott","tiger");
            LogFile = new FileWriter("ServerLog.log",true);
        }
        
        catch(Exception e)
        {
            System.out.println("Server start problem");
            JOptionPane.showMessageDialog(null,"Error in establishing connection.\nAnother instance of the application might be running","Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        final TrayIcon trayIcon;
        
        if (SystemTray.isSupported())
        {
            
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("Icon1.gif");
            
            ActionListener exitListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Exiting...");
                    try
                    {
                        LogFile.close();
                    }
                    catch(Exception Exp)
                    {
                        Exp.printStackTrace();
                    }
                    System.exit(0);
                }
            };
            
            ActionListener aboutListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    ImageIcon icon = new ImageIcon("Icon1.gif");
                    
                    JOptionPane.showMessageDialog(null,"This software has been developed by\nUday Pant, Gaurav Raka, " +
                        "Sameer Kulkarni, Indira Pai.\n\n© lies with the above.","About",JOptionPane.INFORMATION_MESSAGE,icon);
                }
            };
            
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            MenuItem aboutItem = new MenuItem("About");
            defaultItem.addActionListener(exitListener);
            aboutItem.addActionListener(aboutListener);
            
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(defaultItem);
            
            trayIcon = new TrayIcon(image, "Placement Server", popup);
            
            ActionListener actionListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    trayIcon.displayMessage("Action Event",
                            "An Action Event Has Been Peformed!",
                            TrayIcon.MessageType.INFO);
                }
            };
            
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            //trayIcon.addMouseListener(mouseListener);
            
            try
            {
                tray.add(trayIcon);
            }
            catch (AWTException e)
            {
                System.err.println("TrayIcon could not be added.");
            }            
        }
        else
        {
            System.err.println("System tray is currently not supported.");
        }
        
        while(true)
        {
            try
            {
                client= server.accept();
                Con_serv_cl cli = new Con_serv_cl(client,conn,LogFile);
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void main(String args[])
    {
        new pl_server();
    }
}

class Con_serv_cl extends Thread
{
    boolean ack=true;
    Socket cl;
    String data;
    private BufferedReader in;
    private PrintStream out;
    Statement stmt;
    Connection conn;
    ResultSet rs;
    int insert_counter=0;
    String del_query="",select_query="";
    FileWriter LogFile = null;
    
    public Con_serv_cl(Socket client, Connection conn, FileWriter Log)
    {
        this.cl = client;
        this.conn = conn;
        this.LogFile = Log;
        try
        {
            in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            out= new PrintStream(cl.getOutputStream());
            String ClientAddr = client.getInetAddress().getHostAddress();
            
            LogFile.write("Server Connected to client " + ClientAddr +". Data transfer started\n");
            System.out.println("Server Connected to client " + ClientAddr +". Data transfer started");
            stmt=conn.createStatement();
        }
        catch(Exception e)
        {
            System.out.println("Server connection error");
        }
        this.start();
    }
    
    public void run()
    {
        String []InsertQuery = new String[3];
        String []del_str=null;
        CallableStatement InsertCStmt = null;
        boolean Proceed = false;

        while(true)
        {
            try
            {                
                data = in.readLine();                    
                String Tempo;
                char Input1[]=new char[data.length()];
                char Output1[]=new char[data.length()];
                Input1=data.toCharArray();
                int KeySize=5;
                for(int z=0;z<data.length();z++)
                {
                    if((Input1[z]>='a'&&Input1[z]<='z')||(Input1[z]>='A'&&Input1[z]<='Z'))
                        Output1[z]=Decrypt(Input1[z],KeySize);
                    else
                        Output1[z]=Input1[z];
                }
                //Tempo=Output1.toString();

                data=new String(Output1);
                System.out.println(data);
                String ackn="true";
                if(data.equalsIgnoreCase("close"))
                {
                    cl.close();
                    LogFile.write("Connection Closed Client : " + cl.getInetAddress().getHostAddress() + "\n");
                    System.out.println("client disconnected");
                    break;
                }

                if(data.startsWith("insert"))
                {
                    String[] Query = data.split(":");
                    int QueryNum = Integer.parseInt(Query[1]);                                        

                    if(QueryNum == 1)
                    {
                        InsertCStmt = conn.prepareCall("{?=call PlacementFunc(?,?,?,?,?)}");
                        InsertCStmt.registerOutParameter(1,Types.INTEGER);
                    }
                    
                    String InsertQueryStr = Query[2];
                    InsertCStmt.setString(QueryNum + 1,InsertQueryStr);                        
                    LogFile.write("Insert query from client : " + cl.getInetAddress().getHostAddress() + " : " + InsertQueryStr + "\n");
                    if(QueryNum == 3)
                    {                        
                        InsertCStmt.setString(5,del_str[1]);
                        InsertCStmt.setString(6,del_str[2]);
                        
                        InsertCStmt.execute();
                        int Result=InsertCStmt.getInt(1);                                                
                        
                        if(Result == 1)
                        {
                            out.println("true");
                            disp d1 = new disp(del_str[1]);                            
                        }
                        else
                            out.println("false");                        
                    }
                }

                if(data.startsWith("delete"))
                {
                    del_str=data.split(":");                    
                    del_query="delete from stud_per where roll_no='"+del_str[1]+"' and sess='"+del_str[2]+"'";
                    LogFile.write("Delete query from client : " + cl.getInetAddress().getHostAddress() + " : " + del_query + "\n");
                    System.out.println("Delete query is " + del_query);
                }

                if(data.startsWith("select"))
                {
                    String []select_str=data.split(":");
                    LogFile.write("Select query from client : " + cl.getInetAddress().getHostAddress() + " for roll no : " + select_str[1] + " and Session : " + select_str[2] + "\n");

                    select_query="select * from stud_per where roll_no='"+select_str[1]+"' and sess="+select_str[2];
                    Statement Newstmt = conn.createStatement();                    
                    rs=Newstmt.executeQuery(select_query);
                    rs.next();
                    for(int i=3;i<=12;i++)
                    {
                        String temp1=rs.getString(i);
                        //    System.out.println(temp1);
                        out.println(temp1);

                    }

                    out.println(Integer.toString(rs.getInt(13)));

                    select_query="select * from stud_acad where roll_no='"+select_str[1]+"' and sess="+select_str[2];
                    rs=stmt.executeQuery(select_query);
                    rs.next();

                    for(int i=3;i<=15;i++)
                    {
                        int tt=rs.getInt(i);                            
                        out.println(Integer.toString(tt));
                    }

                    select_query="select * from stud_extra where roll_no='"+select_str[1]+"' and sess="+select_str[2];
                    rs=stmt.executeQuery(select_query);
                    rs.next();
                    for(int i=3;i<=5;i++)
                    {
                        //System.out.println(rs.getString(i));
                        out.println(rs.getString(i));                            
                    }
                }
            }
            catch(SQLException SQLExp)
            {
                out.println("false");
                System.out.println(SQLExp.toString());
                SQLExp.printStackTrace();
                if(data.startsWith("select"))
                {                    
                    continue;
                }
                else
                {
                    try
                    {
                        LogFile.write("Error in record from client : " + cl.getInetAddress().getHostAddress() + " for roll no : " + del_str[1] + " and Session : " + del_str[2] + "\n");
                    }
                    catch(Exception Exp)
                    {
                        Exp.printStackTrace();
                    }
                    System.out.println("Error in Record insertion");
                }
            }
            catch(IOException e)
            {
                try
                {
                    LogFile.write("Connection Abruplty closed by client : " + cl.getInetAddress().getHostAddress() + "\n");                    
                }
                catch(Exception Exp)
                {
                    Exp.printStackTrace();
                }
                System.out.println("Stream error for server");
                System.out.println(e);
                out.println("false");
                break;
            }
        }        
    }
    
    private char Decrypt(char Input, int KeySize)
    {
        int i = 0;
        char Temp = Input;
        for (i = 0; i < KeySize; i++)
        {
            if (Temp == 'a' || Temp == 'A')
            {
                switch (Temp)
                {
                    case 'a':
                        Temp = 'z';
                        break;
                    case 'A':
                        Temp = 'Z';
                        break;
                }
            }
            else
                Temp--;
            
        }
        
        return Temp;
    }    
}

class disp extends Thread
{
    plcmnt_server.Server_op op1;
    public disp(String str)
    {
        try
        {
            op1=new plcmnt_server.Server_op(str);            
            op1.setLocation(1024-op1.getWidth(),700);
            op1.setVisible(true);
            this.start();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public void run()
    {
        try
        {
            this.sleep(5000);
            op1.setVisible(false);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    protected void finalize() throws Throwable
    {
        System.out.println("Exiting thread");
    }
    
}