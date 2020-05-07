//package Server;

import java.net.DatagramPacket;
import java.util.HashMap;


public class ClientHandler extends Thread {

    private DatagramPacket clientMessage;
    HashMap<String, String> clientInfo;  // create HashMap to store client info


    public ClientHandler(DatagramPacket clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        String output = new String(clientMessage.getData(), 0, clientMessage.getLength());
        String[] arr = output.split(" ");

        String IP = arr[2];
        String Port = arr[arr.length-1];

        System.out.println("\n"+ output); // print out the client ip address and port number
        System.out.println("IP Address: " + IP);
        System.out.println("Port Number: " + Port);

        clientInfo = new HashMap<>();
        // store string version of IP and Port into HashMap
        clientInfo.put("IP", IP);
        clientInfo.put("Port", Port);
    }

    // get client information
    public HashMap<String, String> getClientInfo() {
        return clientInfo;
    }
}
