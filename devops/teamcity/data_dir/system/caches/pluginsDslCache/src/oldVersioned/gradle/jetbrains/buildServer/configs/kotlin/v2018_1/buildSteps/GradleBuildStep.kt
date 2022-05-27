package jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Gradle) running gradle script
 * 
 * @see gradle
 */
open class GradleBuildStep() : BuildStep() {

    init {
        type = "gradle-runner"
    }

    constructor(init: GradleBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * Space separated task names, when not set the 'default' task is used
     */
    var tasks by stringParameter("ui.gradleRunner.gradle.tasks.names")

    /**
     * Path to build file
     */
    var buildFile by stringParameter("ui.gradleRUnner.gradle.build.file")

    /**
     * When set to true the :buildDependents task will be run on projects affected by changes
     */
    var incremental by booleanParameter("ui.gradleRunner.gradle.incremental", trueValue = "true", falseValue = "")

    /**
     * Custom working directory for gradle script
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * Path to the Gradle home directory (parent of 'bin' directory). Overrides agent GRADLE_HOME environment variable
     */
    var gradleHome by stringParameter("ui.gradleRunner.gradle.home")

    /**
     * Additional parameters will be added to the 'Gradle' command line
     */
    var gradleParams by stringParameter("ui.gradleRunner.additional.gradle.cmd.params")

    /**
     * Whether TeamCity should look for Gradle Wrapper scripts in the checkout directory and run script using it
     */
    var useGradleWrapper by booleanParameter("ui.gradleRunner.gradle.wrapper.useWrapper", trueValue = "true", falseValue = "")

    /**
     * Optional path to the Gradle wrapper script, relative to the working directory
     */
    var gradleWrapperPath by stringParameter("ui.gradleRunner.gradle.wrapper.path")

    /**
     * Whether Gradle should be executed with the -d option
     */
    var enableDebug by booleanParameter("ui.gradleRunner.gradle.debug.enabled", trueValue = "true", falseValue = "")

    /**
     * Whether Gradle should be executed with the -s option
     */
    var enableStacktrace by booleanParameter("ui.gradleRunner.gradle.stacktrace.enabled", trueValue = "true", falseValue = "")

    /**
     * Custom [JDK](https://www.jetbrains.com/help/teamcity/?Predefined+Build+Parameters#PredefinedBuildParameters-DefiningJava-relatedEnvironmentVariables) to use.
     * The default is JAVA_HOME environment variable or the agent's own Java.
     */
    var jdkHome by stringParameter("target.jdk.home")

    /**
     * Space-separated list of additional arguments for JVM
     */
    var jvmArgs by stringParameter()

    /**
     * Specifies which Docker image platform will be used to run this build step.
     */
    var dockerImagePlatform by enumParameter<ImagePlatform>("plugin.docker.imagePlatform", mapping = ImagePlatform.mapping)

    /**
     * If enabled, "docker pull [image][dockerImage]" will be run before docker run.
     */
    var dockerPull by booleanParameter("plugin.docker.pull.enabled", trueValue = "true", falseValue = "")

    /**
     * Specifies which Docker image to use for running this build step. I.e. the build step will be run inside specified docker image, using 'docker run' wrapper.
     */
    var dockerImage by stringParameter("plugin.docker.imageId")

    /**
     * Additional docker run command arguments
     */
    var dockerRunParameters by stringParameter("plugin.docker.run.parameters")

    /**
     * Specifies coverage engine to use
     */
    var coverageEngine by compoundParameter<CoverageEngine>("teamcity.coverage.runner")

    sealed class CoverageEngine(value: String? = null): CompoundParam<CoverageEngine>(value) {
        class Idea() : CoverageEngine("IDEA") {

            /**
             * Newline-separated patterns for fully qualified class names to be analyzed by code coverage.
             * A pattern should start with a valid package name and can contain a wildcard, for example: org.apache.*
             */
            var includeClasses by stringParameter("teamcity.coverage.idea.includePatterns")

            /**
             * Newline-separated patterns for fully qualified class names to be excluded from the coverage. Exclude patterns have priority over include patterns.
             */
            var excludeClasses by stringParameter("teamcity.coverage.idea.excludePatterns")

        }

        class Jacoco() : CoverageEngine("JACOCO") {

            /**
             * Newline-delimited set of path patterns in the form of +|-:path to scan for classfiles to be analyzed.
             * Excluding libraries and test classes from analysis is recommended. Ant like patterns are supported.
             */
            var classLocations by stringParameter("teamcity.coverage.jacoco.classpath")

            /**
             * Newline-separated patterns for fully qualified class names to be excluded from the coverage.
             * Exclude patterns have priority over include patterns.
             */
            var excludeClasses by stringParameter("teamcity.coverage.jacoco.patterns")

            /**
             * JaCoCo version to use
             */
            var jacocoVersion by stringParameter("teamcity.tool.jacoco")

        }
    }

    fun idea(init: CoverageEngine.Idea.() -> Unit = {}) : CoverageEngine.Idea {
        val result = CoverageEngine.Idea()
        result.init()
        return result
    }

    fun jacoco(init: CoverageEngine.Jacoco.() -> Unit = {}) : CoverageEngine.Jacoco {
        val result = CoverageEngine.Jacoco()
        result.init()
        return result
    }

    /**
     * Docker image platforms
     */
    enum class ImagePlatform {
        Any,
        Linux,
        Windows;

        companion object {
            val mapping = mapOf<ImagePlatform, String>(Any to "", Linux to "linux", Windows to "windows")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Gradle) running gradle script
 * @see GradleBuildStep
 */
fun BuildSteps.gradle(init: GradleBuildStep.() -> Unit): GradleBuildStep {
    val result = GradleBuildStep(init)
    step(result)
    return result
}
