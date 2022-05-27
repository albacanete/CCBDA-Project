package jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A build step running a Kotlin script with from a specified file
 * 
 * @see kotlinFile
 */
open class KotlinScriptFileBuildStep() : BuildStep() {

    init {
        type = "kotlinScript"
        param("scriptType", "file")
    }

    constructor(init: KotlinScriptFileBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for the script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * A path to a file with .main.kts extension
     */
    var path by stringParameter("scriptFile")

    /**
     * Path to Kotlin compiler
     */
    var compiler by stringParameter("kotlinPath")

    /**
     * Space-separated list of additional arguments for Kotlin script
     */
    var arguments by stringParameter("kotlinArgs")

    /**
     * Custom [JDK](https://www.jetbrains.com/help/teamcity/?Predefined+Build+Parameters#PredefinedBuildParameters-DefiningJava-relatedEnvironmentVariables) to use.
     * The default is JAVA_HOME environment variable or the agent's own Java.
     */
    var jdkHome by stringParameter("target.jdk.home")

    /**
     * Space-separated list of additional arguments for JVM
     */
    var jvmArgs by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (path == null && !hasParam("scriptFile")) {
            consumer.consumePropertyError("path", "mandatory 'path' property is not specified")
        }
    }
}


/**
 * Adds a build step running a Kotlin script from a specified file
 * @see KotlinScriptFileBuildStep
 */
fun BuildSteps.kotlinFile(init: KotlinScriptFileBuildStep.() -> Unit): KotlinScriptFileBuildStep {
    val result = KotlinScriptFileBuildStep(init)
    step(result)
    return result
}
