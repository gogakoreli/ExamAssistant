package send_mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.mysql.jdbc.UpdatableResultSet;

import data_managers.AccountManager;
import data_managers.ExamManager;
import helper.DBConnector;
import helper.OpResult;
import models.EAUser;
import models.Exam;
import models.Lecturer;
import models.Student;

public class SendMail {

	private static String USER_NAME = "ggona14@freeuni.edu.ge"; // GMail user
																// name (just
																// the part
																// before
																// "@gmail.com")
	private static String PASSWORD = "botigavafree"; // GMail password

	public static void main(String[] args) {
		String subject = "Java send mail example";
		String body = "Welcome to JavaMail!";

		ArrayList<EAUser> recipients = new ArrayList<EAUser>();
		AccountManager acm = new AccountManager();
		OpResult<EAUser> user = acm.getUserById(3);
		Student gona = (Student) user.getOpResult();
		recipients.add(gona);
		ExamManager exM = new ExamManager();
		Exam exam = exM.getExamForStudent(gona);

		sendMailFromE(recipients, subject, body, USER_NAME, PASSWORD, exam);
	}

	/**
	 * returns appropriate EAUser to session
	 * 
	 * Parameters: ArrayList<String> recipients: Email adresses of users, who
	 * receive mails String subject: message subject String mailtext: e_mail
	 * Text String userName: persons name who sends this emails String password:
	 * password of person who sends mails Exam exam: for which exam we send
	 * messages
	 */

	public static void sendMailFromE(ArrayList<EAUser> recipients, String subject, String mailtext, String userName,
			String password, Exam exam) {
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
				} else if (user instanceof Lecturer) {
					message.setSubject("მომავალი გამოცდა");
					Lecturer lecturer = (Lecturer) user;
					message.setText(emailMessageForLecturer(lecturer, exam));
				}

				Transport.send(message);
			}

			System.out.println("All Emails are send");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private static String emailMessageForStudent(Student student, Exam exam) {
		DBConnector connector = new DBConnector();
		ExamManager.updateStudentExamInformation(student, exam, connector);
		String msg = "ძვირფასო  " + student.getFirstName() + " " + student.getLastName() + "\n";
		msg += "გიგზავნით მომავალი გამოცდის სარეგისტრაციო ინფორმაციას:" + "\n";
		msg += "საგანი: " + exam.getName() + "\n";
		msg += "თარიღი: " + exam.getStartTime() + "\n";

		msg += "საათი: " + exam.getStartDateTime() + "\n";

		msg += "ადგილი: " + student.getExamInformation().getPlaceID() + "\n";
		msg += "ვარიანტი: " + student.getExamInformation().getVariant() + "\n";
		return msg;
	}

	private static String emailMessageForLecturer(Lecturer lecturer, Exam exam) {
		String msg = "ძვირფასო  " + lecturer.getFirstName() + " " + lecturer.getLastName() + " \n";

		msg += "გიგზავნით მომავალი გამოცდის  ინფორმაციას:" + "\n";
		msg += "საგანი: " + exam.getName() + "\n";
		msg += "თარიღი: " + exam.getStartTime() + "\n";
		msg += "საათი: " + exam.getStartDateTime() + "\n";

		return msg;
	}
}
