<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<form:form action="manager/register.do" modelAttribute="manager">
	<fieldset>
		<legend><spring:message code="manager.userAccountInfo"/></legend>
		<acme:textbox code="manager.username" path="username"/>
		<acme:password code="manager.password" path="password"/>
		<acme:password code="manager.passwordConf" path="passwordCheck"/>
	</fieldset>


	<fieldset>
		<legend><spring:message code="manager.contactInfo"/></legend>
		<acme:textbox code="manager.name" path="name"/>
		<acme:textbox code="manager.surname" path="surName"/>
		<acme:textbox code="manager.myEmail" path="email"/>
		<acme:textbox code="manager.myPhone" path="phone"/>
	</fieldset>
	
	<fieldset>
		<legend><spring:message code="manager.companyData"/></legend>
		<acme:textbox code="manager.company" path="company"/>
		<acme:textbox code="manager.vatNumber" path="vatNumber"/>
	</fieldset>
	
	<form:checkbox path="termsOfUse"/>
	<spring:message code="manager.termsOfUse.confirmation"/> 
	<a href="manager/dataProtection.do">
		<spring:message code="manager.termsOfUse.link" />
	</a>
	<form:errors cssClass="error" path="termsOfUse" />
	<p class="mandatory"><spring:message code="mandatory.fields" /></p>
	<acme:submit name="save" code="manager.accept"/>			
	<acme:cancel url="welcome/index.do" code="manager.cancel"/>

	

</form:form>