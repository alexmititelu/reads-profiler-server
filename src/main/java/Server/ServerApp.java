package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private static final int PORT = 8100;
    private ServerSocket serverSocket;
    private boolean running = false;

    public static void main(String[] args) {
        ServerApp server = new ServerApp();
        server.init();
        server.waitForClients(); //... handle the exceptions!
    }

    // Implement the init() method: create the serverSocket and set running to true
    public void init()
    {
        try {
            serverSocket = new ServerSocket(PORT);
            running=true;
        }
        catch(IOException e){e.printStackTrace(); return;}

    }

    // Implement the waitForClients() method: while running is true, create a new socket for every incoming client and start a ClientThread to execute its request.
    public void waitForClients(){
        try{
            while(running==true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("New Client");
                // Executam solicitarea clientului intr -un fir de executie
                new ClientThread(this,socket).start();

            }

        }
        catch(IOException e)
        {
            e.printStackTrace(); return;
        }
    }
    public void stop() throws IOException {
        this.running = false;
        serverSocket.close();
    }
}
