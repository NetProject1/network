
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBDAO {
	String id;
	String pw;
	
	Statement stmt=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	String url= "jdbc:oracle:thin:@localhost:1521:orcl"; //이부분은 데이터베이스 구현하고 ip port 변경할것
								//뒤에 orcl 은 12c 버젼임. 11g버전 오라클은 xe로 기본적으로 설정되있음
	
	//디비 사용자 아이디 패스워드
	String dbID="networkP";
	String dbPW="practice";
	
	String sql=null;
	Properties info= new Properties();
	
	Connection conn=null;
	
	//DB 접속
	void ConnectDB(){
		try {
			// 오라클 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//접속할 아이디
			
			info.setProperty("user", dbID);
			info.setProperty("password", dbPW);
			
			//url과 db사용자 아이디로 접속
			conn= DriverManager.getConnection(url, info);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ID, PASSWORD 를 받아와 검색
	int checkIDPW(String id, String pw){
		this.id=id;
		this.pw=pw;
		//검색결과 id와 pw가 일치하면 0, 틀릴시 1을 return
		int result=1;
		try {
			//db 접속
			ConnectDB();
			
			stmt=conn.createStatement();
			
			//쿼리문 실행
			sql="select * from player where id='" + id + "'";
			rs=stmt.executeQuery(sql);
			
			
			if(rs.next()==false|| id.isEmpty()){
				//쿼리문 실행 결과가 없는경우와 입력 id가 빈경우
				result=1;
				
			}else{
				//id가 있고 pw가 같은 경우인가?
				if(rs.getString("password").equals(pw)){
					result=0;
				}else{
					result=1;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	int signUp(String uid,String upw, String unickname){
		int result=1;
		try {
			//db 접속
			ConnectDB();
			
			//쿼리문 실행
			sql="select * from player where id='" + uid + "'";
			rs=stmt.executeQuery(sql);
			//아이디 같은 정보가 없다면.
			if(rs.next()==false){
				sql= "insert into player(id,password,nickname,money,win,lose) "+
						"values(?,?,?,?,?,?)";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, uid);
				pstmt.setString(2, upw);
				pstmt.setString(3, unickname);
				pstmt.setInt(4, 1000);
				pstmt.setInt(5, 0);
				pstmt.setInt(6, 0);

				int r=pstmt.executeUpdate();
				if(r>0){
					System.out.println("가입 성공");
					result=0;
				}else{
					System.out.println("가입 실패");
					result=1;
				}
				
			}else{
				result=1;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
