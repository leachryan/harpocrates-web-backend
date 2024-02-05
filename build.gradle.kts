import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("maven-publish")
}

group = "dev.leachryan"
version = "0.1.0"

java {
	sourceCompatibility = JavaVersion.VERSION_19
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Springdoc
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")

	// Jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Bcrypt
	implementation("at.favre.lib:bcrypt:0.10.2")

	// Test containers
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("com.redis:testcontainers-redis:2.0.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Use the Kotlin JUnit 5 integration.
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

	// Use the JUnit 5 integration.
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")

	// AssertK
	testImplementation("com.willowtreeapps.assertk:assertk:0.28.0")

	// Mockk
	testImplementation("io.mockk:mockk:1.13.8")

	// REST-assured
	testImplementation("io.rest-assured:rest-assured:5.4.0")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:1.19.3")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "19"
	}
}

tasks.named<Test>("test") {
	// Use JUnit Platform for unit tests.
	useJUnitPlatform()
}
