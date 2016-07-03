package send_mail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.DBConnector;
import helper.DBConnector.SqlQueryResult;
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

public class SendMail {

	/**
	 * returns appropriate EAUser to session
	 * 
	 * Parameters: ArrayList<String> recipients: Email adresses of users, who
	 * receive mails String subject: message subject String mailtext: e_mail
	 * Text String userName: persons name who sends this emails String password:
	 * password of person who sends mails Exam exam: for which exam we send
	 * messages
	 */

	public static void sendMails(ArrayList<EAUser> recipients, String userName, String password, Exam exam) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		try {

			for (int i = 0; i < recipients.size(); i++) {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(userName));

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients.get(i).getMail()));

				EAUser user = recipients.get(i);
				if (user instanceof Student) {
					message.setSubject("საგამოცდო რეგისტრაცია");
					Student student = (Student) user;
					message.setText(emailMessageForStudent(student, exam));
					Transport.send(message);
				} else if (user instanceof Lecturer) {
					// TODO lektors davukomentare rom ar gaugzavnos jerjerobit
					/*
					 * message.setSubject("მომავალი გამოცდა"); Lecturer lecturer
					 * = (Lecturer) user;
					 * message.setText(emailMessageForLecturer(lecturer, exam));
					 * Transport.send(message);
					 */
				}

				// TODO Transport.send(message);
			}

			System.out.println("All Emails are send");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns email message text for student
	 */
	public static String emailMessageForStudent(Student student, Exam exam) {
		DBConnector connector = new DBConnector();
		ExamManager.updateStudentExamInformation(student, exam, connector);
		String msg = "ძვირფასო  " + student.getFirstName() + " " + student.getLastName() + "\n";
		msg += "გიგზავნით მომავალი გამოცდის სარეგისტრაციო ინფორმაციას:" + "\n";
		msg += "საგანი: " + exam.getName() + "\n";
		msg += "თარიღი: " + exam.getStartTime() + "\n";

		msg += "საათი: " + exam.getStartDateTime() + "\n";

		msg += "ადგილი: " + student.getExamInformation().getPlaceNumber() + "\n";
		msg += "ვარიანტი: " + student.getExamInformation().getVariant() + "\n";
		return msg;
	}

	/**
	 * returns email message text for student
	 */
	public static String emailMessageForLecturer(Lecturer lecturer, Exam exam) {
		String msg = "ძვირფასო  " + lecturer.getFirstName() + " " + lecturer.getLastName() + " \n";

		msg += "გიგზავნით მომავალი გამოცდის  ინფორმაციას:" + "\n";
		msg += "საგანი: " + exam.getName() + "\n";
		msg += "თარიღი: " + exam.getStartTime() + "\n";
		msg += "საათი: " + exam.getStartDateTime() + "\n";

		return msg;
	}

	/**
	 * This is static method which send all mails to students and lectures with
	 * corresponding exam
	 */
	public static void sendAllMailsForExam(int examId, String userName, String Password) {
		DBConnector connector = new DBConnector();
		String getUsers = "select * from userexam join user on userexam.UserID=user.UserID and ExamID=" + examId + ";";
		SqlQueryResult queryResult = connector.getQueryResult(getUsers);
		AccountManager acm = new AccountManager();
		ExamManager exm = new ExamManager();
		ArrayList<EAUser> users = new ArrayList<EAUser>();
		if (queryResult.isSuccess()) {
			ResultSet res = queryResult.getResultSet();
			try {
				while (res.next()) {
					int userId = res.getInt("UserID");
					EAUser user = acm.getUserById(userId).getOpResult();
					System.out.println(user);
					users.add(user);
				}
			} catch (SQLException e) {
				connector.dispose();
				e.printStackTrace();
			}
		}
		connector.dispose();
		Exam exam = exm.getExamByExamId(examId);
		sendMails(users, userName, Password, exam);
	}
}
