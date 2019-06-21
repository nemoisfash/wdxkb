package cn.hxz.webapp.content.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.hxz.webapp.content.entity.ModelField;
import cn.hxz.webapp.content.service.ModelFieldService;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;

@Controller
@RequestMapping("${adminPath}/content/model/field")
public class ModelFieldAdminController extends BaseWorkbenchController {

	@Autowired
	private ModelFieldService bizModelField;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable(value="id") Long id) {
		ModelField entity = bizModelField.load(id);
		modelMap.put("entity", entity);
		return this.view("/content/modelField/view");
	}
}
