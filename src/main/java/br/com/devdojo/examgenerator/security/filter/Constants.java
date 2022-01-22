package br.com.devdojo.examgenerator.security.filter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Constants {
	public static final String SECRET = "SECRET";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 86400000L; // 1 day
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("devdojo"));
		System.out.println(LocalDateTime.now());
		System.out.println(new Date());
	}
}
