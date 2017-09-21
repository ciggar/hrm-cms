package org.deepsl.hrm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.deepsl.hrm.domain.Notice;
import org.deepsl.hrm.domain.User;
import org.deepsl.hrm.service.HrmService;
import org.deepsl.hrm.service.NoticeService;
import org.deepsl.hrm.util.common.HrmConstants;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**   
 * @Description: 处理公告请求控制器
 * @version V1.0   
 */

@Controller
public class NoticeController {

	@Autowired
	NoticeService noticeService;
	
	
	@RequestMapping("notice/addNotice")
	public String addNotice(HttpServletRequest request,Notice Notice,HttpSession session) {
		String flag = request.getParameter("flag");
		if ("1".equals(flag)) {
			return "notice/showAddNotice";
		}
		//添加部门请求
		if ("2".equals(flag)) {
			User user = (User) session.getAttribute(HrmConstants.USER_SESSION);
			Notice.setUser(user);

			noticeService.addNotice(Notice);
			//应该是去查询所有的部门
			return "right";
		}else {
			return "error";
		}
	}
	
	
	//公告查询
	@RequestMapping("notice/selectNotice")
	public String  selectNotice(Notice notice,Model model,HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		PageModel pageModel=null;
		List<Notice> Notices = new ArrayList<>();
		pageModel = new PageModel();
		int recordCount = noticeService.count(notice);
		
		if (recordCount==0) {
			return "notice/notice";
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
		Notices = noticeService.findNotice(notice, pageModel);
		
		model.addAttribute("pageModel", pageModel);
		model.addAttribute("notices",Notices);

		return "notice/notice";
	}
	
	//删除部门请求
	@RequestMapping("notice/removeNotice")
	 public ModelAndView deleteUser(ModelAndView mv,HttpServletRequest request){
			String ids = request.getParameter("ids");
			if (ids!=null) {
				String[] idd = ids.split(",");
				for (int i = 0; i < idd.length; i++) {
					int j = Integer.parseInt(idd[i]);
					noticeService.removeNoticeById(j);
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
	@RequestMapping("notice/updateNotice")
	public String updateUser(HttpServletRequest request,Model model,Notice Notice,ModelAndView mv){
		String flag = request.getParameter("flag");
		//如果flag是1  那么就去查询当前Notice
		if ("1".equals(flag)) {
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			Notice Notice2 = noticeService.findNoticeById(id);
			model.addAttribute("notice", Notice2);
			
		}
		else if ("2".equals(flag)) {
			noticeService.modifyNotice(Notice);
		}
		
		else {
				mv.addObject("message", "修改信息非法，请重新登录");
				// 服务器内部跳转到登录页面
				mv.setViewName("forward:/loginForm");
		}
		return "notice/showUpdateNotice";
	}
	
	@RequestMapping("notice/previewNotice")
	 public String previewNotice(ModelAndView mv,HttpServletRequest request,Model model){
			String id = request.getParameter("id");
			
			Notice notice = noticeService.findNoticeById(Integer.parseInt(id));
			
			model.addAttribute("notice", notice);
			return "notice/previewNotice";
		
	}
	
}
