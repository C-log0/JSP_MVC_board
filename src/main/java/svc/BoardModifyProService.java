package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardModifyProService {
	// 글 수정 가능 여부(= 패스워드 일치 여부) 판별 요청 수행할 isBoardWriter() 메서드 정의
	// => 파라미터 : BoardBean 객체(board)    리턴타입 : boolean(isBoardWriter)
	public boolean isBoardWriter(BoardBean board) {
		boolean isBoardWriter = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 의 isBoardWriter() 메서드를 호출하여 패스워드 확인 작업 수행
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
		isBoardWriter = dao.isBoardWriter(board.getBoard_num(), board.getBoard_pass());
		
		// 공통작업-4. Connection 객체 반환하기
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}

	// 글 수정 작업 요청
	// => 파라미터 : BoardBean 객체    리턴타입 : boolean(isModifySuccess)
	public boolean modifyBoard(BoardBean board) {
		boolean isModifySuccess = false;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 의 updateBoard() 메서드를 호출하여 글 수정 작업 수행
		// => 파라미터 : BoardBean 객체(board)    리턴타입 : int(updateCount)
		int updateCount = dao.updateBoard(board);
		
		// 글 수정 결과 판별 -> 성공 시 commit, 실패 시 rollback
		if(updateCount > 0) {
			JdbcUtil.commit(con);
			isModifySuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		
		// 공통작업-4. Connection 객체 반환하기
		JdbcUtil.close(con);
		
		return isModifySuccess;
	}
}











