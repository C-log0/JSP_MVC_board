package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;

public class BoardDeleteProService {

	// 글 삭제 가능 여부(= 패스워드 일치 여부) 판별 요청 수행할 isBoardWriter() 메서드 정의
	// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
	public boolean isBoardWriter(int board_num, String board_pass) {
		boolean isBoardWriter = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 의 isBoardWriter() 메서드를 호출하여 패스워드 확인 작업 수행
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
		isBoardWriter = dao.isBoardWriter(board_num, board_pass);
		
		// 공통작업-4. Connection 객체 반환하기
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}

	// 글번호(board_num)에 해당하는 게시물 삭제 작업 수행하는 removeBoard() 메서드
	//  => 파라미터 : 글번호(board_num)    리턴타입 : int(deleteCount)
	public boolean removeBoard(int board_num) {
			boolean isDeleteSuccess = false;
			
			// 공통작업-1. Connection 객체 가져오기
			Connection con = JdbcUtil.getConnection();
			
			// 공통작업-2. BoardDAO 객체 가져오기
			BoardDAO dao = BoardDAO.getInstance();
			
			// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
			dao.setConnection(con);
			
			// BoardDAO 의 deleteBoard() 메서드를 호출하여 글 삭제 작업 수행
			// => 파라미터 : 글번호    리턴타입 : int(deleteCount)
			int deleteCount = dao.deleteBoard(board_num);
			
			// 리턴받은 결과를 판별하여 commit, rollback
			if(deleteCount > 0) {
				JdbcUtil.commit(con);
				isDeleteSuccess = true;
			} else {
				JdbcUtil.rollback(con);
			}
			
			// 공통작업-4. Connection 객체 반환하기
			JdbcUtil.close(con);
			
		
		return isDeleteSuccess;
	}

}




