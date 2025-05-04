package com.king.logx.util

import com.king.logx.LogX
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 用于数据格式化的工具类，例如：格式化JSON、XML
 *
 * Utility class for data formatting (e.g., JSON, XML).
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object FormatUtils {

    private const val DEFAULT_INDENT_SPACES = 4
    private const val JSON_OBJECT_PREFIX = '{'
    private const val JSON_ARRAY_PREFIX = '['
    private const val YES = "yes"
    private const val XSLT_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount"

    private val transformerFactory by lazy {
        TransformerFactory.newInstance()
    }

    /**
     * 格式化`JSON`字符串
     *
     * Format JSON string.
     *
     * @param json Input JSON string.
     * @param indentSpaces The number of spaces to indent for each level of nesting. Default 4
     */
    @JvmOverloads
    @JvmStatic
    fun formatJson(json: String, indentSpaces: Int = DEFAULT_INDENT_SPACES): String {
        if (json.isBlank()) return json

        return try {
            json.trim().let {
                when(it.first()) {
                    JSON_OBJECT_PREFIX -> JSONObject(it).toString(indentSpaces)
                    JSON_ARRAY_PREFIX -> JSONArray(it).toString(indentSpaces)
                    else -> throw JSONException("Invalid JSON.")
                }
            }
        } catch (e: JSONException) {
            LogX.w(e, "JSON formatting failed.")
            json
        }
    }

    /**
     * 格式化`XML`字符串
     *
     * Format XML string.
     *
     * @param xml Input XML string.
     * @param indentSpaces The number of spaces to indent for each level of nesting. Default 4
     */
    @JvmOverloads
    @JvmStatic
    fun formatXml(xml: String, indentSpaces: Int = DEFAULT_INDENT_SPACES): String {
        if (xml.isBlank()) return xml

        return try {
            StringWriter().use { writer ->
                transformerFactory.newTransformer().apply {
                    setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, YES)
                    setOutputProperty(OutputKeys.INDENT, YES)
                    setOutputProperty(XSLT_INDENT_AMOUNT, indentSpaces.toString())
                }.transform(StreamSource(StringReader(xml)), StreamResult(writer))
                writer.toString().trim()
            }
        } catch (e: TransformerException) {
            LogX.w(e, "XML transformation failed.")
            xml
        }
    }
}
