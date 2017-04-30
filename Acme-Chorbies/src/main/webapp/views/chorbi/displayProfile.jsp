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

<spring:message code="chorbi.personalInfo" var="chorbiPersonalInfo" />
<h2>
	<jstl:out value="${chorbiPersonalInfo}" />
</h2>
<display:table pagesize="5" class="displaytag" name="chorbi"
	requestURI="${requestURI}" id="row">

	<spring:message code="chorbi.picture" var="chorbiPicture" />
	<display:column title="${chorbiPicture}" sortable="false">
		<img src="${row.picture}" width="200" height="100" />
	</display:column>

	<spring:message code="chorbi.name" var="chorbiName" />
	<display:column property="name" title="${chorbiName}" sortable="false" />

	<spring:message code="chorbi.surname" var="chorbiSurname" />
	<display:column property="surName" title="${chorbiSurname}"
		sortable="false" />

	<spring:message code="chorbi.genre" var="chorbiGenre" />
	<display:column property="genre" title="${chorbiGenre}"
		sortable="false" />

	<spring:message code="chorbi.desc" var="chorbiDesc" />
	<display:column property="description" title="${chorbiDesc}"
		sortable="false" />

	<spring:message code="chorbi.birthDate" var="chorbiBirthDate" />
	<display:column property="birthDate" title="${chorbiBirthDate}"
		sortable="false" />

	<spring:message code="chorbi.relationship" var="chorbiRelationship" />
	<display:column property="relationship" title="${chorbiRelationship}"
		sortable="false" />


</display:table>



<security:authorize access="hasRole('CHORBI')">
	<div>

		<jstl:choose>
			<jstl:when test="${toLike==true and principal.id ne row.id}">
				<a href="chorbi/chorbi/like.do?chorbiId=${row.id}"><img
					src="images/like.jpg" width="120" height="120" /></a>
				<br>
				
				&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="chorbi/chorbi/like.do?chorbiId=${row.id}"> <spring:message
						code="chorbi.giveLike" />
				</a>

			</jstl:when>
			<jstl:when test="${toLike==false and principal.id ne row.id}">
				<a href="chorbi/chorbi/cancelLike.do?chorbiId=${row.id}"><img
					src="images/cancelLike.jpg" width="80" height="80" /></a>
				<br>

				<a href="chorbi/chorbi/cancelLike.do?chorbiId=${row.id}"> <spring:message
						code="chorbi.cancelLike" />
				</a>


			</jstl:when>

			<jstl:when test="${principal.id == row.id }">
				<a href="profile/editProfile.do?chorbiId=${row.id}"> <spring:message
						code="chorbi.editprofile" />
				</a>


				<br>
				<spring:message code="chorbi.CreditCard" var="chorbiCC" />
				<h2>
					<jstl:out value="${chorbiCC}" />
				</h2>
				<display:table pagesize="5" class="displaytag" name="creditCard"
					id="creditCard">

					<spring:message code="creditCard.holderName" var="holderName" />
					<display:column property="holderName" title="${holderName}"
						sortable="false" />

					<spring:message code="creditCard.number" var="number" />
					<display:column property="number" title="${number}"
						sortable="false" />

					<spring:message code="creditCard.brandName" var="brandName" />
					<display:column property="brandName" title="${brandName}"
						sortable="false" />

					<spring:message code="creditCard.expirationMonth"
						var="expirationMonth" />
					<display:column property="expirationMonth"
						title="${expirationMonth}" sortable="false" />

					<spring:message code="creditCard.expirationYear"
						var="expirationYear" />
					<display:column property="expirationYear" title="${expirationYear}"
						sortable="false" />

					<spring:message code="creditCard.cvvCode" var="cvvCode" />
					<display:column property="cvvCode" title="${cvvCode}"
						sortable="false" />
				</display:table>
				<br>
				<jstl:choose>
					<jstl:when test="${toCreditCard == true}">
						<a href="profile/editCreditCard.do?chorbiId=${row.id}"> <spring:message
								code="chorbi.editCreditCard" />
						</a>
					</jstl:when>

					<jstl:when test="${toCreditCard == false}">
						<a href="profile/createCreditCard.do"> <spring:message
								code="chorbi.createCreditCard" />
						</a>
					</jstl:when>

				</jstl:choose>
			</jstl:when>
		</jstl:choose>
	</div>
</security:authorize>

<spring:message code="chorbi.locationInfo" var="chorbiLocationInfo" />
<h2>
	<jstl:out value="${chorbiLocationInfo}" />
</h2>
<display:table pagesize="5" class="displaytag" name="chorbi"
	requestURI="${requestURI}" id="coordinate">

	<spring:message code="chorbi.country" var="chorbiCountry" />
	<display:column property="coordinate.country" title="${chorbiCountry}"
		sortable="false" />

	<spring:message code="chorbi.state" var="chorbiState" />
	<display:column property="coordinate.state" title="${chorbiState}"
		sortable="false" />

	<spring:message code="chorbi.province" var="chorbiProvince" />
	<display:column property="coordinate.province"
		title="${chorbiProvince}" sortable="false" />

	<spring:message code="chorbi.city" var="chorbiCity" />
	<display:column property="coordinate.city" title="${chorbiCity}"
		sortable="false" />


</display:table>

<jstl:choose>
<jstl:when test="${principal.id == row.id }">
<br />
<a href="profile/editLocationInformation.do?chorbiId=${row.id}"> <spring:message
		code="chorbi.editLocationInformation" />
</a>
</jstl:when>
</jstl:choose>

<spring:message code="chorbi.contactInfo" var="chorbiContactInfo" />
<h2>
	<jstl:out value="${chorbiContactInfo}" />
</h2>
<display:table pagesize="5" class="displaytag" name="chorbi"
	requestURI="${requestURI}" id="row">

	<spring:message code="chorbi.email" var="chorbiEmail" />
	<display:column title="${chorbiEmail}" sortable="false">
		<jstl:out value="***"></jstl:out>
	</display:column>

	<spring:message code="chorbi.phone" var="chorbiPhone" />
	<display:column title="${chorbiPhone}" sortable="false">
		<jstl:out value="***"></jstl:out>
	</display:column>

</display:table>