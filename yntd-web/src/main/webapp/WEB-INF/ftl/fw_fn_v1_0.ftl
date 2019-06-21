<#ftl strip_whitespace=true>
<#assign clipHtml = "net.chenke.playweb.support.freemarker.ClipHtmlMethod"?new() />

<#function entitiesIds items itemValue='id'>
<#if items?is_sequence>
    <#local retval>
		<#list items as item><#if item?is_hash && item?size gt 0>'${item[itemValue]?html}'<#if item_has_next>,</#if></#if></#list><#t/>
    </#local>
    <#return retval>
</#if>
</#function>

<#function entities2hash items itemValue itemLabel>
<#if items?is_sequence>
    <#local retval>
		{<#list items as item><#if item?is_hash && item?size gt 0>"${item[itemValue]?html}":"${item[itemLabel]?html}<#if item_has_next>",<#else>"</#if></#if></#list>}<#t/>
    </#local>
    <#return retval?eval>
<#else>
    <#return items>
</#if>
</#function>

<#function search_adv_item_cur filters key value='none'>
<#if filters?? && filters?is_hash && filters?size gt 0>
	<#local cssCurr>
	<#if value=='none'>
		${(filters[key]??)?string('', 'search_adv_item_cur')}<#t/>
	<#else>
		${(filters[key]?? && filters[key]==value)?string('search_adv_item_cur', '')}<#t/>
	</#if>
	</#local>
	<#return cssCurr>
<#else>
	<#return ''>
</#if>
</#function>

<#function search_adv_text_cur filters props=[]>
<#if filters?? && filters?is_hash && filters?size gt 0>
    <#local retVal>
        {<#list filters?keys as key><#if props?seq_contains(key) && filters[key]??>${key!}:'${filters[key]!}',</#if></#list>}<#t/>
    </#local>
    <#return retVal>
<#else>
    <#return '{}'>
</#if>
</#function>

<#function cms_admin_uri node>
	<#local retval>
	<#if node.modelId==2>
		${adminPath}/cms/page/${(node.code)}/edit.html<#t/>
	<#elseif node.modelId==3>
		${adminPath}/cms/news/${(node.code)}/list.html<#t/>
	<#elseif node.modelId==4>
		${adminPath}/cms/link/${(node.code)}/list.html<#t/>
	<#elseif node.modelId==5>
		${adminPath}/cms/file/${(node.code)}/list.html<#t/>
	<#else>
		javascript:;<#t/>
	</#if>
	</#local>
	<#return retval>
</#function>