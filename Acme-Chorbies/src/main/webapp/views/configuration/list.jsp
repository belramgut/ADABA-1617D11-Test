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


<display:table pagesize="5" class="displaytag" name="configurations"
	requestURI="${requestURI}" id="row">

	<spring:message code="configuration.hour" var="configurationHour" />
	<display:column property="hour" title="${configurationHour}" sortable="false" />
	
	<spring:message code="configuration.minute" var="configurationMinute" />
	<display:column property="minute" title="${configurationMinute}" sortable="false" />
	
	<spring:message code="configuration.second" var="configurationSecond" />
	<display:column property="second" title="${configurationSecond}" sortable="false" />
	
	<spring:message code="configuration.chorbiesFee" var="configurationChorbiesFee" />
	<display:column property="chorbiesFee" title="${configurationChorbiesFee}" sortable="false" />
	
	<spring:message code="configuration.managersFee" var="configurationManagersFee" />
	<display:column property="managersFee" title="${configurationManagersFee}" sortable="false" />

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a
				href="configuration/administrator/edit.do?configurationId=${row.id}">
				<spring:message code="configuration.change" />
			</a>

		</display:column>
	</security:authorize>

	

</display:table>
