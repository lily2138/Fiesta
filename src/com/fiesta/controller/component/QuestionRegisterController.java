package com.fiesta.controller.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fiesta.controller.Controller;
import com.fiesta.controller.ModelAndView;
import com.fiesta.model.dao.QuestionDaoImpl;

public class QuestionRegisterController implements Controller{

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int companycode = Integer.parseInt(request.getParameter("companycode"));
		System.out.println(companycode);
		String qTitle = request.getParameter("qTitle");
		String qDesc = request.getParameter("qDesc");
		String custEmail = "encore@gmail.com";   //나중에 세션값에서 받아오기
		String path = "";

		QuestionDaoImpl.getInstance().insertQuestion(companycode, qTitle, qDesc, custEmail);
		
/*		request.setAttribute("comCode", comCode);
		request.setAttribute("qTitle", qTitle);
		request.setAttribute("qDesc", qDesc);
		*/
		
		path = "ServiceAllShow.do?companycode="+companycode;
		
		response.sendRedirect("ServiceAllShow.do?companycode="+companycode);
		
		//return new ModelAndView(path);
		return null;
	}

}