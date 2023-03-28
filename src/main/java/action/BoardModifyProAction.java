package action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import action.Action;
import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		
		ActionForward forward = null;
		
		String realPath = "";
		// 수정 작업 결과에 따라 삭제할 파일이 달라지므로 파일명을 저장할 변수 선언
		String deleteFileName = "";
		
		try {
			
			String uploadPath = "upload"; //업로드 가상 디렉토리(이클립스가 관리)
			realPath = request.getServletContext().getRealPath(uploadPath);  
			System.out.println("실제 업로드 경로 : " + realPath);
			// D:\Shared\JSP\workspace_jsp5\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MVC_Board\ upload
			
			// 만약, 해당 디렉토리가 존재하지 않을 경우 디렉토리 생성
			// => java.io.File 클래스 인스턴스 생성(파라미터로 해당 디렉토리 전달)
			File f = new File(realPath);
			// 단, File 객체가 생성되더라도 해당 디렉토리 또는 파일을 직접 생성 X
			// 실제 경로에 대상 존재 여부 판별
			if(!f.exists()) { // 해당 경로가 존재하지 않을 경우
				// File 객체의 mkdir() 메서드를 호출하여 경로 생성  
				f.mkdir();
			
			}
			
			int fileSize = 1024 * 1024 * 10;
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
			board.setBoard_num(Integer.parseInt(multi.getParameter("board_num")));
			board.setBoard_pass(multi.getParameter("board_pass"));
			board.setBoard_subject(multi.getParameter("board_subject"));
			board.setBoard_content(multi.getParameter("board_content"));
			board.setBoard_file(multi.getOriginalFileName("board_file"));
			board.setBoard_real_file(multi.getFilesystemName("board_file"));
//			System.out.println(board);
			// => 만약, 수정할 파일을 선택하지 않았을 경우 파일 명은 null값이 저장됨
			
	
			// BoardModifyProService - isBoardWriter() 호출하여 패스워드 일치 여부 확인
			// => 파라미터 : BoardBean 객체    리턴타입 : boolean(isBoardWriter)
			BoardModifyProService service = new BoardModifyProService();
			boolean isBoardWriter = service.isBoardWriter(board);
			
			// 만약, 게시물 수정 권한이 없는 경우 (= 패스워드 틀림)
			// 자바스크립트 사용하여 "수정 권한이 없습니다!" 출력 후 이전페이지로 돌아가기
			// 아니면, 수정 작업 요청 수행
			if(!isBoardWriter) { // 수정 권한 없음
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('수정 권한이 없습니다!')");
				out.println("history.back()");
				out.println("</script>");
				
				// 삭제할 파일명을 새 파일명의 실제 파일명으로 지정
				deleteFileName = board.getBoard_real_file();
				
			} else { // 수정 권한 있음
				// BoardModifyProService - modifyBoard 호출하여 글 수정 작업 요청
				// => 파라미터 : BoardBean 객체    리턴타입 : boolean(isModifySuccess)
				boolean isModifySuccess = service.modifyBoard(board);
			
				
				// 수정 결과를 판별하여 실패 시 자바스크립트 오류 메세지 출력 및 이전페이지로 이동하고
				// 성공 시 ActionForward 객체를 통해 "BoardDetail.bo" 페이지로 포워딩(Redirect)
				// (=> URL 에 글번호와 페이지 번호를 붙여서 요청)
				if(!isModifySuccess) { // 수정 실패 시 
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('수정 실패!')");
					out.println("history.back()");
					out.println("</script>");
					
					// 삭제할 파일명을 새 파일명의 실제 파일명으로 지정
					deleteFileName = board.getBoard_real_file();
					
				} else { // 수정 성공 시
					forward = new ActionForward();
					forward.setPath("BoardDetail.bo?board_num=" + board.getBoard_num() +"&pageNum=" + multi.getParameter("pageNum"));
					forward.setRedirect(true);
					
					// 삭제할 파일명을 기존 파일의 실제 파일명으로 지정
					// => hidden 속성으로 전달받은 기존 파일명에 대한 파라미터 사용
					// => 단, 수정할 새 파일을 선택했을 경우에만 파일명 지정
					if(board.getBoard_file() != null) {
						deleteFileName = multi.getParameter("board_real_file");
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 예외가 발생하더라도 파일 삭제는 무조건 수행하도록 finally 블록 작성
			// File 객체 생성(파라미터로 디렉토리명, 파일명 전달)
			File f = new File(realPath, deleteFileName);
			
			// 해당 디렉토리 및 파일 존재 여부 판별
			if(f.exists()) { // 존재할 경우
				// File 객체의 delete() 메서드를 호출하여 해당 파일 삭제
				f.delete();
				
			}
		}
		
		
		return forward;
	}

}
