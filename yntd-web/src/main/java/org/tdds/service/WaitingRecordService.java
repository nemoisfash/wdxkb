package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.WaitingRecord;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;


public interface WaitingRecordService {

	void insert(WaitingRecord wRecord);

	Page<WaitingRecord> findAllRecords(QueryFilters filters, PageRequest pageable);

	List<Map<String, Object>> exportData(QueryFilters filters);

}
