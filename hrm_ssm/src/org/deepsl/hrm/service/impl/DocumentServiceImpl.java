package org.deepsl.hrm.service.impl;

import java.util.HashMap;
import java.util.List;

import org.deepsl.hrm.dao.DocumentDao;
import org.deepsl.hrm.domain.Document;
import org.deepsl.hrm.service.DocumentService;
import org.deepsl.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	@Autowired
	DocumentDao documentDao;
	@Override
	public List<Document> findDocument(Document document, PageModel pageModel) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("firstIndex", pageModel.getFirstLimitParam());
		hashMap.put("size", pageModel.getPageSize());
		if (document.getTitle()!=null&&(!document.getTitle().equals(""))) {
			hashMap.put("title", document.getTitle());
		}
		return documentDao.selectByPage(hashMap);
	}

	@Override
	public void addDocument(Document document) {
		
		documentDao.save(document);
	}

	@Override
	public Document findDocumentById(Integer id) {
		// TODO Auto-generated method stub
		return documentDao.selectById(id);
	}

	@Override
	public void removeDocumentById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyDocument(Document document) {
		documentDao.update(document);
		
	}

	@Override
	public int count(Document document) {
		HashMap<String, Object> hashMap= new HashMap<>();
		
		if (document.getTitle()!=null) {
			hashMap.put("title",document.getTitle());
		}
		return documentDao.count(hashMap);
	}

}
