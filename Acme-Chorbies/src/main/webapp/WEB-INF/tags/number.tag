<%--
 * textbox.tag
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="path" required="true" %>
<%@ attribute name="code" required="true" %>
<%@ attribute name="min" required="true" %>
<%@ attribute name="max" required="false" %>
<%@ attribute name="step" required="true" %>

<%@ attribute name="readonly" required="false" %>
<%@ attribute name="text" required="false" %>
<%@ attribute name="placeholder" required="false" %>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false" />
</jstl:if>

<%-- Definition --%>

<div class="form-group">
	<form:label path="${path}">
		<spring:message code="${code}" />
	</form:label>
	<jstl:choose>
	<jstl:when test="${placeholder != null}">
		<form:input type="number" min="${min}" max="${max}" step="${step}" path="${path}" readonly="${readonly}" class="form-control" placeholder="${placeholder}"/>
	</jstl:when>
	<jstl:otherwise>
		<form:input type="number" min="${min}" max="${max}" step="${step}" path="${path}" readonly="${readonly}" class="form-control"/>
		
	</jstl:otherwise>
	</jstl:choose>	

	<jstl:if test="${text != null}">
		<spring:message code="${text}"/>
	</jstl:if>
	<form:errors path="${path}" cssClass="error" />
</div>	
