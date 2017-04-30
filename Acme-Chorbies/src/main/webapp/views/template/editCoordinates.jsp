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

<form:form action="template/editCoordinate.do" modelAttribute="coordinate">





	<acme:textbox code="coordinate.country" path="country" />
	<br>

	<acme:textbox code="coordinate.state" path="state" />
	<br>
	
	<acme:textbox code="coordinate.province" path="province" />
	<br>
	
	<acme:textbox code="coordinate.city" path="city" />
	<br>


	<acme:submit name="save" code="template.save" />

	<acme:cancel url="template/list.do" code="template.cancel" />

</form:form>