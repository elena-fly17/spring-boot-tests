package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// @SpringBootTest
class DemoApplicationTests {
	Calculator underTest = new Calculator();

	@Test
	void itShouldAddTwoNumbers() {
		// дано
		int numberOne = 20;
		int numberTwo = 30;
		// когда
		int result = underTest.add(numberOne, numberTwo);
		// в итоге
		int expected = 50;
		assertThat(result).isEqualTo(expected);
	}

	class Calculator {
		int add(int a, int b) {
			return a + b;
		}
	}
}
