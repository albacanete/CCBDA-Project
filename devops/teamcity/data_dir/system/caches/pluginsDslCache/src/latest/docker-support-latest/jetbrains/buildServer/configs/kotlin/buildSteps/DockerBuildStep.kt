package jetbrains.buildServer.configs.kotlin.buildSteps

import jetbrains.buildServer.configs.kotlin.*

/**
 * Deprecated, was used for Docker Build build step.
 * Please use [Docker command runner step](https://www.jetbrains.com/help/teamcity/docker.html#Docker-DockerCommand) instead.
 * 
 * @see dockerBuild
 */
@Deprecated("Deprecated, previously was used for Docker Build build step.", replaceWith = ReplaceWith("DockerCommandStep"))
open class DockerBuildStep() : BuildStep() {

    init {
        type = "DockerBuild"
    }

    constructor(init: DockerBuildStep.() -> Unit): this() {
        init()
    }

    /**
     * Specifies the source of the Dockerfile
     */
    var source by compoundParameter<Source>("dockerfile.source")

    sealed class Source(value: String? = null): CompoundParam<Source>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Path() : Source("PATH") {

            /**
             * The specified path should be relative to the checkout directory.
             */
            var path by stringParameter("dockerfile.path")

            override fun validate(consumer: ErrorConsumer) {
                if (path == null && !hasParam("dockerfile.path")) {
                    consumer.consumePropertyError("source.path", "mandatory 'source.path' property is not specified")
                }
            }
        }

        class Url() : Source("URL") {

            /**
             * URL to Dockerfile
             */
            var url by stringParameter("dockerfile.url")

            override fun validate(consumer: ErrorConsumer) {
                if (url == null && !hasParam("dockerfile.url")) {
                    consumer.consumePropertyError("source.url", "mandatory 'source.url' property is not specified")
                }
            }
        }

        class Content() : Source("CONTENT") {

            /**
             * 
             */
            var content by stringParameter("dockerfile.content")

            override fun validate(consumer: ErrorConsumer) {
                if (content == null && !hasParam("dockerfile.content")) {
                    consumer.consumePropertyError("source.content", "mandatory 'source.content' property is not specified")
                }
            }
        }
    }

    /**
     * Sets filesystem path to Dockerfile
     */
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
     * Newline-separated list of the image name:tag(s).
     */
    var namesAndTags by stringParameter("docker.image.namesAndTags")

    /**
     * Additional arguments that will be passed to the 'build' command.
     */
    var commandArgs by stringParameter("command.args")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        source?.validate(consumer)
    }
}


/**
 * @see DockerCommandStep instead
 */
@Deprecated("Deprecated, previously was used for Docker Build build step.", replaceWith = ReplaceWith("DockerCommandStep"))
fun BuildSteps.dockerBuild(init: DockerBuildStep.() -> Unit): DockerBuildStep {
    val result = DockerBuildStep(init)
    step(result)
    return result
}
