//package Server;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Server {

    private static final int PORT_TO_RECEIVE = 61691; // Port for establishing the connection and receiving info
    List<ClientHandler> client = new LinkedList<>(); // store clientHandler threads
    HashMap<String, String> clientData = new HashMap<>(); // copy client information from clientHandler to server

    public Server() {
        DatagramSocket server = null;
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
                clientData.putAll(client.get(i).getClientInfo());
            }

            Thread.sleep(5000); // 5s later server will send multicast message

            System.out.println("Server has shut down!");

            server = new DatagramSocket(); // sending packet does not need to specify the port

            for(int i=0; i<clientData.size(); i++) {
                sendMultiCastMessage(
                        clientData.get("IP"),
                        Integer.parseInt(clientData.get("Port")),
                        server
                );
            }
        }
        catch (SocketException e) {
            e.getMessage();
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

    // send multicast message to all clients
    private void sendMultiCastMessage(String ip, int port, DatagramSocket server) throws IOException {
        String text = "Server has shut down!";
        InetAddress newIp = InetAddress.getByName(ip);
        DatagramPacket serverMessage = new DatagramPacket(text.getBytes(), text.length(), newIp, port);
        server.send(serverMessage);
        server.close();
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
