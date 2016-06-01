package io.jandy.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by syjsmk on 6/2/16.
 */

@Controller
@RequestMapping(value = "/api")
public class OpenApiController {

    @RequestMapping(value = "/requestAccessToken", method = RequestMethod.POST)
    @ResponseBody
    public String generateAccessToken(@RequestParam(value = "githubAccessToken") String githubAccessToken) {

        String jandyAccessToken = "";
        ResponseEntity<String> response = null;

        if(checkAccessToken()) {

            System.out.println("request_access_token");
            System.out.println("githubAccessToken : " + githubAccessToken);


            jandyAccessToken = generateJandyAccessToken(githubAccessToken);


        } else {
            jandyAccessToken = "Github access token is not valid";

        }


        return jandyAccessToken;
    }

    private String generateJandyAccessToken(String githubAccessToken) {
        return "yes";
    }

    // if access token is not oauth access token?
    private boolean checkAccessToken() {
        return true;
    }
}
