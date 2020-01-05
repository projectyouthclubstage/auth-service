package de.youthclubstage.authserver.service;

import de.youthclubstage.authserver.service.model.GoogleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "GoogleValidation",url = "https://www.google.com/recaptcha/api/")
public interface GoogleValidation {

    @RequestMapping(method = RequestMethod.GET, value = "/siteverify")
    GoogleResponse captchaValidation(@RequestParam(value = "secret") String secret, @RequestParam(value = "response") String response);

}
