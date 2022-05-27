package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * [Parallel Tests Feature](https://www.jetbrains.com/help/teamcity/parallel-tests.html)
 * can automatically split tests of a build into several batches, by test classes. It will run them in parallel on suitable build agents and gather results in a composite build overview.
 * 
 * @see parallelTests
 */
open class ParallelTestsFeature() : BuildFeature() {

    init {
        type = "parallelTests"
    }

    constructor(init: ParallelTestsFeature.() -> Unit): this() {
        init()
    }

    /**
     * Number of batches
     */
    var numberOfBatches by intParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (numberOfBatches == null && !hasParam("numberOfBatches")) {
            consumer.consumePropertyError("numberOfBatches", "mandatory 'numberOfBatches' property is not specified")
        }
    }
}


/**
 * @see ParallelTestsFeature
 */
fun BuildFeatures.parallelTests(init: ParallelTestsFeature.() -> Unit): ParallelTestsFeature {
    val result = ParallelTestsFeature(init)
    feature(result)
    return result
}
