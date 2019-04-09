package com.none.web.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection_jdbc {
	     static Connection con=null;
	    final static Connection_jdbc connection_jdbc=new Connection_jdbc();
	     
        public Connection getCon() {
        	InputStream in;
        	Properties p = new Properties();   
			try {
				in =  Connection_jdbc.class.getResourceAsStream("/SystemConfig.properties");
	        	p.load(in); 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}   
        	//连接MySql数据库
		   	     String urls = p.getProperty("dataSource.url") ;    
		   	     String username = p.getProperty("dataSource.username") ;   
		   	     String password = p.getProperty("dataSource.password") ;   
	   	     try{   
	   	    	    //加载MySql的驱动类   
	   	    	    Class.forName(p.getProperty("dataSource.driverClassName")) ;   
	   	     }catch(ClassNotFoundException e){   
	   	    	    System.out.println("找不到驱动程序类 ，加载驱动失败！");   
	   	    	    e.printStackTrace() ;   
	   	    	    }   	   
	   	     
	   	     try{   
	   			    con = DriverManager.getConnection(urls , username , password ) ;   
	   	     }catch(SQLException se){   
	   		    System.out.println("数据库连接失败！");   
	   		    se.printStackTrace() ;   
	   	     }
			return con;
		}
		
		private Connection_jdbc(){

		}
         public static Connection_jdbc   getInstance(){
        	 Connection_jdbc conn_jdbc=connection_jdbc;
        	 if(conn_jdbc==null){
        		 conn_jdbc=new Connection_jdbc();
        	 }
        	 return conn_jdbc;
         }
         public static void colseConnection(){
        	 if(con!=null){
        		 try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	 }
         }
}
