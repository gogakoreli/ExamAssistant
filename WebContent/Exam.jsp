<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, models.*, helper.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	Exam exam = (Exam) request.getAttribute("exam");
	Student student = (Student) request.getAttribute("student");
	ArrayList<ExamMaterial> materials = (ArrayList<ExamMaterial>) request.getAttribute("materials");
	Date timeLeft = student.getExamInformation().getTimeLeft(exam);
%>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title><%=exam.getName()%></title>

<style>
body {
	text-align: center;
	font-family: sans-serif;
	font-weight: 100;
}

h1 {
	color: #396;
	font-weight: 100;
	font-size: 40px;
	margin: 40px 0px 20px;
}

#clockdiv {
	font-family: sans-serif;
	color: #fff;
	display: inline-block;
	font-weight: 100;
	text-align: center;
	font-size: 30px;
}

#clockdiv>div {
	padding: 10px;
	border-radius: 3px;
	background: #00BF96;
	display: inline-block;
}

#clockdiv div>span {
	padding: 15px;
	border-radius: 3px;
	background: #00816A;
	display: inline-block;
}

.smalltext {
	padding-top: 5px;
	font-size: 16px;
}
</style>

</head>

<body>

	<%
		for (int i = 0; i < materials.size(); i++) {
	%>
	<a href="Download?fileLocation=<%=materials.get(i).getLocation()%>"><%=i + 1%>
		: <%=materials.get(i).getMaterial()%></a>
	<br>
	<%
		}
	%>

	<h1>დარჩენილი დრო</h1>
	<div id="clockdiv">
		<div>
			<span class="hours"></span>
			<div class="smalltext">საათი</div>
		</div>
		
		<div>
			<span class="minutes"></span>
			<div class="smalltext">წუთი</div>
		</div>
		
		<div>
			<span class="seconds"></span>
			<div class="smalltext">წამი</div>
		</div>
	</div>
	
	<br>
	<br>
	
	 <form method="POST" action="Upload" enctype="multipart/form-data" >
            File:
            <input type="file" name="file" id="file" />
            <br>
            <br>
            <input type="submit" value="Upload" name="upload" id="upload" />
       </form>

	<script type="text/javascript">
		function getTimeRemaining(endtime) {
			var t = Date.parse(endtime) - Date.parse(new Date());
			var seconds = Math.floor((t / 1000) % 60);
			var minutes = Math.floor((t / 1000 / 60) % 60);
			var hours = Math.floor((t / (1000 * 60 * 60)) % 24);
			return {
				'total' : t,
				'hours' : hours,
				'minutes' : minutes,
				'seconds' : seconds
			};
		}

		function initializeClock(id, endtime) {
			var clock = document.getElementById(id);
			var hoursSpan = clock.querySelector('.hours');
			var minutesSpan = clock.querySelector('.minutes');
			var secondsSpan = clock.querySelector('.seconds');

			function updateClock() {
				var t = getTimeRemaining(endtime);

				hoursSpan.innerHTML = ('0' + t.hours).slice(-2);
				minutesSpan.innerHTML = ('0' + t.minutes).slice(-2);
				secondsSpan.innerHTML = ('0' + t.seconds).slice(-2);

				if (t.total <= 0) {
					clearInterval(timeinterval);
				}
			}

			updateClock();
			var timeinterval = setInterval(updateClock, 1000);
		}

		var deadline = new Date(Date.parse(new Date())
				+
	<%=timeLeft.getTime()%>
		);
		initializeClock('clockdiv', deadline);
	</script>

</body>
</html>

