package study.dao;

import study.model.User;

import java.sql.*;

public class UserDao {

    ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public long add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(name, password) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());

        int effectCount = ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        long generatedKey = 0;
        if (rs.next()) {
            generatedKey = rs.getInt(1);
        }

        ps.close();
        c.close();

        return generatedKey;
    }

    public User get(Long id) throws ClassNotFoundException, SQLException {
        try {

            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE id = ?");
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

            rs.close();
            ps.close();
            c.close();

            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public User get(String name) throws ClassNotFoundException, SQLException {
        try {

            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE name = ?");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            rs.next();
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

            rs.close();
            ps.close();
            c.close();

            return user;
        } catch (SQLException e) {
            return null;
        }
    }
}
