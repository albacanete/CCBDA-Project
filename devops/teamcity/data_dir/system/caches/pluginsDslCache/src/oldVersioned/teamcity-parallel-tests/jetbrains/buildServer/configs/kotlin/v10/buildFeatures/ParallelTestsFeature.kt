package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * [Parallel Tests Feature](https://www.jetbrains.com/help/teamcity/parallel-tests.html)
 * can automatically split tests of a build into several batches, by test classes. It will run them in parallel on suitable build agents and gather results in a composite build overview.
 * 
 * @see parallelTests
 */
open class ParallelTestsFeature : BuildFeature {
    constructor(init: ParallelTestsFeature.() -> Unit = {}, base: ParallelTestsFeature? = null): super(base = base as BuildFeature?) {
        type = "parallelTests"
        init()
    }

    /**
     * Number of batches
     */
    var numberOfBatches by intParameter()

}


/**
 * @see ParallelTestsFeature
 */
fun BuildFeatures.parallelTests(base: ParallelTestsFeature? = null, init: ParallelTestsFeature.() -> Unit = {}) {
    feature(ParallelTestsFeature(init, base))
}
