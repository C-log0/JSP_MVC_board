package action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import action.Action;
import svc.BoardReplyProService;
import svc.BoardWriteProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
		
		try {
			String uploadPath = "upload"; //업로드 가상 디렉토리(이클립스가 관리)
			// 업로드 실제 디렉토리(톰캣) 얻어오기
			String realPath = request.getServletContext().getRealPath(uploadPath);  
			System.out.println("실제 업로드 경로 : " + realPath);
			// D:\Shared\JSP\workspace_jsp5\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MVC_Board\ upload
			int fileSize = 1024 * 1024 * 10;
			
			//------------------------------------------------------
			// 게시물 작성자(= 클라이언트)의 IP주소 얻어와야 할 경우
			 String writerIpaddr = request.getRemoteAddr();
			 System.out.println("작성자 ip 주소 : " + writerIpaddr);
			 
			//---------------------------------------------------------
			 //파일 업로드 처리를 위해(enctype="multipart/form-data")
			 // MultipartRequest 객체 생성 => cos.jar 라이브러리 필요
			 MultipartRequest multi = new MultipartRequest(
					 request, //1) 실제 요청 정보(파라미터)가 포함된 request 객체
					 realPath,  //2) 실제 업로드 경로
					 fileSize,  //3) 업로드 파일 최대 사이즈
					 "UTF-8",  //4) 한글 파일명 처리 위한 인코딩 방식
					 new DefaultFileRenamePolicy() //5) 중복 파일명 처리할 객체
			);
			 
			// 전달받은 파라미터 데이터를 BoardBean 클래스 인스턴스 생성 후 저장
			BoardBean board = new BoardBean();
			 
			board.setBoard_name(multi.getParameter("board_name"));
			board.setBoard_pass(multi.getParameter("board_pass"));
			board.setBoard_subject(multi.getParameter("board_subject"));
			board.setBoard_content(multi.getParameter("board_content"));
			board.setBoard_file(multi.getOriginalFileName("board_file"));
			board.setBoard_real_file(multi.getFilesystemName("board_file"));
			// 입력받지 않고 hidden 속성으로 전달한 ref, lev, seq 값도 저장 필수! 
			board.setBoard_re_ref(Integer.parseInt(multi.getParameter("board_re_ref")));
			board.setBoard_re_lev(Integer.parseInt(multi.getParameter("board_re_lev")));
			board.setBoard_re_seq(Integer.parseInt(multi.getParameter("board_re_seq")));
//			System.out.println(board);
						
			//만약, 파일명이 null일 경우 널스트링으로 교체(답글은 파일 업로드가 선택사항)
			if(board.getBoard_file() == null) {
				board.setBoard_file("");
				board.setBoard_real_file("");
			}
			

			// -------------------------------------------------------------------------
			// BoardReplyProService 클래스 인스턴스 생성 후
			// registReplyBoard() 메서드를 호출하여 글쓰기 작업 요청
			// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isWriteSuccess)
			BoardReplyProService service = new BoardReplyProService();
			boolean isWriteSuccess = service.registReplyBoard(board);
			
			
			// 글쓰기 요청 처리 결과 판별
			if(!isWriteSuccess) { // 실패 시
				// 업로드 된 실제 파일 삭제
				File f = new File(realPath, board.getBoard_real_file());
				
				// 해당 디렉토리 및 파일 존재 여부 판별
				if(f.exists()) { // 존재할 경우
					// File 객체의 delete() 메서드를 호출하여 해당 파일 삭제
					f.delete();
					
				}
				// 자바스크립트 사용하여 "글쓰기 실패!" 출력 후 이전페이지로 돌아가기
				// => 웹브라우저로 HTML 태그 등을 내보내기(출력) 위한 작업 수행
				//    (자바 클래스 내에서 출력스트림을 활용하여 HTML 태그 출력해야함)
				// => 응답 데이터 생성을 위해 응답 객체인 response 객체 활용
				// 1) 출력할 HTML 형식에 대한 문서 타입(contentType) 설정
				//    => 응답 데이터의 타입으로 HTML 태그가 사용됨을 클라이언트에게 알려줌
				//    => response 객체의 setContentType() 메서드를 호출하여 문서 타입 전달
				//       (jsp 파일 최상단의 page 디렉티브 내의 contentType=XXX 항목 활용)
				response.setContentType("text/html; charset=UTF-8");
				
				// 2) 자바 코드를 사용하여 HTML 태그 등을 출력(전송)하려면
				//    java.io.PrintWriter 객체가 필요함(= 출력스트림으로 사용할 객체)
				//    => response 객체의 getWriter() 메서드를 호출하여 얻어올 수 있다!
				PrintWriter out = response.getWriter();
				
				// 3) PrintWriter 객체의 print() 또는 println() 메서드를 호출하여
				//    파라미터로 HTML 태그 등의 코드를 문자열 형태로 전달
				out.println("<script>");
				out.println("alert('글쓰기 실패!')");
				out.println("history.back()");
				out.println("</script>");
				// ActionForward 객체 생성하지 않음!! => null 값 전달
				
			} else { // 성공시
				// 포워딩 정보 저장을 위한 ActionForward 객체 생성 후
				// 경로 : BoardList.bo, 포워딩 방식 : Redirect
				//=> 페이지 번호 전달
				forward = new ActionForward();
				forward.setPath("BoardList.bo?pageNum=" + multi.getParameter("pageNum"));
				forward.setRedirect(true);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return forward;
	}

}
