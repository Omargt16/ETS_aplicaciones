package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static void main(String[] args) {
        try {
            // Create a Selector
            Selector selector = Selector.open();

            // Create a DatagramChannel and bind it to a specific port
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(12345));

            // Register the channel with the Selector for write operations
            channel.register(selector, SelectionKey.OP_WRITE);

            System.out.println("Server is running. Waiting for incoming messages...");

            while (true) {
                // Wait for events
                selector.select();

                // Get the selected keys
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isWritable()) {
                        // Clear the key's ready set
                        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

                        // Get the channel associated with the key
                        DatagramChannel datagramChannel = (DatagramChannel) key.channel();

                        // Create a message to send to the client
                        String message = "Hello, client!";
                        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());

                        // Send the message to the client
                        datagramChannel.send(buffer, new InetSocketAddress("localhost", 54321));

                        System.out.println("Message sent to the client.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
