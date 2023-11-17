/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marioserver;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author gustav.andoff
 */
public class MarioServer extends JFrame implements Runnable {

    private Thread t;

    private ArrayList<String> scores;

    public MarioServer() {
        scores = new ArrayList();
        t = new Thread(this);
        t.start();
    }

    /**
     * en thread som kollar om servern har fått något meddelande
     */
    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(2000);
            byte[] data = new byte[256];
            while (!Thread.interrupted()) {
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());

                switch (getMessageType(message)) { // när ett meddelande har fått kollas vilken typ av meddelande det är
                    case "1": // "1." berättar för servern att detta meddelande innehåller ett nytt score
                        scores.add(getTimeFromMessage(message) + " - " + getNameFromMessage(message)); // lägger till den nyta tiden i en lista med alla tider
                        updateScoreFile();

                        break;
                    case "2": // "2." berättar för servern att klienten vill hämta scoreboardet med detta meddelande
                        sendScoreboard(message);
                        break;
                }

            }
        } catch (IOException e) {
            System.out.println("Fel: IOException");
            return;
        }
    }

    /**
     * hämtar vilken typ av meddelande ett visst meddelande är
     * @param m meddelandet
     * @return typen
     */
    private String getMessageType(String m) {
        String result = "";
        try {
            result = m.substring(0, m.indexOf("."));
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
        }

        return result;
    }

    /**
     * hämtar vilken tid ett visst meddelande innehåller
     * @param m meddelandet
     * @return tiden
     */
    private String getTimeFromMessage(String m) {
        String result = "";
        try {
            result = m.substring(m.indexOf(".") + 1, m.indexOf("\\"));
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
        }

        return result;
    }

    /**
     * hämtar ip-adress som skickas med i ett meddelande
     * @param m meddelandet
     * @return ip-adressen
     */
    private String getAddressFromMessage(String m) {
        String result = "";
        try {
            result = m.substring(m.indexOf("\\") + 1, m.indexOf("-"));
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
        }

        return result;
    }

    /**
     * hämtar namnet som skickas med i ett meddelande
     * @param m meddelandet
     * @return namnet
     */
    private String getNameFromMessage(String m) {
        String result = "";
        try {
            result = m.substring(m.indexOf("-") + 1);
        } catch (java.lang.StringIndexOutOfBoundsException ex) {
        }

        return result;
    }

    /**
     * uppdaterar filen med alla tider
     */
    private void updateScoreFile() {
        try {
            scores.sort(new CompareScore()); // sorterar listan så att tiderna går från snabbast till långsammast

            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("scores.txt")));
            for (String score : scores) {
                out.append(score + "\n"); // går igenom listan med scores och lägger till alla i en fil
            }
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Fel: FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("Fel: IOException");
        }

    }

    /**
     * skickar listan med scores till en användare
     * @param m meddelandet som användaren skickade
     * @throws UnknownHostException
     * @throws FileNotFoundException 
     */
    private void sendScoreboard(String m) throws UnknownHostException, FileNotFoundException {
        scores.clear();

        String scoreBoard = "";

        Scanner sc = new Scanner(new File("scores.txt"));
        while (sc.hasNextLine()) { // hämtar alla scores från filen
            String line = sc.nextLine();
            scores.add(line);
        }

        scores.sort(new CompareScore());

        for (String score : scores) {
            scoreBoard += score + "\n"; // lägger till alla listans scores i en sträng
        }

        String stringAddress = getAddressFromMessage(m); // användarens ip-adress

        InetAddress address = InetAddress.getByName(stringAddress); // ip-adress omvandlas från en sträng till en InetAddress
        int port = 3000;
        byte[] dataToSend = scoreBoard.getBytes(); // listan skickas till användaren

        try {
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, address, port);
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
        MarioServer marioServer = new MarioServer();
    }

}
