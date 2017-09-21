package org.deepsl.hrm.controller;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.deepsl.hrm.domain.Dept;
import org.deepsl.hrm.domain.Employee;
import org.deepsl.hrm.domain.Job;
import org.deepsl.hrm.service.DeptService;
import org.deepsl.hrm.service.EmployeeService;
import org.deepsl.hrm.service.JobService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**   
 * @Description: 处理员工请求控制器   
 * @version V1.0   
 */
@Controller
public class EmployeeController {
	 
	
	@Autowired
	EmployeeService EmployeeService;
	@Autowired
	DeptService deptService;
	@Autowired
	JobService jobService;
	
	@RequestMapping("employee/addEmployee")
	public String addEmployee(HttpServletRequest request,Employee Employee,Model model) {
		String flag = request.getParameter("flag");
		if ("1".equals(flag)) {
			
			List<Dept> dept = deptService.findAllDept();
			List<Job> allJob = jobService.findAllJob();
			model.addAttribute("depts", dept);
			model.addAttribute("jobs", allJob);
			return "employee/showAddEmployee";
		}
		//添加部门请求
		if ("2".equals(flag)) {
			String job_id = request.getParameter("job_id");
			String dept_id = request.getParameter("dept_id");
			
			int did = Integer.parseInt(dept_id);
			int jid = Integer.parseInt(job_id);
			
			Dept dept = deptService.findDeptById(did);
			Job job = jobService.findJobById(jid);
			
			Employee.setJob(job);
			Employee.setDept(dept);
			EmployeeService.addEmployee(Employee);
			//应该是去查询所有的部门
			return "right";
		}else {
			return "error";
		}
	}
	
	
	//部门查询
	@RequestMapping("employee/selectEmployee")
	public String  selectEmployee(Employee Employee,Model model,HttpServletRequest request) {
		System.out.println("EmployeeController.selectEmployee()"+Employee);
		
		String pageIndex = request.getParameter("pageIndex");
		PageModel pageModel=null;
		List<Employee> Employees = new ArrayList<>();
		pageModel = new PageModel();
		
		//回显部门名称以及工作名称
		List<Dept> depts = deptService.findAllDept();
		List<Job> allJob = jobService.findAllJob();
		model.addAttribute("depts", depts);
		model.addAttribute("jobs", allJob);
		
		//查询出符合条件的employee个数
		String job_id = request.getParameter("job_id");
		String dept_id = request.getParameter("dept_id");
		
		if (job_id!=null &&(!job_id.equals("0"))) {
			Job job = jobService.findJobById(Integer.parseInt(job_id));
			Employee.setJob(job);
		}
		if (dept_id!=null &&(!dept_id.equals("0"))) {
			Dept dept = deptService.findDeptById(Integer.parseInt(dept_id));
			Employee.setDept(dept);
		}
		
		int recordCount = EmployeeService.findEmployeeCount(Employee);
		
		if (recordCount==0) {
			return "employee/employee";
		}
		pageModel.setRecordCount(recordCount);
		//如果不等于null,查出pageIndex页面
		if (pageIndex!=null) {
			pageModel.setPageIndex(Integer.parseInt(pageIndex));
		}
		//查出第一页
		else {
			pageModel.setPageIndex(1);
		}
		//分页查询出页面要显示的Employee集合
		Employees = EmployeeService.findEmployee(Employee, pageModel);
		
		model.addAttribute("pageModel", pageModel);
		model.addAttribute("employees",Employees);

		return "employee/employee";
	}
	
	//删除部门请求
	@RequestMapping(value="employee/removeEmployee")
	 public ModelAndView deleteUser(ModelAndView mv,HttpServletRequest request){
			String ids = request.getParameter("ids");
			if (ids!=null) {
				String[] idd = ids.split(",");
				for (int i = 0; i < idd.length; i++) {
					int j = Integer.parseInt(idd[i]);
					EmployeeService.removeEmployeeById(j);
				}
			}else{
				// 设置登录失败提示信息
				mv.addObject("message", "删除失败");
				// 服务器内部跳转到登录页面
				mv.setViewName("forward:/loginForm");
			}
			
			// 服务器内部跳转到登录页面
			mv.setViewName("redirect:/right");
		
		return mv;
		
	}
	
	//修改部门 请求
	@RequestMapping("employee/updateEmployee")
	public String updateUser(HttpServletRequest request,Model model,Employee Employee,ModelAndView mv){
		String flag = request.getParameter("flag");
		//如果flag是1  那么就去查询当前Employee
		if ("1".equals(flag)) {
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			Employee Employee2 = EmployeeService.findEmployeeById(id);
			
			List<Dept> depts = deptService.findAllDept();
			List<Job> allJob = jobService.findAllJob();
			model.addAttribute("depts", depts);
			model.addAttribute("jobs", allJob);
			//转化日期格式
			Date birth = Employee2.getBirthday();
			String birthday;
			try {
				birthday = transDate(birth);
				model.addAttribute("birthday", birthday);
				model.addAttribute("employee", Employee2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//	System.out.println("EmployeeController.updateUser()"+Employee2);
		}
		
		else if ("2".equals(flag)) {
			
			
			
			String job_id = request.getParameter("job_id");
			String dept_id = request.getParameter("dept_id");
			
			if (job_id!=null &&(!job_id.equals("0"))) {
				Job job = jobService.findJobById(Integer.parseInt(job_id));
				Employee.setJob(job);
			}
			if (dept_id!=null &&(!dept_id.equals("0"))) {
				Dept dept = deptService.findDeptById(Integer.parseInt(dept_id));
				Employee.setDept(dept);
			}
			System.out.println("EmployeeController.updateUser()"+Employee);
			EmployeeService.modifyEmployee(Employee);
		}
		
		else {
				mv.addObject("message", "修改信息非法，请重新登录");
				// 服务器内部跳转到登录页面
				mv.setViewName("forward:/loginForm");
		}
		return "employee/showUpdateEmployee";
	}


	
	//这是一个日期格式转换的工具类，用于回显
	private String transDate(Date birthday) throws ParseException {
		 DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	      //将已有的时间字符串转化为Date对象
	      Date date = df.parse(birthday.toString());// 那天是周一
	      // 创建所需的格式
	      df = new SimpleDateFormat("yyyy-MM-dd");
	      String str = df.format(date);// 获得格式化后的日期字符串
	      
	      
	      return str;
	}
	
}
