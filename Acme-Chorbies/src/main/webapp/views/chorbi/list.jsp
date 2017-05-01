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


<display:table pagesize="5" class="displaytag" name="chorbies" requestURI="${requestURI}" id="row">
	
	<spring:message code="chorbi.picture" var="chorbiPicture" />
	<display:column title="${chorbiPicture}" sortable="false" >
		<img src="${row.picture}" width="200" height="100" />
	</display:column>
	
	<spring:message code="chorbi.name" var="chorbiName" />
	<display:column property="name" title="${chorbiName}" sortable="false" />
	
	<spring:message code="chorbi.surname" var="chorbiSurname" />
	<display:column property="surName" title="${chorbiSurname}" sortable="false" />
	
	<spring:message code="chorbi.genre" var="chorbiGenre" />
	<display:column property="genre" title="${chorbiGenre}" sortable="false" />
	
	<spring:message code="chorbi.birthDate" var="chorbiBirthDate" />
	<display:column property="birthDate" title="${chorbiBirthDate}" sortable="false" format="{0,date,YYYY/MM/dd}"/>
	
	
	
	<spring:message code="chorbi.relationship" var="chorbiRelationship" />
	<display:column property="relationship" title="${chorbiRelationship}" sortable="false" />
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="chorbi.updateDate" var="chorbiUpdateDate" />
		<display:column property="updateDate" title="${chorbiUpdateDate}" sortable="false" />
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="chorbi.totalChargedFee" var="chorbiTotalChargedFee" />
		<display:column property="totalChargedFee" title="${chorbiTotalChargedFee}" sortable="false" />
	</security:authorize>


	<security:authorize access="hasRole('CHORBI')">
		<display:column>
			<a href="chorbi/profile.do?chorbiId=${row.id}"> <spring:message
					code="chorbi.profile" />
			</a>

		</display:column>
	</security:authorize>



	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="administrator/chorbi/profile.do?chorbiId=${row.id}"> <spring:message
					code="chorbi.profile" />
			</a>

		</display:column>
	</security:authorize>



	<security:authorize access="isAuthenticated()">
		<display:column>
			<a
				href="chorbi/listWhoLikeThem2.do?chorbiId=${row.id}">
				<spring:message code="chorbi.ListWhoLikeThem" />
			</a>

		</display:column>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<display:column>
			<a
				href="chorbi/listWhoLikedThis.do?chorbiId=${row.id}">
				<spring:message code="chorbi.ListWhoLikedThis" />
			</a>

		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:choose>
				<jstl:when test = "${row.ban == false }" >
					<a
						href="administrator/chorbi/banChorbi.do?chorbiId=${row.id}">
						<spring:message code="chorbi.banChorbi" />
					</a>
				</jstl:when>
				<jstl:when test = "${row.ban == true }" >
					<a
						href="administrator/chorbi/unBanChorbi.do?chorbiId=${row.id}">
						<spring:message code="chorbi.unBanChorbi" />
					</a>
				</jstl:when>
			</jstl:choose>

		</display:column>
	</security:authorize>

</display:table>

<security:authorize access="hasRole('ADMIN')">
	
		<button onclick="window.location.href='administrator/chorbi/calculateFee.do'">
			<spring:message code="chorbi.calculateFee" />
		</button>
	
</security:authorize>

