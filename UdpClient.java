/*
 * SocketKlient.java  - "Programmering i Java", 4.utgave - 2009-07-01
 *
 * Programmet kontakter et tjenerprogram som allerede kj�rer p� port 1250.
 * Linjer med tekst sendes til tjenerprogrammet. Det er laget slik at
 * det sender disse tekstene tilbake.
 */

import java.io.*;
import java.net.*;

public class UdpClient {
  private DatagramSocket socket;
  private InetAddress address;

  private byte[] buf = new byte[256];

  public UdpClient() throws SocketException, UnknownHostException {
      socket = new DatagramSocket();
      address = InetAddress.getByName("localhost");
  }

  public String sendEcho(String msg) {
      
      buf = msg.getBytes();
      DatagramPacket packet 
        = new DatagramPacket(buf, buf.length, address, 4445);
      try {
        socket.send(packet);
        buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      String received = new String(
        packet.getData(), 0, packet.getLength());
      return received;
  }

  public void close() {
      socket.close();
  }

  public static void main(String args[]){
    
    System.out.println("Oppgi navnet p� maskinen der tjenerprogrammet kj�rer: ");
    
    try {
      UdpClient klient = new UdpClient();
      System.out.println("N� er forbindelsen opprettet.");
      System.out.println("Hei, du har kontakt med tjenersiden!");
      System.out.println("Skriv hva du vil, s� skal jeg gjenta det, avslutt med linjeskift.");
      String linje = "";
      while (linje != null) {
        System.out.println("send regnestykke: ");
        linje = (new BufferedReader(new InputStreamReader(System.in))).readLine();
        if (linje != null) {
          String svar = klient.sendEcho(linje);
          System.out.println("Resultat: " + svar);
        }
      }
      klient.close();
    } catch (SocketException | UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}

/* Utskrift p� klientsiden:
Oppgi navnet p� maskinen der tjenerprogrammet kj�rer: tonje.aitel.hist.no
N� er forbindelsen opprettet.
Hei, du har kontakt med tjenersiden!
Skriv hva du vil, s� skal jeg gjenta det, avslutt med linjeskift.
Hallo, dette er en prove.
Fra tjenerprogrammet: Du skrev: Hallo, dette er en pr�ve.
Og det fungerer utmerket.
Fra tjenerprogrammet: Du skrev: Og det fungerer utmerket.
*/