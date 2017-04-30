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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<display:table pagesize="5" sort="list"
	class="displaytag" name="events" requestURI="${requestURI}" id="row">


	<spring:message code="event.picture" var="eventPicture" />
	<display:column title="${eventPicture}" sortable="false">
		<img src="${row.picture}" width="200" height="100" />
	</display:column>

	<spring:message code="event.title" var="eventTitle" />
	<display:column property="title" title="${eventTitle}" sortable="false"
		class="text-normal" />

	<spring:message code="event.moment" var="eventMoment" />
	<display:column property="moment" title="${eventMoment}"
		sortable="false" format="{0,date,dd/MM/YYYY HH:mm}"
		class="text-normal" />

	<spring:message code="event.description" var="eventDescription" />
	<display:column property="description" title="${eventDescription}"
		sortable="false" class="text-normal" />

	<spring:message code="event.numberSeatsOffered"
		var="eventnumberSeatsOffered" />
	<display:column property="numberSeatsOffered"
		title="${eventnumberSeatsOffered}" sortable="true" class="text-normal" />

</display:table>