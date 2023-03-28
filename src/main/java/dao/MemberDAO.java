package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.JdbcUtil;
import vo.BoardBean;
import vo.MemberBean;

public class MemberDAO {
	// ------------ 싱글톤 디자인 패턴을 활용한 MemberDAO 인스턴스 생성 작업 -------------
	private static MemberDAO instance = new MemberDAO();
	
	private MemberDAO() {}
	
	public static MemberDAO getInstance() {
		return instance;
	}

	// ----------------------------------------------------------------------------------
	// 데이터베이스 접근에 사용할 Connection 객체를 Service 객체로부터 전달받기 위한
	// Connection 타입 멤버변수 선언 및 Setter 메서드 정의
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
	
	//-----------------------------------------------------------------------------------
	// 회원 추가
	public int insertMember(MemberBean member) {
		int insertCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "INSERT INTO member VALUES (?,?,?,?,?,now())";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPasswd());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getGender());
			System.out.println(member);
			insertCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류! - insertMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
			
		}
		
		return insertCount;
	}
	
	// 로그인
	public int selectMember(MemberBean member) {
		int isloginSucess = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// member 테이블에서 아이디, 비밀번호가 일치하는 1개 레코드 조회
			String sql = "SELECT passwd FROM member WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있을 경우
			if(rs.next()) { // 아이디가 일치할 경우
				//조회된 패스워드를 입력받은 패스워드와 비교
				if(member.getPasswd().equals(rs.getString("passwd"))) { //패스워드 일치할 경우
					isloginSucess = 1;
				} else { // 패스워드 불일치할 경우
					isloginSucess = 0;
				} 
			} else { // 아이디가 일치하지 않을 경우(= 아이디가 존재하지 않음)
				isloginSucess = -1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// DB 자원 반환
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isloginSucess;
	}

	// 모든 회원 목록 조회 selectMemberList()
	public List<MemberBean> selectMemberList() {
		// 리턴할 ArrayList 타입 변수 선언
		List<MemberBean> memberList = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 전체 회원 목록 조회
			String sql = "SELECT * FROM member";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			// 전체 회원정보를 저장할 List 객체 생성
			memberList = new ArrayList<MemberBean>();
			
			while (rs.next()) {
				// 1명의 회원 정보를 저장할 MemberBean 객체 생성
				MemberBean member = new MemberBean();
				
				member.setName(rs.getString("name"));
				member.setId(rs.getString("id"));
				member.setPasswd(rs.getString("passwd"));
				member.setEmail(rs.getString("email"));
				member.setGender(rs.getString("gender"));
				member.setDate(rs.getDate("date"));
				
//				System.out.println(member.toString());
				
				//List객체에 memberBean 객체 추가
				memberList.add(member);
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - selectMemberList()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return memberList;
		
	}

}