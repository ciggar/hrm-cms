package org.deepsl.hrm.service;

import java.util.List;

import org.deepsl.hrm.domain.Dept;
import org.deepsl.hrm.domain.Document;
import org.deepsl.hrm.domain.Employee;
import org.deepsl.hrm.domain.Job;
import org.deepsl.hrm.domain.Notice;
import org.deepsl.hrm.util.tag.PageModel;

//保留所有service需要实现的接口 
//都需要去实现

public interface OtherServiceInterface {

	
	
	/**
	 * 获得所有职位
	 * @return Job对象的List集合
	 * */
	List<Job> findAllJob();
	
	List<Job> findJob(Job job,PageModel pageModel);
	
	/**
	 * 根据id删除职位
	 * @param id
	 * */
	public void removeJobById(Integer id);
	
	/**
	 * 添加职位
	 * @param Job 部门对象
	 * */
	void addJob(Job job);
	
	/**
	 * 根据id查询职位
	 * @param id
	 * @return 职位对象
	 * */
	Job findJobById(Integer id);
	
	/**
	 * 修改职位
	 * @param dept 部门对象
	 * */
	void modifyJob(Job job);
	
	
	
	
	/**
	 * 获得所有文档
	 * @return Document对象的List集合
	 * */
	List<Document> findDocument(Document document,PageModel pageModel);
	
	/**
	 * 添加文档
	 * @param Document 文件对象
	 * */
	void addDocument(Document document);
	
	/**
	 * 根据id查询文档
	 * @param id
	 * @return 文档对象
	 * */
	Document findDocumentById(Integer id);
	
	/**
	 * 根据id删除文档
	 * @param id
	 * */
	public void removeDocumentById(Integer id);
	
	/**
	 * 修改文档
	 * @param Document 公告对象
	 * */
	void modifyDocument(Document document);
}
