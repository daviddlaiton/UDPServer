/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.io.*;
import java.net.*;

/**
 *
 * @author andre
 */
public class Server {

    public static void main(String args[]) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        int portServer = Integer.parseInt(inFromUser.readLine());
        DatagramSocket serverSocket = new DatagramSocket(portServer);
        System.out.println("Server iniciado en el puerto:" + serverSocket.getLocalPort());
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        PrintWriter writer = new PrintWriter("Client1.txt", "UTF-8");
        int i = 0;
        int numObj = 0;
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            long init = System.currentTimeMillis();
            serverSocket.receive(receivePacket);
            long fin = System.currentTimeMillis();
            long tiempo = fin - init;
            String sentence = new String(receivePacket.getData());
            System.out.println(i + tiempo);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
