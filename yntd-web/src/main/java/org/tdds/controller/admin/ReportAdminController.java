package org.tdds.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tdds.entity.Machine;
import org.tdds.entity.Report;
import org.tdds.service.MachineService;
import org.tdds.service.ReportService;

import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("/admin/report")
public class ReportAdminController extends BaseWorkbenchController{
	
	@Autowired
	private ReportService bizReport;
	
	@Autowired
	private MachineService bizMachine;
	
	private static final String uuid = HashUtils.MD5(ReportAdminController.class.getName());
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	private String list(Model model,HttpServletRequest request, HttpServletResponse response) {
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		List<Machine> machines =bizMachine.findMachine();
		for(Machine entity:machines) {
			Report report = bizReport.findByMachineId(entity.getId());
			if(report==null) {
				Report report2 =new Report(); 
				report2.setMachineId(entity.getId());
				report2.setMachineName(entity.getName());
				bizReport.insert(report2);
			}
		}
		model.addAttribute("filters",filters);
		return this.view("/tdds/report/list");
	}
	
	@RequestMapping(value="/data",method = RequestMethod.GET)
	@ResponseBody
	private Object data(HttpServletRequest request,HttpServletResponse response) {
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		Page<Report> entities =bizReport.findAll(filters,pageable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result",entities);
		map.put("number", pageable.getPageNumber());
		return map;
	}
	
	@RequestMapping(value="/update", method = RequestMethod.POST)
	@ResponseBody
	private Object update(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute Report report) {
		Map<String, Object> map = new HashMap<String, Object>();
		int i=bizReport.update(report);
		if(i==1) {
			map.put("success", Boolean.TRUE);
		}else {
			map.put("success", Boolean.FALSE);
		}
		return map;
	}
}
