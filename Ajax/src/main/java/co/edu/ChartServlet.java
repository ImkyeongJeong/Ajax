package co.edu;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/ChartServlet")
public class ChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ChartServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EmpDAO dao = new EmpDAO();
		Map<String, Integer> map = dao.getMemberByDept();
		Gson gson = new GsonBuilder().create();
		response.getWriter().print(gson.toJson(map)); //toJson: object => json형태로 변환
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
