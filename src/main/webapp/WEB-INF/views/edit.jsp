<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
<head>
	<title>Edit Loan Status</title>
	<link rel="stylesheet" href="../resources/css/screen.css" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="../resources/css/print.css" type="text/css" media="print"> 
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="/resources/css/ie.css" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body style="padding-top:15px">

<div class="container">

	<h1>Edit Loan Status</h1>
	
	<form:form commandName="loan">
	<table>
		<tr>
			<td>ID:</td>
			<td>${loan.id}</td>
		</tr>
		<tr>
			<td>Name</td>
			<td>${loan.name}</td>
		</tr>
		<tr>
			<td>Status</td>
			<td><form:input path="status" /></td>
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
		<tr>
			<td colspan="2"><input type="submit" value="Save" /></td>
		</tr>
	</table>
	</form:form>

</div>

</body>
</html>