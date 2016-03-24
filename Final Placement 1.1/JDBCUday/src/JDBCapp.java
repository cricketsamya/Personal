import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;

/*
<applet code="JDBCapp" width=400 height=300>
</applet>
*/

class messagebox extends Frame implements ActionListener
{
	private static final long serialVersionUID = 8166094522905910897L;

	messagebox(String title,String err)
	{
		super(title);
		setSize(200,100);
		setResizable(false);
		setLayout(new FlowLayout());

		JLabel error=new JLabel(err);
		add(error);

		JButton ok=new JButton("OK");
		ok.addActionListener(this);
		add(ok);
	}

	public void actionPerformed(ActionEvent ae)
	{
		setVisible(false);
	}
}

class tableview extends Frame
{
	private static final long serialVersionUID = -6341619948968213124L;

	tableview(Connection conn,String table,String where)
	{
		super("Contents of Table "+table);
		setSize(1024,768);
		try
		{
			Statement stmt=conn.createStatement();
			ResultSet rset;

			funcv adapter=new funcv(this);
			addWindowListener(adapter);

			System.out.println("Button Pressed. Table "+table+" selected.");

			setLayout(new BorderLayout());
			String query="select * from "+table+where;
			System.out.println(query);
			rset=stmt.executeQuery(query);

			ResultSetMetaData meta=rset.getMetaData();
		    int cols=meta.getColumnCount(),rows=0,i=0;

		    final String[] colHeads=new String[cols];
		    for(i=0;i<cols;i++)
		    {
		    	colHeads[i]=meta.getColumnLabel(i+1);
		    }

		    while(rset.next())
		    	rows++;

		    rset=stmt.executeQuery("select * from "+table+ where);

		    final String[][] data=new String[rows][cols];

		    i=0;

		    while(rset.next())
		    {
		    	for(int j=0;j<cols;j++)
			    {
		    		data[i][j]=rset.getString(j+1);
			    }
		    	i++;
		    }

		   JTable jtable=new JTable(data,colHeads);
		   //jtable.setEnabled(false);
		   int v=ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		   int h=ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		   JScrollPane jsp=new JScrollPane(jtable,v,h);
		   add(jsp,BorderLayout.CENTER);
		   jtable.print();
		}
		catch(SQLException e)
		{
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

class tableinsert extends Frame implements ActionListener
{
	private static final long serialVersionUID = -6341619948968213124L;
	Connection conn1;
	String tabname;
	JTextField boxes[];
    JLabel labels[];
    int cols;
    ResultSet rset,rset1;
    Statement stmt;
    ResultSetMetaData meta;

	tableinsert(Connection conn,String table)
	{
		super("Inserting into Table "+table);
		conn1=conn;
		tabname=table;
		setSize(200,500);
		setResizable(false);

		try
		{
			stmt=conn.createStatement();

			funcsi adapter=new funcsi(this);
			
			addWindowListener(adapter);

			System.out.println("Button Pressed. Table "+table+" selected.");

			setLayout(new FlowLayout());

			rset=stmt.executeQuery("select * from "+table);
			meta=rset.getMetaData();
		    cols=meta.getColumnCount();

		    boxes=new JTextField[cols];
		    labels=new JLabel[cols];

		    for(int i=0;i<cols;i++)
		    {		    	
		    	String temp,temp1;	    	
		    	//meta=rset.getMetaData();
		    	temp=meta.getColumnLabel(i+1);
		    	
		    	Statement stmt1=conn.createStatement();
		    	
		    	rset1=stmt1.executeQuery("SELECT column_name FROM USER_CONS_COLUMNS WHERE CONSTRAINT_NAME in (select constraint_name from user_constraints where table_name='EMP') AND TABLE_NAME = 'EMP'");
		    	
		    	while(rset1.next())
		    	{
		    		temp1=rset1.getString(1);
		    		if(temp.equals(temp1))
		    		{
		    			temp="("+temp+")*";		    			
		    			break;
		    		}
		    	}
		    	
		    	rset1.close();
		    	
		    	labels[i]=new JLabel(temp);
		    	boxes[i]=new JTextField(15);

		    	add(labels[i]);
		    	add(boxes[i]);
		    }
		    JButton submit=new JButton("Submit");
		    submit.addActionListener(this);
		    add(submit);
		}
		catch(SQLException e)
		{
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			rset=stmt.executeQuery("select * from "+tabname);
			meta=rset.getMetaData();

			String query="insert into "+ tabname+" values(";

			for(int i=0;i<cols;i++)
			{
				int type=meta.getColumnType(i+1);

				if((type==Types.CHAR) || (type==Types.VARCHAR) || (type==93))
					query+="'"+boxes[i].getText()+"'";
				else
					query+=boxes[i].getText();

				if(i!=(cols-1))
					query+=",";
			}
			query+=")";
			stmt.execute(query);			
			System.out.println(query);			
			this.setVisible(false);
		}

		catch(SQLException e)
		{
			e.printStackTrace();
			messagebox msgbx=new messagebox("ERROR","Error in insertion.Give valid values.");
			msgbx.setVisible(true);
		}
	}
}

class tablesearch extends Frame implements ActionListener,ItemListener
{
	private static final long serialVersionUID = -6341619948968213124L;
	Connection conn1;
	String tabname;
	JTextField boxes;
	JComboBox jc;
    int cols;
    ResultSet rset;
    Statement stmt;
    ResultSetMetaData meta;
    String column;

	tablesearch(Connection conn,String table)
	{
		super("Search from Table "+table);
		conn1=conn;
		tabname=table;
		setSize(200,200);
		setResizable(false);

		try
		{
			stmt=conn.createStatement();

			funcs adapter=new funcs(this);

			addWindowListener(adapter);

			System.out.println("Button Pressed. Table "+table+" selected.");

			setLayout(new FlowLayout());

			rset=stmt.executeQuery("select * from "+table);
			meta=rset.getMetaData();
		    cols=meta.getColumnCount();

		    jc=new JComboBox();

		    for(int i=0;i<cols;i++)
		    {
		    	jc.addItem(meta.getColumnLabel(i+1));
		    	if(i==0)
		    		column=meta.getColumnLabel(i+1);
		    }
		    cols=0;
		    jc.addItemListener(this);
		    add(jc);

		    boxes=new JTextField(15);
		    add(boxes);

		    JButton submit=new JButton("Submit");
		    submit.addActionListener(this);
		    add(submit);

		    JButton delete=new JButton("Delete");
		    delete.addActionListener(this);
		    add(delete);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand() =="Submit")
		{
			try
			{
				String query=" where " + column +"=";

				int type=meta.getColumnType(cols+1);

				if((type==Types.CHAR) || (type==Types.VARCHAR) || (type==93))
					query+="'"+boxes.getText()+"'";
				else
					query+=boxes.getText();

				tableview t=new tableview(conn1,tabname,query);
				t.setVisible(true);
			}
			catch(SQLException e)
			{
				messagebox msgbx=new messagebox("ERROR","ENTRY NOT FOUND.");
				msgbx.setVisible(true);
			}
		}

		if(ae.getActionCommand() =="Delete")
		{
			try
			{
				rset=stmt.executeQuery("select * from "+tabname);
				meta=rset.getMetaData();
			    String where=" where " + column +"=";

			    int type=meta.getColumnType(cols+1),rows=0;

			    //Checking for the Column Type 1=CHAR,12=VARCHAR/VARCHAR2,93=DATE
				if((type==Types.CHAR) || (type==Types.VARCHAR) || (type==93))
					where+="'"+boxes.getText()+"'";
				else
					where+=boxes.getText();

				String query="select * from "+tabname+where;
				ResultSet rset1=stmt.executeQuery(query);
				while(rset1.next())
					rows++;
				System.out.println(rows);
				if(rows==0)
				{
					messagebox msgbx=new messagebox("ERROR","ENRTY NOT FOUND.");
					msgbx.setVisible(true);
				}
				else
				{
					query="delete from "+tabname+where;
					stmt.execute(query);
					messagebox msgbx=new messagebox("SUCCESS",rows+" ENRTY DELETED.");
					msgbx.setVisible(true);
				}
			}
			catch(SQLException e)
			{
				messagebox msgbx=new messagebox("ERROR","Error in Deletion.Give valid values.");
				msgbx.setVisible(true);
			}
		}

	}

	public void itemStateChanged(ItemEvent ie)
	{
		column=(String) ie.getItem();
		cols=jc.getSelectedIndex();
		System.out.println(cols);
	}
}

class funcv extends WindowAdapter
{
	tableview t;

	public funcv(tableview t)
	{
		this.t=t;
	}
	public void windowClosing(WindowEvent we)
	{
		t.setVisible(false);		
	}
}

class funcs extends WindowAdapter
{
	tablesearch t;

	public funcs(tablesearch t)
	{
		this.t=t;
	}
	public void windowClosing(WindowEvent we)
	{
		t.setVisible(false);
	}
}

class funcsi extends WindowAdapter
{
	tableinsert t;

	public funcsi(tableinsert t)
	{
		this.t=t;
	}
	public void windowClosing(WindowEvent we)
	{
		t.setVisible(false);
	}
}


public class JDBCapp extends JApplet implements ItemListener,ActionListener
{
	private static final long serialVersionUID = 1L;

	Container pane=getContentPane();
	Statement stmt;
	ResultSet rset;
	Connection conn;
	JComboBox jc;
	String table;
	String usr,pwd;
	JTextField user;
	JPasswordField pswd;

	public void init()
	{
		setName("JDBC");
		setSize(200,200);

		try
		{
			pane.setLayout(new FlowLayout());
			startpage();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void startpage()
	throws IOException
	{
		JLabel userl=new JLabel("USER     ");
		JLabel pswdl=new JLabel("PASSWORD ");

		user=new JTextField(15);
		pswd=new JPasswordField(15);

		pane.add(userl);
		pane.add(user);
		pane.add(pswdl);
		pane.add(pswd);

		JButton submit=new JButton("Submit");
	    submit.addActionListener(this);
	    pane.add(submit);
	}

	void prog()
	throws SQLException
	{
	    pane.removeAll();
	    repaint();
		pane.setLayout(new FlowLayout());
		stmt = conn.createStatement ();
	    rset = stmt.executeQuery ("select tname from tab where tabtype='TABLE'");

	    JLabel select_tab=new JLabel("Select a Table     ");
	    pane.add(select_tab);

	    jc=new JComboBox();

	    int i=0;

	    while (rset.next ())
	    {
	    	if(i==0)
	    	{
	    		jc.addItem(rset.getString(1));
	    		i=1;
	    	}
	    	else
	    		jc.addItem(rset.getString(1));
	    }
	    jc.addItemListener(this);
	    pane.add(jc);

	    JButton view=new JButton("View Table");
	    view.addActionListener(this);
	    pane.add(view);
	    //pane.add(view );

	    JButton insert=new JButton("Insert Entry");
	    insert.addActionListener(this);
	    pane.add(insert);

	    JButton search=new JButton("Search/Delete Entry");
	    search.addActionListener(this);
	    pane.add(search);

	    resize(202,200);
	}

	public void itemStateChanged(ItemEvent ie)
	{
		table=(String) ie.getItem();
	}

	public void actionPerformed(ActionEvent ap)
	{
		if(ap.getActionCommand()=="Insert Entry")
		{
			tableinsert t= new tableinsert(conn,table);
			t.setVisible(true);
		}

		if(ap.getActionCommand()=="Search/Delete Entry")
		{
			tablesearch t= new tablesearch(conn,table);
			t.setVisible(true);
		}

		if(ap.getActionCommand()=="Submit")
		{
			usr=user.getText();
			pwd=String.valueOf(pswd.getPassword());
			 
			System.out.println("User : "+usr+"\nPassword : "+pwd);

			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

				System.out.println ("Connecting to oracle...");

			    conn = DriverManager.getConnection ("jdbc:odbc:JDBCOracle" ,usr, pwd);

			    System.out.println ("Connected to Oracle.");
			    
				prog();
			}

			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			
			catch (SQLException e)
			{
				e.printStackTrace();		
				System.out.println("\n\n\nWrong paswword");
				messagebox msgbx=new messagebox("ERROR","WRONG USER OR PASSWORD\n");
				msgbx.setVisible(true);
			}
		}

		if(ap.getActionCommand()=="View Table")
		{
			tableview t= new tableview(conn,table,"");
			t.setVisible(true);
		}
	}
}
