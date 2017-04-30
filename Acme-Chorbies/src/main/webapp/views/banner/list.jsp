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


<display:table pagesize="5" class="displaytag" name="banners"
	requestURI="${requestURI}" id="row">

	<spring:message code="banner.url" />
	<display:column title="${bannerURL}"
		sortable="false" >
		<img src="${row.url}" width="200" height="100" />
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a
				href="banner/administrator/edit.do?bannerId=${row.id}">
				<spring:message code="banner.change" />
			</a>

		</display:column>
	</security:authorize>

	

</display:table>
