plugins {
	application
	alias(libs.plugins.freefairLombokPLugin)
	alias(libs.plugins.benManesVersionsPlugin)
	alias(libs.plugins.johnrengelmanShadowPlugin)
	alias(libs.plugins.patrikerdesUseLatestVersionsPlugin)
	alias(libs.plugins.littlerobotsVersionCatalogUpdatePlugin)
	alias(libs.plugins.springFrameworkPlugin)
	alias(libs.plugins.springDependencyManagementPlugin)
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
