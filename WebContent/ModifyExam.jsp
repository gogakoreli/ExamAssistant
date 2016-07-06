<%@page import="models.ExamMaterial"%>
<%@page import="models.EAUser"%>
<%@page import="models.SecureExam"%>
<%@page import="models.Lecturer"%>
<%@page import="servlets.ModifyExamServlet"%>
<%@ page import="models.Exam"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
	Exam exam = (Exam) (request.getAttribute("exam"));
	EAUser user = (EAUser) (request.getAttribute("user"));
	if (exam == null)
		response.sendRedirect("ErrorPage.jsp");
	SecureExam sExam = new SecureExam(exam);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;" charset="UTF-8">
<title>Exam</title>
<title>Student Page</title>


<style>
#logout {
	margin-top: 5px;
	margin-right: 5px;
	position: absolute;
	top: 0;
	right: 0;
}

.dispInline {
	display: inline-flex;
}

.disabledfieldwidth {
	width: 200px;
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
	
	<%if (user instanceof Lecturer)
				out.print("var userType ='lecturer';");
			else
				out.print("var userType ='examboard';");%>

	<%out.print("var examstatus = '" + sExam.getExamStatus() + "_class';");%>

	function submitRequested() {
		if (examstatus === 'new_class')
			return newExamSubmitRequested();
		else
			return finalSubmitRequested();

	}

	/* validates user can change status from new */
	function newExamSubmitRequested() {
		if (!$('#examName').val()) {
			alert("Cant submit exam without name!");
			return false;
		}

		if (!$('#examDuration').val()) {
			alert("Cant submit exam without duration!");
			return false;
		}

		//alert();
		return confirm("After submitting You won't be able to change exams name and duration.\n "
				+ "Board will be able to see your exam and set start time for it. \n Are you sure you want to continue? ");
	}

	/* validates user can change status from new */
	function finalSubmitRequested() {
		if (userType === 'lecturer') {
			if (!validateLecturer())
				return false;
		} else {
			if (!validateBoard())
				return false;
		}

		return confirm("After submitting You won't be able to make any changes in exam\n "
				+ "if you still want to continue changin exam info choose save instead. \n Are you sure you want to continue? ");
	}

	function validateLecturer() {
		if (!$('#examName').val()) {
			//alert("Cant submit exam without name!");
			//return false;
		}

		if (!$('#examDuration').val()) {
			//alert("Cant submit exam without duration!");
			//return false;
		}
	}

	function validateBoard() {
		if (!$('#examStartDate').val()) {
			alert("Cant submit exam without start date!");
			return false;
		}

		if (!$('#examStartTime').val()) {
			alert("Cant submit exam without start time!");
			return false;
		}
	}
</script>

<script type="text/javascript">
	//show hide open note option 
	$(document).ready(function() {
		$('#openbookcb').click(function() {
			$("#opennodediv").toggle();
		});
	});
</script>

<script type="text/javascript">
	$("#file").change(function() {
		//file uploaded 
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

			if (hasClass(x[i], userType) && hasClass(x[i], examstatus)) {
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

		$.each(data, function(i, item) {
			var name = item.firstName + ' ' + item.lastName;
			items.push('<li> <a onclick="addNewLecturer(' + item.userID + ',\''
					+ name + '\'); return false;" name="sugglink">' + item.mail
					+ '<br /> <span>' + name + '</span></a></li>');
		}); // close each()

		$('#suggestionsList').empty();
		$('#suggestionsList').append(items.join(''));

	}

	//removes @id from list 
	function removeSubLectuer(id) {
		var list = document.getElementById("sublecturerslist");
		list.removeChild(document.getElementById(id));
	}
	
	//removes @id from list 
	function removeFromList(id, listid) {
		var list = document.getElementById(listid);
		list.removeChild(document.getElementById(id));
	}
	
	function addNewMaterial(id, materialname, materialLocation, listid, paramname){
		console.log("Adding new Material to list "  + listid + " material= " + materialname);
		var liid = "material" + id;
		var ul = document.getElementById(listid);
		var li = document.createElement("li");
		li.innerHTML = '<label> '
				+ '<a href=\'' +  materialLocation +'\'>' + materialname + '</a>'
				+ ' </label> <input type="hidden" name="' + paramname + '[]" value="' + id
				+'"> <button type="button" onclick="removeFromList(\''
				+ liid + '\', \''+listid+'\')">'
				+ '<i class="glyphicon-remove"></i></button>';
		li.setAttribute("id", liid); // added line
		ul.appendChild(li);
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
					+ ' </label> <input type="hidden" name="sublec[]" value="' + id
					+'"> <button type="button" onclick="removeSubLectuer(\''
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

<style>
.colored_purple {
	background-color: #af4c9f;
	border: none;
	color: white;
	padding: 18px 40px;
	text-decoration: none;
	margin: 8px 2px;
	cursor: pointer;
}

.bnt_edit_css {
	cursor: pointer;
	background-color: #495884;
	border: none;
	color: white;
	text-decoration: none;
	margin: 8px 2px;
	margin-top: 8px;
	margin-right: 2px;
	margin-bottom: 8px;
	margin-left: 2px;
	width: 60px;
	height: 40px;
}
</style>

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

	<form action="ModifyExam" method="post">

		<input type="hidden" name="<%=ModifyExamServlet.EXAM_ID_PARAM_NAME%>"
			value="<%=sExam.getExamID()%>">

		<div id="startExam">
			<div class="dispInline">
				<p class="title">გამოცდა</p>
				<input onclick="EnibleEditing()" type="button" class="bnt_edit_css"
					value="Edit">
			</div>
			<table class="detail-car-table">
				<tbody>
					<tr>
						<th class="th-left">
							<div class="th-key">მასწავლებელი:</div>
							<div class="th-value">
								<%
									if (sExam.isExamNew())
										out.print(user.getFirstName() + " " + user.getLastName());
									else
										out.print(sExam.getCreatorName());
								%>
							</div> <!--  <div class="th-value">ამირან მელია</div>  -->
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">დასახელება:</div>
							<div class="th-value">
								<input type="text" id="examName" name="examName"
									class="disabledfield lecturer disabledfieldwidth new_class"
									value="<%=sExam.getName()%>" disabled>
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">გამოცდის ტიპი:</div>
							<div class="th-value">
								<input type="radio" name="examType" value="Final"
									class="disabledfield lecturer new_class pending_class board_ready_class"
									<%if (sExam.getType().equals(Exam.ExamType.FINAL))
				out.print("checked='checked'");%>
									disabled />Final <input type="radio" name="examType"
									value="Midterm"
									class="disabledfield lecturer new_class pending_class board_ready_class"
									<%if (sExam.getType().equals(Exam.ExamType.MIDTERM))
				out.print("checked='checked'");%>
									disabled /> Midterm <input type="radio" name="examType"
									value="Quizz"
									class="disabledfield lecturer new_class pending_class board_ready_class"
									<%if (sExam.getType().equals(Exam.ExamType.QUIZZ))
				out.print("checked='checked'");%>
									disabled /> Quizz
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">ხანგრძლივობა:</div>
							<div class="th-value">

								<input type="text" id="examDuration" name="examDuration"
									class="disabledfield lecturer disabledfieldwidth new_class"
									value="<%=sExam.getDuration()%>" disabled> წუთი.

							</div>
						</th>
					</tr>


					<tr>
						<th class="th-left">
							<div class="th-key">თარიღი:</div>
							<div class="th-value">
								<input type="date" id="examStartDate" name="examStartDate"
									class="disabledfield examboard disabledfieldwidth pending_class lecturer_ready_class"
									value="<%=sExam.getExamStartDate()%>" disabled>
							</div>
						</th>
					</tr>

					<tr>
						<th class="th-left">
							<div class="th-key">საწყისი დრო:</div>

							<div class="th-value">


								<input type="text" id="examStartTime" name="examStartTime"
									class="disabledfield examboard disabledfieldwidth pending_class lecturer_ready_class"
									value="<%=sExam.getStartDateTime()%>" disabled>
							</div>
						</th>
					</tr>

					<tr>
						<th class="th-left">
							<div class="th-key">Open Book:</div>
							<div class="th-value">
								<input type="checkbox" name="openbookcb" id="openbookcb"
									class="disabledfield lecturer new_class pending_class board_ready_class"
									value="<%=Exam.NoteType.OPEN_BOOK%>" disabled>
								<div id="opennodediv" class="hiddencb">
									Open Note: <input type="checkbox" name="openNote" id="openNote"
										value="<%=Exam.NoteType.OPEN_NOTE%>"
										class="disabledfield lecturer new_class pending_class board_ready_class"
										disabled>
								</div>
							</div>
						</th>

						<!-- values of open book and open note -->
						<script>
							
						<%out.print("var noteType='" + sExam.getResourceType() + "';");%>
							
							function correctNoteTypecb() {
								if (noteType === '<%=Exam.NoteType.OPEN_BOOK%>') {
									$('#openbookcb').prop('checked', true);
									$("#opennodediv").toggle();
								} else if (noteType === '<%=Exam.NoteType.OPEN_NOTE%>') {
									$('#openbookcb').prop('checked', true);
									$('#openNote').prop('checked', true);
									$("#opennodediv").toggle();
								}

							}
							correctNoteTypecb();
						</script>
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
											class="disabledfield lecturer disabledfieldwidth new_class pending_class board_ready_class"
											value="" placeholder="მასწავლებლის სახელი..."
											autocomplete="off" disabled>
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



										<!-- <li id="lecid0"><label>გიორგი გონაშვილი</label>
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
											</button></li> -->
									</ul>
								</div>


								<script type='text/javascript'>
									
								<%/* adding sub lecturers to exam */
			for (Lecturer curLec : sExam.getSubLecturers()) {

				out.append("addNewLecturer(" + curLec.getUserID() + ",'"
						+ (curLec.getFirstName() + " " + curLec.getLastName()) + "');\n\r");
			}%>
									
								</script>



								<!-- selected lecturers  -->
							</div>
						</th>
					</tr>



					<tr>
						<th class="th-left">
							<div class="th-key">გამოსაყენებელი მასალა:</div>
							<div class="th-value">
								<input type="file" name="materialsmult[]"
									class="disabledfield lecturer disabledfieldwidth new_class pending_class board_ready_class"
									disabled />

								<div class="selectedlecturers">
									<!-- list of uploaded materials  -->
									<ul class="sublecturerslist customlist" id="uploadedmaterialslist">
									</ul>
								</div>



								<script type='text/javascript'>
									
								<%/* adding materials to exam */
			for (ExamMaterial curmaterial : sExam.getExamMaterialsList()) {

				out.append("addNewMaterial(" + curmaterial.getMaterialID() + ",'" + curmaterial.getMaterial() + "','"
						+ curmaterial.getLocation() + "', 'uploadedmaterialslist', 'materialslist');\n\r");
			}%>
									
								</script>

							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">ვარიანტები:</div>
							<div class="th-value">
								<input type="file" name="variantssmult[]"
									class="disabledfield lecturer disabledfieldwidth new_class pending_class board_ready_class"
									disabled />


								<div class="selectedlecturers">
									<!-- list of uploaded materials  -->
									<ul class="sublecturerslist customlist" id="uploadedvariantslist">
									</ul>
								</div>



								<script type='text/javascript'>
									
								<%/* adding materials to exam */
			for (ExamMaterial curmaterial : sExam.getExamVariantsList()) {

				out.append("addNewMaterial(" + curmaterial.getMaterialID() + ",'" + curmaterial.getMaterial() + "','"
						+ curmaterial.getLocation() + "', 'uploadedvariantslist', 'variantsslist');\n\r");
			}%>
									
								</script>
							</div>
						</th>
					</tr>
					<tr>
						<th class="th-left">
							<div class="th-key">სტუდენტების სია:</div>
							<div class="th-value">
								<input type="file" name="studentsList[]"
									class="disabledfield examboard disabledfieldwidth pending_class lecturer_ready_class"
									disabled />
									
									<div class="selectedlecturers">
									<!-- list of uploaded materials  -->
									<ul class="sublecturerslist customlist" id="uploadedstudentslist">
									</ul>
								</div>



								<script type='text/javascript'>
									
								<%/* adding materials to exam */
			for (ExamMaterial curmaterial : sExam.getExamStudentsList()) {

				out.append("addNewMaterial(" + curmaterial.getMaterialID() + ",'" + curmaterial.getMaterial() + "','"
						+ curmaterial.getLocation() + "', 'uploadedstudentslist', 'studentslist');\n\r");
			}%>
									
								</script>

							</div>
						</th>
					</tr>
				</tbody>
			</table>
			<div id="submitButtons" >
				<input class="start" type="submit" value="Save " name="saveButton">
				<input class="colored_purple" type="submit"
					name="saveAndSubmitButton" onclick="return submitRequested()"
					value="Submit">
			</div>
		</div>
	</form>
</body>
</html>
