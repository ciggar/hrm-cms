package org.deepsl.hrm.service.impl;

import java.util.HashMap;
import java.util.List;

import org.deepsl.hrm.dao.JobDao;
import org.deepsl.hrm.domain.Job;
import org.deepsl.hrm.service.JobService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JobServiceImpl implements JobService{
	
	@Autowired
	JobDao jobDao;
	
	@Override
	public List<Job> findJob(Job job, PageModel pageModel) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("firstIndex", pageModel.getFirstLimitParam());
		hashMap.put("size", pageModel.getPageSize());
		hashMap.put("name", job.getName());
		
		List<Job> jobs = jobDao.selectByPage(hashMap);
		return jobs;
	}

	@Override
	public List<Job> findAllJob() {
		// TODO Auto-generated method stub
		return jobDao.selectAllJob();
	}

	@Override
	public void removeJobById(Integer id) {
		// TODO Auto-generated method stub
		jobDao.deleteById(id);
	}

	@Override
	public void addJob(Job job) {
		jobDao.save(job);
		
	}

	@Override
	public Job findJobById(Integer id) {
		
		return jobDao.selectById(id);
	}

	@Override
	public void modifyJob(Job Job) {
		jobDao.update(Job);
		
	}

	@Override
	public int finJobCount(Job Job) {
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", Job.getName());
		return jobDao.count(hashMap);
	}
	

}
