package jetbrains.buildServer.configs.kotlin.v10.buildSteps

import jetbrains.buildServer.configs.kotlin.v10.*

/**
 * A python build step
 * 
 * @see python
 */
open class PythonBuildStep : BuildStep {
    constructor(init: PythonBuildStep.() -> Unit = {}, base: PythonBuildStep? = null): super(base = base as BuildStep?) {
        type = "python-runner"
        init()
    }

    /**
     * [Build working directory](https://www.jetbrains.com/help/teamcity/?Build+Working+Directory) for python run,
     * specify it if it is different from the [checkout directory](https://www.jetbrains.com/help/teamcity/?Build+Checkout+Directory).
     */
    var workingDir by stringParameter("teamcity.build.workingDir")

    var pythonVersion by compoundParameter<PythonVersion>()

    sealed class PythonVersion(value: String? = null): CompoundParam(value) {
        class Python2() : PythonVersion("python2") {

            /**
             * Python interpreter arguments
             */
            var arguments by stringParameter()

        }

        class Python3() : PythonVersion("python3") {

            /**
             * Python interpreter arguments
             */
            var arguments by stringParameter()

        }

        class CustomPython() : PythonVersion("custom") {

            /**
             * Python interpreter arguments
             */
            var arguments by stringParameter()

            /**
             * Path to the Python executable
             */
            var executable by stringParameter("pythonExecutable")

        }
    }

    fun python2(init: PythonVersion.Python2.() -> Unit = {}) : PythonVersion.Python2 {
        val result = PythonVersion.Python2()
        result.init()
        return result
    }

    fun python3(init: PythonVersion.Python3.() -> Unit = {}) : PythonVersion.Python3 {
        val result = PythonVersion.Python3()
        result.init()
        return result
    }

    fun customPython(init: PythonVersion.CustomPython.() -> Unit = {}) : PythonVersion.CustomPython {
        val result = PythonVersion.CustomPython()
        result.init()
        return result
    }

    var environment by compoundParameter<Environment>("envTool")

    sealed class Environment(value: String? = null): CompoundParam(value) {
        class Virtualenv() : Environment("virtualenv") {

            /**
             * Files with packages requirements
             */
            var requirementsFile by stringParameter("virtualenvFile")

            /**
             * Name of environment
             */
            var name by stringParameter("virtualenvEnvName")

            /**
             * Additional arguments for pip run
             */
            var pipArgs by stringParameter("virtualenvPipArgs")

            /**
             * Additional arguments for creating virtual environment
             */
            var virtualenvArgs by stringParameter()

        }

        class Venv() : Environment("venv") {

            /**
             * Files with packages requirements
             */
            var requirementsFile by stringParameter("virtualenvFile")

            /**
             * Name of environment
             */
            var name by stringParameter("virtualenvEnvName")

            /**
             * Additional arguments for pip run
             */
            var pipArgs by stringParameter("virtualenvPipArgs")

            /**
             * Additional arguments for creating virtual environment
             */
            var virtualenvArgs by stringParameter()

        }

        class Pipenv() : Environment("pipenv") {

            /**
             * Additional arguments for pipenv install run
             */
            var arguments by stringParameter("pipenvArgs")

        }

        class Poetry() : Environment("poetry") {

            /**
             * Additional arguments for poetry install run
             */
            var installArgs by stringParameter("poetryInstallArgs")

            /**
             * Poetry executable path
             */
            var executable by stringParameter("poetryExecutable")

        }

        class None() : Environment("none") {

        }
    }

    fun virtualenv(init: Environment.Virtualenv.() -> Unit = {}) : Environment.Virtualenv {
        val result = Environment.Virtualenv()
        result.init()
        return result
    }

    fun venv(init: Environment.Venv.() -> Unit = {}) : Environment.Venv {
        val result = Environment.Venv()
        result.init()
        return result
    }

    fun pipenv(init: Environment.Pipenv.() -> Unit = {}) : Environment.Pipenv {
        val result = Environment.Pipenv()
        result.init()
        return result
    }

    fun poetry(init: Environment.Poetry.() -> Unit = {}) : Environment.Poetry {
        val result = Environment.Poetry()
        result.init()
        return result
    }

    fun none() = Environment.None()

    var command by compoundParameter<Command>()

    sealed class Command(value: String? = null): CompoundParam(value) {
        class Custom() : Command("custom") {

