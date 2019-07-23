package org.tdds.controller.admin;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;
import org.tdds.entity.Machine;
import org.tdds.service.MachineService;

import cn.hxz.webapp.syscore.entity.Site;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;
import cn.hxz.webapp.util.AjaxFileUploadUtil;
import cn.hxz.webapp.util.UploadUtils;
import net.chenke.playweb.QueryFilters;
import net.chenke.playweb.support.mybatis.Page;
import net.chenke.playweb.support.mybatis.PageRequest;
import net.chenke.playweb.util.FiltersUtils;
import net.chenke.playweb.util.HashUtils;

@Controller
@RequestMapping("/${adminPath}/machine")
public class MachineAdminController extends BaseWorkbenchController {
	
	@Autowired
	private MachineService bizMachine;
	
	private static final String uuid = HashUtils.MD5(MachineAdminController.class.getName());
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,HttpServletRequest request, HttpServletResponse response){
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		model.addAttribute("filters",filters);
		return this.view("/tdds/machine/list");
	}
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	@ResponseBody
	public Object data(Model model,HttpServletRequest request, HttpServletResponse response){
		Boolean success=true;
		Map<String, Object> map = new HashMap<>();
		QueryFilters filters = FiltersUtils.getQueryFilters(request, response, uuid);
		PageRequest pageable = FiltersUtils.getPageable(filters);
		Page<Machine> machines = bizMachine.findMachines(filters,pageable);
		if(machines==null){
			success=false;
		}
		map.put("success", success);
		map.put("resault",machines);
		map.put("count",machines.getContent().size());
		map.put("number", pageable.getPageNumber());
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public Object uploadFile(HttpServletRequest request,@RequestParam(value = "id") Long id,
			@RequestParam(value = "upload", required = false) MultipartFile file, HttpServletResponse response) {
		String realPath = null;
		try {
			realPath = WebUtils.getRealPath(request.getSession().getServletContext(), "/");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, Object> map=AjaxFileUploadUtil.uploadFile(realPath,new Site(),"machine",file);
		Boolean success= Boolean.parseBoolean(map.get("success").toString()); 
		if(success){
			Machine machine =  new Machine();
			String imageUri=map.get("imageUri").toString();
			machine.setId(id);
			machine.setImgUrl(imageUri);
			bizMachine.updateImage(machine);
		}
		return map;
	}

	@RequestMapping(value = "/removeImage", method = RequestMethod.GET)
	public void deleteImg(HttpServletRequest request,@RequestParam(value = "id") Long id,@RequestParam(value = "imageUri",required = false) String imageUri) {
		Boolean success= UploadUtils.deleteFile(request, imageUri);
		if(success){
			Machine machine =  new Machine();
			machine.setId(id);
			machine.setImgUrl("");
			bizMachine.updateImage(machine);
		}
	}	
	
}
