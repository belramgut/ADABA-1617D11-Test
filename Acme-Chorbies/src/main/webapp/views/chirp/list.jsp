<%--

 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table pagesize="10" class="displaytag" name="chirps"
	requestURI="${requestURI}" id="row">

	<jstl:if test="${requestURI == 'chirp/listReceivedMessages.do'}">

		<spring:message code="chirp.sender" var="chirpSender" />
		<display:column property="sender.userAccount.username"
			title="${chirpSender}" sortable="false" />

	</jstl:if>

	<jstl:if test="${requestURI == 'chirp/listSentMessages.do'}">

		<spring:message code="chirp.recipient" var="chirpRecipient" />
		<display:column property="recipient.userAccount.username"
			title="${chirpRecipient}" sortable="false" />

	</jstl:if>




	<spring:message code="chirp.subject" var="chirpSubject" />
	<display:column property="subject" title="${chirpSubject}"
		sortable="false" />

	<spring:message code="chirp.text" var="chirpText" />
	<display:column property="text" title="${chirpText}" sortable="false" />

	<spring:message code="chirp.attachments" var="attachChirp" />
	<display:column title="${attachChirp}">

		<jstl:choose>

			<jstl:when test="${fn:contains(row.attachments, ',')}">
				<jstl:set var="attachparts"
					value="${fn:split(row.attachments, ',')}" />


				<jstl:forEach var="i" begin="0" end="${fn:length(attachparts)}">
					<a href="${attachparts[i]}"> <jstl:out
							value="${attachparts[i]}"></jstl:out></a>
						&nbsp; &nbsp;
			</jstl:forEach>

			</jstl:when>
			<jstl:otherwise>
				<a href="${row.attachments}"> <jstl:out
						value="${row.attachments}  "></jstl:out></a>
			</jstl:otherwise>

		</jstl:choose>

	</display:column>

	<spring:message code="chirp.moment" var="chirpMoment" />
	<display:column property="moment" title="${chirpMoment}"
		sortable="false" />


	<jstl:if test="${requestURI == 'chirp/listReceivedMessages.do'}">


		<jstl:if test="${row.sender.userAccount.authorities[0].authority=='CHORBI'}">

			<display:column>

				<a href="chirp/response/create.do?actorId=${row.sender.id}"> <spring:message
						code="chirp.create" />
				</a>

			</display:column>

			<display:column>

				<a href="chirp/reply.do?chirpId=${row.id}"> <spring:message
						code="chirp.reply" />
				</a>

			</display:column>

			<display:column>

				<acme:confirmDelete url="chirp/deleteReceived.do?chirpId=${row.id}"
					code="chirp.delete" codeConfirm="chirp.confirm.delete" />

			</display:column>

		</jstl:if>



		<jstl:if test="${row.sender.userAccount.authorities[0].authority=='MANAGER'}">

			<display:column>
				<spring:message code="chirp.notRespond" var="notRespond" />
				<p class="text-muted">
					<jstl:out value="${notRespond}"></jstl:out>
				</p>
			</display:column>

			<display:column>
				<spring:message code="chirp.notReply" var="notReply" />
				<p class="text-muted">
					<jstl:out value="${notReply}"></jstl:out>
				</p>
			</display:column>


			<display:column>

				<acme:confirmDelete url="chirp/deleteReceived.do?chirpId=${row.id}"
					code="chirp.delete" codeConfirm="chirp.confirm.delete" />

			</display:column>

		</jstl:if>

	</jstl:if>


	<jstl:if test="${requestURI == 'chirp/listSentMessages.do'}">

		<display:column>

			<a href="chirp/reply.do?chirpId=${row.id}"> <spring:message
					code="chirp.reply" />
			</a>

		</display:column>

		<display:column>
			<acme:confirmDelete url="chirp/deleteSent.do?chirpId=${row.id}"
				code="chirp.delete" codeConfirm="chirp.confirm.delete" />

		</display:column>

	</jstl:if>




</display:table>