package Server;

import java.sql.*;

public class UsersController {
    public void create(String username,String password) throws SQLException {
        Connection con = Database.getConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("insert into users(username,password) values (?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
            Database.rollback();
        }
    }
    public boolean checkIfUsernameExists(String username){
        Connection con = Database.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from users where username=''" + username+"''");
            rs.next();
            if (rs.getInt(1) != 0)
                return true;

            return false;
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            Database.rollback();
            return true;
        }
    }
    public Integer getIdByUsername(String username){
        Connection con = Database.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id from users where username=''" + username+"''");
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            Database.rollback();
            return null;
        }
    }

}
