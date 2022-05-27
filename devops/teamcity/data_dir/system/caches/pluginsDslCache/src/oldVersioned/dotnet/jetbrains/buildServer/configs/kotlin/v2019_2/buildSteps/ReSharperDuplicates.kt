package jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps

import jetbrains.buildServer.configs.kotlin.v2019_2.*

/**
 * A [Duplicates finder (ReSharper) build step](https://www.jetbrains.com/help/teamcity/?duplicates-finder-resharper)
 * to find C# and VB duplicate code.
 * 
 * @see reSharperDuplicates
 */
open class ReSharperDuplicates() : BuildStep() {

    init {
        type = "dotnet-tools-dupfinder"
    }

    constructor(init: ReSharperDuplicates.() -> Unit): this() {
        init()
    }

    /**
     * Use newline-delimited Ant-like wildcards relative to the checkout root
     * to specify the files to be included into the duplicates search.
     * Visual Studio solution files are parsed and replaced by all source files
     * from all projects within a solution.
     * Example: src\MySolution.sln
     */
    var includeFiles by stringParameter("dotnet-tools-dupfinder.include_files")

    /**
     * Enter newline-delimited Ant-like wildcards to exclude files from the duplicates search
     * (for example, generated{*}{}.cs). The entries should be relative to the checkout root.
     */
    var excludeFiles by stringParameter("dotnet-tools-dupfinder.exclude_files")

    /**
     * A custom path to R# CLT Home Directory. Paths relative to the checkout directory are supported.
     * The value can reference to JetBrains ReSharper Command Line Tool specified via Administration | Tools.
     */
    var cltPath by stringParameter("jetbrains.resharper-clt.clt-path")

    /**
     * The platform bitness of the dupFinder tool. By default, x64.
     * The cross-platform duplicates finder is also supported in ReSharper 2020.2.1 or later.
     */
    var cltPlatform by enumParameter<Platform>("jetbrains.resharper-clt.platform", mapping = Platform.mapping)

    /**
     * If this option is checked, similar contents with different
     * namespace specifications will be recognized as duplicates.
     */
    var discardNamespaces by booleanParameter("dotnet-tools-dupfinder.hashing.normalize_types", trueValue = "true", falseValue = "")

    /**
     * If this option is checked, similar content with different type names will be recognized as duplicates.
     * These include all possible type references.
     */
    var discardTypesName by booleanParameter("dotnet-tools-dupfinder.hashing.discard_types", trueValue = "true", falseValue = "")

    /**
     * If this option is checked, the similar code fragments with
     * different field names will be recognized as duplicates.
     */
    var discardFieldsName by booleanParameter("dotnet-tools-dupfinder.hashing.discard_fields_name", trueValue = "true", falseValue = "")

    /**
     * If this option is checked, similar code fragments with
     * different local variable names will be recognized as duplicates.
     */
    var discardLocalVariablesName by booleanParameter("dotnet-tools-dupfinder.hashing.discard_local_variables_name", trueValue = "true", falseValue = "")

    /**
     * If this option is checked, similar lines of code with
     * different literals will be recognized as duplicates.
     */
    var discardLiterals by booleanParameter("dotnet-tools-dupfinder.hashing.discard_literals", trueValue = "true", falseValue = "")

    /**
     * Ignore duplicates with complexity lower than given value.
     * Use this field to specify the lowest level of complexity of code blocks
     * to be taken into consideration when detecting duplicates.
     * Positive numbers and parameter references are supported.
     */
    var discardCost by stringParameter("dotnet-tools-dupfinder.discard_cost")

    /**
     * Skip files by opening comment. Enter newline-delimited keywords to exclude files
     * that contain the keyword in the file's opening comments from the duplicates search.
     */
    var excludeOpeningComment by stringParameter("dotnet-tools-dupfinder.exclude_by_opening_comment")

    /**
     * Skip regions by message substring. Enter newline-delimited keywords
     * that exclude regions that contain the keyword in the message substring from the duplicates search.
     * Entering "generated code", for example,
     * will skip regions containing "Windows Form Designer generated code".
     */
    var excludeRegionMessageSubstring by stringParameter("dotnet-tools-dupfinder.exclude_region_message_substring")

    /**
     * Set true to include debug messages in the build log and publish
     * the file with additional logs (dotnet-tools-dupfinder.log) as an artifact
     */
    var debugOutput by booleanParameter("dotnet-tools-dupfinder.debug", trueValue = "true", falseValue = "")

    /**
     * Specify newline-separated command line parameters to add to calling dupFinder.exe.
     */
    var customCmdArgs by stringParameter("dotnet-tools-dupfinder.customCmdArgs")

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

    enum class Platform {
        X64,
        CROSS_PLATFORM;

        companion object {
            val mapping = mapOf<Platform, String>(X64 to "x64", CROSS_PLATFORM to "Cross-platform")
        }

    }
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
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds a [Duplicates finder (ReSharper) build step](https://www.jetbrains.com/help/teamcity/?duplicates-finder-resharper)
 * to find C# and VB duplicate code.
 * @see ReSharperDuplicates
 */
fun BuildSteps.reSharperDuplicates(init: ReSharperDuplicates.() -> Unit): ReSharperDuplicates {
    val result = ReSharperDuplicates(init)
    step(result)
    return result
}
