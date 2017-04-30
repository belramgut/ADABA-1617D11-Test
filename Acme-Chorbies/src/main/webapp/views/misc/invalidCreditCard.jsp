<%--
 * forbiddenOperation.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:choose>
<jstl:when test="${creditCard == null }">
	<p><spring:message code = "nullCreditCard" /></p> 
</jstl:when>
<jstl:otherwise>
	<p><spring:message code = "invalidCreditCard" /></p>	
</jstl:otherwise>
</jstl:choose>
 

<p><a href="<spring:url value='/' />"><spring:message code = "back" /></a><p>