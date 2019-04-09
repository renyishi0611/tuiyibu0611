package com.none.web.QRcode;

import java.sql.*;
public class TestInsert {
 public static void main(String[] args) {
  Connection conn = null;
  Statement stmt = null;
  try {
   Class.forName("com.mysql.jdbc.Driver");
   conn = DriverManager
     .getConnection("jdbc:mysql://localhost/staffapp?useUnicode=true&characterEncoding=utf-8&user=root&password=");
   stmt = conn.createStatement();
   String sql = "insert into t_user_app(user_id,chinese_name) values('1211','人村')";
   stmt.executeUpdate(sql);
  } catch (Exception e) {
   e.printStackTrace();
  } finally {
   try {
    stmt.close();
    conn.close();
   } catch (SQLException e) {
    e.printStackTrace();
   }
  }
 }
}
