/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 *
 * @author Adrian
 */
public class EmailSender {
    
// Server mail user & pass
        private String userPath = ResourceBundle.getBundle("serverapplication.utilities.KeysProperties").getString("user_email");
        private String emailPath = ResourceBundle.getBundle("serverapplication.utilities.KeysProperties").getString("email_email");
        private String passPath = ResourceBundle.getBundle("serverapplication.utilities.KeysProperties").getString("pass_email");
	private String user = "";
	private String pass = "";

	// DNS Host + SMTP Port
	private String smtp_host = "smtp.gmail.com";
	private int smtp_port = 25;
        
        //Email sender paramethers
        private String emailSender="";

	/**
	 * Sends the given <b>text</b> from the <b>sender</b> to the <b>receiver</b>. In
	 * any case, both the <b>sender</b> and <b>receiver</b> must exist and be valid
	 * mail addresses. <br/>
	 * <br/>
	 * 
	 * Note the <b>user</b> and <b>pass</b> for the authentication is provided in
	 * the class constructor. Ideally, the <b>sender</b> and the <b>user</b>
	 * coincide.

	 * @param receiver The mail's TO part
	 * @param subject  The mail's SUBJECT
	 * @param text     The proper MESSAGE
	 * @throws MessagingException Is something awry happens
	 * 
	 */
	public void sendMail( String receiver, String subject, String text) throws MessagingException, IOException, Exception {
		getCredentials();
		// Mail properties
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", smtp_host);
		properties.put("mail.smtp.port", smtp_port);
		properties.put("mail.smtp.ssl.trust", smtp_host);
		properties.put("mail.imap.partialfetch", false);

		// Authenticator knows how to obtain authentication for a network connection.
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});

		// MIME message to be sent
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailSender)); // Ej: emisor@gmail.com
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver)); // Ej: receptor@gmail.com
		message.setSubject(subject); // Asunto del mensaje

		// A mail can have several parts
		Multipart multipart = new MimeMultipart();

		// A message part (the message, but can be also a File, etc...)
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(text, "text/html");
		multipart.addBodyPart(mimeBodyPart);

		// Adding up the parts to the MIME message
		message.setContent(multipart);

		// And here it goes...
		Transport.send(message);
	}

    private void getCredentials() throws IOException, Exception {
        InputStream in = null;
        byte[] variableBytes = null;
        in = EncriptationAsymmetric.class.getClassLoader().getResourceAsStream(userPath);
        variableBytes = new byte[in.available()];
        in.read(variableBytes);
        in.close();
        user = EncriptationAsymmetric.decrypt(EncriptationAsymmetric.toHexadecimal(variableBytes));
        
        in = EncriptationAsymmetric.class.getClassLoader().getResourceAsStream(emailPath);
        variableBytes = new byte[in.available()];
        in.read(variableBytes);
        in.close();
        emailSender = EncriptationAsymmetric.decrypt(EncriptationAsymmetric.toHexadecimal(variableBytes));
        
        in = EncriptationAsymmetric.class.getClassLoader().getResourceAsStream(passPath);
        variableBytes = new byte[in.available()];
        in.read(variableBytes);
        in.close();
        pass = EncriptationAsymmetric.decrypt(EncriptationAsymmetric.toHexadecimal(variableBytes));
        
        
    }

}
