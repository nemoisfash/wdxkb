package org.tdds.service;


import java.util.List;

import org.tdds.entity.Report;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;

public interface ReportService {

	void insert(Report report);

	Report findByMachineId(Long id);

	Page<Report> findAll(QueryFilters filters, PageRequest pageable);

	int update(Report report);

	List<Report> findAll();

}
