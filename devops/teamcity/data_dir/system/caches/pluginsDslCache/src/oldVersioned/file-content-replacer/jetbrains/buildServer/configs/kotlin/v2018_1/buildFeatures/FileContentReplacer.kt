package jetbrains.buildServer.configs.kotlin.v2018_1.buildFeatures

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * A [build feature](https://www.jetbrains.com/help/teamcity/?File+Content+Replacer) which processes text files by performing regular expressions
 * 
 * @see replaceContent
 */
open class FileContentReplacer() : BuildFeature() {

    init {
        type = "JetBrains.FileContentReplacer"
    }

    constructor(init: FileContentReplacer.() -> Unit): this() {
        init()
    }

    /**
     * Comma- or newline-separated set of rules in the form of ```+|-:[path relative to the checkout directory]```,
     * wildcards are supported
     */
    var fileRules by stringParameter("teamcity.file.content.replacer.wildcards")

    /**
     * Pattern to search for, either in the [regular expression]
     * [java.util.regex.Pattern] (default) or in the [fixed strings]
     * [RegexMode.FIXED_STRINGS] format, depending on the [regexMode].
     */
    var pattern by stringParameter("teamcity.file.content.replacer.pattern")

    /**
     * Whether the search should be case-sensitive. Enabled by default.
     * A value of ```false``` sets [CASE_INSENSITIVE]
     * [java.util.regex.Pattern.CASE_INSENSITIVE] and [UNICODE_CASE]
     * [java.util.regex.Pattern.UNICODE_CASE] flags.
     * Set to ```false``` for case-insensitive languages (e.g. _Visual Basic_).
     */
    var caseSensitivePattern by booleanParameter("teamcity.file.content.replacer.pattern.case.sensitive")

    /**
     * Enables/disables build failure in case there are no files matching the specified file pattern. Enabled by default.
     */
    var failBuildIfNoFilesMatchPattern by booleanParameter("teamcity.file.content.replacer.failBuild")

    /**
     * Controls the way the search pattern and the replacement text
     * are interpreted. The default is [RegexMode.REGEX]. Set to
     * [RegexMode.FIXED_STRINGS] to perform a _fixed strings_ search.
     * @since 2017.1
     */
    var regexMode by enumParameter<RegexMode>("teamcity.file.content.replacer.regexMode")

    /**
     * Replacement text.
     * In [REGEX][RegexMode.REGEX] [mode][regexMode], **```$N```** sequence
     * references **```N```**-th capturing group. All backslashes (**```\```**)
     * and dollar signs (**```$```**) without a special meaning should be
     * quoted (as **```\\```** and **```\$```**, respectively).
     * In [REGEX_MIXED][RegexMode.REGEX_MIXED] and [FIXED_STRINGS]
     * [RegexMode.FIXED_STRINGS] modes, backslashes (**```\```**) and dollar
     * signs (**```$```**) have no special meaning.
     */
    var replacement by stringParameter("teamcity.file.content.replacer.replacement")

    /**
     * A file encoding to use
     * @see FileEncoding
     * @see customEncodingName
     */
    var encoding by enumParameter<FileEncoding>("teamcity.file.content.replacer.file.encoding", mapping = FileEncoding.mapping)

    /**
     * A name of the custom encoding to use, works when encoding is set to FileEncoding.CUSTOM
     * @see encoding
     */
    var customEncodingName by stringParameter("teamcity.file.content.replacer.file.encoding.custom")

    /**
     * Controls the way the search pattern and the replacement text
     * are interpreted.
     * @since 2017.1
     */
    enum class RegexMode {
        /**
         * Treat both the search pattern and the replacement text as
         * fixed strings, similarly to ```grep -F|--fixed-strings```.
         * [LITERAL][java.util.regex.Pattern.LITERAL] mode is on.
         * Equivalent to un-checking the **Regex** box in the _File Content
         * Replacer_ UI.
         */
        FIXED_STRINGS,
        /**
         * Treat both the search pattern and the replacement text as
         * regular expressions. Trailing ```\``` or single ```$``` characters
         * are not allowed in the replacement text.
         * [MULTILINE][java.util.regex.Pattern.MULTILINE] mode is on by default.
         * Equivalent to checking the **Regex** box in the _File Content
         * Replacer_ UI.
         * This is the only mode in _TeamCity_ 9.1 and 10.0 and the default one
         * since 2017.1.
         */
        REGEX,
        /**
         * Treat the search pattern as a regular expression (similarly
         * to [REGEX]), but [quote][java.util.regex.Matcher.quoteReplacement] the
         * replacement text (as in [FIXED_STRINGS] mode). ```\``` and ```$```
         * characters have no special meaning.
         * [MULTILINE][java.util.regex.Pattern.MULTILINE] mode is on by default.
         * Useful if the replacement text contains ```%build.parameters%``` which
         * may be expanded to an arbitrary value.
         * This mode is not available via the _TeamCity_ UI.
         */
        REGEX_MIXED;

    }
    /**
     * File Encoding
     */
    enum class FileEncoding {
        /**
         * File encoding is automatically detected
         */
        AUTODETECT,
        /**
         * The 'US-ASCII' encoding
         */
        ASCII,
        /**
         * The 'UTF-8' encoding
         */
        UTF_8,
        /**
         * The 'UTF-16BE' encoding
         */
        UTF_16BE,
        /**
         * The 'UTF-16LE' encoding
         */
        UTF_16LE,
        /**
         * A custom encoding, the encoding name should be specified in the customEncodingName
         */
        CUSTOM;

        companion object {
            val mapping = mapOf<FileEncoding, String>(AUTODETECT to "autodetect", ASCII to "US-ASCII", UTF_8 to "UTF-8", UTF_16BE to "UTF-16BE", UTF_16LE to "UTF-16LE", CUSTOM to "custom")
        }

    }
    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
        if (fileRules == null && !hasParam("teamcity.file.content.replacer.wildcards")) {
            consumer.consumePropertyError("fileRules", "mandatory 'fileRules' property is not specified")
        }
        if (pattern == null && !hasParam("teamcity.file.content.replacer.pattern")) {
            consumer.consumePropertyError("pattern", "mandatory 'pattern' property is not specified")
        }
        if (replacement == null && !hasParam("teamcity.file.content.replacer.replacement")) {
            consumer.consumePropertyError("replacement", "mandatory 'replacement' property is not specified")
        }
    }
}


/**
 * Adds a [build feature](https://www.jetbrains.com/help/teamcity/?File+Content+Replacer) which processes text files by performing regular expressions
 * @see FileContentReplacer
 */
fun BuildFeatures.replaceContent(init: FileContentReplacer.() -> Unit): FileContentReplacer {
    val result = FileContentReplacer(init)
    feature(result)
    return result
}
