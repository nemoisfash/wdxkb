<#ftl strip_whitespace=true>
<#--
  # id: long
      # 必选；站点ID
-->
<#assign site = "cn.hxz.webapp.syscore.freemarker.SiteDirective"?new() />
<#--
  # iterate: boolean
      # 可选；是否自动迭代
-->
<#assign siteList = "cn.hxz.webapp.syscore.freemarker.SiteListDirective"?new() />
<#--
  # id: long
  # username: string    
      # 二选一；用户ID或用户登录名
-->
<#assign user = "cn.hxz.webapp.syscore.freemarker.UserDirective"?new() />
<#--
  # 无参数；取当前登录用户
-->
<#assign currentUser = "cn.hxz.webapp.syscore.freemarker.CurrentUserDirective"?new() />
<#--
  # iterate: boolean
      # 可选；是否自动迭代
-->
<#assign roleList = "cn.hxz.webapp.syscore.freemarker.RoleListDirective"?new() />
<#--
  # id: long
  # code: string    
      # 二选一；权限ID或标识
-->
<#assign permission = "cn.hxz.webapp.syscore.freemarker.PermissionDirective"?new() />
<#--
  # iterate: boolean
      # 可选；是否自动迭代
-->
<#assign permissionList = "cn.hxz.webapp.syscore.freemarker.PermissionListDirective"?new() />
<#--
  # parentId: long 
      # 可选；父级实体ID，如果不指定此参数，取根级
  # id: long 
      # 必选；实体ID
-->
<#assign region = "cn.hxz.webapp.syscore.freemarker.RegionDirective"?new() />
<#--
  # parentId: long 
      # 可选；父级实体ID，如果不指定此参数，取根级
  # page: int
      # 可选；第几页，默认1
  # size: int
      # 可选；每页大小，默认100
  # iterate: boolean
      # 可选；是否自动迭代
-->
<#assign regionList = "cn.hxz.webapp.syscore.freemarker.RegionListDirective"?new() />
<#--
    id:        必选。对应Option.id
    isCache:   是否使用缓存；默认值为true
-->
<#assign option = "cn.hxz.webapp.syscore.freemarker.OptionDirective"?new() />

<#--
    code:      与groupId二选一，至少有一个存在；对应OptionGroup.code
    groupId:   与code二选一，至少有一个存在；对应OptionGroup.id
    isCache:   是否使用缓存
    isIterate: 是否自动对查到的内容进行迭代；默认值为true
-->
<#assign optionList = "cn.hxz.webapp.syscore.freemarker.OptionListDirective"?new() />

<#--
    id:       必选；站点ID；[SiteAttr.id:long]
    cached:   可选；是否使用缓存；[boolean:true]
-->
<#assign siteAttr = "cn.hxz.webapp.syscore.freemarker.SiteAttrDirective"?new() />
<#--
    id:        必选；站点ID；[SiteAttr.id:long]
	field:     必选；属性名；[SiteAttr.filed:string]
    cached:    可选；是否使用缓存；[boolean:true]
-->
<#assign siteAttrMap = "cn.hxz.webapp.syscore.freemarker.SiteAttrMapDirective"?new() />

<#-- <#assign machineList = "org.tdds.freemarker.MachineListDirective"?new() /> -->
