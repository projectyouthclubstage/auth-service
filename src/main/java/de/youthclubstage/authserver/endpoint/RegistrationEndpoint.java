package de.youthclubstage.authserver.endpoint;

import de.youthclubstage.authserver.endpoint.model.RegistrationOrderDto;
import de.youthclubstage.authserver.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
public class RegistrationEndpoint {

    private final RegistrationService registrationService;

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
            "               {0}"+
            "            </div>\n" +
            "        </div>\n" +
            "\n" +
            "</div>\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    @Autowired
    public RegistrationEndpoint(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @PostMapping( value = "registration", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Void> hasUser2Fa(@Valid @RequestBody RegistrationOrderDto registration){
        registrationService.registration(registration);
        return ResponseEntity.ok().build();
    }

    @GetMapping( value = "regconfirm")
    String validationEmail(@RequestParam("email") String email,@RequestParam("key") String key){
        if(registrationService.validationEmail(email,key)){
            return java.text.MessageFormat.format(template,"<p>Account wurde erfolgreich aktiviert</p>");
        }else{
            return java.text.MessageFormat.format(template,"<p style=\"color:red;\">Es ist ein Fehler aufgetreten!</p>");
        }

    }

}
