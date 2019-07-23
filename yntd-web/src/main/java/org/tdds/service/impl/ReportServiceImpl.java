package org.tdds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Report;
import org.tdds.mapper.ReportMapper;
import org.tdds.service.ReportService;

import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageImpl;
import net.chenke.playweb.support.mybatis.PageRequest;
import tk.mybatis.mapper.entity.Example;

@Service
public class ReportServiceImpl implements ReportService{
	
	@Autowired
	private ReportMapper daoReport;
	
	@Override
	public void insert(Report report) {
		daoReport.insert(report);
	}

	@Override
	public Report findByMachineId(Long id) {
		Report report = new Report();
		report.setMachineId(id);
		return daoReport.selectOne(report);
	}

	@Override
	public int update(Report report) {
		return daoReport.updateByPrimaryKeySelective(report);
	}

	@Override
	public Page<Report> findAll(QueryFilters filters, PageRequest pageable) {
		Example example = new Example(Report.class);
		example.setOrderByClause("id desc");
		List<Report> entitiesList = daoReport.selectByExampleAndRowBounds(example, pageable);
		return new PageImpl<Report>(entitiesList, pageable);
	}

	@Override
	public List<Report> findAll() {
		return daoReport.selectAll();
	}
}
