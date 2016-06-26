<%@page import="models.Lecturer"%>
<%@page import="servlets.ModifyExamServlet"%>
<%@ page import="models.Exam"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<%
	 Exam exam = (Exam) request.getAttribute("exam");
	 if (exam == null) response.sendRedirect("ErrorPage.jsp");
	%>
	
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;" charset="UTF-8">
<title>Student Page</title>
<style>
#logout {
	margin-top: 5px;
	margin-right: 5px;
	position: absolute;
	top: 0;
	right: 0;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=2">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="../ExamAssistant/includes/css/table.css">
<link rel="stylesheet" type="text/css"
	href="../ExamAssistant/includes/css/dropdown.css">

<script type="text/javascript">
	//show hide open note option 
	$(document).ready(function() {
		$('#openbookcb').click(function() {

			$("#opennodediv").toggle();
		});
	});
</script>
<script>
	function hasClass(elem, klass) {
		return (" " + elem.className + " ").indexOf(" " + klass + " ") > -1;
	}

	//enible editing of textfields 
	function EnibleEditing() {

		var x = document.getElementsByClassName("disabledfield");
		var arrayLength = x.length;
		for (var i = 0; i < arrayLength; i++) {

			if (hasClass(x[i], 'lecturer')) {
				//alert("I am an alert box!");
				x[i].removeAttribute("disabled");
				if (!hasClass(x[i], 'visibletextbox')) {
					x[i].className += " visibletextbox";
				}

			}
			//Do something
		}

	}
</script>


<script>
	//add event on addLectures input change 
	function getLectuerersSuggestions() {
		$.ajax({
			type : 'GET',
			url : 'getjsresult',
			data : {
				'opname' : 'getlecsuggestions',
				'st_pattern' : getlecstartpattern()
			},
			dataType : 'json',
			success : procdownloadedjson
		});
	}

	/* returns value of input box @tfaddlectuers */
	function getlecstartpattern() {
		var x = document.getElementById("tfaddlectuers");
		return x.value;
	}

	/* process downloaded data into list*/
	function procdownloadedjson(data) {
		var x = document.getElementById("suggestionsList");
		//show suggestions div
		if (!hasClass(x, 'drp_search_display')) {
			x.className += " drp_search_display";
		}

		var items = [];

		$
				.each(
						data,
						function(i, item) {
							var name = item.firstName + ' ' + item.lastName;
							items
									.push('<li> <a onclick="addNewLecturer('
											+ item.userID
											+ ',\''
											+ name
											+ '\'); return false;" name="sugglink">'
											+ item.mail
											+ '<br /> <span>'
											+ name + '</span></a></li>');
						}); // close each()

		$('#suggestionsList').empty();
		$('#suggestionsList').append(items.join(''));

	}

	//removes @id from list 
	function removeSubLectuer(id) {
		var list = document.getElementById("sublecturerslist");
		list.removeChild(document.getElementById(id));
	}

	//adds new lectuer to list 
	function addNewLecturer(id, name) {
		console.log("Adding new lectuer ");
		var liid = "lecid" + id;
		//dont add second time if already added 
		var element = document.getElementById('liid');
		if (element == null) {
			// not exists.
			var ul = document.getElementById("sublecturerslist");
			var li = document.createElement("li");
			li.innerHTML = '<label> '
					+ name
					+ ' </label> <button type="button" onclick="removeSubLectuer(\''
					+ liid + '\')">'
					+ '<i class="glyphicon-remove"></i></button>';
			li.setAttribute("id", liid); // added line
			ul.appendChild(li);
			
			var x = document.getElementById("tfaddlectuers");
			x.value = '';
			lectSuggestionsBlur();

		}

	}
	
	$(document).on('click', function(event) {
	  if (!$(event.target).closest('#searchsuggestionsid').length) {
		lectSuggestionsBlur();
	  }
	});

	function lectSuggestionsBlur() {
		//console.log(document.activeElement.parentElement.name);
		document.getElementById("suggestionsList").className = 'drp_results';
	}
</script>

