/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fofprotocol;

/**
 *
 * @author Sameer
 */
public class Startup {

    public static void main(String[] args) {
        ClientStorageControl client = new ClientStorageControl(); //new object for client
        ServerStorageControl server = new ServerStorageControl(); //new object for server
        server.setClientStorageControl(client); //client object set for server
        client.setServerStorageControl(server); //server object set for client
        server.start(); //Thread1 for server strats
        client.start(); //Thread2 for client strats
    }
}
