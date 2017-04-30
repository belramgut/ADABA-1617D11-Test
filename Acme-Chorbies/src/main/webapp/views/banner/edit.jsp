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

<form:form action="${requestURI}" modelAttribute="banner">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<fieldset>
		<legend>
			<spring:message code="banner.info" />
		</legend>
		<div>
			<form:label path="url">
				<spring:message code="banner.url" />
			</form:label>
			<form:input path="url" placeholder="https://c1.staticflickr.com/xxxxx.jpg" size="70" />
			<form:errors path="url" cssClass="error" />
		</div>
		<br>
	</fieldset>



	<acme:submit name="save" code="banner.save" />

	<acme:cancel url="banner/administrator/list.do" code="banner.cancel" />

</form:form>