            /**
             * Command Python arguments (e.g modules, parameters)
             */
            var arguments by stringParameter("innerArguments")

        }

        class Module() : Command("module") {

            /**
             * Python module
             */
            var module by stringParameter()

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }

        class File() : Command("file") {

            /**
             * Python script file
             */
            var filename by stringParameter("scriptFile")

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }

        class Script() : Command("script") {

            /**
             * Custom Python script
             */
            var content by stringParameter("scriptContent")

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }

        class Pylint() : Command("pylint") {

            /**
             * Autoinstall the tool on command run
             */
            var installToolPackage by booleanParameter(trueValue = "true", falseValue = "")

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }

        class Flake8() : Command("flake") {

            /**
             * Autoinstall the tool on command run
             */
            var installToolPackage by booleanParameter(trueValue = "true", falseValue = "")

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }

        class Unittest() : Command("unittest") {

            /**
             * Enable test reporting via teamcity-messages
             */
            var isTestReportingEnabled by booleanParameter(trueValue = "true", falseValue = "")

            /**
             * Enable coverage collecting via coverage.py
             */
            var isCoverageEnabled by booleanParameter()

            /**
             * Additional arguments for coverage run
             */
            var coverageArgs by stringParameter()

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }

        class Pytest() : Command("pytest") {

            /**
             * Autoinstall the tool on command run
             */
            var installToolPackage by booleanParameter(trueValue = "true", falseValue = "")

            /**
             * Enable test reporting via teamcity-messages
             */
            var isTestReportingEnabled by booleanParameter(trueValue = "true", falseValue = "")

            /**
             * Enable coverage collecting via coverage.py
             */
            var isCoverageEnabled by booleanParameter()

            /**
             * Additional arguments for coverage run
             */
            var coverageArgs by stringParameter()

            /**
             * A space-separated list of arguments that will be passed to the user script or module after their call.
             * To pass an argument containing whitespaces, wrap it in double quotes.
             */
            var scriptArguments by stringParameter()

        }
    }

    fun custom(init: Command.Custom.() -> Unit = {}) : Command.Custom {
        val result = Command.Custom()
        result.init()
        return result
    }

    fun module(init: Command.Module.() -> Unit = {}) : Command.Module {
        val result = Command.Module()
        result.init()
        return result
    }

    fun file(init: Command.File.() -> Unit = {}) : Command.File {
        val result = Command.File()
        result.init()
        return result
    }

    fun script(init: Command.Script.() -> Unit = {}) : Command.Script {
        val result = Command.Script()
        result.init()
        return result
    }

    fun pylint(init: Command.Pylint.() -> Unit = {}) : Command.Pylint {
        val result = Command.Pylint()
        result.init()
        return result
    }

    fun flake8(init: Command.Flake8.() -> Unit = {}) : Command.Flake8 {
        val result = Command.Flake8()
        result.init()
        return result
    }

    fun unittest(init: Command.Unittest.() -> Unit = {}) : Command.Unittest {
        val result = Command.Unittest()
        result.init()
        return result
    }

    fun pytest(init: Command.Pytest.() -> Unit = {}) : Command.Pytest {
        val result = Command.Pytest()
        result.init()
        return result
    }

    /**
     * Specifies which Docker image platform will be used to run this build step.
     */
    var dockerImagePlatform by enumParameter<ImagePlatform>("plugin.docker.imagePlatform", mapping = ImagePlatform.mapping)

    /**
     * If enabled, "docker pull [image][dockerImage]" will be run before docker run.
     */
    var dockerPull by booleanParameter("plugin.docker.pull.enabled", trueValue = "true", falseValue = "")

    /**
     * Specifies which Docker image to use for running this build step. I.e. the build step will be run inside specified docker image, using 'docker run' wrapper.
     */
    var dockerImage by stringParameter("plugin.docker.imageId")

    /**
     * Additional docker run command arguments
     */
    var dockerRunParameters by stringParameter("plugin.docker.run.parameters")

    /**
     * Docker image platforms
     */
    enum class ImagePlatform {
        Any,
        Linux,
        Windows;

        companion object {
            val mapping = mapOf<ImagePlatform, String>(Any to "", Linux to "linux", Windows to "windows")
        }

    }
}


/**
 * Adds a python build step
 * @see PythonBuildStep
 */
fun BuildSteps.python(base: PythonBuildStep? = null, init: PythonBuildStep.() -> Unit = {}) {
    step(PythonBuildStep(init, base))
}
