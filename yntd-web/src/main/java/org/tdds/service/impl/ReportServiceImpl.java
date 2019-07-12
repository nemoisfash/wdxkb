package org.tdds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdds.entity.Report;
import org.tdds.mapper.ReportMapper;
import org.tdds.service.ReportService;

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
	public List<Report> findAll() {
		return daoReport.selectAll();
	}

	@Override
	public int update(Report report) {
		return daoReport.updateByPrimaryKeySelective(report);
	}
}
