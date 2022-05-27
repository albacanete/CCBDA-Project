package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build feature](https://github.com/JetBrains/teamcity-investigations-auto-assigner) assigning
 * investigations of build failures automatically based on heuristics
 * 
 * @see investigationsAutoAssigner
 */
open class InvestigationsAutoAssigner : BuildFeature {
    constructor(init: InvestigationsAutoAssigner.() -> Unit = {}, base: InvestigationsAutoAssigner? = null): super(base = base as BuildFeature?) {
        type = "InvestigationsAutoAssigner"
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

}


/**
 * Configures Investigations Auto Assigner behaviour.
 * @see InvestigationsAutoAssigner
 */
fun BuildFeatures.investigationsAutoAssigner(base: InvestigationsAutoAssigner? = null, init: InvestigationsAutoAssigner.() -> Unit = {}) {
    feature(InvestigationsAutoAssigner(init, base))
}
