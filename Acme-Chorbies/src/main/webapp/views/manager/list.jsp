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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<display:table pagesize="5" class="displaytag" name="managers" requestURI="${requestURI}" id="row">
	
	
	<spring:message code="manager.name" var="managerName" />
	<display:column property="name" title="${managerName}" sortable="false" />
	
	<spring:message code="manager.surname" var="managerSurname" />
	<display:column property="surName" title="${managerSurname}" sortable="false" />
	
	<spring:message code="manager.company" var="managerCompany" />
	<display:column property="company" title="${managerCompany}" sortable="false" />
	
	<spring:message code="manager.vatNumber" var="vatNumberManager" />
	<display:column property="vatNumber" title="${vatNumberManager}" sortable="false"/>
	
	<spring:message code="manager.totalChargedFee" var="managertotalChargedFee" />
	<display:column property="totalChargedFee" title="${managertotalChargedFee}" sortable="false"/>
	
		
</display:table>



