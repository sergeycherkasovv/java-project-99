import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
	checkstyle
	jacoco
	id("io.freefair.lombok") version "8.13.1"
	id("org.sonarqube") version "6.2.0.5505"
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.sentry.jvm.gradle") version "5.8.0"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
    mainClass = "hexlet.code.AppApplication"
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	// DataBase
	runtimeOnly("com.h2database:h2")

	// Utilities
	implementation("net.datafaker:datafaker:2.3.0")
	implementation("org.instancio:instancio-junit:5.0.2")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	// MapStruct
	implementation("org.mapstruct:mapstruct:1.6.2")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.1")

	// OpenAPI(Swagger)
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

	// Tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation(platform("org.junit:junit-bom:5.11.2"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.11.1")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = setOf(
			TestLogEvent.FAILED,
			TestLogEvent.PASSED,
			TestLogEvent.SKIPPED
		)
		showStandardStreams = true
	}
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

sonar {
	properties {
		property("sonar.projectKey", "sergeycherkasovv_java-project-99")
		property("sonar.organization", "sergeycherkasovv")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

sentry {
  // Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
  // This enables source context, allowing you to see your source
  // code as part of your stack traces in Sentry.
  includeSourceContext = true

  org = "hexlet-rf"
  projectName = "task-manager"
  authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.sentryBundleSourcesJava {
    enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}

configurations {
	all {
		resolutionStrategy {
			disableDependencyVerification() // Sensitive: dependency verification is disabled
		}
	}
}
