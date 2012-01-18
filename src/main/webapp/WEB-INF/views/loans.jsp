<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
<head>
	<title>Loans</title>
	<link rel="stylesheet" href="resources/css/screen.css" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="resources/css/print.css" type="text/css" media="print"> 
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="/resources/css/ie.css" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body style="padding-top:15px">

<div class="container">

	<h1>All Loans</h1>
	
	<p>Add a <a href="new">new loan</a> &middot; <a href="loans">View all loans</a></p>
	
	<form:form method="GET">
	<p>
	Filter loans by country name:
	<input type="text" name="keyword">
	<input type="submit" value="Go" />
	
	</p>
	</form:form>
	
	<table widtd="100%" cellpadding="5" cellspacing="2">
	<tr>
		<th>Loan</th>
		<th>Name</th>
		<th>Status</th>
		<th>Amount</th>
		<th>Country</th>
		<th>Activity</th>
		<th>Use</th>
	</tr>
	<c:forEach items="${loans}" var="loan">
	<tr>
		<td widtd="100"><a href="show/${loan.id}">${loan.id}</a></td>
		<td>${loan.name}</td>
		<td>${loan.status}</td>
		<td>${loan.funded_amount}</td>
		<td>${loan.country}</td>
		<td>${loan.activity}</td>
		<td>${loan.use}</td>
	</tr>
	</c:forEach>
	</table>

</div>

</body>
</html>