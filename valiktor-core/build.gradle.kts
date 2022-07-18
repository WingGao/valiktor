val coroutinesVersion = "1.3.5"

dependencies {
    implementation("io.swagger:swagger-annotations:1.6.6")
    compile(kotlin("reflect"))

    testCompile("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testCompile("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$coroutinesVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}
