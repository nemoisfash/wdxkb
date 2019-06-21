<#ftl strip_whitespace=true>

<#macro btnLook text, onclick='none', href='javascript:;'>
<a href="${href!}"${('none'!=onclick)?string(' onclick="${onclick!}"','')} class="btn btn-xs btn-default">${text!}</a>
</#macro>

<#macro btnInfo text, onclick='none', href='javascript:;'>
<a href="${href!}"${('none'!=onclick)?string(' onclick="${onclick!}"','')} class="btn btn-xs btn-info">${text!}</a>
</#macro>

<#macro btnWarn text, onclick='none', href='javascript:;'>
<a href="${href!}"${('none'!=onclick)?string(' onclick="${onclick!}"','')} class="btn btn-xs btn-danger">${text!}</a>
</#macro>

<#macro btnBool entity, field, onclick='none', href='javascript:;'>
<#local whether = (entity?? && (entity[field]?string=='true' || entity[field]?string == '1')) />
<a href="${href!}"${('none'!=onclick)?string(' onclick="${onclick!}"','')} class="btn btn-xs${whether?string(' btn-success',' btn-warning')}">${whether?string('是','否')}</a>
</#macro>

<#macro btnBoolean entity, field, onclick='none', href='javascript:;'>
<#local whether = (entity?? && (entity[field]?string=='true' || entity[field]?string == '1')) />
<a href="${href!}"${('none'!=onclick)?string(' onclick="${onclick!}"','')} class="btn btn-xs${whether?string(' btn-success',' btn-warning')}">${whether?string('是','否')}</a>
</#macro>