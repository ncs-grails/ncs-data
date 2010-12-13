<html>
  <head>
	<title>Identify - Web Services - National Children's Study - University of Minnesota</title>
	<meta name="layout" content="ncs" />
	<style>
	  dd {
		margin-left: 1em;
}
dt {
  color: maroon;
}
	</style>
  </head>
  <body>
	<div id="nav">
	</div>
	<div id="pageBody">
	  <h1>National Children's Study - University of Minnesota - Web Services</h1>
	  <h2>Welcome to the National Children's Study Data Export Application</h2>

	  <hr />

	  <dl>
		<dt>Your address:</dt>
		<dd>${ipAddress}</dd>

		<g:if test="${clientHost}">
		  <dt>We Know you as:</dt>
		  <dd>${clientHost.hostname}</dd>
		</g:if>

		<g:if test="${dataExchangePartnerInstanceList}">
		  <dt>You are associated with:</dt>
		  <g:each var="dep" in="${dataExchangePartnerInstanceList}" >

			<dd>${dep.name}</dd>
		  </g:each>

		</g:if>

	  </dl>

	</div>
  </body>
</html>