</head>
<body>
	<!-- 
         <a id="logout" href="Logout" class="btn btn-info btn-lg"> <span
         	class="glyphicon glyphicon-log-out"> </span> Log out
         </a>
         -->
	<a id="logout" href="Logout" class="btn btn-info btn-lg"> <span
		class="glyphicon glyphicon-log-out"> </span> Log out
	</a>
	<h1>This is student page</h1>
	<form action="Student" method="post">
		<div id="startExam">
			<p class="title">გამოცდა</p>
			<table class="detail-car-table">
				<tbody>
					<tr>
						<th class="th-left">
							<div class="th-key">მასწავლებელი:</div>
							<div class="th-value"><%=exam.getCreatorName()%></div>
							<!--  <div class="th-value">ამირან მელია</div>  -->
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">დასახელება:</div>
							<div class="th-value">
								<input type="text" id="examName" name="examName"
									class="disabledfield lecturer" value="<%=exam.getName()%>"
									disabled>
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">გამოცდის ტიპი:</div>
							<div class="th-value">
								<input type="radio" name="examType" value="Final"
									class="disabledfield lecturer" checked="checked" disabled />Final 
								<input type="radio" name="examType" value="Midterm"
									class="disabledfield lecturer" disabled /> Midterm 
								<input	type="radio" name="examType" value="Quizz"
									class="disabledfield lecturer" disabled /> Quizz
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">ხანგრძლივობა:</div>
							<div class="th-value">

								<input type="text" id="fname" name="examDuration"
									class="disabledfield lecturer" value="<%=exam.getDuration()%>" disabled>
								წუთი.

							</div>
						</th>
					</tr>


					<tr>
						<th class="th-left">
							<div class="th-key">თარიღი:</div>
							<div class="th-value">
								<input type="date" id="myDate" name="examStartDate"
									class="disabledfield examboard" value="<%=exam.getExamStartDate()%>" disabled>
							</div>
						</th>
					</tr>

					<tr>
						<th class="th-left">
							<div class="th-key">საწყისი დრო:</div>

							<div class="th-value">


								<input type="text" id="fname" name="examStartTime"
									class="disabledfield examboard" value="<%=exam.getExamStartTime()%>" disabled>
							</div>
						</th>
					</tr>

					<tr>
						<th class="th-left">
							<div class="th-key">Open Book:</div>
							<div class="th-value">
								<input type="checkbox" name="openbookcb" id="openbookcb">
								<div id="opennodediv" class="hiddencb">
									Open Note: <input type="checkbox" name="openNote" id="openNote">
								</div>
							</div>
						</th>
					</tr>

					<tr>
						<th class="th-left">
							<div class="th-key">დამხმარე ლექტორები:</div>
							<div class="th-value">


								<!-- search for lecturers -->

								<div class="searchsuggestions" id="searchsuggestionsid">
									<section class="drp_main" id="drp_main">
									<div class="drp_search">
										<input type="text" id="tfaddlectuers" name="examName"
											oninput="getLectuerersSuggestions()"
											
											class="disabledfield lecturer" value=""
											placeholder="მასწავლებლის სახელი..." autocomplete="off"
											disabled>
										<ul class="drp_results" id="suggestionsList">
											<li><a href="index.html">Search Result #1<br /> <span>Description...</span></a></li>
										</ul>
									</div>
									</section>
								</div>
								<!-- end of suggestions-->


								<!-- already selected lecturers -->
								<div class="selectedlecturers">
									<ul class="sublecturerslist" id="sublecturerslist">



										<li id="lecid0"><label>გიორგი გონაშვილი</label>
											<button type="button" onclick="removeSubLectuer('lecid0')">
												<i class="glyphicon-remove"></i>
											</button></li>
										<li id="lecid1"><label>ანა რაქვიაშვილი</label>
											<button type="button" onclick="removeSubLectuer('lecid1')">
												<i class="glyphicon-remove"></i>
											</button></li>
										<li id="lecid2"><label>გოგა კორელი</label>
											<button type="button" onclick="removeSubLectuer('lecid2')">
												<i class="glyphicon-remove"></i>
											</button></li>
									</ul>
								</div>
								
								
								<script type='text/javascript'>
								alert("aeeee");
								<%
									
									/* adding sub lecturers  to exam */
									for (Lecturer curLec : exam.getSubLecturers()){
										out.append("addNewLecturer(" + curLec.getUserID() + "," +  (curLec.getFirstName() + " " + curLec.getLastName()) + ");\n\r");
									}
								%>
									
								</script>
								
								
								
								<!-- selected lecturers  -->
							</div>
						</th>
					</tr>



					<tr>
						<th class="th-left">
							<div class="th-key">გამოსაყენებელი მასალა:</div>
							<div class="th-value">
								<input type="file" name="file" />
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">ვარიანტები:</div>
							<div class="th-value">
								<input type="file" name="file" />
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">სტუდენტების სია:</div>
							<div class="th-value">
								<input type="file" name="file" />

							</div>
						</th>
					</tr>
				</tbody>
			</table>
			<input class="start" onclick="EnibleEditing()" type="button"
				value="გამოცდის დაწყება">
		</div>
	</form>
</body>
</html>
	