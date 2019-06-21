package cn.hxz.webapp.content.service;

import cn.hxz.webapp.content.entity.Keywords;

public interface KeywordsService {

	int addkeywords(String type, String keywords);

	int create(Keywords entity);

	int update(Keywords keywords);

	Keywords loadByType(Long typeId, long type);

}
