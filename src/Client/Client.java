//package Client;

import java.io.IOException;
import java.net.*;

public class Client {

    private final static String ADDRESS = "localhost";
    private static final int PORT_TO_RECEIVE = 61691; // Port for establishing the connection and receiving info
    private static final int PORT_TO_SEND = 61246; // Port for send the info
    private DatagramSocket client = null;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {

        try {
            client = new DatagramSocket(); // create a default client datagram socket
            sendDatagramSocket(client); // send message to server

            while (true) {
                // datagram socket using port for receiving packet
                client = new DatagramSocket(PORT_TO_RECEIVE);
                receiveDatagramSocket(client); // listening for message sent back from the server
            }
        } catch (UnknownHostException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }

    }

    private void receiveDatagramSocket(DatagramSocket client) throws IOException {
        byte[] buff = new byte[1024];
        DatagramPacket serverMessage = new DatagramPacket(buff, buff.length);
        client.receive(serverMessage); // receive server message

        String output = new String(serverMessage.getData(), 0, serverMessage.getLength());
        System.out.println(output); // once client receives the message, client closes its socket connection
    }

    // send client message to server
    private void sendDatagramSocket(DatagramSocket client) throws IOException {

        InetAddress ip = InetAddress.getByName(ADDRESS);

        String text = "Client on " + ADDRESS + " is listening to " + PORT_TO_SEND;

        DatagramPacket clientMessage = new DatagramPacket(text.getBytes(), text.length(), ip, PORT_TO_RECEIVE);

        System.out.println("IP address: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Port Number: " + PORT_TO_SEND);

        client.send(clientMessage);
        client.close();
    }
}
