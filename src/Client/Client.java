//package Client;

import java.io.IOException;
import java.net.*;

public class Client {

    private final static String ADDRESS = "netprog1.csit.rmit.edu.au";
    private static final int PORT = 61691; // different to the port client is listening to
    private int listeningPortNumber;

    public static void main(String[] args) {
        int listeningPortNumber = Integer.parseInt(args[0]);
        new Client(listeningPortNumber);
    }

    public Client(int listeningPortNumber) {
        this.listeningPortNumber = listeningPortNumber;

        try {
            DatagramSocket client = new DatagramSocket(PORT); // create a client datagram socket listening on port 61246

            sendDatagramSocket(); // send message to server

            while (true) {
                try {
                    receiveDatagramSocket(client); // listening for message sent back from the server
                }
                catch (BindException e) {
                    continue;
                }
                catch (SocketException e) {
                    client.close();
                    break;
                }
            }
        }
        catch (UnknownHostException e) {
            e.getMessage();
        }
        catch (IOException e) {
            e.getMessage();
        }
        catch (Exception e) {
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
    private void sendDatagramSocket() throws IOException {

        DatagramSocket ds = new DatagramSocket();

        InetAddress ip = InetAddress.getByName(ADDRESS);

        String text = "Client on " + ADDRESS + " is listening to " + listeningPortNumber;

        DatagramPacket clientMessage = new DatagramPacket(text.getBytes(), text.length(), ip, PORT);

        System.out.println("IP address: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Port Number: " + listeningPortNumber);

        clientMessage.setData(text.getBytes());
        ds.send(clientMessage);
        ds.close();
    }
}
