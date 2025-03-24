package com.order_service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

	@LocalServerPort
	int port;

	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
		stubFor(WireMock.get(urlMatching("/api/products/" + code)).willReturn(
				aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withStatus(200).withBody("""
						    {
						        "code": "%s",
						        "name": "%s",
						        "price": %f
						    }
						""".formatted(code, name, price.doubleValue()))));
	}
}
