package jetbrains.buildServer.configs.kotlin.v10.buildFeatures

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * [Golang feature](https://www.jetbrains.com/help/teamcity/golang.html)
 * processing Golang tests
 * 
 * @see golang
 */
open class GolangFeature : BuildFeature {
    constructor(init: GolangFeature.() -> Unit = {}, base: GolangFeature? = null): super(base = base as BuildFeature?) {
        type = "golang"
        init()
    }

    /**
     * The output format of the test Golang to processing
     */
    var testFormat by stringParameter("test.format")

}


/**
 * @see golang
 */
fun BuildFeatures.golang(base: GolangFeature? = null, init: GolangFeature.() -> Unit = {}) {
    feature(GolangFeature(init, base))
}
