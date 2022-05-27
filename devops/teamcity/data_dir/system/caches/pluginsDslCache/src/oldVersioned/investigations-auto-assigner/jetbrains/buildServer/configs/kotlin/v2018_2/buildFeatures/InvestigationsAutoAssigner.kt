package jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [build feature](https://github.com/JetBrains/teamcity-investigations-auto-assigner) assigning
 * investigations of build failures automatically based on heuristics
 * 
 * @see investigationsAutoAssigner
 */
open class InvestigationsAutoAssigner() : BuildFeature() {

    init {
        type = "InvestigationsAutoAssigner"
    }

    constructor(init: InvestigationsAutoAssigner.() -> Unit): this() {
        init()
    }

    /**
     * Username of a user to whom an investigation is assigned if no other possible investigator is found.
     */
    var defaultAssignee by stringParameter("defaultAssignee.username")

    /**
     * The newline-separated list of usernames to exclude from investigation auto-assignment.
     */
    var excludeUsers by stringParameter("excludeAssignees.usernames")

    /**
     * When 'true', compilation build problems are ignored.
     */
    var ignoreCompilationProblems by stringParameter("ignoreBuildProblems.compilation")

    /**
     * When 'true', exit code build problems are ignored.
     */
    var ignoreExitCodeProblems by stringParameter("ignoreBuildProblems.exitCode")

    /**
     * Whether investigations auto-assigner should use "on second failure" strategy.
     */
    var assignOnSecondFailure by booleanParameter(trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Configures Investigations Auto Assigner behaviour.
 * @see InvestigationsAutoAssigner
 */
fun BuildFeatures.investigationsAutoAssigner(init: InvestigationsAutoAssigner.() -> Unit): InvestigationsAutoAssigner {
    val result = InvestigationsAutoAssigner(init)
    feature(result)
    return result
}
