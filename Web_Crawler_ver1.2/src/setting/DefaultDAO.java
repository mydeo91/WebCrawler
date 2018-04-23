package setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 추상 클래스-메소드로 선언
public abstract class DefaultDAO {
	// DB 연결 및 입력 클래스
	
	// 데이터 입력 메소드
    public int insert(BoardDTO boardDTO) {
    	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
        int result = 0;

        try{
        	
        	// DB Connection
        	conn = getConnection();
        	
        	//STEP 4: Execute a query
            String sql = "INSERT INTO CRAWL_DATA (BOARD_SEQ,SUBJECT,CONTENT,REG_NM,DEPT_NM,REG_DT,READ_CNT,CRAWL_DT,ATTACH_FILE_NAME) VALUES ((SELECT NVL(MAX(BOARD_SEQ)+1, 1) FROM CRAWL_DATA), ?, ?, ?, ?, TO_DATE(REPLACE(REPLACE(?,'.',''),'-',''),'YYYYMMDD'), ?, SYSDATE,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, boardDTO.getSubject());
            pstmt.setString(2, boardDTO.getContent());
            pstmt.setString(3, boardDTO.getReg_nm());
            pstmt.setString(4, boardDTO.getDept_nm());
            pstmt.setString(5, boardDTO.getReg_dt());
            pstmt.setInt(6, Integer.parseInt(boardDTO.getRead_cnt()));
            pstmt.setString(7, boardDTO.getAttach_file_name());
            
            result = pstmt.executeUpdate();
        	
            //STEP 6: Clean-up environment
            pstmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
                if(pstmt!=null) pstmt.close();
            } catch(SQLException se2) {
            }// nothing we can do

            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        
        return result;
    }//end main
	
    // Drive Connection
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
    
}
