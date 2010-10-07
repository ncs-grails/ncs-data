<html>
  <head>
	<title>National Children's Study - University of Minnesota - Web Services</title>
	<meta name="layout" content="ncs" />
	<style type="text/css" media="screen">

	  #nav {
		margin-top:20px;
		margin-left:30px;
		width:228px;
		float:left;

	  }
	  .homePagePanel * {
		margin:0px;
	  }
	  .homePagePanel .panelBody ul {
		list-style-type:none;
		margin-bottom:10px;
	  }
	  .homePagePanel .panelBody h1 {
		text-transform:uppercase;
		font-size:1.1em;
		margin-bottom:10px;
	  }
	  .homePagePanel .panelBody {
		background: url(images/leftnav_midstretch.png) repeat-y top;
		margin:0px;
		padding:15px;
	  }
	  .homePagePanel .panelBtm {
		background: url(images/leftnav_btm.png) no-repeat top;
		height:20px;
		margin:0px;
	  }

	  .homePagePanel .panelTop {
		background: url(images/leftnav_top.png) no-repeat top;
		height:11px;
		margin:0px;
	  }
	  h2 {
		margin-top:15px;
		margin-bottom:15px;
		font-size:1.2em;
	  }
	  #pageBody {
		margin-left:280px;
		margin-right:20px;
	  }
	</style>
  </head>
  <body>
	<div id="nav">
	  <div class="homePagePanel">
		<div class="panelTop"></div>
		<div class="panelBody">
		  <h1>NCS Application Status</h1>
		  <ul>
			<li>App version: <g:meta name="app.version"></g:meta></li>
			<li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
			<li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
			<li>JVM version: ${System.getProperty('java.version')}</li>
			<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
			<li>Domains: ${grailsApplication.domainClasses.size()}</li>
			<li>Services: ${grailsApplication.serviceClasses.size()}</li>
			<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
		  </ul>
		  <h1>Installed Plugins</h1>
		  <ul>
			<g:set var="pluginManager"
				   value="${applicationContext.getBean('pluginManager')}"></g:set>

			<g:each var="plugin" in="${pluginManager.allPlugins}">
			  <li>${plugin.name} - ${plugin.version}</li>
			</g:each>

		  </ul>
		</div>
		<div class="panelBtm"></div>
	  </div>
	</div>
	<div id="pageBody">
	  <h1>National Children's Study - University of Minnesota - Web Services</h1>
	  <h2>Welcome to the National Children's Study Data Export Application</h2>
	  <p>This application provides RESTful web access to data import and
			  export utilities to collaborators of the University of Minnesota,
			  Ramsey County screening center.</p>

	  <p>Below are some services available to you for consumtion.  If you
			  do not yet have a "Partner Private Key" issued, you may use the key
			  "DEMO" to view demo data for initial testing of your applications.</p>

	  <p>If you need access to these services, please send an inquiry to
			  info&#64;ncs.umn.edu.</p>

	  <p>All services require that the partner
			  private key be passed as the "key" parameter.</p>

	  <p>For general information on RESTful web services, please see the
		<a href="http://en.wikipedia.org/wiki/Representational_State_Transfer">Wikipedia article</a>, or
			  the <a href="http://www.grails.org/doc/1.3.x/guide/13.%20Web%20Services.html">grails web services</a>
			  section for Grails specific implementation questions.

	  </p>

	  <div id="controllerList" class="dialog">
		<h2>Available Demo Services:</h2>
		<ul>

		  <li class="controller">
		  Mailings
			<ul>
			  <li>
				<g:link controller="mailing" action="list" params="[key: 'DEMO']">
				  List Mailings
				</g:link>
			  </li>
			  <li>
				<g:link controller="mailing" action="show" params="[key: 'DEMO']" id="1">
					View Specific Mailing
				</g:link>
			  </li>
			</ul>
		  </li>

		</ul>
	  </div>
	</div>
  </body>
</html>
