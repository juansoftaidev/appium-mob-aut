package com.autjuan.factories;

import java.util.function.Supplier;

import com.github.javafaker.Faker;

/**
 * 
 * Refactored FakerFactory by [@Juansito] old sch - to New school -Java Lmda [Suppliers and predicatrs]
 * 
 * Use of Functional Interface  [Supplier<String> supplier]
 * []
 * 
 */
public class FakeDataGenerator {
	private static Faker faker;
	private static FakeDataGenerator instance;

	private FakeDataGenerator() {
		faker = new Faker();
	}

	public static FakeDataGenerator getInstance() {
		if (instance == null) {
			instance = new FakeDataGenerator();
		}
		return instance;
	}

	private String generate(Supplier<String> supplier) {
		return supplier.get();
	}

	public String getEmail() {
		return generate(faker.internet()::emailAddress);
	}

	public String getPassword() {
		return generate(faker.internet()::password);
	}

	public String getFirstName() {
		return generate(faker.name()::firstName);
	}

	public String getLastName() {
		return generate(faker.name()::lastName);
	}

	public String getFullName() {
		return generate(faker.name()::fullName);
	}

	public String getAddress() {
		return generate(faker.address()::fullAddress);
	}

	public String getStreet() {
		return generate(faker.address()::streetAddress);
	}

	public String getCity() {
		return generate(faker.address()::cityName);
	}

	public String getPostCode() {
		return generate(faker.address()::zipCode);
	}

	public String getCountry() {
		return generate(faker.address()::country);
	}

	public String getPhone() {
		return generate(faker.phoneNumber()::phoneNumber);
	}
}

