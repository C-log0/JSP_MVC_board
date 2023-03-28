package action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

public class BoardListAction_backup {
	// 컨트롤러로부터 호출받아 글목록 비즈니스 로직을 수행할 execute() 메서드 정의
	// => 파라미터 : request, response 객체  리턴타입 : ActionForward
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null; // 선언부를 맨위로 이동
		
		// 글목록 조회했다고 가정

		// 포워딩 정보를 저장할 ActionForward 인스턴스 생성(forward)
		forward = new ActionForward();
		// 포워딩 경로 저장 => "board/qna_board_list.jsp"
		forward.setPath("board/qna_board_list.jsp");
		// 포워딩 방식 저장 => Dispatch 방식
		forward.setRedirect(false);
		
		return forward;// BoardFrontController
				
	
	}
}
