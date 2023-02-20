
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

public class UdpServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public UdpServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;

        while (running) {
            buf = new byte[256];
            DatagramPacket packet 
              = new DatagramPacket(buf, buf.length);
            try {
              socket.receive(packet);
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            

            // add calculator logic here, store result in buf

            String received = new String(packet.getData(), 0, packet.getLength());
            received = received.replaceAll("\0+$", "");
            System.out.println("En klient skrev: " + received);

            System.out.println(received.length());

            if (received.length() == 0) {
              continue;
            }

            String[] parts = received.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            double result = 0;
            String erroString = "";

            try{
              if (parts.length == 3) {
                double a = Integer.parseInt(parts[0]);
                double b = Integer.parseInt(parts[2]);
                String operator = parts[1];

                switch (operator) {
                  case "+":
                    result = a + b;
                    break;
                  case "-":
                    result = a - b;
                    break;
                  case "*":
                    result = a * b;
                    break;
                  case "/":
                    result = a / b;
                    break;
                  default:
                    erroString = "Error: vennligst send et gyldig regnestykke";
                    break;
                }
              }else{
                erroString = "Error:vennligst send et gyldig regnestykke";
              }
            
            } catch (Exception e) {
              erroString = "Error: " + e.getMessage();
            }


            if (erroString.length() > 0) {
              buf = erroString.getBytes();
            } else {
              DecimalFormat df = new DecimalFormat("0.#");
              String resultString = received + " = " + df.format(result);
              buf = resultString.getBytes();
            }

            packet = new DatagramPacket(buf, buf.length, address, port);
            
            
            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
              socket.send(packet);
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }
        socket.close();
    }


    public static void main(String[] args) throws SocketException {
        UdpServer server = new UdpServer();
        server.start();
    }
}
