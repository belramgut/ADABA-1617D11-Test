<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="configuration">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<fieldset>
		<legend>
			<spring:message code="configuration.info" />
		</legend>
		<acme:number min="0" step="1" code="configuration.hour" path="hour"/>
		<br>
		<acme:number min="0" max="60" step="1" code="configuration.minute" path="minute"/>
		<br>
		<acme:number min="0" max="60" step="1" code="configuration.second" path="second"/>
		<br>
		<acme:textbox code="configuration.chorbiesFee" path="chorbiesFee"/>
		<br>
		<acme:textbox code="configuration.managersFee" path="managersFee"/>
		<br>
	</fieldset>

	<acme:submit name="save" code="configuration.save" />

	<acme:cancel url="configuration/administrator/list.do" code="configuration.cancel" />

</form:form>