package Server;

import java.io.PrintWriter;
import java.sql.*;

public class BooksController {
    public Integer getID(Integer isbn)
    {
        Connection con = Database.getConnection();
        try {
            Statement stmt = con.createStatement();
            String query="select books.ID from books WHERE isbn="+isbn.toString();
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Integer id=rs.getInt(1);
                return id;
            }
        } catch (SQLException e) {
            System.err.println(e);
            Database.rollback();
            return null;
        }
        return null;
    }
    public void list(PrintWriter out){
            Connection con = Database.getConnection();
            System.out.println("Listing all books:");
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select GENRE,NAME,TITLE,YEAR,ISBN,RATING from books join authors on authors.id=books.authorid");
                while (rs.next()) {
                    out.println(rs.getString(1));
                    out.println(rs.getString(2));
                    out.println(rs.getString(3));
                    out.println(rs.getInt(4));
                    out.println(rs.getInt(5));
                    out.println(rs.getInt(6));
                    out.flush();
                }
                out.println("-1");
                out.flush();
            } catch (SQLException e) {
                System.err.println(e);
                Database.rollback();
            }
    }
    public void addRating(Integer rating,Integer isbn)
    {
        Connection con = Database.getConnection();
        System.out.println("Adding rating...");
        try {
            PreparedStatement pstmt = con.prepareStatement("update books set rating = (rating * nrofratings + ?)/(nrofratings + 1), nrofratings = nrofratings + 1 where isbn =?");
            pstmt.setInt(1,rating);
            pstmt.setInt(2,isbn);
            pstmt.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void filter(String type,String pattern,PrintWriter out)
    {
        Connection con = Database.getConnection();
        System.out.println("Filtering...");
        try {
            Statement stmt = con.createStatement();
            String query="select GENRE,NAME,TITLE,YEAR,ISBN,RATING from books join authors ";
            if(type.equals("GENRE"))
                query=query+"where instr(lower(genre),lower('"+pattern+"'))!=0";
            if(type.equals("AUTHOR"))
                query=query+"where instr(lower(name),lower('"+pattern+"'))!=0";
            if(type.equals("TITLE"))
                query=query+"where instr(lower(title),lower('"+pattern+"'))!=0";
            if(type.equals("ISBN"))
                query=query+"where isbn="+pattern;
            if(type.equals("YEAR"))
                query=query+"where year="+pattern;
            if(type.equals("RATING"))
                query=query+"where rating="+pattern;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                out.println(rs.getString(1));
                out.println(rs.getString(2));
                out.println(rs.getString(3));
                out.println(rs.getInt(4));
                out.println(rs.getInt(5));
                out.println(rs.getInt(6));
            }
            out.println("-1");
        } catch (SQLException e) {
            System.err.println(e);
            Database.rollback();
        }
    }
}
