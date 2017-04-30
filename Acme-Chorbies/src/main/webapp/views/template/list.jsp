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


<display:table pagesize="5" class="displaytag" name="template" id="row">
	
	<spring:message code="template.genre" var="templateGenre" />
	<display:column property="genre" title="${templateGenre}" sortable="false" />
	
	<spring:message code="template.relationship" var="templateRelationship" />
	<display:column property="relationShip" title="${templateRelationship}" sortable="false" />
	
	
	<spring:message code="template.Age" var="templateapproximatedAge" />
	<display:column property="approximatedAge" title="${templateapproximatedAge}" sortable="false" />
	
	<spring:message code="template.keyword" var="templateKeyword" />
	<display:column property="keyword" title="${templateKeyword}" sortable="false" />

	<display:column>
		<a href="template/edit.do?templateId=${row.id}"> <spring:message
				code="template.edit" />
		</a>

	</display:column>

</display:table>


<display:table pagesize="5" class="displaytag" name="coordinates" id="row">
	
	<spring:message code="coordinate.country" var="coordinateCountry" />
	<display:column property="country" title="${coordinateCountry}" sortable="false" />
	
	<spring:message code="coordinate.state" var="coordinateState" />
	<display:column property="state" title="${coordinateState}" sortable="false" />
	
	<spring:message code="coordinate.province" var="coordinateProvince" />
	<display:column property="province" title="${coordinateProvince}" sortable="false" />
	
	<spring:message code="coordinate.city" var="coordinateCity" />
	<display:column property="city" title="${coordinateCity}" sortable="false" />
	
	<security:authorize access="hasRole('CHORBI')">
		<display:column>
			<a href="template/editCoordinate.do?templateId=${template.id}"> <spring:message
					code="template.editCoordinate" />
			</a>

		</display:column>
	</security:authorize>
	
	
</display:table>

<security:authorize access="hasRole('CHORBI')">
	<jstl:if test="${template.coordinate == null }">
		<a href="template/createCoordinate.do"> <spring:message
				code="template.createCoordinate" />
		</a>
	</jstl:if>
</security:authorize>
