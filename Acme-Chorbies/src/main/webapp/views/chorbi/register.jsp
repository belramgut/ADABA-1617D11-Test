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



<form:form action="chorbi/register.do" modelAttribute="chorbi">
	<fieldset>
		<legend><spring:message code="chorbi.userAccountInfo"/></legend>
		<acme:textbox code="chorbi.username" path="username"/>
		<acme:password code="chorbi.password" path="password"/>
		<acme:password code="chorbi.passwordConf" path="passwordCheck"/>
	</fieldset>


	<fieldset>
		<legend><spring:message code="chorbi.contactInfo"/></legend>
		<acme:textbox code="chorbi.name" path="name"/>
		<acme:textbox code="chorbi.surname" path="surName"/>
		<acme:textbox code="chorbi.myEmail" path="email"/>
		<acme:textbox code="chorbi.myPhone" path="phone"/>
	</fieldset>
	
	
	<fieldset>
		<legend><spring:message code="chorbi.personalInfo"/></legend>
		<acme:textbox code="chorbi.picture" path="picture"/>
		<acme:textarea code="chorbi.desciption" path="description"/>
		<acme:textbox code="chorbi.birthDate" path="birthDate"/>
		
		
		<spring:message code="chorbi.genre" />
		<form:select path="genre" >
		<option value="MALE">MALE</option>
  		<option value="FEMALE" selected>FEMALE</option>
		</form:select>
		<br/>
		
		<spring:message code="chorbi.relationship" />
		<form:select path="relation" >
		<option value="ACTIVITIES">ACTIVITIES</option>
  		<option value="FRIENDSHIP">FRIENDSHIP</option>
		<option value="LOVE" selected>LOVE</option>	
		</form:select>
		</fieldset>
		
		
	<fieldset>
		<legend><spring:message code="chorbi.locationInfo"/></legend>
		<acme:textbox code="chorbi.country" path="country"/>
		<acme:textbox code="chorbi.state" path="state"/>
		<acme:textbox code="chorbi.province" path="province"/>
		<acme:textbox code="chorbi.city" path="city"/>
	</fieldset>
	
	
	
	<form:checkbox path="termsOfUse"/>
	<spring:message code="chorbi.termsOfUse.confirmation"/> 
	<a href="chorbi/dataProtection.do">
		<spring:message code="chorbi.termsOfUse.link" />
	</a>
	<form:errors cssClass="error" path="termsOfUse" />
	<p class="mandatory"><spring:message code="mandatory.fields" /></p>
	<acme:submit name="save" code="chorbi.accept"/>			
	<acme:cancel url="welcome/index.do" code="chorbi.cancel"/>

	

</form:form>