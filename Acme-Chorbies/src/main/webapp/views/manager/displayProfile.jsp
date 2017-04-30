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

<spring:message code="manager.personalInfo" var="managerPersonalInfo" />
<h2>
	<jstl:out value="${managerPersonalInfo}" />
</h2>
<display:table pagesize="5" class="displaytag" name="manager"
	requestURI="${requestURI}" id="row">

	<spring:message code="manager.name" var="managerName" />
	<display:column property="name" title="${managerName}" sortable="false" />

	<spring:message code="manager.surname" var="managerSurname" />
	<display:column property="surName" title="${managerSurname}"
		sortable="false" />
		
	<spring:message code="manager.company" var="managerCompany" />
	<display:column property="company" title="${managerCompany}"
		sortable="false" />	

</display:table>



<security:authorize access="hasRole('MANAGER')">
	<div>

		<jstl:choose>
			
			<jstl:when test="${principal.id == row.id }">
				<br />
				<br>
				<spring:message code="manager.CreditCard" var="managerCC" />
				<h2>
					<jstl:out value="${managerCC}" />
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
						<a href="profile/editCreditCardManager.do?managerId=${row.id}"> <spring:message
								code="chorbi.editCreditCard" />
						</a>
					</jstl:when>

					<jstl:when test="${toCreditCard == false}">
						<a href="profile/createCreditCardManager.do"> <spring:message
								code="chorbi.createCreditCard" />
						</a>
					</jstl:when>

				</jstl:choose>
			</jstl:when>
		</jstl:choose>
	</div>
</security:authorize>

