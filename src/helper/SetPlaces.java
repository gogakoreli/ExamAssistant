package helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.sql.Timestamp;

import data_managers.ExamManager;
import helper.DBConnector.SqlQueryResult;
import models.Exam;
import models.Student;

public class SetPlaces {

	/**
	 * for exam with examID gets all students who write this exam and sets for
	 * them places and variants
	 * 
	 * @param examID
	 */
	public static void setPlacesForStudentsByExam(int examID) {
		ExamManager exm = new ExamManager();
		Exam exam = exm.getExamByExamId(examID);
		DBConnector connector = new DBConnector();
		String getFreePlaceIds = "select * from (select PlaceID, UserID, exam.ExamID, StartTime, Duration from  "
				+ "(select PlaceID, UserID, ExamID from userplace join userexam on userplace.UserExamID=userexam.UserExamID) "
				+ "as x join exam on x.ExamID=exam.ExamID) as y "
				+ "join place on y.PlaceID=place.PlaceID where IsWorking=true;";
		SqlQueryResult queryResult = connector.getQueryResult(getFreePlaceIds);
		Set<Place> freePlaces = getAllWorkingPlaces();
		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				while (res.next()) {
					int placeId = res.getInt("PlaceID");
					Timestamp startTime = res.getTimestamp("StartTime");
					int duration = res.getInt("Duration");
					if (checkTimesIntercection(startTime, duration, exam)) {
						freePlaces.remove(placeId);
					}
				}
			} catch (SQLException e) {
				connector.dispose();
				e.printStackTrace();
			}
		}
		connector.dispose();
		// iseti adgilebi agar unda iyos romlebic gverdigverdaa
		filterFreeSpaces(freePlaces);
		setPlacesForExam(exam, freePlaces, exm);
	}

	private static void filterFreeSpaces(Set<Place> freePlaces) {
		Iterator<Place> it = freePlaces.iterator();
		List<Place> arr = new ArrayList<Place>();
		while (it.hasNext()) {
			arr.add(it.next());
		}
		Collections.sort(arr);
		for (int i = 1; i < arr.size(); i++) {
			if (arr.get(i).placeNumber - arr.get(i - 1).placeNumber <= 1) {
				freePlaces.remove(arr.get(i));
				i++;
			}
		}
	}

	private static class UserPlace {
		int userExamId;
		int variant;
		int placeId;

		public UserPlace(int userExamId, int variant, int placeId) {
			this.userExamId = userExamId;
			this.variant = variant;
			this.placeId = placeId;
		}
	}

	private static void setPlacesForExam(Exam exam, Set<Place> freePlaces, ExamManager exm) {
		ArrayList<Student> students = exm.getAllStudentWhoWritesThisExam(exam);
		if (students.size() > freePlaces.size()) {
			System.out.println("You Can Not Make seats for This exam, because students are more than seats");
			return;
		}

		ArrayList<UserPlace> userPlaces = new ArrayList<UserPlace>();
		Iterator<Place> it = freePlaces.iterator();
		int variant = 1;

		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			int placeId = -1;
			if (it.hasNext()) {
				placeId = it.next().placeId;
			}
			variant = (variant + 1) % exam.getNumVariants() + 1;
			DBConnector connector = new DBConnector();
			String query = "select * from userexam where UserID =" + student.getUserID() + " and ExamID ="
					+ exam.getExamID() + ";";
			SqlQueryResult queryResult = connector.getQueryResult(query);
			if (queryResult.isSuccess()) {
				ResultSet res = queryResult.getResultSet();
				try {
					if (res.next()) {
						int userExamId = res.getInt("UserExamID");
						UserPlace userPlace = new UserPlace(userExamId, variant, placeId);
						userPlaces.add(userPlace);
					}
				} catch (SQLException e) {
					connector.dispose();
					e.printStackTrace();
				}
			}
			connector.dispose();
		}

		updateUserPlaceInDb(userPlaces);
	}

	private static void updateUserPlaceInDb(ArrayList<UserPlace> userPlaces) {
		DBConnector connector = new DBConnector();
		for (int i = 0; i < userPlaces.size(); i++) {
			UserPlace userPlace = userPlaces.get(i);
			String updateQuery = "insert into userplace (UserExamID, Variant, PlaceID) value (" + userPlace.userExamId
					+ " ," + userPlace.variant + " ," + userPlace.placeId + ");";
			connector.updateDatabase(updateQuery);
		}
		connector.dispose();
	}

	// TODO ak unda shemowmdes xoar emtxveva droshi
	private static boolean checkTimesIntercection(Timestamp startTime, int duration, Exam exam) {
		long t = startTime.getTime();
		long m = duration * 60 * 1000;
		Timestamp curExamEndTime = new Timestamp(t + m);
		return (startTime.after(exam.getEndDateTime()) || exam.getEndDateTime().after(curExamEndTime));
	}

	private static class Place implements Comparable<Place> {
		int placeNumber;
		int placeId;

		public Place(int placeId, int placeNumber) {
			this.placeNumber = placeNumber;
			this.placeId = placeId;
		}

		@Override
		public int compareTo(Place o) {
			return placeNumber - o.placeNumber;
		}
	}

	public static Set<Place> getAllWorkingPlaces() {
		Set<Place> places = new HashSet<Place>();

		DBConnector connector = new DBConnector();
		String getPlaceIds = "select * from place where IsWorking=true;";
		SqlQueryResult queryResult = connector.getQueryResult(getPlaceIds);
		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				while (res.next()) {
					int placeId = res.getInt("PlaceID");
					int placeNumber = res.getInt("Number");
					places.add(new Place(placeId, placeNumber));
				}
			} catch (SQLException e) {
				connector.dispose();
				e.printStackTrace();
			}
		}
		connector.dispose();
		return places;
	}

	public static void main(String[] args) {
		setPlacesForStudentsByExam(3);
	}

}
