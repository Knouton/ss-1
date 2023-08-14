package com.poluhin.ss.demo.sample;

import com.poluhin.ss.demo.domain.model.RegistrationUser;

public class RegistrationUserSample {

	public static final RegistrationUser registrationUser_sample1 = new RegistrationUser("test_user",
	                                                                 "test_user1",
	                                                                 "test_user1");

	public static final RegistrationUser registrationUser_sampleErr1 = new RegistrationUser("test_user",
	                                                                    "test_user1",
	                                                                    "test_user");
}
