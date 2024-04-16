import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import java.net.Socket;

public class DummyClient{
    private Socket socket;//communication with master
    private BufferedReader in; //take input from user
    private DataOutputStream out;
    private int choice;
    private String filter;//send it to socket

    public DummyClient(String address, int port){
        try{ //try to establish connection
            socket = new Socket(address,port);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to our Booking Application");
            System.out.println("Search with Filters");
            System.out.println("1. Search by Areas");
            System.out.println("2. Search by Dates");
            System.out.println("3. Search by Number of Persons");
            System.out.println("4. Search within a price range");
            System.out.println("5. Search by Reviews");
            System.out.println("6. Exit");

            choice = scanner.nextInt();

            in = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());

            do {
                switch (choice){
                    case 1:
                        System.out.println("Select Area: ");
                        filter = scanner.nextLine();
                        out.writeUTF(filter);
                        break;
                    case 2:
                        System.out.println("Select preferred dates(dd/mm/yyyy - dd/mm/yyyy): ");
                        out.writeUTF(filter);
                        break;
                    case 3:
                        System.out.println("Select Maximum No of Persons: ");
                        out.writeUTF(filter);
                        break;
                    case 4:
                        System.out.println("Select Maximum Price per night: ");
                        out.writeUTF(filter);
                        break;
                    case 5:
                        System.out.println("Select Minimum Number of stars: ");
                        out.writeUTF(filter);
                        break;
                    case 6:
                        System.out.println("Exiting Searching Mode...");
                        out.writeUTF(filter);
                        break;
                    default:
                        System.out.println("Enter your choice of Area: ");
                        out.writeUTF(filter);
                        break;
                }

            } while(true);
        }
        catch(UnknownHostException e){
            System.out.println(e);
        }
        catch(IOException e){
            System.out.println(e);
        }
        finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket !=null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String mapsetup(String filter){
        switch (choice){
            case 1:
                return  "area";
            case 2:
                return  "availableDates";
            case 3:
                return "noOfPersons";
            case 4:
                return "pricePerNight";
            case  5:
                return  "stars";
            default:
                return null;

        }

    }

    public static void main(String args[]){
        ManagerApp client = new ManagerApp("127.0.0.1", 6666);
    }
}