package jetbrains.buildServer.configs.kotlin.buildSteps

import jetbrains.buildServer.configs.kotlin.*

/**
 * A [Rake](https://www.jetbrains.com/help/teamcity/?Rake) build step.
 * 
 * @see rake
 */
open class RakeStep() : BuildStep() {

    init {
        type = "rake-runner"
    }

    constructor(init: RakeStep.() -> Unit): this() {
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for ant script,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    var rakefile by compoundParameter<Rakefile>("use-custom-build-file")

    sealed class Rakefile(value: String? = null): CompoundParam<Rakefile>(value) {
        class File() : Rakefile("") {

            /**
             * Enter a Rakefile path if you don't want to use the default one.
             * The specified path should be relative to the Build Checkout Directory.
             */
            var path by stringParameter("build-file-path")

        }

        class Content() : Rakefile("true") {

            /**
             * Rakefile content. The new Rakefile will be created dynamically from the specified content before running Rake.
             */
            var content by stringParameter("build-file")

        }
    }

    fun file(init: Rakefile.File.() -> Unit = {}) : Rakefile.File {
        val result = Rakefile.File()
        result.init()
        return result
    }

    fun content(init: Rakefile.Content.() -> Unit = {}) : Rakefile.Content {
        val result = Rakefile.Content()
        result.init()
        return result
    }

    /**
     * Space-separated tasks names if you don't want to use the default task.
     * For example, test:functionals or mytask:test mytask:test2.
     */
    var tasks by stringParameter("ui.rakeRunner.rake.tasks.names")

    /**
     * Specified parameters will be added to rake command line.
     */
    var rakeAdditionalParameters by stringParameter("ui.rakeRunner.additional.rake.cmd.params")

    var rubyInterpreterMode by compoundParameter<RubyInterpreterMode>("ui.rakeRunner.ruby.use.mode")

    sealed class RubyInterpreterMode(value: String? = null): CompoundParam<RubyInterpreterMode>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class Default() : RubyInterpreterMode("default") {

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class Path() : RubyInterpreterMode("path") {

            /**
             * The path to Ruby interpreter. The path cannot be empty.
             * This field supports values of environment and system variables.
             * For example: "%env.I_AM_DEFINED_IN_BUILDAGENT_CONFIGURATION%"
             */
            var path by stringParameter("ui.rakeRunner.ruby.interpreter.path")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class Rvm() : RubyInterpreterMode("rvm") {

            /**
             * E.g.: 'ruby-1.8.7-p249', 'jruby-1.4.0' or 'system'
             */
            var rvmInterpreter by stringParameter("ui.rakeRunner.ruby.rvm.sdk.name")

            /**
             * A gemset configured on a build agent.
             * If gemset isn't specified, the default one will be used.
             */
            var gemset by stringParameter("ui.rakeRunner.ruby.rvm.gemset.name")

            override fun validate(consumer: ErrorConsumer) {
                if (rvmInterpreter == null && !hasParam("ui.rakeRunner.ruby.rvm.sdk.name")) {
                    consumer.consumePropertyError("rubyInterpreterMode.rvmInterpreter", "mandatory 'rubyInterpreterMode.rvmInterpreter' property is not specified")
                }
            }
        }
    }

    /**
     * Use Ruby interpreter settings defined in the Ruby environment configurator build feature settings
     * or the interpreter will be searched in the PATH.
     */
    fun default() = RubyInterpreterMode.Default()

    fun path(init: RubyInterpreterMode.Path.() -> Unit = {}) : RubyInterpreterMode.Path {
        val result = RubyInterpreterMode.Path()
        result.init()
        return result
    }

    fun rvm(init: RubyInterpreterMode.Rvm.() -> Unit = {}) : RubyInterpreterMode.Rvm {
        val result = RubyInterpreterMode.Rvm()
        result.init()
        return result
    }

    /**
     * If your project uses the Bundler requirements manager and your Rakefile doesn't load the bundler setup script,
     * this option will allow you to launch rake tasks using the bundle exec command emulation.
     * If you want to execute bundle install command, you need to do it in the Command Line step before the Rake step.
     * Also, remember to set up the Ruby environment configurator build feature to automatically pass Ruby interpreter to the command line runner.
     */
    var execBundle by booleanParameter("ui.rakeRunner.bunlder.exec.enabled", trueValue = "true", falseValue = "")

    /**
     * Whether to enable showing Invoke stage data in the build log.
     */
    var trackInvokeExecuteStages by booleanParameter("ui.rakeRunner.rake.trace.invoke.exec.stages.enabled", trueValue = "true", falseValue = "")

    /**
     * Additional parameters for interpreter, useful for JRuby interpreters. E.g. '-J-Xmx512m'
     */
    var interpreterAdditionalParameters by stringParameter("ui.rakeRunner.ruby.interpreter.additional.params")

    /**
     * Whether to attach Test::Unit framework results to Tests tab of the Build Results page.
     */
    var enableTestUnit by booleanParameter("ui.rakeRunner.frameworks.testunit.enabled", trueValue = "true", falseValue = "")

    /**
     * Whether to attach Test-Spec framework results to Tests tab of the Build Results page.
     */
    var enableTestSpec by booleanParameter("ui.rakeRunner.frameworks.testspec.enabled", trueValue = "true", falseValue = "")

    /**
     * Whether to attach Shoulda framework results to Tests tab of the Build Results page.
     */
    var enableShoulda by booleanParameter("ui.rakeRunner.frameworks.shoulda.enabled", trueValue = "true", falseValue = "")

    /**
     * Whether to attach RSpec framework results to Tests tab of the Build Results page.
     */
    var enableRSpec by booleanParameter("ui.rakeRunner.frameworks.rspec.enabled", trueValue = "true", falseValue = "")

    /**
     * Rake will be invoked with a "SPEC_OPTS={internal options} {user options}".
     */
    var rspecSpecOptions by stringParameter("ui.rakeRunner.rspec.specoptions")

    /**
     * Whether to attach Cucumber framework results to Tests tab of the Build Results page.
     */
    var enableCucumber by booleanParameter("ui.rakeRunner.frameworks.cucumber.enabled", trueValue = "true", falseValue = "")

    /**
     * Rake will be invoked with a "CUCUMBER_OPTS={internal options} {user options}".
     */
    var cucumberOptions by stringParameter("ui.rakeRunner.cucumber.options")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        rubyInterpreterMode?.validate(consumer)
    }
}


/**
 * Add s [Rake](https://www.jetbrains.com/help/teamcity/?Rake) build step.
 * @see RakeStep
 */
fun BuildSteps.rake(init: RakeStep.() -> Unit): RakeStep {
    val result = RakeStep(init)
    step(result)
    return result
}
