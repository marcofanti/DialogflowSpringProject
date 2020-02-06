package com.behaviosec.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class OTPModel {
	@Size(min = 6, max = 6, message = "OTP value must be 6 digits")
	String otpValue;
	@Email
	String email;
	String other;
	String type;
}
