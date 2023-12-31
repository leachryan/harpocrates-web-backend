import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("maven-publish")
}

group = "dev.leachryan"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_19
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

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
