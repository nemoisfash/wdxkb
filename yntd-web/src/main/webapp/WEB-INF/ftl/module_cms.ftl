<#ftl strip_whitespace=true>
<#--
  # site: long 
      # 必选；站点ID
  # code: string
      # 必选；分类标识
  # parentId: long
      # 可选；可选；父级实体ID，如果不指定此参数，取根级
  # iterate: boolean
      # 可选；是否自动迭代；默认值为true
-->
<#assign navlinkList = "cn.hxz.webapp.content.freemarker.NavlinkListDirective"?new() />
<#assign article = "cn.hxz.webapp.content.freemarker.ArticleDirective"?new() />
<#assign articleProvious = "cn.hxz.webapp.content.freemarker.ArticleProviousDirective"?new() />
<#--
  params:
	site:      站点ID；必选
	node:      节点标识，对应channel.code属性
	page:      当前页；默认值为1
	size:      页大小；默认值为6
	orderBy:   排序方法；可选
	isCache:   是否使用缓存数据；默认值为true
	isIterate: 是否自动迭代；默认值为true
-->
<#assign articleList = "cn.hxz.webapp.content.freemarker.ArticleListDirective"?new() />
<#assign relatedList = "cn.hxz.webapp.content.freemarker.RelatedLinkListDirective"?new() />
<#assign downloadList = "cn.hxz.webapp.content.freemarker.RelatedFileListDirective"?new() />
<#--
  params:
	id:        Article.id
  return:
	Map<String,Object>
-->
<#assign articleAttr = "cn.hxz.webapp.content.freemarker.ArticleAttrDirective"?new() />
<#--
  params:
    isCache:   是否使用缓存数据；默认值为true
    isIterate: 是否自动迭代；默认值为true
-->
<#assign channel = "cn.hxz.webapp.content.freemarker.ChannelDirective"?new() />
<#--
  params:
    isCache:   是否使用缓存数据；默认值为true
    isIterate: 是否自动迭代；默认值为true
-->
<#assign channelList = "cn.hxz.webapp.content.freemarker.ChannelListDirective"?new() />
<#--
  params:
    isCache:   是否使用缓存数据；默认值为true
    isIterate: 是否自动迭代；默认值为true
-->
<#assign channelFieldList = "cn.hxz.webapp.content.freemarker.ChannelAdditionalFieldList"?new() />
<#--
	site:      站点ID；必选
	code:      节点标识，对应advertGroup.code属性
	page:      当前页；默认值为1
	size:      页大小；默认值为6
	loop:      是否对查询结果自动循环；默认值为true
	filters:   查询条件
	orderBy:   排序，按数据表字段名
    cached:    是否使用缓存数据；默认值为true
-->
<#assign advertList = "cn.hxz.webapp.content.freemarker.AdvertListDirective"?new() />
<#--

-->
<#assign advertTypeList = "cn.hxz.webapp.content.freemarker.AdvertTypeListDirective"?new() />

<#assign articlesList = "cn.hxz.webapp.content.freemarker.ArticlesListDirective"?new() />

<#assign articleCount = "cn.hxz.webapp.content.freemarker.ArticleCountDirective"?new() />

<#assign modelList = "cn.hxz.webapp.content.freemarker.ModelListDirective"?new() />

<#assign roleList = "cn.hxz.webapp.content.freemarker.RoleListDirective"?new() />
