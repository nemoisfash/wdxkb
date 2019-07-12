package org.tdds.service;

import java.util.List;

import org.tdds.entity.Report;

public interface ReportService {

	void insert(Report report);

	Report findByMachineId(Long id);

	List<Report> findAll();

	int update(Report report);

}
