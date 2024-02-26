package wistron.defectEdx.util.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.wistron.mail.sender.Mail;
import com.wistron.mail.sender.MailSender;
import com.wistron.mail.sender.MailType;

@Service
@Lazy
public class MailService {
	
	@Value("${wistron.mail-owner}")
	private String mailOwner;
		
		private static final String LOADER_NAME = "Defect EDX Loader";
		
		public void sendMail(MailType mailType, String mailTitle, String mailContext){
			Mail mail = creatMail(mailType);
			mail.setCustomSubject(mailTitle);
			mail.setMailContext(mailContext);
			try {
				MailSender.send(mail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void sendMail(MailType mailType, String mailTitle, Throwable throwable){
			Mail mail = creatMail(mailType);
			mail.setCustomSubject(mailTitle);
			mail.setThrowable(throwable);
			try {
				MailSender.send(mail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private Mail creatMail(MailType mailType) {
			return new Mail(LOADER_NAME, mailType, mailOwner);
		}
}
