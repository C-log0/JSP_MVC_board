package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardReplyProService {

	// 글쓰기 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isWriteSuccess)
		public boolean registReplyBoard(BoardBean board) {
			boolean isWriteSuccess = false;
			
			// 공통작업-1. Connection 객체 가져오기
			Connection con = JdbcUtil.getConnection();
			
			// 공통작업-2. BoardDAO 객체 가져오기
			BoardDAO dao = BoardDAO.getInstance();
			
			// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
			dao.setConnection(con);
			
			// BoardDAO 객체의 xxx() 메서드를 호출하여 xxx 작업 수행 요청 및 결과 리턴받기
			//    insertBoard() 메서드를 호출하여 글쓰기 작업 요청 및 결과 리턴받기
			// => 파라미터 : BoardBean 객체   리턴타입 : int(insertCount)
			int insertCount = dao.insertReplyBoard(board);
			
			// 6. 작업 처리 결과에 따른 트랜잭션 처리
			if(insertCount > 0) { // 성공 시
				JdbcUtil.commit(con);
				isWriteSuccess = true;
			} else { // 실패 시
				JdbcUtil.rollback(con);
			}
			
			// 공통작업-4. Connection 객체 반환하기
					JdbcUtil.close(con);
			
			return isWriteSuccess; // BoardWriteProAction 으로 리턴
		}

	}