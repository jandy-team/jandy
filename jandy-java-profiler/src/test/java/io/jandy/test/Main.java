package io.jandy.test;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class Main {
  public static void main(String[] args) {
    String password = args.length >= 1 ? args[0] : "strong";
    String text = args.length >= 2 ? args[1] : "i am hero";

    PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    String encryptedPassword = passwordEncryptor.encryptPassword(password);

    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    textEncryptor.setPassword(encryptedPassword);
    String myEncryptedText = textEncryptor.encrypt(text);

    System.out.println(myEncryptedText);
  }
}
