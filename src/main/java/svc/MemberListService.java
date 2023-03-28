package svc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberListService {

	// 회원 목록 조회 - getMemberList()
	public List<MemberBean> getMemberList() {
		List<MemberBean> memberList = null;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. MemberDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 공통작업-3. MemberDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// MemberDAO 객체의 selectMemberList() 메서드를 호출하여 답글 쓰기 작업 요청
		// => 파라미터 : 없음   리턴타입 : List<MemberBean>(memberList)
		memberList = dao.selectMemberList();

		// 공통작업-4. Connection 객체 반환하기
		JdbcUtil.close(con);
		
		return memberList;
	}

}