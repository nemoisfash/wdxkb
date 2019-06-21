package cn.hxz.webapp.content.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hxz.webapp.content.entity.Keywords;
import cn.hxz.webapp.content.entity.Keywords.TypeEnum;
import cn.hxz.webapp.content.mapper.KeyWordsMapper;
import cn.hxz.webapp.content.service.KeywordsService;

@Service
public class KeywordsServiceImpl implements KeywordsService {

	@Autowired
	private KeyWordsMapper daoKeyWords;

	@Override
	public int addkeywords(String type, String keywords) {
		String[] arys = keywords.split(",");
		Keywords entity = new Keywords();
		for (String ary : arys) {
			if (type.equalsIgnoreCase("adr")) {
				entity.setType(TypeEnum.AbrPrinciple.getValue());
				entity.setName(ary);
			} else if (type.equalsIgnoreCase("inv")) {
				entity.setType(TypeEnum.InvPrinciple.getValue());
				entity.setName(ary);
			} else if (type.equalsIgnoreCase("son")) {
				entity.setType(TypeEnum.Solution.getValue());
				entity.setName(ary);
			}
		}

		return daoKeyWords.insert(entity);
	}

	@Override
	public int create(Keywords entity) {
		return daoKeyWords.insert(entity);
	}

	@Override
	public int update(Keywords entity) {

		return daoKeyWords.updateByPrimaryKey(entity);
	}

	@Override
	public Keywords loadByType(Long typeId, long type){
		Keywords entity = new Keywords();
		entity.setTypeId(typeId);
		entity.setType(type);
		return daoKeyWords.selectOne(entity);
	}

}
