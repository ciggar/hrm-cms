package org.deepsl.hrm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.deepsl.hrm.domain.User;
import org.deepsl.hrm.service.HrmService;
import org.deepsl.hrm.util.common.HrmConstants;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理用户请求控制器
 * */
@Controller
public class UserController {
	
	/**
	 * 自动注入UserService
	 * */
	@Autowired
	@Qualifier("hrmService")
	private HrmService hrmService;
		
	/**
	 * 处理登录请求
	 * @param String loginname  登录名
	 * @param String password 密码
	 * @return 跳转的视图
	 * */
	@RequestMapping(value="/login")
	 public ModelAndView login(@RequestParam("loginname") String loginname,
			 @RequestParam("password") String password,
			 HttpSession session,
			 ModelAndView mv){
		// 调用业务逻辑组件判断用户是否可以登录
		User user = hrmService.login(loginname, password);
		if(user != null){
			// 将用户保存到HttpSession当中
			session.setAttribute(HrmConstants.USER_SESSION, user);
			// 客户端跳转到main页面
			mv.setViewName("redirect:/main");
		}else{
			// 设置登录失败提示信息
			mv.addObject("message", "登录名或密码错误!请重新输入");
			// 服务器内部跳转到登录页面
			mv.setViewName("forward:/loginForm");
		}
		return mv;
		
	}
	
	/**
	 * 处理查询请求
	 * @param pageIndex 请求的是第几页
	 * @param employee 模糊查询参数
	 * @param Model model
	 * */
 
	
	/**
	 * 处理删除用户请求
	 * @param String ids 需要删除的id字符串
	 * @param ModelAndView mv
	 * */
 
	
	//user/addUser?flag=1
	/*
	 处理添加用户请求
	 */
	@RequestMapping("user/addUser")
	 public String addUser(String flag,User user) {
		
		//跳转进入jsp
		if (flag.equals("1")) {
			System.out.println("UserController.addUser()"+user);
			return "user/showAddUser";
		}
		//jsp提交请求处理界面
		else if (flag.equals("2")) {
			System.out.println("UserController.addUser()"+user);
			hrmService.addUser(user);
			
			return "right";	
		//其他请求
		}else {
			return "error";
		}	
	}
	
	
	//用户查询
	@RequestMapping("user/selectUser")
	public String  selectUser(User user,Model model,HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		PageModel pageModel=null;
		List<User> users = new ArrayList<>();
		pageModel = new PageModel();
		int recordCount = hrmService.findUserCount(user);
		
		if (recordCount==0) {
			return "user/user";
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
		users = hrmService.findUser(user, pageModel);
		
		model.addAttribute("pageModel", pageModel);
		model.addAttribute("users",users);

		return "user/user";
	}
	

	@RequestMapping(value="user/removeUser")
	 public ModelAndView deleteUser(ModelAndView mv,HttpServletRequest request){
			String ids = request.getParameter("ids");
			if (ids!=null) {
				String[] idd = ids.split(",");
				for (int i = 0; i < idd.length; i++) {
					int j = Integer.parseInt(idd[i]);
					hrmService.removeUserById(j);
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
	@RequestMapping("user/updateUser")
	public String updateUser(HttpServletRequest request,Model model,User user,ModelAndView mv){
		String flag = request.getParameter("flag");
		//如果flag是1  那么就去查询当前user
		if ("1".equals(flag)) {
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			User user2 = hrmService.findUserById(id);
			model.addAttribute("user", user2);
			
		}
		else if ("2".equals(flag)) {
			System.out.println(user);
			hrmService.modifyUser(user);
		}
		
		else {
				mv.addObject("message", "查询信息非法，请重新登录");
				// 服务器内部跳转到登录页面
				mv.setViewName("forward:/loginForm");
		}
		
		return "user/showUpdateUser";
	}
	
}
