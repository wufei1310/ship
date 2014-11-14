<%@ page import="admin.AdminApk" %>



<div class="fieldcontain ${hasErrors(bean: adminApkInstance, field: 'app_type', 'error')} ">
	<label for="app_type">
		<g:message code="adminApk.app_type.label" default="Apptype" />
		
	</label>
	<g:select name="app_type" from="${adminApkInstance.constraints.app_type.inList}" value="${adminApkInstance?.app_type}" valueMessagePrefix="adminApk.app_type" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminApkInstance, field: 'platform', 'error')} ">
	<label for="platform">
		<g:message code="adminApk.platform.label" default="Platform" />
		
	</label>
	<g:select name="platform" from="${adminApkInstance.constraints.platform.inList}" value="${adminApkInstance?.platform}" valueMessagePrefix="adminApk.platform" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminApkInstance, field: 'newapkfeature', 'error')} ">
	<label for="newapkfeature">
		<g:message code="adminApk.newapkfeature.label" default="Newapkfeature" />
		
	</label>
  <textarea name="newapkfeature" cols="150" rows="10">${adminApkInstance?.newapkfeature}</textarea>
</div>

<div class="fieldcontain ${hasErrors(bean: adminApkInstance, field: 'versioncode', 'error')} required">
	<label for="versioncode">
		<g:message code="adminApk.versioncode.label" default="Versioncode" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="versioncode" type="number" value="${adminApkInstance.versioncode}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: adminApkInstance, field: 'versionname', 'error')} ">
	<label for="versionname">
		<g:message code="adminApk.versionname.label" default="Versionname" />
		
	</label>
	<g:textField name="versionname" value="${adminApkInstance?.versionname}"/>
</div>

