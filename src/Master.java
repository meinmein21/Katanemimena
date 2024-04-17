import java.net.*;
import java.io.*;

public class Master {
    private ServerSocket server;
    public static final int numberOfWorkers = 3;//hard coded number of workers
    public static Worker[] workers;//static array to start worker threads
    public static Reducer reducer;
    public int reducerport = 9999;

    public Master(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started");
            System.out.println("Waiting for client...");

            //start reducer as a thread
            Thread reducerThread = new Thread(() -> {
                reducer = new Reducer(reducerport);
                reducer.doer(reducerport);
            });
            reducerThread.start();


            workers = new Worker[numberOfWorkers];
            //start all the worker threads at once ready to store information
            for (int i = 0; i < numberOfWorkers; i++) {
                workers[i] = new Worker(i);
                workers[i].start();
            }



            //master always stays open for upcoming connections from managers/user
            while (true) {
                Socket socket = server.accept();// a client connected
                System.out.println("Client accepted");

                //distinguish whether client is manager/user
                //and start the appropriate thread
                String clientType = readClientType(socket);
                if ("MANAGER".equalsIgnoreCase(clientType)) {
                    ManagerHandler managerHandler = new ManagerHandler(socket);
                    managerHandler.start();
                } else if ("DUMMY".equalsIgnoreCase(clientType)) {
                    ClientHandler dummyHandler = new ClientHandler(socket);
                    dummyHandler.start();
                } else {
                    System.out.println("Unknown client type");
                    socket.close();
                }
            }
        }catch (IOException e) {
            System.out.println("Error occured : " + e);
        }finally {
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException e) {
                System.out.println("Error occured : " + e);
            }
        }
    }

    public static void main(String[] args) {
        Master server = new Master(6666);
    }



    private String readClientType(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        String clientType = in.readUTF();
        System.out.println("Received client type from " + socket.getInetAddress() + ": " + clientType); // Add this line
        return clientType;
    }

}