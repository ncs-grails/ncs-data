grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
		grailsRepo "http://svn.cccs.umn.edu/ncs-grails-plugins"

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        mavenRepo "http://artifact.ncs.umn.edu/plugins-release"
        //mavenRepo "http://artifact.ncs.umn.edu/plugins-snapshot"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'mysql:mysql-connector-java:5.1.18'
		compile "org.codehaus.gpars:gpars:0.11"
    }
	plugins {
		compile ":hibernate:$grailsVersion"
		compile ":tomcat:$grailsVersion"

		compile ":audit-logging:0.5.4"
		compile ":joda-time:1.0"
		compile ":mail:1.0-SNAPSHOT"
		compile ":ncs-event:1.1"
		compile ":ncs-norc-link:0.4"
		compile ":ncs-people:0.8"
		compile ":ncs-recruitment:1.0"
		compile ":ncs-tracking:3.2.2"
		compile ":ncs-web-template:0.2"
		compile ":spring-security-core:1.2.7.1"

		test ":code-coverage:1.2.5"
		test ":codenarc:0.16.1"
	}
}

codenarc.reports = {
	JenkinsXmlReport('xml') {
		outputFile = 'target/test-reports/CodeNarcReport.xml'
		title = 'CodeNarc Report for NCS Data Web Service'
	}
	JenkinsHtmlReport('html') {
		outputFile = 'CodeNarcReport.html'
		title = 'CodeNarc Report for NCS Data Web Service'
	}
}
codenarc.propertiesFile = 'grails-app/conf/codenarc.properties'
