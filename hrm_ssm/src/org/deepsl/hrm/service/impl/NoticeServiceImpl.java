package org.deepsl.hrm.service.impl;

import java.util.HashMap;
import java.util.List;

import org.deepsl.hrm.dao.NoticeDao;
import org.deepsl.hrm.domain.Notice;
import org.deepsl.hrm.service.NoticeService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired
	NoticeDao noticeDao;
	@Override
	public List<Notice> findNotice(Notice notice, PageModel pageModel) {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<>();
		if (notice.getTitle()!=null||(!("").equals(notice.getTitle()))) {
			hashMap.put("title", notice.getTitle());
		}
		if (notice.getContent()!=null||(!("").equals(notice.getContent()))) {
			hashMap.put("content", notice.getContent());
		}
		hashMap.put("firstIndex", pageModel.getFirstLimitParam());
		hashMap.put("size", pageModel.getPageSize());
		
		return noticeDao.selectByPage(hashMap);
	}

	@Override
	public Notice findNoticeById(Integer id) {
		// TODO Auto-generated method stub
		return noticeDao.selectById(id);
	}

	@Override
	public void removeNoticeById(Integer id) {
		noticeDao.deleteById(id);
		
	}

	@Override
	public void addNotice(Notice notice) {
		noticeDao.save(notice);
		
	}

	@Override
	public void modifyNotice(Notice notice) {
		noticeDao.update(notice);
		
	}

	@Override
	public int count(Notice notice) {
		
		HashMap<String, Object> hashMap = new HashMap<>();
		if (notice.getTitle()!=null||(!("").equals(notice.getTitle()))) {
			hashMap.put("title", notice.getTitle());
		}
		if (notice.getContent()!=null||(!("").equals(notice.getContent()))) {
			hashMap.put("content", notice.getContent());
		}
		return noticeDao.count(hashMap);
	}

}
