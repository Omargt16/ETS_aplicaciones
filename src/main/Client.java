package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {

    public static void main(String[] args) {
        try {
            // Create a DatagramChannel
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);

            // Connect the channel to the server address
            channel.connect(new InetSocketAddress("localhost", 12345));

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // Receive a message from the server
            channel.receive(buffer);
            buffer.flip();

            // Extract the message from the buffer
            String message = new String(buffer.array(), 0, buffer.limit());

            System.out.println("Received message from server: " + message);

            // Close the channel
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
