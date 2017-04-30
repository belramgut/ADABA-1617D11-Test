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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="profile/editProfile.do" modelAttribute="chorbi">
<form:hidden path="id"/>
<form:hidden path="version"/>

<fieldset>
		<legend><spring:message code="chorbi.contactInfo"/></legend>
		<acme:textbox code="chorbi.name" path="name"/>
		<acme:textbox code="chorbi.surname" path="surName"/>
		<acme:textbox code="chorbi.email" path="email"/>
		<acme:textbox code="chorbi.phone" path="phone"/>

	</fieldset>
	<acme:submit name="save" code="chorbi.accept"/>			
	
</form:form>