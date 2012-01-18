<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Loan ${loan.id}</title>
	<link rel="stylesheet" href="../resources/css/screen.css" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="../resources/css/print.css" type="text/css" media="print"> 
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="/resources/css/ie.css" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body style="padding-top:15px">

<div class="container">

	<h1>Loan ${loan.id}</h1>
	
	<p><a href="../edit/${loan.id}">Edit this loan</a> &middot; <a href="../loans">View all loans</a></p>
	
	<table cellpadding="5" cellspacing="2">
	<tr>
		<td>Name</td>
		<td>${loan.name}</td>
	</tr>
	<tr>
		<td>Status</td>
		<td>${loan.status}</td>
	</tr>
	<tr>
		<td>Amount</td>
		<td>${loan.funded_amount}</td>
	</tr>
	<tr>
		<td>Country</td>
		<td>${loan.country}</td>
	</tr>
	<tr>
		<td>Activity</td>
		<td>${loan.activity}</td>
	</tr>
	<tr>
		<td>Use</td>
		<td>${loan.use}</td>
	</tr>
	</table>

</div>

</body>
</html>