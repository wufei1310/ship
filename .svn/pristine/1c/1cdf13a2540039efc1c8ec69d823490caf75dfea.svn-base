
<%@ page import="admin.AdminApk" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="adminMain"/>
		<g:set var="entityName" value="${message(code: 'adminApk.label', default: 'AdminApk')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-adminApk" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-adminApk" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="app_type" title="${message(code: 'adminApk.app_type.label', default: 'Apptype')}" />
					
						<g:sortableColumn property="platform" title="${message(code: 'adminApk.platform.label', default: 'Platform')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'adminApk.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'adminApk.lastUpdated.label', default: 'Last Updated')}" />
					
						<g:sortableColumn property="newapkfeature" title="${message(code: 'adminApk.newapkfeature.label', default: 'Newapkfeature')}" />
					
						<g:sortableColumn property="versioncode" title="${message(code: 'adminApk.versioncode.label', default: 'Versioncode')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${adminApkInstanceList}" status="i" var="adminApkInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${adminApkInstance.id}">${fieldValue(bean: adminApkInstance, field: "app_type")}</g:link></td>
					
						<td>${fieldValue(bean: adminApkInstance, field: "platform")}</td>
					
						<td><g:formatDate date="${adminApkInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${adminApkInstance.lastUpdated}" /></td>
					
						<td>${fieldValue(bean: adminApkInstance, field: "newapkfeature")}</td>
					
						<td>${fieldValue(bean: adminApkInstance, field: "versioncode")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${adminApkInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
