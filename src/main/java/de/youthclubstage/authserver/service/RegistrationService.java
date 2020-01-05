package de.youthclubstage.authserver.service;

import de.youthclubstage.authserver.endpoint.model.RegistrationOrderDto;
import de.youthclubstage.authserver.entity.User;
import de.youthclubstage.authserver.repository.UserRepository;
import de.youthclubstage.authserver.service.model.GoogleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RegistrationService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final GoogleValidation googleValidation;
    private final String googleSecret;
    private final PasswordEncoder passwordEncoder;

    private final String template = "<html>\n" +
            "\n" +
            "<head>\n" +
            "    <link rel=\"stylesheet\" href=\"https://bootswatch.com/4/slate/bootstrap.min.css\" />\n" +
            "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Deeg-Soltions Registration</title>\n" +
            "\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"container\">\n" +
            "    <div class=\"card mx-auto\" style=\"max-width: 36rem; margin-top: 20px;\">\n" +
            "            <div class=\"card-body\">\n" +
            "                <h2>Willkommen bei {1}</h2>\n" +
            "                <br>\n" +
            "                <p>Um ihre Registrierung abzuschliessen, klicken Sie bitte <a href=\"{0}\">hier</a></p>\n" +
            "                <br>\n" +
            "                <br>\n" +
            "                Sollten Sie sich nie registriert haben, k&ouml;nnen Sie die Mail ignorieren. Oder schreiben uns an info@youthclubstage.de.\n" +
            "                <br>\n" +
            "                <br>\n" +
            "                <p>\n" +
            "                    Mit freundlichen Gr&uuml;&szlig;en<br>\n" +
            "                    {1}\n" +
            "                </p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "\n" +
            "</div>\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    @Autowired
    public  RegistrationService(JavaMailSender javaMailSender,
                                UserRepository userRepository,
                                GoogleValidation googleValidation,
                                @Value("${google.secret}") String googleSecret){
        this.googleValidation = googleValidation;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.googleSecret = googleSecret;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void registration(RegistrationOrderDto registration)  {
        log.error(registration.toString());
        GoogleResponse response = googleValidation.captchaValidation(googleSecret,registration.getCaptcha());
        if(!response.getSuccess())
        {
            log.error(response.toString());
            throw new RuntimeException();
        }


        Optional<User> user = userRepository.findByEmailAddress(registration.getEmail());
        if(user.isPresent()){
            throw new RuntimeException();
        }
        User newUser = null;
        try {
            newUser = new User(UUID.randomUUID(),
                    registration.getEmail(),
                    registration.getFirstName(),
                    registration.getLastName(),
                    passwordEncoder.encode(registration.getPassword()),
                    false,
                    "",
                    false,
                    generateKey(),
                    false,
                    Calendar.getInstance());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        userRepository.save(newUser);

        try {
            sendMail(newUser);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    private String generateKey() throws NoSuchAlgorithmException {
        return UUID.randomUUID().toString();
    }

    private void sendMail(User user) throws MessagingException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(user.getEmailAddress());
        helper.setFrom("test3@deeg-solutions.de");
        helper.setSubject("Registration abschließen");
        helper.setText(java.text.MessageFormat.format(template,
                "https://api.youthclubstage.de/auth/regconfirm?email="+user.getEmailAddress()+"&key="+user.getEmailKey(),
                "YouthLClubStage",
                "Das Team"),
                true);
        javaMailSender.send(mail);

        log.info("Send email '{}' to: {}", "Registration abschließen", user.getEmailAddress());
    }

    public boolean validationEmail(String email,String key){
        Optional<User> user = userRepository.findByEmailAddress(email);
        if(user.isPresent() && user.get().getEmailKey().equals(key)){
            user.get().setEnabled(true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
