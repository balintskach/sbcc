package hu.mik.java2.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


@Service("emailService")
@Scope(scopeName = "session")
public class EmailService {

	@Autowired
	
	private MailSender vehicleEmail;
	
	public void readyToSendEmail(String toAddress, String fromAddress, String subject, String message){
			SimpleMailMessage vehicleMsg = new SimpleMailMessage();
			vehicleMsg.setTo(toAddress);
			vehicleMsg.setFrom(fromAddress);
			vehicleMsg.setSubject(subject);
			vehicleMsg.setText(message);
			vehicleEmail.send(vehicleMsg);
	}
}
