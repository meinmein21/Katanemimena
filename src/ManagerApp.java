import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.net.*;
import java.io.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

public class ManagerApp{
    private Socket socket;//communication with master
    private BufferedReader in; //take input from user
    private DataOutputStream out; //send it to socket

    public ManagerApp(String address, int port){
        try{ //try to establish connection
            socket = new Socket(address,port);
            System.out.println("Conncected"); //we didnt connect to the server. We connected to the socket

            in = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                System.out.print("Enter Room's path or type 'Exit' to exit ");
                String filePath = in.readLine();//.json's file path

                if (filePath.equalsIgnoreCase("Exit")) {
                    out.writeUTF("Goodbye"); //if user typed "exit", send goodbye message to master to stop the connection
                    break;
                }

                StringBuilder jsoncontent = new StringBuilder();//store json's file data
                try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {//reading the file
                    String line ;
                    while ((line = fileReader.readLine()) != null){
                        jsoncontent.append(line);
                    }

                }
                out.writeUTF(jsoncontent.toString());//turns the json file into a string and sends it to master
                System.out.println("Room's data sent to the server");
            }
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

    public static void main(String args[]){
        ManagerApp client = new ManagerApp("127.0.0.1", 6666);
    }
}