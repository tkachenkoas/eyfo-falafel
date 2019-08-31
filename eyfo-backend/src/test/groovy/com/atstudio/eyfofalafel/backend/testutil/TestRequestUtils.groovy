package com.atstudio.eyfofalafel.backend.testutil

import com.jayway.restassured.path.json.JsonPath
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.builder.ResponseSpecBuilder
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.specification.RequestSpecification
import com.jayway.restassured.specification.ResponseSpecification
import org.springframework.web.multipart.MultipartFile

import static com.jayway.restassured.RestAssured.given

class TestRequestUtils {

    private static String serverUrl;

    static String getServerUrl() {
        if (serverUrl == null) {
            Properties props = new Properties()
            InputStream is = TestRequestUtils.getClassLoader().getResourceAsStream("application.properties")
            try {
                props.load(is)
            } finally {
                is.close()
            }
            serverUrl = "http://localhost:${props.getProperty("tomcat.test.port")}"
        }
        return serverUrl
    }

    static String getUrlWithHost(CharSequence url) {
        return "${getServerUrl()}/${url}".toString()
    }

    static ResponseSpecification success() {
        return ResponseSpecBuilder.newInstance()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build()
    }

    static RequestSpecification reqSpec() {
        return RequestSpecBuilder.newInstance()
                .setContentType(ContentType.JSON)
                .build()
    }

    static <T> T multipart(String url, MultipartFile file, Class<T> extractClass) {
        return  given().contentType('multipart/form-data')
                .multiPart("file", file.getOriginalFilename(), file.getBytes())
                .post(getUrlWithHost(url)).then().spec(success())
                .extract().as(extractClass)
    }

    static byte[] getFileContent(String fullPath) {
        return given()
                .get(getUrlWithHost(fullPath))
                .then().extract().asByteArray()
    }

    static JsonPath rawGet(CharSequence url, Map<String, Object> params = [:]) {
        return given().spec(reqSpec())
                .params(params)
                .get(getUrlWithHost(url))
                .then().spec(success())
                .extract().body().jsonPath()
    }

    static <T> T performGet(CharSequence url, Map<String, Object> params = [:], Class<T> extractClass) {
        return given().spec(reqSpec())
                .params(params)
                .post(getUrlWithHost(url))
                .then().spec(success())
                .extract().as(extractClass)
    }

    static void performDelete(CharSequence url) {
        given()
                .delete(getUrlWithHost(url))
                .then().statusCode(200)
    }

    static <T> T performPost(CharSequence url, Object body, Class<T> extractClass) {
        return given().spec(reqSpec())
                            .body(body)
                            .post(getUrlWithHost(url))
                            .then().spec(success())
                            .extract().as(extractClass)
    }

}
