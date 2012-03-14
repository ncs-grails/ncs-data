<%@ page contentType="text/html"%>
<html>
  <head>
	<title>Notification of data upload</title>
  </head>
  <body>
  	<h1>Data was uploaded to NCS Data Web Service</h1>
  	<p>Upload Time: ${referenceDate} </p>
  	<pre>Data Exchange Partner: ${dataExchangePartner?.name}<pre>
  	<pre>Client Address: ${clientAddress}<pre>
	<h2>Data Details</h2><g:each var="k" in="${dataDetails.keySet()}">
	<strong>${k}:</strong> ${dataDetails[k]}<br/></g:each>
  </body>
</html>
