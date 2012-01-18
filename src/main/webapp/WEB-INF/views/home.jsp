<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<link rel="stylesheet" href="resources/css/screen.css" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="resources/css/print.css" type="text/css" media="print"> 
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="/resources/css/ie.css" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body style="padding-top:15px">

<div class="container">

	<div style="float:left;width:200px"><a href="http://www.cloudspokes.com"><img src="resources/images/cloudspokes-logo.png"></a></div>

	<h1>Kiva Loan Browser Demo</h1>
	
	<p>This is a small demo written by <a href="http://www.cloudspokes.com">CloudSpokes</a> that demonstrates some basic features of the DynamoDB. This demo loads
	loan data from this <a href="http://api.kivaws.org/v1/loans/newest.json" target="_blank">Kiva JSON feed</a> into DynamoDB,  displays these loans from the database, 
	filters them by keyword, adds new records and updates existing ones. You can find the source for this demo on our <a href="http://www.github.com/cloudspokes">GitHub repo.</a></p>
	
	<p><a href="loans">Click here to start the demo</a></p>
	
	<p>You can also <a href="loadData">refresh the database</a> with new loan data from Kiva. This will delete all of the current items 
	in the database, grab the current loans from the <a href="http://api.kivaws.org/v1/loans/newest.json" target="_blank">Kiva JSON feed</a> 
	and insert them into DynamoDB.</p>
	
	<h3>Visit the <a href="http://www.cloudspokes.com">CloudSpokes website</a> or follow us on:</h3> 
	
	<a href="http://www.github.com/cloudspokes"><img src="https://s3.amazonaws.com/cs-public/email_images/github_64.png"></a>
	<a href="http://www.twitter.com/cloudspokes"><img src="https://s3.amazonaws.com/cs-public/email_images/twitter_64.png"></a>
	<a href="http://www.facebook.com/cloudspokes"><img src="https://s3.amazonaws.com/cs-public/email_images/facebook_64.png"></a>
	<a href="http://www.youtube.com/cloudspokescommunity"><img src="https://s3.amazonaws.com/cs-public/email_images/youtube_icon.png" width="59" style="padding-left:5px;padding-top:3px"></a>
	<a href="http://blog.cloudspokes.com"><img src="https://s3.amazonaws.com/cs-public/email_images/blogicon.png" width="55" style="padding-left:10px;padding-top:4px"></a>
	<a href="http://www.cloudspokes.com/challenges/feed"><img src="https://s3.amazonaws.com/cs-public/email_images/rss_icon.png" width="57" style="padding-left:10px;padding-top:3px"></a>

</div>

</body>
</html>
