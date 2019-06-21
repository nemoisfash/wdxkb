package cn.hxz.webapp.content.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.hxz.webapp.content.entity.Model;
import cn.hxz.webapp.content.service.ModelService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;

@Controller
@RequestMapping("${adminPath}/content/model")
public class ModelAdminController extends BaseWorkbenchController {

	@Autowired
	private ModelService bizModel;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable(value="id") Long id) {
		Model entity = bizModel.load(id);
		modelMap.put("entity", entity);
		return this.view("/content/model/view");
	}
}
