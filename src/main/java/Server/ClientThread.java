package Server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

// LOGARE = 1
// CREARECONT = 2
// DESCARCARE = 3
// CAUTARE = 4
// RATING = 5
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
            Integer currentUserID=-1;
            while (true) {
                String request = in.readLine();
                Integer command=Integer.parseInt(request);
                if(command==7)
                    break;
                switch(command){
                    case(1): {
                        String username = in.readLine();
                        String hashPassword = in.readLine();
                        currentUserID=logare(out,username,hashPassword);
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
                        descarcare(in,out);
                        break;
                    }
                    case(4):{
                        String type=in.readLine();
                        String pattern=in.readLine();
                        searchFor(type,request,out);
                        break;
                    }
                    case(5):{
                        Integer rating=Integer.parseInt(in.readLine());
                        Integer isbn=Integer.parseInt(in.readLine());
                        submitRating(rating,isbn);
                        break;
                    }
                    case(6):{
                        afisare_carti(out);
                        break;
                    }
                }
            }
            in.close();
            out.close();
            socket.close();
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
    public Integer logare(PrintWriter out,String username,String password){
            UsersController usersController = new UsersController();
            if (!usersController.checkIfUsernameExists(username)) {
                out.println(0);
                return -1;
            }
            out.println(1);
            return usersController.getIdByUsername(username);
    }
    public void afisare_carti(PrintWriter out){
        BooksController booksController=new BooksController();
        booksController.list(out);
    }
    public void descarcare(BufferedReader in,PrintWriter out)
    {
        try {
            String genre = in.readLine();
            String authorName=in.readLine();
            String title=in.readLine();
            Integer year=Integer.parseInt(in.readLine());
            Integer isbn=Integer.parseInt(in.readLine());
            Integer rating=Integer.parseInt(in.readLine());
            BooksController booksController=new BooksController();
            Integer id=booksController.getID(genre,authorName,title,year,isbn,rating);
            if(id==null)
                out.println(0);
            else
                out.println(1);
            File file = new File("D:\\JavaProject\\reads-profiler-server\\src\\Books\\"+id.toString()+".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
                out.println(st);
            out.println("-1");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    public void submitRating(Integer rating,Integer isbn)
    {
        BooksController booksController=new BooksController();
        booksController.addRating(rating,isbn);
    }
    public void searchFor(String type,String pattern,PrintWriter out)
    {
        BooksController booksController=new BooksController();
        booksController.filter(type,pattern,out);
    }

}
