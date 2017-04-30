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


<display:table pagesize="5" class="displaytag" name="tastes" requestURI="${requestURI}" id="row">
	
	<spring:message code="chorbi.picture" var="chorbiPicture" />
	<display:column title="${chorbiPicture}" sortable="false" >
		<img src="${row.chorbi.picture}" width="200" height="100" />
	</display:column>
	
	<spring:message code="chorbi.name" var="chorbiName" />
	<display:column property="chorbi.name" title="${chorbiName}" sortable="false" />
	
	<spring:message code="chorbi.surname" var="chorbiSurname" />
	<display:column property="chorbi.surName" title="${chorbiSurname}" sortable="false" />
	
	<spring:message code="chorbi.genre" var="chorbiGenre" />
	<display:column property="chorbi.genre" title="${chorbiGenre}" sortable="false" />
	
	<spring:message code="chorbi.birthDate" var="chorbiBirthDate" />
	<display:column property="chorbi.birthDate" title="${chorbiBirthDate}" sortable="false" format="{0,date,YYYY/MM/dd}"/>
	

	<spring:message code="chorbi.relationship" var="chorbiRelationship" />
	<display:column property="chorbi.relationship" title="${chorbiRelationship}" sortable="false" />
	
	
	<spring:message code="taste.comment" var="tasteComment" />
	<display:column title="${tasteComment}" sortable="false" ><jstl:out value="${row.comment}"/></display:column>
	
	<%-- <spring:message code="taste.momment" var="tasteMomment" />
	<display:column property="moment" title="${tasteMomment}" sortable="false" format="{0,date,dd/MM/yyyy HH:mm}" /> --%>
	
	<spring:message code="taste.momment" var="tasteMoment" />
	<display:column title="${tasteMoment}" sortable="false">
		<fmt:formatDate type="both" dateStyle="medium" timeStyle="short"
			value="${row.moment}" />
		
	</display:column>
	
	<spring:message code="taste.stars" var="tasteStars" />
	<display:column title="${tasteStars}" sortable="false" ><jstl:out value="${row.stars}"/></display:column>
	
	
	<security:authorize access="isAuthenticated()">
		<display:column>
			<a
				href="chorbi/profile.do?chorbiId=${row.chorbi.id}">
				<spring:message code="chorbi.profile" />
			</a>

		</display:column>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<display:column>
			<a
				href="chorbi/listWhoLikeThem.do?chorbiId=${row.chorbi.id}">
				<spring:message code="chorbi.ListWhoLikeThem" />
			</a>

		</display:column>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<display:column>
			<a
				href="chorbi/listWhoLikedThis.do?chorbiId=${row.chorbi.id}">
				<spring:message code="chorbi.ListWhoLikedThis" />
			</a>

		</display:column>
	</security:authorize>
	

</display:table>