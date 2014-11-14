
<%@ page import="admin.AdminApk" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="adminMain"/>
		<g:set var="entityName" value="${message(code: 'adminApk.label', default: 'AdminApk')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-adminApk" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-adminApk" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list adminApk">
			
				<g:if test="${adminApkInstance?.app_type}">
				<li class="fieldcontain">
					<span id="app_type-label" class="property-label"><g:message code="adminApk.app_type.label" default="Apptype" /></span>
					
						<span class="property-value" aria-labelledby="app_type-label"><g:fieldValue bean="${adminApkInstance}" field="app_type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminApkInstance?.platform}">
				<li class="fieldcontain">
					<span id="platform-label" class="property-label"><g:message code="adminApk.platform.label" default="Platform" /></span>
					
						<span class="property-value" aria-labelledby="platform-label"><g:fieldValue bean="${adminApkInstance}" field="platform"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminApkInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="adminApk.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${adminApkInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminApkInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="adminApk.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${adminApkInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminApkInstance?.newapkfeature}">
				<li class="fieldcontain">
					<span id="newapkfeature-label" class="property-label"><g:message code="adminApk.newapkfeature.label" default="Newapkfeature" /></span>
					
						<span class="property-value" aria-labelledby="newapkfeature-label"><g:fieldValue bean="${adminApkInstance}" field="newapkfeature"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminApkInstance?.versioncode}">
				<li class="fieldcontain">
					<span id="versioncode-label" class="property-label"><g:message code="adminApk.versioncode.label" default="Versioncode" /></span>
					
						<span class="property-value" aria-labelledby="versioncode-label"><g:fieldValue bean="${adminApkInstance}" field="versioncode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${adminApkInstance?.versionname}">
				<li class="fieldcontain">
					<span id="versionname-label" class="property-label"><g:message code="adminApk.versionname.label" default="Versionname" /></span>
					
						<span class="property-value" aria-labelledby="versionname-label"><g:fieldValue bean="${adminApkInstance}" field="versionname"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${adminApkInstance?.id}" />
					<g:link class="edit" action="edit" id="${adminApkInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
