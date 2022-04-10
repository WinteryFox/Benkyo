import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib")
    kotlin("jvm")
    kotlin("plugin.spring")

    id("nu.studer.jooq")
}

group = "com.benkyo.decks"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_16

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.auth0:java-jwt:3.19.0")
    implementation("com.auth0:jwks-rsa:0.21.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    jooqGenerator("org.postgresql:postgresql")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "16"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from.image = "adoptopenjdk/openjdk16:alpine-jre"
    to.image = "winteryfox/benkyo"
}

val POSTGRES_HOST = System.getenv()["POSTGRES_HOST"] ?: "localhost"
val POSTGRES_PORT = System.getenv()["POSTGRES_PORT"] ?: "5432"
val POSTGRES_DB = System.getenv()["POSTGRES_DB"] ?: "postgres"
val POSTGRES_USER = System.getenv()["POSTGRES_DB"] ?: "postgres"
val POSTGRES_PASSWORD = System.getenv()["POSTGRES_DB"] ?: "12345"

jooq {
    version.set("3.16.4")  // the default (can be omitted)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // the default (can be omitted)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.INFO

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$POSTGRES_DB"

                    user = POSTGRES_USER
                    password = POSTGRES_PASSWORD
                }

                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"

                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }

                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos  = true
                        isFluentSetters = true
                        isPojosAsKotlinDataClasses = true
                        isRelations = true
                    }

                    target.apply {
                        packageName = "com.benkyo.decks.jooq"
                    }

                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

// Make Spring use the same version of JOOQ as the plugin
ext["jooq.version"] = jooq.version.get()
