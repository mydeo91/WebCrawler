package setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BoardDAO extends DefaultDAO{

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		
		// JDBC driver name and database URL		
		String JDBC_DRIVER = "Driver Name";
	    String DB_URL = "jdbc:oracle:thin:DB_URL";
	    
	    // Database credentials
	    String USER = "USER_ID";
	    String PASS = "PASSWORD";
		
		// 1. Resister JDBC Driver
		Class.forName(JDBC_DRIVER);
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		
		return conn;
	}
	
	
	@Override
	public int insert(BoardDTO boardDTO) {
		return super.insert(boardDTO);
	}
	
}
