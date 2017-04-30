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


<form:form action="profile/createCreditCardManager.do" modelAttribute="creditCard">
<form:hidden path="id"/>
<form:hidden path="version"/>

<fieldset>
		<legend><spring:message code="creditCard.Information"/></legend>
		<acme:textbox code="creditCard.holderName" path="holderName"/>
		<spring:message code="creditCard.brandName" />
		<form:select path="brandName" >
		<option value="VISA">VISA</option>
  		<option value="MASTERCARD">MASTERCARD</option>
  		<option value="DISCOVER">DISCOVER</option>
  		<option value="DINNERS">DINNERS</option>
  		<option value="AMEX">AMEX</option>
  		</form:select>
		
		<br/>
		<acme:textbox code="creditCard.number" path="number"/>
		<acme:textbox code="creditCard.expirationMonth" path="expirationMonth"/>
		<acme:textbox code="creditCard.expirationYear" path="expirationYear"/>
		<acme:textbox code="creditCard.cvvCode" path="cvvCode"/>
	
	
	</fieldset>
	<acme:submit name="save" code="creditCard.accept"/>			
	
</form:form>