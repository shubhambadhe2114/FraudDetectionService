plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "com.banking.finance"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka") // Kafka binder
	runtimeOnly("javax.cache:cache-api:1.1.1")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.data:spring-data-redis")
	runtimeOnly("redis.clients:jedis")
	implementation("org.springframework.boot:spring-boot-starter-cache")
//	implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2020.0.3"))

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.3") // Check for the latest version suitable for your needs
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
