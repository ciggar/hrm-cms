package org.deepsl.hrm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.deepsl.hrm.domain.Dept;
import org.deepsl.hrm.domain.Document;
import org.deepsl.hrm.domain.Document;
import org.deepsl.hrm.domain.Job;
import org.deepsl.hrm.domain.User;
import org.deepsl.hrm.service.DocumentService;
import org.deepsl.hrm.service.HrmService;
import org.deepsl.hrm.service.JobService;
import org.deepsl.hrm.util.common.HrmConstants;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 处理上传下载文件请求控制器
 * @version V1.0
 */

@Controller
public class DocumentController {

	@Autowired
	DocumentService documentService;

	@RequestMapping("document/addDocument")
	public String addjob(HttpServletRequest request, Document document,
			HttpSession session){
		String flag = request.getParameter("flag");
		if ("1".equals(flag)) {
			return "document/showAddDocument";
		}
		// 添加部门请求
		if ("2".equals(flag)) {

			User user = (User) session.getAttribute(HrmConstants.USER_SESSION);

			System.out.println("DocumentController.addjob()" + document);
			MultipartFile file = document.getFile();
			// 接下来该把文件保存起来
			String realPath = request.getRealPath("/file");

			File file2 = new File(realPath, file.getOriginalFilename());
			try {
				file.transferTo(file2);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 把document信息保存到数据库中
			String name = file.getOriginalFilename();
			document.setFileName(name);
			document.setUser(user);
			documentService.addDocument(document);

			return "right";
		} else {
			return "error";
		}
	}

	@RequestMapping("document/selectDocument")
	public String selectDocument(HttpServletRequest request, Document document,
			Model model) {
		String pageIndex = request.getParameter("pageIndex");
		PageModel pageModel = null;
		List<Document> documents = new ArrayList<>();
		pageModel = new PageModel();
		int recordCount = documentService.count(document);

		if (recordCount == 0) {
			return "document/document";
		}
		pageModel.setRecordCount(recordCount);
		// 如果不等于null,查出pageIndex页面
		if (pageIndex != null) {
			pageModel.setPageIndex(Integer.parseInt(pageIndex));
		}
		// 查出第一页
		else {
			pageModel.setPageIndex(1);
		}
		// 分页查询出页面要显示的user集合
		documents = documentService.findDocument(document, pageModel);

		model.addAttribute("pageModel", pageModel);
		model.addAttribute("documents", documents);

		return "document/document";
	}

	@RequestMapping("document/updateDocument")
	public String updateUser(HttpServletRequest request, Model model,
			Document document, ModelAndView mv){
		String flag = request.getParameter("flag");
		// 如果flag是1 那么就去查询当前document
		if ("1".equals(flag)) {
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			Document document2 = documentService.findDocumentById(id);
			model.addAttribute("document", document2);

		} else if ("2".equals(flag)) {
			// 如果是2,那么就去修改文档
			MultipartFile file = document.getFile();
			// 接下来该把文件保存起来
			String realPath = request.getRealPath("/file");

			File file2 = new File(realPath, file.getOriginalFilename());
			try {
				file.transferTo(file2);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String filename = file.getOriginalFilename();
			document.setFileName(filename);

			documentService.modifyDocument(document);
		}

		else {
			mv.addObject("message", "修改信息非法，请重新登录");
			// 服务器内部跳转到登录页面
			mv.setViewName("forward:/loginForm");
		}
		return "document/showUpdateDocument";
	}

	@RequestMapping("document/downLoad")
	public String downLoad(HttpServletRequest request, Model model,HttpServletResponse response) {
		
		String id = request.getParameter("id");
		Document document = documentService.findDocumentById(Integer.parseInt(id));
		
		String fileName = document.getFileName();
		
		if (fileName != null) {
			String realPath = request.getServletContext().getRealPath("/file");
			File file = new File(realPath, fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition",
						"attachment;fileName=" + fileName);// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	
}
