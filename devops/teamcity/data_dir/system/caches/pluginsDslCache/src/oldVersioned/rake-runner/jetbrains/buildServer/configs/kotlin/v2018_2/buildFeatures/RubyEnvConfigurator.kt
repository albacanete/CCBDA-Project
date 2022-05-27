package jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_2.*

/**
 * A [Ruby Environment Configurator](https://www.jetbrains.com/help/teamcity/?Ruby+Environment+Configurator)
 * build feature passes Ruby interpreter to all build steps.
 * 
 * @see rubyEnvConfigurator
 */
open class RubyEnvConfigurator() : BuildFeature() {

    init {
        type = "ruby.env.configurator"
    }

    constructor(init: RubyEnvConfigurator.() -> Unit): this() {
        init()
    }

    /**
     * Ruby Environment Configurator method.
     */
    var method by compoundParameter<Method>("ui.ruby.configurator.use.rvm")

    sealed class Method(value: String? = null): CompoundParam<Method>(value) {
        abstract fun validate(consumer: ErrorConsumer)

        class RubyInterpreter() : Method("") {

            /**
             * The path to Ruby interpreter.
             * If not specified, the interpreter will be searched in the PATH.
             * In this field you can use values of environment and system variables.
             */
            var path by stringParameter("ui.ruby.configurator.ruby.interpreter.path")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class InterpreterAndGemset() : Method("manual") {

            /**
             * E.g.: 'ruby-1.8.7-p249', 'jruby-1.4.0' or 'system'.
             */
            var interpreter by stringParameter("ui.ruby.configurator.rvm.sdk.name")

            /**
             * Leave empty to use default gemset.
             */
            var gemset by stringParameter("ui.ruby.configurator.rvm.gemset.name")

            /**
             * Create gemset if not exists.
             */
            var createGemsetIfNotExists by booleanParameter("ui.ruby.configurator.rvm.gemset.create.if.non.exists", trueValue = "true", falseValue = "")

            var requireRVM by booleanParameter("ui.ruby.configurator.rvm.path", trueValue = "%env.rvm_path%", falseValue = "")

            override fun validate(consumer: ErrorConsumer) {
                if (interpreter == null && !hasParam("ui.ruby.configurator.rvm.sdk.name")) {
                    consumer.consumePropertyError("method.interpreter", "mandatory 'method.interpreter' property is not specified")
                }
            }
        }

        class Rvmrc() : Method("rvmrc") {

            /**
             * Specify here the path to a .rvmrc file relative to the checkout directory.
             * If the file is specified, TeamCity will fetch environment variables using the rvm-shell
             * and will pass it to all build steps.
             */
            var path by stringParameter("ui.ruby.configurator.rvm.rvmrc.path")

            var requireRVM by booleanParameter("ui.ruby.configurator.rvm.path", trueValue = "%env.rvm_path%", falseValue = "")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class RvmConfigDirectory() : Method("rvm_ruby_version") {

            /**
             * Path to a directory with '.ruby-version' and '.ruby-gemset' files
             * relative to a checkout directory .
             * Leave empty to use checkout directory.
             */
            var path by stringParameter("ui.ruby.configurator.rvm.ruby_version.path")

            var requireRVM by booleanParameter("ui.ruby.configurator.rvm.path", trueValue = "%env.rvm_path%", falseValue = "")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class Rbenv() : Method("rbenv") {

            /**
             * E.g.: '1.9.3-p286' or 'jruby-1.7.0'
             */
            var interpreterVersion by stringParameter("ui.ruby.configurator.rbenv.version.name")

            var requireRbenv by booleanParameter("ui.ruby.configurator.rbenv.root.path", trueValue = "%env.RBENV_ROOT%", falseValue = "")

            override fun validate(consumer: ErrorConsumer) {
            }
        }

        class RbenvConfigDirectory() : Method("rbenv_file") {

            /**
             * Path to a directory with '.ruby-version' or '.rbenv-version' file
             * relative to a checkout directory.
             * Leave empty to use ".ruby-version"(preferred) or ".rbenv-version"
             */
            var path by stringParameter("ui.ruby.configurator.rbenv.file.path")

            var requireRbenv by booleanParameter("ui.ruby.configurator.rbenv.root.path", trueValue = "%env.RBENV_ROOT%", falseValue = "")

            override fun validate(consumer: ErrorConsumer) {
            }
        }
    }

    fun rubyInterpreter(init: Method.RubyInterpreter.() -> Unit = {}) : Method.RubyInterpreter {
        val result = Method.RubyInterpreter()
        result.init()
        return result
    }

    fun interpreterAndGemset(init: Method.InterpreterAndGemset.() -> Unit = {}) : Method.InterpreterAndGemset {
        val result = Method.InterpreterAndGemset()
        result.init()
        return result
    }

    fun rvmrc(init: Method.Rvmrc.() -> Unit = {}) : Method.Rvmrc {
        val result = Method.Rvmrc()
        result.init()
        return result
    }

    fun rvmConfigDirectory(init: Method.RvmConfigDirectory.() -> Unit = {}) : Method.RvmConfigDirectory {
        val result = Method.RvmConfigDirectory()
        result.init()
        return result
    }

    fun rbenv(init: Method.Rbenv.() -> Unit = {}) : Method.Rbenv {
        val result = Method.Rbenv()
        result.init()
        return result
    }

    fun rbenvConfigDirectory(init: Method.RbenvConfigDirectory.() -> Unit = {}) : Method.RbenvConfigDirectory {
        val result = Method.RbenvConfigDirectory()
        result.init()
        return result
    }

    /**
     * Whether fail a build if the Ruby environment configurator cannot pass the Ruby interpreter
     * to the step execution environment because the interpreter wasn't found on the agent.
     */
    var failIfInterpreterNotFound by booleanParameter("ui.ruby.configurator.fail.build.if.interpreter.not.found", trueValue = "true", falseValue = "")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (method == null && !hasParam("ui.ruby.configurator.use.rvm")) {
            consumer.consumePropertyError("method", "mandatory 'method' property is not specified")
        }
        method?.validate(consumer)
    }
}


/**
 * Adds a [Ruby Environment Configurator](https://www.jetbrains.com/help/teamcity/?Ruby+Environment+Configurator)
 * build feature passes Ruby interpreter to all build steps.
 * @see RubyEnvConfigurator
 */
fun BuildFeatures.rubyEnvConfigurator(init: RubyEnvConfigurator.() -> Unit): RubyEnvConfigurator {
    val result = RubyEnvConfigurator(init)
    feature(result)
    return result
}
