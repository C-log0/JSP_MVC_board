package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.ActionForward;


public class MemberLogoutAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		
		forward = new ActionForward();
		forward.setPath("index.jsp");
		forward.setRedirect(true); //생략 가능
		
		
		return forward;
		
		
	}

}
