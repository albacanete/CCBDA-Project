package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * [Golang feature](https://www.jetbrains.com/help/teamcity/golang.html)
 * processing Golang tests
 * 
 * @see golang
 */
open class GolangFeature() : BuildFeature() {

    init {
        type = "golang"
    }

    constructor(init: GolangFeature.() -> Unit): this() {
        init()
    }

    /**
     * The output format of the test Golang to processing
     */
    var testFormat by stringParameter("test.format")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (testFormat == null && !hasParam("test.format")) {
            consumer.consumePropertyError("testFormat", "mandatory 'testFormat' property is not specified")
        }
    }
}


/**
 * @see golang
 */
fun BuildFeatures.golang(init: GolangFeature.() -> Unit): GolangFeature {
    val result = GolangFeature(init)
    feature(result)
    return result
}
