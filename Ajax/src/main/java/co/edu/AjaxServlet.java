package co.edu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@WebServlet({ "/AjaxServlet", "/ajax.do" })
public class AjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AjaxServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//HttpServletRequest : 사용자가 서버쪽으로 데이터 요청 할 때, 요청에 대한 기능, 속성을 갖고 있는 객체
		//HttpServletResponse: 서버에서 만든 데이터를 사용자에게 넘겨줄 때 사용하는 객체
		response.setCharacterEncoding("utf-8"); //(응답정보에 한글이 포함되어 있으면 인코딩)
		response.setContentType("text/html"); 	//지금 보여주는 컨텐츠타입 설정 (html에 한글이 포함되어 있으면 인코딩) 
		
		String job = request.getParameter("job"); //요청정보에서 job이란 파라메터를 읽어오겠다는 의미
		PrintWriter out = response.getWriter();   //출력스트림
		
		if(job.equals("html")) { //job타입이 html이면
			out.print("<h3>아작스페이지입니다</h3>");
			out.print("<a href='index.html'>첫페이지로</a>");			
		} else if(job.equals("json")) { //job타입이 json이면
			//여러건 일 때 [{"fname":?, "lname":?},{},{}]
			String json = "[";
			EmpDAO dao = new EmpDAO();
			List<Employee> list = dao.empList();
//			for (int i = 0; i < list.size(); i++) {
//				json += "{\"fname\":"+ list.get(i).getFirstName() + "}";
//				if(i != list.size()-1) { //마지막 데이터가 아니면 ,를 넣어줌
//					json += ",";
//				}
//			}
//			json += "]";
			Gson gson = new GsonBuilder().create();
			out.print(gson.toJson(list));
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		String cmd = request.getParameter("cmd");
		
		//emp.html의 form에 사용자가 입력했던 값을 받아옴(form의 name속성은 파라메터 네임속성)
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String email = request.getParameter("email");
		String job = request.getParameter("job");
		String hdate = request.getParameter("hdate");
		String empId = request.getParameter("empId");  //getParameter는 반환을 String으로 
		
		Employee emp = new Employee();
		emp.setFirstName(fname);
		emp.setLastName(lname);
		emp.setEmail(email);
		emp.setJobId(job);
		emp.setHireDate(hdate);

		if(cmd.equals("insert")) {
			EmpDAO dao = new EmpDAO();
			dao.insertEmp(emp);
			System.out.println(emp);
		} else if(cmd.equals("update")) {
			EmpDAO dao = new EmpDAO();
			emp.setEmployeeId(Integer.parseInt(empId));
			if(dao.updateEmp(emp) == null) { //EmpDAO의 updateEmp리턴값이 null이면
				System.out.println("error");
			} else {
				System.out.println("success");
			}
		} else if(cmd.equals("delete")) {
			EmpDAO dao = new EmpDAO();
			emp.setEmployeeId(Integer.parseInt(empId));
			dao.deleteEmployee(emp);
		}

		Gson gson = new GsonBuilder().create();
		response.getWriter().print(gson.toJson(emp));
	}
}
