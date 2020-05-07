//package Server;

import java.net.DatagramPacket;
import java.util.ArrayList;


public class ClientHandler extends Thread {

    private DatagramPacket clientMessage;
    ArrayList<Client> clientInfo;  // create HashMap to store client info


    public ClientHandler(DatagramPacket clientMessage) {
        this.clientMessage = clientMessage;
    }

    class Client {
        private String ip;
        private String port;

        public Client(String ip, String port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public String getPort() {
            return port;
        }
    }

    @Override
    public void run() {

        String output = new String(clientMessage.getData(), 0, clientMessage.getLength());
        String[] arr = output.split(" ");

        Client client = new Client(arr[2], arr[arr.length-1]);

        System.out.println("\n"+ output); // print out the client ip address and port number
        System.out.println("IP Address: " + client.getIp());
        System.out.println("Port Number: " + client.getPort());

        clientInfo = new ArrayList<>();
        // store client into arraylist
        clientInfo.add(client);
    }

    // get client information
    public ArrayList<Client> getClientInfo() {
        return clientInfo;
    }
}
