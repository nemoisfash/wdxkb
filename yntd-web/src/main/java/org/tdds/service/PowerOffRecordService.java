package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.PowerOffRecord;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;


public interface PowerOffRecordService {

	void insert(PowerOffRecord pr);

	Page<PowerOffRecord> findAllRecords(QueryFilters filters, PageRequest pageable);

	List<Map<String, Object>> exportData(QueryFilters filters);


}
