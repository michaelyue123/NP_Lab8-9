package Client;

import java.io.IOException;
import java.net.*;

public class Client {

    private final static String ADDRESS = "netprog1.csit.rmit.edu.au";
    private static final int PORT = 61246;
    private InetAddress ip;

    public Client() {
        DatagramSocket client;

        try {
            client = new DatagramSocket(PORT); // create a client datagram socket listening on port 61246

            sendDatagramSocket(client); // send message to server

            while (true) {
                try {
                    receiveDatagramSocket(client); // listening for message sent back from the server
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.getMessage();
        } finally {

        }
    }

    private void receiveDatagramSocket(DatagramSocket client) throws IOException {
        byte[] buff = new byte[1024];
        DatagramPacket serverMessage = new DatagramPacket(buff, buff.length);
        client.receive(serverMessage); // receive server message

        String output = new String(serverMessage.getData(), 0, serverMessage.getLength());
        System.out.println(output); // once client receives the message, client closes its socket connection
        client.close();
    }

    // send client message to server
    private void sendDatagramSocket(DatagramSocket client) throws IOException {
        ip = InetAddress.getByName(ADDRESS);
        String text = "Client on " + ADDRESS + " is listening on " + PORT;
        DatagramPacket clientMessage = new DatagramPacket(text.getBytes(), text.length(), ip, PORT);
        client.send(clientMessage);
        System.out.println("IP address: " + ip);
        System.out.println("Port Number: " + PORT);
    }


    public static void main(String[] args) {
        new Client();
    }
}
