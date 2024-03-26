plugins {
	application
	checkstyle
	jacoco
	alias(libs.plugins.freefairLombokPLugin)
	alias(libs.plugins.benManesVersionsPlugin)
//	alias(libs.plugins.johnrengelmanShadowPlugin)
	alias(libs.plugins.patrikerdesUseLatestVersionsPlugin)
	alias(libs.plugins.littlerobotsVersionCatalogUpdatePlugin)
	alias(libs.plugins.springFrameworkPlugin)
	alias(libs.plugins.springDependencyManagementPlugin)
	alias(libs.plugins.sonarCubePlugin)
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_20
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

	runtimeOnly(libs.h2)
	runtimeOnly(libs.postgresql)

	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	testImplementation(libs.bundles.junitBundle)
	testImplementation(platform(libs.junitBom))

	implementation(libs.bundles.springStudy)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sonar {
	properties {
		property("sonar.projectKey", "sergeloie_java-project-99")
		property("sonar.organization", "sergeloie")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}
