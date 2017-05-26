
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBLogin {
	String id;
	String pw;
	
	Statement stmt=null;
	ResultSet rs=null;
	String url= "jdbc:oracle:thin:@localhost:1521:orcl"; //이부분은 데이터베이스 구현하고 ip port 변경할것
								//뒤에 orcl 은 12c 버젼임. 11g버전 오라클은 xe로 기본적으로 설정되있음
	
	//디비 사용자 아이디 패스워드
	String dbID="networkP";
	String dbPW="practice";
	
	String sql=null;
	Properties info=null;
	
	Connection conn=null;
	
	//ID, PASSWORD 를 받아와 검색
	int checkIDPW(String id, String pw){
		this.id=id;
		this.pw=pw;
		//검색결과 id와 pw가 일치하면 0, 틀릴시 1을 return
		int result=1;
		try {
			// 오라클 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//접속할 아이디
			info= new Properties();
			info.setProperty("user", dbID);
			info.setProperty("password", dbPW);
			
			//url과 db사용자 아이디로 접속
			conn= DriverManager.getConnection(url, info);
			
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
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
}
