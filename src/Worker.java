import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Worker extends Thread {
    private ServerSocket serverSocket;
    private Socket reducersocket;
    private int id;
    private List<Room> rooms;
    private String reducerHost;
    //private int reducerPort = 9999;

    //public static Reducer reducer;


    public Worker(int id) {
        this.id = id;
        this.rooms = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(7777 + id); // Each worker has its own port (7777, 7778, 7779, ...)

        } catch (IOException e) {
            System.out.println("Error creating worker server socket: " + e.getMessage());
        }
    }





    @Override
    public void run() {
        try {

            while (true) {


                Socket socket = serverSocket.accept();
                System.out.println("Received room from Master on Worker " + id);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Room room = (Room) objectInputStream.readObject();
                rooms.add(room);
                System.out.println("Room added to Worker " + id + ": " + room.getRoomName());
                socket.close();



            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error in Worker " + id + ": " + e.getMessage());
        }
    }

    //this method should be synchronized as many threads may use it concurrently
    public synchronized void addRoomManager(Room room) {
        rooms.add(room);
        System.out.println("Room added to Worker " + id + ": " + room.getRoomName());
    }

    public List<Room> getRooms() {
        return rooms;
    }
    //Map function that add key and rooms to lists
    public void  Map(String key,String value) throws IOException {
        List<Room> FilteredRooms = new ArrayList<>();
        for (Room room:rooms) {
            switch (key) {
                case "area":
                    if(room.getArea().equalsIgnoreCase(value)){
                        FilteredRooms.add(room);
                    }
                    break;
                case "availableDates":
                    if (room.getAvailableDates().contains(value)){
                        FilteredRooms.add(room);
                    }
                    break;
                case "noOfPersons":
                    if (room.getNoOfPersons() == Integer.parseInt(value)) {
                        FilteredRooms.add(room);
                    }
                    break;
                case "noOfReviews":
                    if (room.getNoOfReviews() == Integer.parseInt(value)){
                        FilteredRooms.add(room);
                    }
                    break;

            }
        }
        //return  FieredRooms;
        ObjectOutputStream out = new ObjectOutputStream(reducersocket.getOutputStream());
        out.writeObject(FilteredRooms); // Αποστολή των φιλτραρισμένων δωματίων στον Reducer
        out.flush();



    }
    public void setReducerSocket(Socket reducersocket) {
        this.reducersocket = reducersocket;
    }

    //socket = new Socket();
    //OUT LISTA
   // Reducer reducer = new Reducer(reducerPort);


}