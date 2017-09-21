package org.deepsl.hrm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.deepsl.hrm.domain.Dept;
import org.deepsl.hrm.domain.User;
import org.deepsl.hrm.service.DeptService;
import org.deepsl.hrm.service.HrmService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**   
 * @Description: 处理部门请求控制器
 * @author   
 * @version V1.0   
 */
	
@Controller
public class DeptController {
	
	@Autowired
	DeptService deptService;
	
	
	@RequestMapping("dept/addDept")
	public String addDept(HttpServletRequest request,Dept dept) {
		String flag = request.getParameter("flag");
		if ("1".equals(flag)) {
			return "dept/showAddDept";
		}
		//添加部门请求
		if ("2".equals(flag)) {
			deptService.addDept(dept);
			//应该是去查询所有的部门
			return "right";
		}else {
			return "error";
		}
	}
	
	
	//部门查询
	@RequestMapping("dept/selectDept")
	public String  selectDept(Dept dept,Model model,HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		PageModel pageModel=null;
		List<Dept> depts = new ArrayList<>();
		pageModel = new PageModel();
		int recordCount = deptService.finDeptCount(dept);
		
		if (recordCount==0) {
			return "dept/dept";
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
		//分页查询出页面要显示的user集合
		depts = deptService.findDept(dept, pageModel);
		
		model.addAttribute("pageModel", pageModel);
		model.addAttribute("depts",depts);

		return "dept/dept";
	}
	
	//删除部门请求
	@RequestMapping(value="dept/removeDept")
	 public ModelAndView deleteUser(ModelAndView mv,HttpServletRequest request){
			String ids = request.getParameter("ids");
			if (ids!=null) {
				String[] idd = ids.split(",");
				for (int i = 0; i < idd.length; i++) {
					int j = Integer.parseInt(idd[i]);
					deptService.removeDeptById(j);
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
	@RequestMapping("dept/updateDept")
	public String updateUser(HttpServletRequest request,Model model,Dept dept,ModelAndView mv){
		String flag = request.getParameter("flag");
		//如果flag是1  那么就去查询当前dept
		if ("1".equals(flag)) {
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			Dept dept2 = deptService.findDeptById(id);
			model.addAttribute("dept", dept2);
			
		}
		else if ("2".equals(flag)) {
			deptService.modifyDept(dept);
		}
		
		else {
				mv.addObject("message", "修改信息非法，请重新登录");
				// 服务器内部跳转到登录页面
				mv.setViewName("forward:/loginForm");
		}
		return "dept/showUpdateDept";
	}
	
}
