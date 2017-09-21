package org.deepsl.hrm.service;

import java.util.List;

import org.deepsl.hrm.domain.Job;
import org.deepsl.hrm.util.tag.PageModel;

public interface JobService {
	
	List<Job> findJob(Job job,PageModel pageModel);
	
	/**
	 * 获得所有部门
	 * @return Job对象的List集合
	 * */
	List<Job> findAllJob();
	
	/**
	 * 根据id删除部门
	 * @param id
	 * */
	public void removeJobById(Integer id);
	
	/**
	 * 添加部门
	 * @param Job 部门对象
	 * */
	void addJob(Job job);
	
	/**
	 * 根据id查询部门
	 * @param id
	 * @return 部门对象
	 * */
	Job findJobById(Integer id);
		
	/**
	 * 修改部门
	 * @param Job 部门对象
	 * */
	void modifyJob(Job Job);

	int finJobCount(Job Job);
}
