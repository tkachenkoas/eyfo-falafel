package com.atstudio.eyfofalafel.backend


import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@Import([DataSourceAutoConfiguration, HibernateJpaAutoConfiguration])
@PropertySource("application.properties")
class TestDataSourceAutoConfiguration {

}