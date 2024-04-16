import java.net.*;
import java.io.*;
import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Master {
    private ServerSocket server;
    private static final int numberOfWorkers = 3;//no. of workers hard coded
    private static Worker[] workers;//static array to start worker threads

    public Master(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started");
            System.out.println("Waiting for client...");

            workers = new Worker[numberOfWorkers];
            for (int i = 0; i < numberOfWorkers; i++) {//start all the worker threads at once
                workers[i] = new Worker(i);
                workers[i].start();
            }

            while (true) {//master always stays open for upcoming connections from managers
                Socket socket = server.accept();//accepts the client
                System.out.println("Client accepted");

                ManagerHandler handler = new ManagerHandler(socket);//starts a new thread to handle the manager
                handler.start();//calls ManagerHandler's run()
            }
        }catch (IOException e) {
            System.out.println(e);
        }finally {
            try {
                server.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String args[]) {
        Master server = new Master(6666);
    }

    // MasterThread for managers
    private static class ManagerHandler extends Thread {
        private Socket socket;
        private Gson gson;//library instance for mapping json strings to objects
        private Socket[] connections;//stores the connections (sockets) in array
        private static int noOfConnections = 0;

        public ManagerHandler(Socket socket) {
            this.socket = socket;//takes client's socket as argument
            gson = new Gson();
            noOfConnections++;
        }

        @Override
        public void run() {
            try {
                System.out.println("Manager connected from IP: " + socket.getInetAddress() + " and port " + socket.getPort());
                //System.out.println("Waiting for client...");
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));//receive the data sent by client
                String line;

                while (!(line = in.readUTF()).equals("Goodbye")) {//while manager didnt type "exit"
                    System.out.println("Received room's data from Manager with address: " + socket.getInetAddress() +" "+ line);
                    Room room = gson.fromJson(line, Room.class);// deserialize json to room object

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//the room's date field format
                    String[] dateRange = room.getAvailableDates().split(" - ");//store startDate and endDate strings in an array

                    try{
                        Date startDate = dateFormat.parse(dateRange[0]);//parsing the string to date object
                        Date endDate = dateFormat.parse(dateRange[1]);//parsing the string to date object
                        room.setStartDate(startDate);//update room's available dates
                        room.setEndDate(endDate);//update room's available dates
                    }catch(ParseException e){
                        e.printStackTrace();
                    }

                    // Print room details
                    System.out.println("Room Name: " + room.getRoomName());
                    System.out.println("No. of Persons: " + room.getNoOfPersons());
                    System.out.println("Area: " + room.getArea());
                    System.out.println("No. of Reviews: " + room.getNoOfReviews());
                    System.out.println("Room Image: " + room.getRoomImage());
                    System.out.println("Available Dates: " + room.getStartDate() + " - " + room.getEndDate());

                    int hashResult = Hashfunction(room.getRoomName());//call Hashfunction with roomName as argument
                    workers[hashResult].addRoom(room);//send the room to the appropriate worker
                    System.out.println(room.getRoomName() + " stored in Worker" + hashResult);
                }

                System.out.println("Closing connection with" + socket.getInetAddress()+ ":" + socket.getPort());
                noOfConnections--;
                socket.close();
                in.close();
                if (noOfConnections == 0){
                    System.out.println("Server is empty. Waiting for Managers...");
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        private int Hashfunction(String roomName) {
            return roomName.length() % numberOfWorkers;
        }
    }
}