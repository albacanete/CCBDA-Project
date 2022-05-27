package jetbrains.buildServer.configs.kotlin.v2017_2.triggers

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * Base class for [Finish Build Triggers](https://www.jetbrains.com/help/teamcity/?Configuring+Finish+Build+Trigger).
 * Finish build trigger runs build when some other build finishes.
 * 
 * @see finishBuildTrigger
 */
open class FinishBuildTrigger() : Trigger() {

    init {
        type = "buildDependencyTrigger"
    }

    constructor(init: FinishBuildTrigger.() -> Unit): this() {
        init()
    }

    /**
     * Use buildType instead
     */
    @Deprecated("", replaceWith = ReplaceWith("buildType"))
    var buildTypeExtId by stringParameter("dependsOn")

    /**
     * Id of the build configuration to watch. A new build will be triggered when a build
     * in the watched build configuration is finished.
     */
    var buildType by stringParameter("dependsOn")

    /**
     * Whether the build should be triggered only after a successful build in the watched build configuration
     */
    var successfulOnly by booleanParameter("afterSuccessfulBuildOnly", trueValue = "true", falseValue = "")

    /**
     * [Branch filter](https://www.jetbrains.com/help/teamcity/?Configuring+Finish+Build+Trigger#ConfiguringFinishBuildTrigger-branchFilter)
     * allows to limit the branches where finished builds are watched.
     */
    var branchFilter by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (buildTypeExtId == null && !hasParam("dependsOn")) {
            consumer.consumePropertyError("buildTypeExtId", "mandatory 'buildTypeExtId' property is not specified")
        }
        if (buildType == null && !hasParam("dependsOn")) {
            consumer.consumePropertyError("buildType", "mandatory 'buildType' property is not specified")
        }
    }
}


/**
 * Adds [Finish Build Trigger](https://www.jetbrains.com/help/teamcity/?Configuring+Finish+Build+Trigger) to build configuration or template
 * @see FinishBuildTrigger
 */
fun Triggers.finishBuildTrigger(init: FinishBuildTrigger.() -> Unit): FinishBuildTrigger {
    val result = FinishBuildTrigger(init)
    trigger(result)
    return result
}
