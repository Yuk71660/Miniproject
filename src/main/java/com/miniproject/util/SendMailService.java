package com.miniproject.util;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailService {
	private String mailTo;
	private String authCode;

	public SendMailService(String mailTo, String authCode) {
		super();
		this.mailTo = mailTo;
		this.authCode = authCode;
	}

	public void send() throws Exception {
		String subject = "miniproject.com에서 보내는 회원 가입 이메일 인증번호입니다";
		String message = "회원가입을 환영합니다... 인증번호 : " + this.authCode + "를 입력하시고, 인증 버튼을 눌러 회원 가입을 완료하세요!";

		// SMTP(Send Mail Transfer Protocol) 설정을 위해
		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.naver.com"); // smtp 호스트 주소 등록
		props.put("mail.smtp.port", "465"); // naver smtp의 포트번호
		props.put("mail.smtp.starttls.required", "true"); // 동기식 전송을 위해 설정
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ssl.enable", "true"); // SSL 사용
		props.put("mail.smtp.auth", "true"); // 인증 과정을 거치겠다.

		// SMTP서버에 인증
		// 메일주소, 비밀번호를 properties 파일에서 얻어오도록...

		String mailId = PropertiesTask.getPropertiesValue("mail.properties", "mailID");
		String mailPwd = PropertiesTask.getPropertiesValue("mail.properties", "mailPwd");

//      System.out.println(mailId + ", " + mailPwd);
		
		// 메일 서버에 로그인 할 수 있는 세션을 얻어옴
		Session mailServerSession = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(mailId, mailPwd);
			}
		});
		
		if (mailServerSession != null) {
			MimeMessage mime = new MimeMessage(mailServerSession);
			
			mime.setFrom(new InternetAddress("dbrrmsdn51@naver.com")); // 
			mime.addRecipient(RecipientType.TO, new InternetAddress(mailTo)); // 
			
			mime.setSubject(subject, "utf-8");
			mime.setText(message, "utf-8");
			
			Transport trans = mailServerSession.getTransport("smtp");
			trans.connect(mailId, mailPwd);
			trans.send(mime);
			trans.close();
		}

	}

}
