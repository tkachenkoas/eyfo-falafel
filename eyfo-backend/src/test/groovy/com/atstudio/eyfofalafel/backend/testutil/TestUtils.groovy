package com.atstudio.eyfofalafel.backend.testutil


import com.jayway.restassured.RestAssured
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.builder.ResponseSpecBuilder
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.specification.RequestSpecification
import com.jayway.restassured.specification.ResponseSpecification
import org.springframework.web.multipart.MultipartFile

class TestUtils {

    private static String serverUrl;

    static String getServerUrl() {
        if (serverUrl == null) {
            Properties props = new Properties()
            InputStream is = TestUtils.getClassLoader().getResourceAsStream("application.properties")
            try {
                props.load(is)
            } finally {
                is.close()
            }
            serverUrl = "http://localhost:${props.getProperty("tomcat.test.port")}"
        }
        return serverUrl
    }

    static String getUrlWithHost(String url) {
        return "${getServerUrl()}/${url}"
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
        return  RestAssured.given().contentType('multipart/form-data')
                .multiPart("file", file.getOriginalFilename(), file.getBytes())
                .post(url).then().spec(success())
                .extract().as(extractClass)
    }

    static byte[] getFileContent(String fullPath) {
        return RestAssured.given()
                .get(getUrlWithHost(fullPath))
                .then().extract().asByteArray()
    }

    static <T> T performGet(CharSequence url, Class<T> extractClass) {
        return RestAssured.given()
                            .get(url.toString())
                            .then().spec(success())
                            .extract().as(extractClass)
    }

    static <T> T performDelete(CharSequence url) {
        return RestAssured.given()
                .delete(url.toString())
                .then().statusCode(200)
    }

    static <T> T performPost(CharSequence url, Object body, Class<T> extractClass) {
        return RestAssured.given().spec(reqSpec())
                            .body(body)
                            .post(url.toString())
                            .then().spec(success())
                            .extract().as(extractClass)
    }

}
