/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import fofprotocol.ClientStorageControl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Sameer
 */
public class Connection implements ConnectionIf {

    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String strHost = null;
    private String strGuest = null;
    private ServerSocket serverSocket;
    private InetSocketAddress isaClient;
    private String strIpAddress;
    private int nPort;

    public Connection() {
    }


    public Connection(int port, String strHost, String strGuest) {//to be used by server
        try {
            System.out.println("Waiting for Storage Function Simulator to connect..");
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            System.out.println("Storage Function Simulator Connected..");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.strHost = strHost;
            this.strGuest = strGuest;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Connection(String ip, int port, String strHost, String strGuest) throws InterruptedException {
        //to be used by client

        this.strHost = strHost;
        this.strGuest = strGuest;
        boolean isConnected = false;
        System.out.println("Waiting for Control Command Generator to connect..");
        while (!isConnected) {
            try {
                strIpAddress = ip;
                nPort = port;
                isaClient = new InetSocketAddress(ip, port);
                socket = new Socket();
                socket.connect(isaClient);
                System.out.println("Connected to Control Command Generator");
                isConnected = true;
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public MessageIf receive() {
        boolean isEveryThingFine = false;
        MessageIf objMessageIf = null;
        while (!isEveryThingFine) {
            try {
                objMessageIf = Message.receive(in);
                isEveryThingFine = true;
            } catch (Exception e) {
                System.out.println(e);
                in = null;
                out = null;
                System.out.println("Connection lost waiting for a connection");
                try {
                    socket.close();
                    socket=null;
                    if (serverSocket == null) {
                        isaClient=null;
                        boolean isConnected = false;
                        while (!isConnected) {
                            try {
                                socket = new Socket();                                
                                isaClient = new InetSocketAddress(getStrIpAddress(), getnPort());
                                socket.connect(isaClient);
                                isConnected = true;
                                ClientStorageControl.bConnectionChanged  = true;
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                    } else {
                        socket = serverSocket.accept();
                    }
                    System.out.println("Connected..");
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        return objMessageIf;
    }

    public void send(MessageIf msg) {
        boolean isSent = false;
        while (!isSent) {
            try {
                Message message = ((Message) msg);
                byte[] rawMessage = message.getFofRawMessage(message.getSender(), message.getReceiver());
                out.write(rawMessage);
                isSent = true;
            } catch (Exception e) {
              System.out.println(e);
                in = null;
                out = null;
                System.out.println("Connection lost waiting for a connection");
                try {
                    socket.close();
                    socket=null;
                    if (serverSocket == null) {
                        isaClient=null;
                        boolean isConnected = false;
                        while (!isConnected) {
                            try {
                                socket = new Socket();
                                isaClient = new InetSocketAddress(getStrIpAddress(), getnPort());
                                socket.connect(isaClient);
                                ClientStorageControl.bConnectionChanged  = true;
                                isConnected = true;
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                    } else {
                        socket = serverSocket.accept();
                    }
                    System.out.println("Connected..");
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public String getStrGuest() {
        return strGuest;
    }

    public void setStrGuest(String strGuest) {
        this.strGuest = strGuest;
    }

    public String getStrHost() {
        return strHost;
    }

    public void setStrHost(String strHost) {
        this.strHost = strHost;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public InetSocketAddress getIsaClient() {
        return isaClient;
    }

    public void setIsaClient(InetSocketAddress isaClient) {
        this.isaClient = isaClient;
    }

    public int getnPort() {
        return nPort;
    }

    public void setnPort(int nPort) {
        this.nPort = nPort;
    }

    public String getStrIpAddress() {
        return strIpAddress;
    }

    public void setStrIpAddress(String strIpAddress) {
        this.strIpAddress = strIpAddress;
    }
}
