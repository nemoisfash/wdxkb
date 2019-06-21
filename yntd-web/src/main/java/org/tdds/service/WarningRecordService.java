package org.tdds.service;

import java.util.List;
import java.util.Map;

import org.tdds.entity.WarningRecord;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

public interface WarningRecordService {

	void insert(WarningRecord warningRecord);

	Page<WarningRecord> findAllRecords(QueryFilters filters, PageRequest pageable);

	List<Map<String, Object>> exportData(QueryFilters filters);
}
