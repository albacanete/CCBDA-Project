package jetbrains.buildServer.configs.kotlin.v10.failureConditions

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A [build failure condition](https://www.jetbrains.com/help/teamcity/?Build+Failure+Conditions#BuildFailureConditions-Failbuildonmetricchange)
 * failing build on metric change.
 * 
 * @see failOnMetricChange
 */
open class BuildFailureOnMetric : FailureCondition {
    constructor(init: BuildFailureOnMetric.() -> Unit = {}, base: BuildFailureOnMetric? = null): super(base = base as FailureCondition?) {
        type = "BuildFailureOnMetric"
        init()
    }

    /**
     * Type of the metric
     * @see MetricType
     */
    var metric by enumParameter<MetricType>("metricKey", mapping = MetricType.mapping)

    /**
     * Threshold for build failure. A build will fail when a metric is changed more significantly than the specified threshold.
     * Threshold change is treated according to the selected units. If not specified, the default value 1 is used.
     * @see units
     * @see MetricUnit
     */
    var threshold by intParameter("metricThreshold")

    /**
     * Specifies the unit of the given threshold (default metric units or percents)
     * @see MetricUnit
     */
    var units by enumParameter<MetricUnit>("metricUnits", mapping = MetricUnit.mapping)

    /**
     * On which type of metric change a build should fail
     * @see MetricComparison
     */
    var comparison by enumParameter<MetricComparison>("moreOrLess", mapping = MetricComparison.mapping)

    /**
     * Specifies which value should be compared to the metric value in the build
     */
    var compareTo by compoundParameter<CompareTo>("withBuildAnchor")

    sealed class CompareTo(value: String? = null): CompoundParam(value) {
        class Value() : CompareTo("false") {

        }

        class Build() : CompareTo("true") {

            var buildRule by compoundParameter<BuildRule>("anchorBuild")

            sealed class BuildRule(value: String? = null): CompoundParam(value) {
                class LastSuccessful() : BuildRule("lastSuccessful") {

                }

                class LastPinned() : BuildRule("lastPinned") {

                }

                class LastFinished() : BuildRule("lastFinished") {

                }

                class BuildWithNumber() : BuildRule("buildNumber") {

                    /**
                     * A build number to use
                     */
                    var number by stringParameter("buildNumberPattern")

                }

                class BuildWithTag() : BuildRule("buildTag") {

                    /**
                     * A tag to use
                     */
                    var tag by stringParameter("buildTag")

                }
            }

            /**
             * Matches the last successful build in the build configuration
             */
            fun lastSuccessful() = BuildRule.LastSuccessful()

            /**
             * Matches the last pinned build in the build configuration
             */
            fun lastPinned() = BuildRule.LastPinned()

            /**
             * Matches the last finished build in the build configuration
             */
            fun lastFinished() = BuildRule.LastFinished()

            /**
             * Matches the build with the specified build number
             */
            fun buildWithNumber(init: BuildRule.BuildWithNumber.() -> Unit = {}) : BuildRule.BuildWithNumber {
                val result = BuildRule.BuildWithNumber()
                result.init()
                return result
            }

            /**
             * Matches the build with the specified tag
             */
            fun buildWithTag(init: BuildRule.BuildWithTag.() -> Unit = {}) : BuildRule.BuildWithTag {
                val result = BuildRule.BuildWithTag()
                result.init()
                return result
            }

        }
    }

    /**
     * The metric value in the build should be compared to the threshold
     */
    fun value() = CompareTo.Value()

    /**
     * The metric value in the build should be compared to the metric value from a build matched by the rule
     */
    fun build(init: CompareTo.Build.() -> Unit = {}) : CompareTo.Build {
        val result = CompareTo.Build()
        result.init()
        return result
    }

    /**
     * Immediately stop the build if it fails due to this failure condition
     */
    var stopBuildOnFailure by booleanParameter(trueValue = "true", falseValue = "")

    /**
     * A type of the metric
     */
    enum class MetricType {
        /**
         * Size of artifacts excluding [internal artifacts](https://www.jetbrains.com/help/teamcity/?Build+Artifact#BuildArtifact-HiddenArtifacts) under .teamcity directory
         */
        ARTIFACT_SIZE,
        /**
         * Total size of artifacts including [internal artifacts](https://www.jetbrains.com/help/teamcity/?Build+Artifact#BuildArtifact-HiddenArtifacts)
         */
        ARTIFACTS_TOTAL_SIZE,
        /**
         * Build duration in seconds
         */
        BUILD_DURATION,
        /**
         * Build log size estimate in bytes
         */
        BUILD_LOG_SIZE,
        /**
         * Total number of java classes
         */
        CLASS_COUNT,
        /**
         * Duplicates count
         */
        DUPLICATE_COUNT,
        /**
         * Number of java classes covered by tests
         */
        COVERAGE_CLASS_COUNT,
        /**
         * Number of lines covered by tests
         */
        COVERAGE_LINE_COUNT,
        /**
         * Number of methods covered by tests
         */
        COVERAGE_METHOD_COUNT,
        /**
         * Percentage of blocks covered by tests
         */
        COVERAGE_BLOCK_PERCENTAGE,
        /**
         * Percentage of conditional branches covered by tests
         */
        COVERAGE_BRANCH_PERCENTAGE,
        /**
         * Percentage of java classes covered by tests
         */
        COVERAGE_CLASS_PERCENTAGE,
        /**
         * Percentage of lines covered by tests
         */
        COVERAGE_LINE_PERCENTAGE,
        /**
         * Percentage of methods covered by tests
         */
        COVERAGE_METHOD_PERCENTAGE,
        /**
         * Percentage of statements covered by tests
         */
        COVERAGE_STATEMENT_PERCENTAGE,
        /**
         * Number of inspections marked as error
         */
        INSPECTION_ERROR_COUNT,
        /**
         * Number of inspections marked as warning
         */
        INSPECTION_WARN_COUNT,
        /**
         * Total number of lines of code
         */
        LINE_COUNT,
        /**
         * Total number of methods
         */
        METHOD_COUNT,
        /**
         * Number of passed tests
         */
        PASSED_TEST_COUNT,
        /**
         * Total number of tests
         */
        TEST_COUNT,
        /**
         * Number of failed tests
         */
        TEST_FAILED_COUNT,
        /**
         * Number of ignored tests
         */
        TEST_IGNORED_COUNT,
        /**
         * Tests duration in seconds
         */
        TEST_DURATION,
        /**
         * Number of critical Qodana problems
         */
        QODANA_CRITICAL_PROBLEMS_COUNT,
        /**
         * Number of high Qodana problems
         */
        QODANA_HIGH_PROBLEMS_COUNT,
        /**
         * Number of info Qodana problems
         */
        QODANA_INFO_PROBLEMS_COUNT,
        /**
         * Number of low Qodana problems
         */
        QODANA_LOW_PROBLEMS_COUNT,
        /**
         * Number of moderate Qodana problems
         */
        QODANA_MODERATE_PROBLEMS_COUNT,
        /**
         * Total number of Qodana problems
         */
        QODANA_TOTAL_PROBLEMS_COUNT,
        /**
         * Total number of Qodana checks
         */
        QODANA_CHECKS_COUNT;

        companion object {
            val mapping = mapOf<MetricType, String>(ARTIFACT_SIZE to "VisibleArtifactsSize", ARTIFACTS_TOTAL_SIZE to "ArtifactsSize", BUILD_DURATION to "BuildDurationNetTime", BUILD_LOG_SIZE to "buildLogSize", CLASS_COUNT to "CodeCoverageAbsCTotal", DUPLICATE_COUNT to "DuplicatorStats", COVERAGE_CLASS_COUNT to "CodeCoverageAbsCCovered", COVERAGE_LINE_COUNT to "CodeCoverageAbsLCovered", COVERAGE_METHOD_COUNT to "CodeCoverageAbsMCovered", COVERAGE_BLOCK_PERCENTAGE to "CodeCoverageB", COVERAGE_BRANCH_PERCENTAGE to "CodeCoverageR", COVERAGE_CLASS_PERCENTAGE to "CodeCoverageC", COVERAGE_LINE_PERCENTAGE to "CodeCoverageL", COVERAGE_METHOD_PERCENTAGE to "CodeCoverageM", COVERAGE_STATEMENT_PERCENTAGE to "CodeCoverageS", INSPECTION_ERROR_COUNT to "InspectionStatsE", INSPECTION_WARN_COUNT to "InspectionStatsW", LINE_COUNT to "CodeCoverageAbsLTotal", METHOD_COUNT to "CodeCoverageAbsMTotal", PASSED_TEST_COUNT to "buildPassedTestCount", TEST_COUNT to "buildTestCount", TEST_FAILED_COUNT to "buildFailedTestCount", TEST_IGNORED_COUNT to "buildIgnoredTestCount", TEST_DURATION to "TestsDuration", QODANA_CRITICAL_PROBLEMS_COUNT to "QodanaProblemsCritical", QODANA_HIGH_PROBLEMS_COUNT to "QodanaProblemsHigh", QODANA_INFO_PROBLEMS_COUNT to "QodanaProblemsInfo", QODANA_LOW_PROBLEMS_COUNT to "QodanaProblemsLow", QODANA_MODERATE_PROBLEMS_COUNT to "QodanaProblemsModerate", QODANA_TOTAL_PROBLEMS_COUNT to "QodanaProblemsTotal", QODANA_CHECKS_COUNT to "QodanaChecks")
        }

    }
    /**
     * Specifies how to treat the threshold specified in the failure condition
     */
    enum class MetricUnit {
        /**
         * Threshold is specified in the default metric units
         */
        DEFAULT_UNIT,
        /**
         * Threshold is specified in percents
         */
        PERCENTS;

        companion object {
            val mapping = mapOf<MetricUnit, String>(DEFAULT_UNIT to "metricUnitsDefault", PERCENTS to "metricUnitsPercents")
        }

    }
    /**
     * A type of metric change
     */
    enum class MetricComparison {
        /**
         * Fail the build when the metric increases
         */
        MORE,
        /**
         * Fail the build when the metric decreases
         */
        LESS,
        /**
         * Fail the build when the metric increases or decreases
         */
        DIFF;

        companion object {
            val mapping = mapOf<MetricComparison, String>(MORE to "more", LESS to "less", DIFF to "different")
        }

    }
}


/**
 * Adds a [build failure condition](https://www.jetbrains.com/help/teamcity/?Build+Failure+Conditions#BuildFailureConditions-Failbuildonmetricchange)
 * failing build on metric change.
 * @see BuildFailureOnMetric
 */
fun FailureConditions.failOnMetricChange(base: BuildFailureOnMetric? = null, init: BuildFailureOnMetric.() -> Unit = {}) {
    failureCondition(BuildFailureOnMetric(init, base))
}
