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

<form:form action="chirp/delete.do" modelAttribute="chirp">

	<form:hidden path="id" />
	<form:hidden path="version" />


	<acme:textbox code="chirp.subject" path="subject" />
	<br>


	<acme:delete name="delete" code="chirp.delete"
		confirmationCode="<spring:chirp code="chirp.confirm.detele" />" />

	<acme:cancel url="welcome/index.do" code="chirp.cancel" />

</form:form>