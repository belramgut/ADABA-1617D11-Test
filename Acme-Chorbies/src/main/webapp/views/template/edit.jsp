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

<form:form action="template/edit.do" modelAttribute="template">


<form:hidden path="chorbies"/>



	<form:label path="genre">
		<spring:message code="template.genre" />:
		</form:label>
	<form:select path="genre">
		<form:option value="0" label="----" />
		<form:options items="${Genre}" />
	</form:select>
	<br>
	<br>


	<form:label path="relationShip">
		<spring:message code="template.relationship" />:
		</form:label>
	<form:select path="relationShip">
		<form:option value="0" label="----" />
		<form:options items="${Relationship}" />
	</form:select>
	<br>
	<br>

	<acme:textbox code="template.Age" path="approximatedAge" />
	<br>

	<acme:textbox code="template.keyword" path="keyword" />
	<br>


	<acme:submit name="save" code="template.save" />

	<acme:cancel url="template/list.do" code="template.cancel" />

</form:form>