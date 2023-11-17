/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author gusta
 */
public class ServerCommunicator implements Runnable {

    private Thread t;

    public ServerCommunicator() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(3000);
            byte[] data = new byte[256];
            while (!Thread.interrupted()) {
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                
                SidePanel.setScoreboard(message); // om klienten fått data via sin socket är det ett uppdateras scoreboard
            }
        } catch (IOException ex) {
            System.out.println("Fel: IOException");
        }
    }

    /**
     * bygger ihop den sträng som ska skickas till servern då man ska ladda upp en ny tid
     * @param score den nya tiden
     * @param name användarens namn
     * @throws UnknownHostException 
     */
    public void sendScore(String score, String name) throws UnknownHostException {
        String thisAddress = InetAddress.getLocalHost().getHostAddress(); // hämtar klientens ip-adress
        score += "\\" + thisAddress + "-" + name;
        
        sendMessage("1." + score); // "1." berättar för servern att detta meddelande innehåller ett nytt score
    }
    
    /**
     * bygger ihop den sträng som ska skickas till servern då man vill få ett uppdaterat scoreboard
     * @throws UnknownHostException 
     */
    public void getScoreboard() throws UnknownHostException{
        String thisAddress = InetAddress.getLocalHost().getHostAddress(); // hämtar klientens ip-adress
        sendMessage("2.\\" + thisAddress); // "2." berättar för servern att klienten vill hämta scoreboardet med detta meddelande
    }

    /**
     * skickar en sträng till servern
     * @param m den sträng som ska skickas
     * @throws UnknownHostException 
     */
    private void sendMessage(String m) throws UnknownHostException {
        InetAddress serverAddress = InetAddress.getByName("localhost");
        int port = 2000;
        byte[] dataToSend = m.getBytes();

        try {
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, serverAddress, port);
            new DatagramSocket().send(packet);
        } catch (UnknownHostException e) {
            System.out.println("UH " + e.getMessage());
        } catch (SocketException e) {
            System.out.println("SE " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO " + e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        ServerCommunicator sc = new ServerCommunicator();
    }

}
