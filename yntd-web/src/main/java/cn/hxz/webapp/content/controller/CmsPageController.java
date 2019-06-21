package cn.hxz.webapp.content.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.hxz.webapp.content.entity.Channel;
import cn.hxz.webapp.content.entity.SinglesPage;
import cn.hxz.webapp.content.service.ChannelService;
import cn.hxz.webapp.content.service.SinglesPageService;
import cn.hxz.webapp.syscore.entity.User;
import cn.hxz.webapp.syscore.support.AuthorizingUtils;
import cn.hxz.webapp.syscore.support.BaseWorkbenchController;

@Controller
@RequestMapping("${adminPath}/cms/page")
public class CmsPageController extends BaseWorkbenchController {
	
	@Autowired
	private ChannelService bizChannel;
	@Autowired
	private SinglesPageService bizPage;
	
	@RequestMapping(value = "/{code}/edit", method = RequestMethod.GET)
	public String edit(Model model,
			@PathVariable(value = "code") String channelCode){
		
		Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);
		
		SinglesPage entity = bizPage.loadByChannelId(channel.getId());
		if (entity==null){
			entity = new SinglesPage();
			entity.setChannelId(channel.getId());
		}

		model.addAttribute("entity", entity);
		model.addAttribute("channel", channel);
		return this.view("/cms/page/edit");
	}
	
	@RequestMapping(value = "/{code}/save", method = RequestMethod.POST)
	public String save(RedirectAttributes model,
			@PathVariable(value = "code") String channelCode,
			@Valid @ModelAttribute("entity") SinglesPage entity, BindingResult binding){
		
        if(binding.hasErrors()){
        	Channel channel = bizChannel.loadCached(this.getSite().getId(), channelCode);
        	model.addAttribute("entity", entity);
    		model.addAttribute("channel", channel);
            return this.view("/cms/page/edit");
        }
        User user = AuthorizingUtils.loadCurrentUser();
        if (entity.getId()==null){
        	entity.setCreateBy(user.getId());
		    entity.setCreateTime(new Date());
        	bizPage.create(entity);
        }
        else{
        	entity.setUpdateBy(user.getId());
			entity.setUpdateTime(new Date());
        	bizPage.update(entity);
        }
        model.addFlashAttribute("message", "已保存");
		return this.redirect("/cms/page/{code}/edit.html");
	}

}
