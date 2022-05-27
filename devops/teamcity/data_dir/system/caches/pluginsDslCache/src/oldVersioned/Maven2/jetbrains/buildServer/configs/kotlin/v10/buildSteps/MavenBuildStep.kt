package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/?Maven) running maven
 * 
 * @see maven
 */
open class MavenBuildStep : BuildStep {
    constructor(init: MavenBuildStep.() -> Unit = {}, base: MavenBuildStep? = null): super(base = base as BuildStep?) {
        type = "Maven2"
        init()
    }

    /**
     * Space-separated list of goals to execute
     */
    var goals by stringParameter()

    /**
     * Path to POM file. Should be relative to the checkout directory.
     */
    var pomLocation by stringParameter()

    /**
     * Additional Maven command line parameters.
     */
    var runnerArgs by stringParameter()

    /**
     * Custom working directory for maven. If not specified, the checkout directory is used.
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * Maven version to use
     */
    var mavenVersion by compoundParameter<MavenVersion>("maven.path")

    sealed class MavenVersion(value: String? = null): CompoundParam(value) {
        class Default() : MavenVersion("%teamcity.tool.maven.AUTO%") {

        }

        class Auto() : MavenVersion("%teamcity.tool.maven.AUTO%") {

        }

        class DefaultProvidedVersion() : MavenVersion("%teamcity.tool.maven.DEFAULT%") {

        }

        class Custom() : MavenVersion(null) {

            /**
             * The path to a custom Maven installation
             */
            var path by stringParameter("maven.path")

        }

        class Bundled_2() : MavenVersion("%teamcity.tool.maven%") {

        }

        class Bundled_3_0() : MavenVersion("%teamcity.tool.maven3%") {

        }

        class Bundled_3_1() : MavenVersion("%teamcity.tool.maven3_1%") {

        }

        class Bundled_3_2() : MavenVersion("%teamcity.tool.maven3_2%") {

        }

        class Bundled_3_3() : MavenVersion("%teamcity.tool.maven3_3%") {

        }

        class Bundled_3_5() : MavenVersion("%teamcity.tool.maven3_5%") {

        }

        class Bundled_3_6() : MavenVersion("%teamcity.tool.maven3_6%") {

        }
    }

    /**
     * In TeamCity 10.0 the meaning of this option was: Maven version specified in M2_HOME environment variable, if the environment variable is empty, then the Maven version 3.0.5.
     * In TeamCity 2017.1 this option is renamed to auto(), please use it instead.
     * If you want to always use the default Maven version provided by TeamCity server, switch to defaultProvidedVersion().
     * @see auto
     * @see defaultProvidedVersion
     */
    @Deprecated("Please use auto() instead", replaceWith = ReplaceWith("auto()"))
    fun default() = MavenVersion.Default()

    /**
     * Maven version specified by the M2_HOME environment variable.
     * If the environment variable is empty, then the default Maven version provided by TeamCity server will be used.
     */
    fun auto() = MavenVersion.Auto()

    /**
     * The default Maven version provided by TeamCity server
     */
    fun defaultProvidedVersion() = MavenVersion.DefaultProvidedVersion()

    /**
     * The custom Maven version found at the specified path
     */
    fun custom(init: MavenVersion.Custom.() -> Unit = {}) : MavenVersion.Custom {
        val result = MavenVersion.Custom()
        result.init()
        return result
    }

    /**
     * Use maven 2 bundled with TeamCity
     */
    fun bundled_2() = MavenVersion.Bundled_2()

    /**
     * Use maven 3.0.5 bundled with TeamCity
     */
    fun bundled_3_0() = MavenVersion.Bundled_3_0()

    /**
     * Use maven 3.1.1 bundled with TeamCity
     */
    fun bundled_3_1() = MavenVersion.Bundled_3_1()

    /**
     * Use maven 3.2.5 bundled with TeamCity
     */
    fun bundled_3_2() = MavenVersion.Bundled_3_2()

    /**
     * Use maven 3.3.9 bundled with TeamCity
     */
    fun bundled_3_3() = MavenVersion.Bundled_3_3()

    /**
     * Use maven 3.5.4 bundled with TeamCity
     */
    fun bundled_3_5() = MavenVersion.Bundled_3_5()

    /**
     * Use maven 3.6.3 bundled with TeamCity
     */
    fun bundled_3_6() = MavenVersion.Bundled_3_6()

    /**
     * Use one of the predefined settings files or provide a custom path. By default, the standard Maven settings file location is used.
     */
    var userSettingsSelection by stringParameter()

    /**
     * The path to a user settings file
     */
    var userSettingsPath by stringParameter()

    /**
     * Use own local artifact repository
     */
    @Deprecated("Please use localRepoScope instead.", replaceWith = ReplaceWith("localRepoScope"))
    var useOwnLocalRepo by booleanParameter()

    var localRepoScope by enumParameter<RepositoryScope>(mapping = RepositoryScope.mapping)

    /**
     * Enable incremental building
     */
    var isIncremental by booleanParameter()

    /**
     * A path to [JDK](https://www.jetbrains.com/help/teamcity/?Predefined+Build+Parameters#PredefinedBuildParameters-DefiningJava-relatedEnvironmentVariables) to use.
     * By default the JAVA_HOME environment variable or the agent's own Java is used.
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
     * Maven local repository scope.
     */
    enum class RepositoryScope {
        /**
         * Shared by all build configurations and all agents on the machine.
         */
        MAVEN_DEFAULT,
        /**
         * Shared by all the builds on the agent. Can be cleaned by agent to free disk space.
         */
        AGENT,
        /**
         * Shared by all the builds in a single build configuration. Can be cleaned by agent to free disk space.
         */
        BUILD_CONFIGURATION;

        companion object {
            val mapping = mapOf<RepositoryScope, String>(MAVEN_DEFAULT to "mavenDefault", AGENT to "agent", BUILD_CONFIGURATION to "buildConfiguration")
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
 * Adds a [build step](https://www.jetbrains.com/help/teamcity/?Maven) running maven
 */
fun BuildSteps.maven(base: MavenBuildStep? = null, init: MavenBuildStep.() -> Unit = {}) {
    step(MavenBuildStep(init, base))
}
