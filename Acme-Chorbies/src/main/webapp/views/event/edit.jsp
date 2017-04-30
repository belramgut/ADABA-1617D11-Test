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

<form:form action="${requestURI}" modelAttribute="event">

	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<fieldset>
		<legend>
			<spring:message code="event.info" />
		</legend>
		
		<acme:textbox code="event.title" path="title" />
		<br>
		<acme:textarea code="event.description" path="description" />
		<br>
		<acme:textbox code="event.moment" path="moment" />
		<br>
		<form:label path="picture">
			<spring:message code="event.picture" />
			</form:label>
			<form:input path="picture" placeholder="https://c1.staticflickr.com/xxxxx.jpg" size="90" />
			<form:errors path="picture" cssClass="error" />
		<br>
		<br>
		<acme:textbox code="event.numberSeatsOffered" path="numberSeatsOffered" />
		<br>
		
	
	</fieldset>
	<br><br>


	<acme:submit name="save" code="event.save" />

	<acme:cancel url="event/myEvents.do" code="event.cancel" />

</form:form>