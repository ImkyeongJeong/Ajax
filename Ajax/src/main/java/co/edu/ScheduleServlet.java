package co.edu;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ScheduleServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		
		EmpDAO dao = new EmpDAO();
		List<Schedule> list = dao.scheduleList();
		Gson gson = new GsonBuilder().create();
		response.getWriter().print(gson.toJson(list));
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String job = request.getParameter("job"); 		//job이란 파라미터를 통해 등록, 삭제인지 구분
		String title = request.getParameter("title");
		String sd = request.getParameter("startDate");
		String ed = request.getParameter("endDate");
		
		//insertSchedule,deleteSchedule을 매개값으로 실행하기 위한 Schedule필드 초기화
		Schedule sch = new Schedule();
		sch.setTitle(title);
		sch.setStart(sd);
		sch.setEnd(ed);
		
		EmpDAO dao = new EmpDAO();
		if(job.equals("add")) { 		//job으로 들어온 파라미터값이 add(등록)이면 insert처리
			dao.insertSchedule(sch);
			//{"retCode":"Success"}
			response.getWriter().print("{\"retCode\":\"Success\"}"); //응답의 프린트 설정
		} else if (job.equals("del")) { //job으로 들어온 값이 del(삭제)이면 delete처리
			dao.deleteSchedule(sch);
			response.getWriter().print("{\"retCode\":\"Success\"}");
		} else {
			response.getWriter().print("{\"retCode\":\"No Request\"}");
		}
	}

}
