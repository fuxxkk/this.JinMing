package com.myhadoop.log;


import java.sql.*;
import java.util.Map;

public class DBLoader {

    public static void getINFO(Map<String, String> map) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", "root", "1234");
            ps =  con.prepareStatement("select url,content from url_rule");
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getString(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (con!=null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

}
