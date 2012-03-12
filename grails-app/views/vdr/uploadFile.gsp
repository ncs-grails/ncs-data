<html>
  <head>
	<title>National Children's Study - University of Minnesota - Web Services</title>
	<meta name="layout" content="ncs" />
	<style>
		input {
			border-style: solid;
			border-width: thin;
			border-radius: 0.5em;
			-moz-border-radius: 0.2em;
			-webkit-border-radius: 0.2em;
		}
		input[type="submit"]{
			padding-left: 1em;
			padding-right: 1em;
		}
		input[type="submit"]:hover{
			background-color: #fc3;
			padding-right: 1em;
			cursor: pointer;
		}
	</style>
  </head>
  <body>
	<div id="pageBody">
	  <h1>National Children's Study - University of Minnesota - Web Services</h1>
	  <g:if test="${flash.message}">
	  <div class="message">${flash.message}</div>
	  </g:if>
	  <h2>Welcome to the National Children's Study Data Export Application</h2>
	  <p>This application provides RESTful web access to data import and
			  export utilities to collaborators of the University of Minnesota,
			  Ramsey County screening center.</p>


	  <g:if test="${key}">
	  <h2>Upload Datafile - Key Accepted</h2>
	  <p>You may now upload a data file to this service by selecting a file below and pressing "Upload".</p>

	  <g:form action="upload" method="post" enctype="multipart/form-data">
	  <label for="key">Key:</label>
	  <g:hiddenField name="key" value="${key}"/>
	  <input type="file" name="vdrFile" />
	  <g:submitButton name="upload" value="Upload" />
	  </g:form>
	  </g:if>
	  </p>
	</div>
  </body>
</html>
