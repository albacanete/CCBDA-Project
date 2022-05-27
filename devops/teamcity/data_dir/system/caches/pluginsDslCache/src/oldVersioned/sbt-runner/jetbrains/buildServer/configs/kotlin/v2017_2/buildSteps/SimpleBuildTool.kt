package jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2017_2.*

/**
 * A [Simple Build Tool (Scala)](https://www.jetbrains.com/help/teamcity/?Simple+Build+Tool+(Scala))
 * build step running SBT builds.
 * 
 * @see simpleBuildTool
 */
open class SimpleBuildTool() : BuildStep() {

    init {
        type = "SBT"
    }

    constructor(init: SimpleBuildTool.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for python run,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    /**
     * Commands to execute, e.g. 'clean compile test' or
     * ';clean;set scalaVersion:="2.11.6";compile;test' for commands containing quotes.
     */
    var commands by stringParameter("sbt.args")

    var installationMode by compoundParameter<InstallationMode>("sbt.installationMode")

    sealed class InstallationMode(value: String? = null): CompoundParam<InstallationMode>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Auto() : InstallationMode("auto") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class Custom() : InstallationMode("custom") {

            /**
             * The path to the existing SBT home directory.
             */
            var sbtHome by stringParameter("sbt.home")

            override fun validate(consumer: ErrorConsumer) {
                if (sbtHome == null && !hasParam("sbt.home")) {
                    consumer.consumePropertyError("installationMode.sbtHome", "mandatory 'installationMode.sbtHome' property is not specified")
                }
            }
        }
    }

    /**
     * TeamCity bundled SBT launcher will be used.
     */
    fun auto() = InstallationMode.Auto()

    /**
     * The installed SBT will the launched from the SBT home (sbtHome).
     */
    fun custom(init: InstallationMode.Custom.() -> Unit = {}) : InstallationMode.Custom {
        val result = InstallationMode.Custom()
        result.init()
        return result
    }

    /**
     * Specify the path to your custom JDK which will be used to run the build.
     * The default is JAVA_HOME environment variable or the agent's own Java.
     * The value could reference to some environment variable, e.g. "%env.JDK_18%".
     */
    var jdkHome by stringParameter("target.jdk.home")

    /**
     * Specify the desired Java Virtual Machine parameters,
     * such as maximum heap size or parameters that enable remote debugging.
     * These settings are passed to the JVM used to run your build.
     */
    var jvmArgs by stringParameter()

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        installationMode?.validate(consumer)
    }
}


/**
 * Adds a [Simple Build Tool (Scala)](https://www.jetbrains.com/help/teamcity/?Simple+Build+Tool+(Scala))
 * build step running SBT builds.
 * @see SimpleBuildTool
 */
fun BuildSteps.simpleBuildTool(init: SimpleBuildTool.() -> Unit): SimpleBuildTool {
    val result = SimpleBuildTool(init)
    step(result)
    return result
}
