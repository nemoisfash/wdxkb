package org.tdds.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tdds.entity.ManualRecord;
import org.tdds.mapper.ManualRecordMapper;
import org.tdds.service.ManualRecordService;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ManualRecordServiceImpl implements ManualRecordService {
	private  static final String ORDER_BY="id desc";
	@Autowired
	private ManualRecordMapper daoManualRecord;

	@Override
	public void insert(ManualRecord mr) { 
		daoManualRecord.insert(mr);
	}

	@Override
	public Page<ManualRecord> findAllRecords(QueryFilters filters, PageRequest pageable) {
		Example example = new Example(ManualRecord.class);
		example.setOrderByClause(ORDER_BY);
		Criteria criteria = example.createCriteria();
		if(StringUtils.hasText(Objects.toString(filters.get("recordTime"), null))){
			String recordTime = Objects.toString(filters.get("recordTime"), null);
			if(recordTime.indexOf("&")>-1){
				String startTime=recordTime.split("&")[0];
				String endTime=recordTime.split("&")[1];
				criteria.andBetween("recordTime",startTime, endTime);
			} 
			if(NumberUtils.isNumber(recordTime)){
				Integer num=Integer.valueOf(recordTime);
				Map<String, String> map = getTime(num);
				criteria.andBetween("recordTime",map.get("startTime"), map.get("endTime"));
			}
		} 
		if(StringUtils.hasText(Objects.toString(filters.get("name"), null))){
			 criteria.andEqualTo("machineName", Objects.toString(filters.get("name")));
		 }
		List<ManualRecord> entities=daoManualRecord.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<ManualRecord>(entities, pageable);
	}
	
	private Map<String, String> getTime(Integer flag){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		Map<String , String> timeMap = new HashMap<>();
		timeMap.put("endTime", sdf.format(now.getTime()));
		switch (flag) {
		case 3:
			now.add(Calendar.DAY_OF_MONTH, -3);
			timeMap.put("startTime", sdf.format(now.getTime()));
			break;
		case 7:
			now.add(Calendar.DAY_OF_MONTH, -7);
			timeMap.put("startTime", sdf.format(now.getTime()));
			break;
		case 15:
			now.add(Calendar.DAY_OF_MONTH, -15);
			timeMap.put("startTime", sdf.format(now.getTime()));
			break;
		case 30:
			now.add(Calendar.MONTH, -1);
			timeMap.put("startTime", sdf.format(now.getTime()));
			break;
		default:
			now.add(Calendar.HOUR_OF_DAY, -24);
			timeMap.put("startTime", sdf.format(now.getTime()));
			break;
		}
		return timeMap;
	}

	@Override
	public List<Map<String, Object>> exportData(QueryFilters filters) {
		Map<String, Object> filter = new HashMap<>();
		if(StringUtils.hasText(Objects.toString(filters.get("recordTime"), null))){
			String recordTime = Objects.toString(filters.get("recordTime"), null);
			String startTime=null;
			String endTime=null;
			if(recordTime.indexOf("&")>-1){
				 startTime=recordTime.split("&")[0];
				 endTime=recordTime.split("&")[1];
			} 
			if(NumberUtils.isNumber(recordTime)){
				Integer num=Integer.valueOf(recordTime);
				Map<String, String> map = getTime(num);
				 startTime=map.get("startTime");
				 endTime=map.get("endTime");
			}
			filter.put("startTime",startTime);
			filter.put("endTime", endTime);
		}
		if(StringUtils.hasText(Objects.toString(filters.get("name"), null))){
			filter.put("name", Objects.toString(filters.get("name")));
		 }
		return daoManualRecord.exportData(filter);
	}
	
}
