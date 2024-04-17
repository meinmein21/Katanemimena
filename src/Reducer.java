import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Reducer {
    private ServerSocket serverSocket;
    private List<Room> finalRooms;

    public Reducer(int port) {
        finalRooms = new ArrayList<>();
    }
    public void doer(int port){

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Reducer started and listening on port " + port);

            // Listen for incoming connections indefinitely
            while (true) {
                // Accept an incoming connection from a worker
                Socket workerSocket = serverSocket.accept();
                System.out.println("worker" + workerSocket.getPort() + " connected");

                // Create a new thread to handle the worker connection
                Thread workerThread = new WorkerHandler(workerSocket);
                workerThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error starting Reducer: " + e.getMessage());
        }
    }

    // Inner class to handle worker connections in separate threads
    private class WorkerHandler extends Thread {
        private Socket socket;

        public WorkerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Receive filtered rooms list from Worker
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                List<Room> filteredRooms = (List<Room>) in.readObject();

                // Merge received list with finalRooms
                synchronized (finalRooms) {
                    finalRooms.addAll(filteredRooms);
                }
                // Close the socket
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling worker connection: " + e.getMessage());
            }
        }
    }

    public List<Room> getFinalRooms() {
        return finalRooms;
    }
}
