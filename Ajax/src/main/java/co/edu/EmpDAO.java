package co.edu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpDAO extends DAO {
	
	//<fullcalendar>
	//스케줄 리스트
	public List<Schedule> scheduleList(){
		List<Schedule> list = new ArrayList<Schedule>();
		connect();
		String sql = "select * from schedules";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setTitle(rs.getString("title"));
				schedule.setStart(rs.getString("start_date"));
				schedule.setEnd(rs.getString("end_date"));
				list.add(schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}
	//스케줄 등록
	public void insertSchedule(Schedule sched) {
		connect();
		String sql = "";
		//end_date를 입력하시 않으면 공백이 아닌 start_date값이 end_date가 된다.
		sql ="insert into schedules values(?, ?, ?)";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, sched.getTitle());
			psmt.setString(2, sched.getStart());
			psmt.setString(3, sched.getEnd());
			psmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//스케줄 삭제
	public void deleteSchedule(Schedule sched) {
		connect();
		String sql = "delete from schedules where title = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, sched.getTitle());
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	
	
	//<google charts>
	//부서별 인원(차트)=> 반환되는 타입: 부서명 = 인원. Map<String, Integer>타입으로 받아옴
	public Map<String, Integer> getMemberByDept(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		connect();
		String sql = "select d.department_name, count(1) as cnt "
				+ "from employees e, departments d "
				+ "where e.department_id = d.department_id "
				+ "group by d.department_name";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) { //key=value 가져온 값만큼 루핑 돌면서 map에 담음
				map.put(rs.getString("department_name"), rs.getInt("cnt"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	//리스트
	public List<Employee> empList(){
		connect();
		List<Employee> list = new ArrayList<Employee>();
		try {
		psmt = conn.prepareStatement("SELECT * FROM emp_temp ORDER BY 1");
		rs = psmt.executeQuery();
			while(rs.next()) {
				Employee emp = new Employee();
				emp.setEmployeeId(rs.getInt("employee_id"));
				emp.setFirstName(rs.getString("first_name"));
				emp.setLastName(rs.getString("last_name"));
				emp.setEmail(rs.getString("email"));
				emp.setJobId(rs.getString("job_id"));
				emp.setHireDate(rs.getString("hire_date"));
				
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}
	
	//입력
	public Employee insertEmp(Employee emp) {
		String sql = "INSERT INTO emp_temp (employee_id, first_name, last_name, email, job_id, hire_date)"
				+ " VALUES(?,?,?,?,?,?)";
		String seqSql = "SELECT employees_seq.nextval FROM dual";
		
		connect();
		int nextSeq = -1;
		try {
			//시퀀스 받아오기 위함
			psmt = conn.prepareStatement(seqSql);
			rs = psmt.executeQuery();
			if(rs.next()) {
				nextSeq = rs.getInt(1);
			}
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, nextSeq);
			psmt.setString(2, emp.getFirstName());
			psmt.setString(3, emp.getLastName());
			psmt.setString(4, emp.getEmail());
			psmt.setString(5, emp.getJobId());
			psmt.setString(6, emp.getHireDate());
			int r = psmt.executeUpdate();
			System.out.println(r + "건 입력됨");
			
			emp.setEmployeeId(nextSeq);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return emp;
	}
	
	//수정
	public Employee updateEmp(Employee emp) {
		connect();
		String sql = "UPDATE emp_temp SET first_name=?, last_name=?, email=?, hire_date=? WHERE employee_id=?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, emp.getFirstName());
			psmt.setString(2, emp.getLastName());
			psmt.setString(3, emp.getEmail());
			psmt.setString(4, emp.getHireDate());
			psmt.setInt(5, emp.getEmployeeId());
			int r = psmt.executeUpdate();
			System.out.println(r + "건 수정");
			if(r > 0) {
				return emp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return null; //수정된 것이 없으면 null을 리턴
	}
	//삭제
	public void deleteEmployee(Employee emp) {
		connect();
		String sql = "delete from emp_temp where employee_id = ?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, emp.getEmployeeId());
			int r = psmt.executeUpdate();
			System.out.println(r + "건 삭제");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	//한건조회
	
}
