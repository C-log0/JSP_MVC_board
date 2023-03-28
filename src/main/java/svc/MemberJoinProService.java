package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberJoinProService {

	public boolean joinMember(MemberBean member) {
		System.out.println("MemberJoinProService - joinMember()");
		
		// 1. 회원가입 작업 요청 처리 결과를 저장할 boolean 타입 변수 선언
				boolean isJoinSuccess = false;
				
				// 2. JdbcUtil 객체로부터 Connection Pool 에 저장된 Connection 객체 가져오기(공통)
				// => 트랜잭션 처리를 Service 객체에서 수행하므로
				//    DAO 가 아닌 Service 가 Connection 객체를 관리함
				Connection con = JdbcUtil.getConnection();
				
				// 3. MemberDAO 클래스로부터 MemberDAO 객체 가져오기(공통)
				// => 싱글톤 디자인 패턴으로 구현되어 있는 객체를 getInstance() 메서드로 리턴받기
				MemberDAO dao = MemberDAO.getInstance(); //new~ 대신
				
				// 4. MemberDAO 객체의 setConnection() 메서드를 호출하여 Connection 객체 전달(공통
				dao.setConnection(con);
				
			
				// 5. MemberDAO 객체의 xxx() 메서드를 호출하여 xxx 작업 수행 요청 및 결과 리턴받기
				//    insertMember() 메서드를 호출하여 글쓰기 작업 요청 및 결과 리턴받기
				// => 파라미터 : MemberBean 객체   리턴타입 : int(insertMember)
				int insertCount = dao.insertMember(member);
				
				// 6. 작업 처리 결과에 따른 트랜잭션 처리
				if(insertCount > 0) { // 성공 시
					// INSERT 작업 성공했을 경우의 트랜잭션 처리(commit) 을 위해
					// JdbcUtil 클래스의 commit() 메서드를 호출하여 commit 작업 수행
					// => 파라미터 : Connection 객체
					JdbcUtil.commit(con);
					
					// 작업 처리 결과를 성공으로 표시하여 리턴하기 위해 isWriteSuccess 를 true 로 변경
					isJoinSuccess = true;
					
				} else {
					JdbcUtil.rollback(con);
				}
		return isJoinSuccess;
	}

	}
