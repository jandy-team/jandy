package io.jandy.web.api;

import io.jandy.domain.AccessToken;
import io.jandy.domain.AccessTokenRepository;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * Created by syjsmk on 6/2/16.
 */

// https://github.com/spring-projects/spring-security-oauth/tree/master/samples/oauth2

@Controller
@RequestMapping(value = "/oauth")
public class OpenApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    public OAuth2AccessToken generateJandyAccessToken(@RequestParam(value = "AccessToken") String accessToken) {

        logger.debug("AccessToken : " + accessToken);

        OAuth2AccessToken jandyToken = null;

        jandyToken = generateJandyToken(accessToken);

        accessTokenRepository.save(new AccessToken(jandyToken.getValue(), accessToken));

        logger.debug("saved accessToken : " + accessTokenRepository.findOne(jandyToken.getValue()).getAccessToken());

        return jandyToken;
    }

    private OAuth2AccessToken generateJandyToken(String accessToken) {

        Random randomGenerator = new Random();

        String jandyToken = null;

        String key = String.valueOf(randomGenerator.nextInt());
        logger.debug("key : " + key);

        jandyToken = encrypt(accessToken, key);

        logger.debug("jandyToken : " + jandyToken);

//            accessToken = decrypt(jandyToken, key);
//
//            logger.debug("decrypted access token : " + accessToken);

        return new DefaultOAuth2AccessToken(jandyToken);
    }

    private String decrypt(String jandyToken, String key) {

        BasicTextEncryptor decryptor = new BasicTextEncryptor();
        decryptor.setPassword(key);

        return decryptor.decrypt(jandyToken);

    }

    private String encrypt(String accessToken, String key) {

        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(key);

        return encryptor.encrypt(accessToken);

    }

}
