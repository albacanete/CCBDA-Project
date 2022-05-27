package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Ant) running ant script
 * 
 * @see ant
 */
open class AntBuildStep : BuildStep {
    constructor(init: AntBuildStep.() -> Unit = {}, base: AntBuildStep? = null): super(base = base as BuildStep?) {
        type = "Ant"
        init()
    }

    /**
     * Specifies what ant script will be executed
     */
    var mode by compoundParameter<Mode>("use-custom-build-file")

    sealed class Mode(value: String? = null): CompoundParam(value) {
        class AntFile() : Mode("") {

            /**
             * A path to ant script, if not specified the default value 'build.xml' is used
             */
            var path by stringParameter("build-file-path")

        }

        class AntScript() : Mode("true") {

            /**
             * Ant script content
             */
            var content by stringParameter("build-file")

        }
    }

    /**
     * Executes the ant script at the given path
     */
    fun antFile(init: Mode.AntFile.() -> Unit = {}) : Mode.AntFile {
        val result = Mode.AntFile()
        result.init()
        return result
    }

    /**
     * Executes the ant script with the given content
     */
    fun antScript(init: Mode.AntScript.() -> Unit = {}) : Mode.AntScript {
        val result = Mode.AntScript()
        result.init()
        return result
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for ant script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * Space-separated list of ant targets to execute
     */
    var targets by stringParameter("target")

    /**
     * Path to ant distribution on the agent, specify it when you want to use custom ant
     * instead of the bundled ant 1.9.7
     */
    var antHome by stringParameter("ant.home")

    /**
     * Space-separated list of additional arguments for ant
     */
    var antArguments by stringParameter("runnerArgs")

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
     * Whether TeamCity should reorder tests to reduce test feedback
     * @see TestPolicy
     */
    var reduceTestFeedback by enumParameter<TestPolicy>("teamcity.tests.runRiskGroupTestsFirst", mapping = TestPolicy.mapping)

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

    sealed class CoverageEngine(value: String? = null): CompoundParam(value) {
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

        }

        class Emma() : CoverageEngine("EMMA") {

            var parameters by stringParameter("teamcity.coverage.emma.instr.parameters")

            /**
             * Whether source files should be included into coverage data. True by default.
             */
            var includeSources by booleanParameter("teamcity.coverage.emma.include.source", trueValue = "true", falseValue = "")

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
     * Emma coverage engine
     * Emma is out of date and it does not support Java 8. Consider using IntelliJ IDEA or JaCoCo coverage runners.
     * Add a 'clean' target to your Ant build for this coverage runner to work.
     */
    fun emma(init: CoverageEngine.Emma.() -> Unit = {}) : CoverageEngine.Emma {
        val result = CoverageEngine.Emma()
        result.init()
        return result
    }

    /**
     * Tests reordering policy
     */
    enum class TestPolicy {
        /**
         * Do not reorder tests
         */
        DISABLED,
        /**
         * Run recently failed tests first
         */
        RECENTLY_FAILED,
        /**
         * Run recently failed, new, and modified tests first
         */
        RECENTLY_FAILED_AND_MODIFIED,
        /**
         * Run new and modified tests first
         */
        MODIFIED;

        companion object {
            val mapping = mapOf<TestPolicy, String>(DISABLED to "", RECENTLY_FAILED to "recentlyFailed", RECENTLY_FAILED_AND_MODIFIED to "recentlyFailed,newAndModified", MODIFIED to "newAndModified")
        }

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
}


/**
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Ant) running ant script
 * @see AntBuildStep
 */
fun BuildSteps.ant(base: AntBuildStep? = null, init: AntBuildStep.() -> Unit = {}) {
    step(AntBuildStep(init, base))
}
