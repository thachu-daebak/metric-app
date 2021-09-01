# metric-app
Java Servlet Application

metric-app :
It is a Dynamic Java Web Application.
It is designed to access the json data and store in a data structure.

Requirement 1:
	POST : http://localhost:{port}/metric-app/postMetric
Requirement 2:
	Schedule CRON Job for every 30 minutes to clear older data.
Requirement 3:
	GET : http://localhost:{port}/metric-app/getRecords?t1=1615973008000&t2=1615973008000
