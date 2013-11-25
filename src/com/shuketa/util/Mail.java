package com.shuketa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import com.shuketa.db.DbTest;
import com.shuketa.db.DbTestErrorOperator;
import com.sun.mail.smtp.*;

//http://www58.atwiki.jp/chapati4it/m/pages/124.html
//http://d.hatena.ne.jp/maji-KY/20111001/1317483898
public class Mail<T> {
	
	protected static String mTodayDate = null;
	protected static String mErrorDetail = null;
	
	protected static StringBuilder mMailBody = new StringBuilder();
	
    public static void testTest(){
        // Recipient's email ID needs to be mentioned.
        String to = "shubiz.sakai@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "ryuzaki.biz@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
           // Create a default MimeMessage object.
           MimeMessage message = new MimeMessage(session);

           // Set From: header field of the header.
           message.setFrom(new InternetAddress(from));

           // Set To: header field of the header.
           message.addRecipient(Message.RecipientType.TO,
                                    new InternetAddress(to));

           // Set Subject: header field
           message.setSubject("This is the Subject Line!");

           // Now set the actual message
           message.setText("This is actual message");

           // Send message
           Transport.send(message);
           System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
           mex.printStackTrace();
        }
    }
    

	
	// []
	String createMailBody(DbTestErrorOperator errorOperator) {
		try {

			mMailBody.setLength(0);
			
			if(mTodayDate==null){
				Date d = new Date();
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				System.out.println(df.format(d));
				mTodayDate = df.format(d);
			}
			  
			String path = Config.getCurrentDir() + Config.mailTemplateFile();
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str;
			while ((str = br.readLine()) != null) {
				System.out.println(str);
				if (str.indexOf("[日付]") != -1) {
					String replacedStr = str.replaceAll("[日付]", mTodayDate);
					mMailBody.append(replacedStr);
				} else if (str.indexOf("[事象詳細]") != -1) {
					String replacedStr = str.replaceAll("[事象詳細]", errorOperator.getErrorDetail());
					mMailBody.append(replacedStr);
				} else if (str.indexOf("[会社名]") != -1) {
					String replacedStr = str.replaceAll("[会社名]", errorOperator.getCompanyName());
					mMailBody.append(replacedStr);
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return mMailBody.toString();
	}
}

