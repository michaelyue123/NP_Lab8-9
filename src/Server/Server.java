//package Server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server {

    private static final int PORT_TO_RECEIVE = 61691; // Port for establishing the connection and receiving info
    List<ClientHandler> client = new LinkedList<>(); // store clientHandler threads
    ArrayList<ClientHandler.Client> clientData = new ArrayList<>(); // copy client information from clientHandler to server
    private DatagramSocket server = null;
    private static final int COUNT_DOWN_TIME = 5;

    public Server() {
        int countDown = COUNT_DOWN_TIME;

        try{
            System.out.println("Server starts! Waiting for new connection...");
            server = new DatagramSocket(PORT_TO_RECEIVE); // create a server datagram socket running on port 61246
            server.setSoTimeout(10000);

            while(true) {
                try {
                    receiveDatagramSocket(server); // receive packet sent from client
                }
                catch (SocketTimeoutException e) {
                    System.out.println("\nServer is about to shut down in 5s!");
                    server.close();
                    break;
                }
            }

            for(int i=0; i<client.size(); i++) {
                client.get(i).join(); // avoid race condition among different clientHandler threads
                // copy info in clientHandler to server
                clientData.addAll(client.get(i).getClientInfo());
            }

            // apply count down in last 5 seconds
            while(countDown > 0) {
                threadMessage(" " + countDown);
                countDown--;
                Thread.currentThread().join(1000);
            }

            System.out.println("Server has shut down!");

            for(int i=0; i<clientData.size(); i++) {
                sendMultiCastMessage(
                        clientData.get(i).getIp(),
                        Integer.valueOf(clientData.get(i).getPort())
                );
            }
        }
        catch (InterruptedException e) {
            e.getMessage();
        }
        catch (IOException e) {
            e.getMessage();
        }
        finally {
            if(server != null) server.close();
        }
    }

    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    // send multicast message to all clients
    private void sendMultiCastMessage(String ip, Integer port) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        InetAddress newIp = InetAddress.getByName(ip);
        String text = "Server has shut down!";

        DatagramPacket serverMessage = new DatagramPacket(text.getBytes(), text.length(), newIp, port);
        ds.send(serverMessage);
        ds.close();
    }

    // receive datagram packet from client
    private void receiveDatagramSocket(DatagramSocket server) throws IOException {
        byte[] buff = new byte[1024];
        DatagramPacket clientMessage = new DatagramPacket(buff, buff.length); // incoming packet
        server.receive(clientMessage); // receive the message

        // add clientHandler thread to an arrayList and pass the message to clientHandler
        client.add(new ClientHandler(clientMessage));
        client.get(client.size()-1).start(); // start the thread
    }

    public static void main(String[] args) {
        new Server();
    }
}
