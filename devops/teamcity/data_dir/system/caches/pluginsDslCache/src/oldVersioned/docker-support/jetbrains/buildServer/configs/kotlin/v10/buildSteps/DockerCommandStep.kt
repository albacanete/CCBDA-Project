package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build step](https://www.jetbrains.com/help/teamcity/docker.html#Docker-DockerCommand) for a generic docker command runner (can run Docker build, push, other...)
 * 
 * @see dockerCommand
 */
open class DockerCommandStep : BuildStep {
    constructor(init: DockerCommandStep.() -> Unit = {}, base: DockerCommandStep? = null): super(base = base as BuildStep?) {
        type = "DockerCommand"
        init()
    }

    /**
     * Specifies the type of the command, at the moment "build", "push", "other"
     */
    var commandType by compoundParameter<CommandType>("docker.command.type")

    sealed class CommandType(value: String? = null): CompoundParam(value) {
        class Build() : CommandType("build") {

            /**
             * Specifies the source of the Dockerfile
             */
            var source by compoundParameter<Source>("dockerfile.source")

            sealed class Source(value: String? = null): CompoundParam(value) {
                class File() : Source("PATH") {

                    /**
                     * The specified path should be relative to the checkout directory.
                     */
                    var path by stringParameter("dockerfile.path")

                }

                class Path() : Source("PATH") {

                    /**
                     * The specified path should be relative to the checkout directory.
                     */
                    var path by stringParameter("dockerfile.path")

                }

                class Url() : Source("URL") {

                    /**
                     * URL to Dockerfile
                     */
                    var url by stringParameter("dockerfile.url")

                }

                class Content() : Source("CONTENT") {

                    /**
                     * 
                     */
                    var content by stringParameter("dockerfile.content")

                }
            }

            /**
             * Sets filesystem path to Dockerfile
             */
            fun file(init: Source.File.() -> Unit = {}) : Source.File {
                val result = Source.File()
                result.init()
                return result
            }

            /**
             * Sets filesystem path to Dockerfile
             */
            @Deprecated("Use 'file' option instead")
            fun path(init: Source.Path.() -> Unit = {}) : Source.Path {
                val result = Source.Path()
                result.init()
                return result
            }

            /**
             * Sets Dockerfile URL
             */
            fun url(init: Source.Url.() -> Unit = {}) : Source.Url {
                val result = Source.Url()
                result.init()
                return result
            }

            /**
             * Use Dockerfile content
             */
            fun content(init: Source.Content.() -> Unit = {}) : Source.Content {
                val result = Source.Content()
                result.init()
                return result
            }

            /**
             * If blank, the folder containing the Dockerfile will be used.
             */
            var contextDir by stringParameter("dockerfile.contextDir")

            /**
             * Specifies which Docker image platform will be used to run this build step.
             */
            var platform by enumParameter<ImagePlatform>("dockerImage.platform", mapping = ImagePlatform.mapping)

            /**
             * Newline-separated list of the image name:tag(s).
             */
            var namesAndTags by stringParameter("docker.image.namesAndTags")

            /**
             * Additional arguments that will be passed to the command.
             */
            var commandArgs by stringParameter("command.args")

        }

        class Push() : CommandType("push") {

            /**
             * Newline-separated list of the image name:tag(s).
             */
            var namesAndTags by stringParameter("docker.image.namesAndTags")

            /**
             * Remove image from agent after push.
             */
            var removeImageAfterPush by booleanParameter("docker.push.remove.image", trueValue = "true", falseValue = "")

            /**
             * Additional arguments that will be passed to the command.
             */
            var commandArgs by stringParameter("command.args")

        }

        class Other() : CommandType("other") {

            /**
             * Docker sub-command, like 'pull' or 'tag'
             */
            var subCommand by stringParameter("docker.sub.command")

            /**
             * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for the command script,
             * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
             */
            var workingDir by stringParameter("teamcity.build.workingDir")

            /**
             * Additional arguments that will be passed to the docker command.
             */
            var commandArgs by stringParameter("command.args")

        }
    }

    /**
     * Run "docker build" command
     */
    fun build(init: CommandType.Build.() -> Unit = {}) : CommandType.Build {
        val result = CommandType.Build()
        result.init()
        return result
    }

    /**
     * Run "docker push" command
     */
    fun push(init: CommandType.Push.() -> Unit = {}) : CommandType.Push {
        val result = CommandType.Push()
        result.init()
        return result
    }

    /**
     * Run a specified docker command
     */
    fun other(init: CommandType.Other.() -> Unit = {}) : CommandType.Other {
        val result = CommandType.Other()
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
}


/**
 * @see DockerCommandStep
 */
fun BuildSteps.dockerCommand(base: DockerCommandStep? = null, init: DockerCommandStep.() -> Unit = {}) {
    step(DockerCommandStep(init, base))
}
