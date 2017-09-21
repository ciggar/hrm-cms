package org.deepsl.hrm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.deepsl.hrm.domain.Job;
import org.deepsl.hrm.service.JobService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**   
 * @Description: 处理职位请求控制器  
 * @version V1.0   
 */

@Controller
public class JobController {
	
	@Autowired
	JobService jobService;
	
	
	@RequestMapping("job/addJob")
	public String addjob(HttpServletRequest request,Job job) {
		String flag = request.getParameter("flag");
		if ("1".equals(flag)) {
			return "job/showAddJob";
		}
		//添加部门请求
		if ("2".equals(flag)) {
			jobService.addJob(job);
			//应该是去查询所有的部门
			return "right";
		}else {
			return "error";
		}
	}
	
	
	//部门查询
	@RequestMapping("job/selectJob")
	public String  selectjob(Job job,Model model,HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		PageModel pageModel=null;
		List<Job> jobs = new ArrayList<>();
		pageModel = new PageModel();
		int recordCount = jobService.finJobCount(job);
		
		if (recordCount==0) {
			return "job/job";
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
		jobs = jobService.findJob(job, pageModel);
		
		model.addAttribute("pageModel", pageModel);
		model.addAttribute("jobs",jobs);

		return "job/job";
	}
	
	//删除部门请求
	@RequestMapping(value="job/removeJob")
	 public ModelAndView deleteUser(ModelAndView mv,HttpServletRequest request){
			String ids = request.getParameter("ids");
			if (ids!=null) {
				String[] idd = ids.split(",");
				for (int i = 0; i < idd.length; i++) {
					int j = Integer.parseInt(idd[i]);
					jobService.removeJobById(j);
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
	@RequestMapping("job/updateJob")
	public String updateUser(HttpServletRequest request,Model model,Job job,ModelAndView mv){
		String flag = request.getParameter("flag");
		//如果flag是1  那么就去查询当前job
		if ("1".equals(flag)) {
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			Job job2 = jobService.findJobById(id);
			model.addAttribute("job", job2);
			
		}
		else if ("2".equals(flag)) {
			jobService.modifyJob(job);
		}
		
		else {
				mv.addObject("message", "修改信息非法，请重新登录");
				// 服务器内部跳转到登录页面
				mv.setViewName("forward:/loginForm");
		}
		return "job/showUpdateJob";
	}
	
 
}
