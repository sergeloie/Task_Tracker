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
	alias(libs.plugins.sentryPlugin)
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_20
	targetCompatibility = JavaVersion.VERSION_20
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
	annotationProcessor(libs.mapstruct.annotation.processor)

	testImplementation(libs.bundles.junitBundle)
	testImplementation(platform(libs.junitBom))
	testImplementation(libs.bundles.springTest)

	implementation(libs.bundles.springStudy)
	implementation(libs.jakarta.persistence.api)
	implementation(libs.mapstruct)
	implementation(libs.jackson.databind.nullable)
	implementation(libs.datafaker)
	implementation(libs.instancio)
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

tasks.jacocoTestReport {
	reports {
		xml.required = true
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}

sentry {
	// Enables more detailed log output, e.g. for sentry-cli.
	//
	// Default is false.
	debug.set(true)

	// Generates a source bundle and uploads it to Sentry.
	// This enables source context, allowing you to see your source
	// code as part of your stack traces in Sentry.
	//
	// Default is disabled. To enable, see the source context guide.
	includeSourceContext.set(true)

	// Includes additional source directories into the source bundle.
	// These directories are resolved relative to the project directory.
	additionalSourceDirsForSourceContext.set(setOf("mysrc/java", "other-source-dir/main/kotlin"))

	// Disables or enables dependencies metadata reporting for Sentry.
	// If enabled, the plugin will collect external dependencies and
	// upload them to Sentry as part of events. If disabled, all the logic
	// related to the dependencies metadata report will be excluded.
	//
	// Default is enabled.
	includeDependenciesReport.set(true)

	// Automatically adds Sentry dependencies to your project.
	autoInstallation {
		enabled.set(true)
	}
	projectName.set("project99")
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}


