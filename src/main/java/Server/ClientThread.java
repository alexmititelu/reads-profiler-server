package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

// LOGARE = 1
// CREARECONT = 2
// DESCARCARE = 3
// CAUTARE = 4
// SUGESTII = 5
// TOATE_CARTILE = 6
// DELOGARE = 7
public class ClientThread extends Thread{
    private Socket socket = null;
    private final ServerApp server;
    private BufferedReader in;
    PrintWriter out;

    public ClientThread(ServerApp server,Socket socket) {
        this.socket = socket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //client -> server stream
            PrintWriter out = new PrintWriter(socket.getOutputStream()); //server -> client stream
            while (true) {
                String request = in.readLine();
                Integer command=Integer.parseInt(request);
                if(command==7)
                    break;
                switch(command){
                    case(1): {
                        String username = in.readLine();
                        String hashPassword = in.readLine();
                        logare(out,username,hashPassword);
                        break;
                    }
                    case(2):
                    {
                        String username = in.readLine();
                        String hashPassword = in.readLine();
                        creare_cont(out,username,hashPassword);
                        break;
                    }
                    case(3):{

                    }
                }
            }
            in.close();
            out.close();
            socket.close(); //... usse try-catch-finally to handle the exceptions!
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }
    }
    public void creare_cont(PrintWriter out,String username,String password){
        try {
            UsersController usersController = new UsersController();
            if (usersController.checkIfUsernameExists(username) == true) {
                out.println(0);
                return;
            }
            usersController.create(username, password);
            out.println(1);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void logare(PrintWriter out,String username,String password){
            UsersController usersController = new UsersController();
            if (!usersController.checkIfUsernameExists(username)) {
                out.println(0);
                return;
            }
            out.println(1);
    }
}
