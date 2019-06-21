<#function additionalFieldTypeOptions >
	<#return {"1":"字符文本","2":"整型文本","3":"浮点文本","4":"日期","5":"日期时间","6":"文本区域","7":"下拉列表","8":"单选框","9":"复选框"} />
</#function>

<#function additionalFieldTypeName typeKey>
	<#assign options = additionalFieldTypeOptions()>
	<#return options[typeKey?string] />
</#function>

<#macro additionalFieldInput additionalField>
<#if (additionalField.dataType)?? >
<#if additionalField.dataType == 1 ><#--字符文本-->
<@spring.formInput path="entity.attr['${additionalField.field}']" attributes=' class="form-control input-sm spk-text"' />
<#--${(additionalField.defValue?eval)!}-->
</#if>
<#if additionalField.dataType == 2 ><#--整型文本-->
<@spring.formInput path="entity.attr['${additionalField.field}']" attributes=' class="form-control input-sm spk-text-int"' />
</#if>
<#if additionalField.dataType == 3 ><#--浮点文本-->
<@spring.formInput path="entity.attr['${additionalField.field}']" attributes=' class="form-control input-sm spk-text-float"' />
</#if>
<#if additionalField.dataType == 4 ><#--日期-->
<div class="input-group input-append date datepicker">
<@spring.formInput path="entity.attr['${additionalField.field}']" attributes=' class="form-control input-sm spk-readonly spk-datetimepicker" onclick="laydate({format: \'YYYY-MM-DD\'})"' />
<span class="add-on input-group-addon text"><span class="fa fa-calendar"></span></span>
</div>
</#if>
<#if additionalField.dataType == 5 ><#--日期时间-->
<div class="input-group input-append date datepicker">
<@spring.formInput path="entity.attr['${additionalField.field}']" attributes=' class="form-control input-sm spk-readonly spk-datetimepicker" onclick="laydate({istime:true, format: \'YYYY-MM-DD hh:mm:ss\'})"' />
<span class="add-on input-group-addon text"><span class="fa fa-calendar"></span></span>
</div>
</#if>
<#if additionalField.dataType == 6 ><#--文本区域-->
<#assign areaRows = ((additionalField.areaRows)??)?string('rows="${(additionalField.areaRows)!}"','') />
<#assign areaCols = ((additionalField.areaCols)??)?string('cols="${(additionalField.areaCols)!}"','') />
<@spring.formTextarea path="entity.attr['${additionalField.field}']" attributes='class="form-control input-sm spk-textarea" ${areaRows} ${areaCols}'/>
</#if>
<#if additionalField.dataType == 7 ><#--下拉列表-->
<@spring.formSingleSelect path="entity.attr['${additionalField.field}']" options=additionalField.optValue?split(',') attributes='class="form-control input-sm"' />
</#if>
<#if additionalField.dataType == 8 ><#--单选框-->
<@spring.formRadioButtons path="entity.attr['${additionalField.field}']" options=additionalField.optValue?split(',') separator=' ' attributes='class="radio-inline"' />
</#if>
<#if additionalField.dataType == 9 ><#--复选框-->
</#if>
</#if>
</#macro>