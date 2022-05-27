package jetbrains.buildServer.configs.kotlin.v2019_2.triggers

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Maven+Triggers#ConfiguringMavenTriggers-MavenArtifactDependencyTrigger)
 * running builds when there is a modification of the maven dependency content.
 * 
 * @see mavenArtifact
 */
open class MavenArtifactDependencyTrigger() : Trigger() {

    init {
        type = "mavenArtifactDependencyTrigger"
    }

    constructor(init: MavenArtifactDependencyTrigger.() -> Unit): this() {
        init()
    }

    /**
     * A maven group identifier to the watched artifact belongs to
     */
    var groupId by stringParameter()

    /**
     * A watched maven artifact id
     */
    var artifactId by stringParameter()

    /**
     * Version or Version range
     */
    var version by stringParameter()

    /**
     * A type of the watched artifact. By default, the type is "jar".
     */
    var artifactType by stringParameter("type")

    /**
     * Optional classifier of the watched artifact.
     */
    var classifier by stringParameter()

    /**
     * Maven repository URL
     */
    var repoUrl by stringParameter()

    /**
     * Maven repository ID to match authentication from applied maven settings
     */
    var repoId by stringParameter()

    /**
     * Do not trigger a build if currently running builds can produce this artifact
     */
    var skipIfRunning by booleanParameter()

    /**
     * Use one of the predefined settings files or provide a custom path. By default, the standard Maven settings file location is used.
     */
    var userSettingsSelection by stringParameter()

    /**
     * The path to a user settings file
     */
    var userSettingsPath by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (groupId == null && !hasParam("groupId")) {
            consumer.consumePropertyError("groupId", "mandatory 'groupId' property is not specified")
        }
        if (artifactId == null && !hasParam("artifactId")) {
            consumer.consumePropertyError("artifactId", "mandatory 'artifactId' property is not specified")
        }
        if (version == null && !hasParam("version")) {
            consumer.consumePropertyError("version", "mandatory 'version' property is not specified")
        }
    }
}


/**
 * Adds a [trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Maven+Triggers#ConfiguringMavenTriggers-MavenArtifactDependencyTrigger).
 * running builds when there is a modification of the maven dependency content.
 * @see MavenArtifactDependencyTrigger
 */
fun Triggers.mavenArtifact(init: MavenArtifactDependencyTrigger.() -> Unit): MavenArtifactDependencyTrigger {
    val result = MavenArtifactDependencyTrigger(init)
    trigger(result)
    return result
}
