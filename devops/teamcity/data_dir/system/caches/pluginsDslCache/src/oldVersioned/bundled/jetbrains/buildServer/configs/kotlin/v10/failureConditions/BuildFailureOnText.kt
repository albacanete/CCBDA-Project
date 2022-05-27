package jetbrains.buildServer.configs.kotlin.v10.failureConditions

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build failure condition](https://www.jetbrains.com/help/teamcity/?Build+Failure+Conditions#BuildFailureConditions-Failbuildonspecifictextinbuildlog)
 * failing build on specific text in a build log.
 * 
 * @see failOnText
 */
open class BuildFailureOnText : FailureCondition {
    constructor(init: BuildFailureOnText.() -> Unit = {}, base: BuildFailureOnText? = null): super(base = base as FailureCondition?) {
        type = "BuildFailureOnMessage"
        init()
    }

    /**
     * A type defining how to treat the specified pattern
     * @see pattern
     * @see ConditionType
     */
    var conditionType by enumParameter<ConditionType>("buildFailureOnMessage.conditionType", mapping = ConditionType.mapping)

    /**
     * The pattern to search for in the build log.
     * Pattern interpretation depends on the selected conditionType.
     * @see conditionType
     * @see ConditionType
     */
    var pattern by stringParameter("buildFailureOnMessage.messagePattern")

    /**
     * The message to display in the UI and the build log when build fails
     */
    var failureMessage by stringParameter("buildFailureOnMessage.outputText")

    /**
     * Whether the matching should be reversed, ie. the build should fail if build log doesn't
     * contain the specified pattern.
     */
    var reverse by booleanParameter("buildFailureOnMessage.reverse")

    /**
     * Immediately stop the build if it fails due to this failure condition
     */
    var stopBuildOnFailure by booleanParameter("buildFailureOnMessage.stopBuildOnFailure", trueValue = "true", falseValue = "")

    /**
     * Create build problem only on the first match
     */
    var reportOnlyFirstMatch by booleanParameter("buildFailureOnMessage.reportOnlyFirstMatch", trueValue = "", falseValue = "false")

    /**
     * Defines how to treat the pattern specified in failure condition
     */
    enum class ConditionType {
        /**
         * Treat the pattern as string
         */
        CONTAINS,
        /**
         * Treat the pattern as [java regular expression](http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html)
         */
        REGEXP;

        companion object {
            val mapping = mapOf<ConditionType, String>(CONTAINS to "contains", REGEXP to "matchesRegex")
        }

    }
}


/**
 * Adds a [build failure condition](https://www.jetbrains.com/help/teamcity/?Build+Failure+Conditions#BuildFailureConditions-Failbuildonspecifictextinbuildlog) failing build
 * when specified text is found in a build log.
 * @see BuildFailureOnText
 */
fun FailureConditions.failOnText(base: BuildFailureOnText? = null, init: BuildFailureOnText.() -> Unit = {}) {
    failureCondition(BuildFailureOnText(init, base))
}
