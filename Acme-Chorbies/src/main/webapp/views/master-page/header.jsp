<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="welcome/index.do"><img src="images/logo.png"
		alt="Acme-Chorbies Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/chorbi/list.do"><spring:message
								code="master.page.administrator.listAllChorbies" /></a></li>
					<li><a href="banner/administrator/list.do"><spring:message
								code="master.page.administrator.banner" /></a></li>
					<li><a href="configuration/administrator/list.do"><spring:message
								code="master.page.administrator.configuration" /></a></li>
					<li><a href="administrator/dashboard.do"><spring:message
								code="master.page.administrator.dashboard" /></a></li>
					<li><a href="administrator/listManagers.do"><spring:message
								code="master.page.administrator.list.manager" /></a></li>
				</ul>
			</li>
			
		</security:authorize>

		<security:authorize access="hasRole('MANAGER')">

			<li><a class="fNiv"><spring:message
						code="master.page.myevents" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="event/myEvents.do"><spring:message
								code="master.page.list.myevents" /></a></li>
					<li><a href="event/manager/create.do"><spring:message
								code="master.page.list.create.event" /></a></li>			

				</ul></li>
				<li><a class="fNiv" href="managerDomain/viewProfile.do"><spring:message
						code="master.page.viewMyProfile" /></a>
			</li>
				

		</security:authorize>


		<security:authorize access="hasRole('CHORBI')">
			<li><a class="fNiv"><spring:message
						code="master.page.template" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="template/list.do"><spring:message
								code="master.page.chorbi.editTemplate" /></a></li>
					<li><a href="template/result.do"><spring:message
								code="master.page.chorbi.myTemplate" /></a></li>
				</ul></li>


			<li><a class="fNiv"><spring:message code="master.page.chirp" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="chirp/create.do"><spring:message
								code="master.page.chirp.create" /></a></li>
					<li><a href="chirp/listSentMessages.do"><spring:message
								code="master.page.chirp.sent" /></a></li>
					<li><a href="chirp/listReceivedMessages.do"><spring:message
								code="master.page.chirp.received" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="chorbi/chorbi/myLikes.do"><spring:message
						code="master.page.myLikes" /></a></li>

			<li><a class="fNiv" href="chorbi/chorbi/likesToMe.do"><spring:message
						code="master.page.whoILike" /></a></li>
			<li><a class="fNiv" href="chorbi/viewProfile.do"><spring:message
						code="master.page.viewMyProfile" /></a></li>
						
			<li><a class="fNiv" href="chorbi/listMyEvents.do"><spring:message
						code="master.page.viewMyEvents" /></a></li>



		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<li><a class="fNiv" href="chorbi/register.do"><spring:message
						code="master.page.register" /></a></li>
			<li><a class="fNiv" href="event/listEventOfferMonth.do"><spring:message
						code="master.page.listEventOfferMonth" /></a></li>
			<li><a class="fNiv" href="event/list.do"><spring:message
						code="master.page.listEvents" /></a></li>
			<li><a class="fNiv" href="managerDomain/register.do"><spring:message
						code="master.page.registerManager" /></a>
			</li>

		</security:authorize>

		<security:authorize access="isAuthenticated()">

			<li><a class="fNiv" href="chorbi/list.do"><spring:message
						code="master.page.know.people" /></a></li>
			<li><a class="fNiv" href="event/list.do"><spring:message
						code="master.page.listEvents" /></a></li>
			<li><a class="fNiv" href="managerDomain/register.do"><spring:message
						code="master.page.registerManager" /></a>
			</li>




			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>

				</ul></li>



		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

