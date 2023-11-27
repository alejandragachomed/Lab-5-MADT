package com.example.lab_5_madt
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class Parser {
    private val parsedDataList = mutableListOf<String>()

    fun parseXML(xmlData: String) {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlData.byteInputStream())
        doc.documentElement.normalize()

        val nodeList = doc.getElementsByTagName("item")

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Element.ELEMENT_NODE) {
                val element = node as Element
                val currencyName = getValue("targetCurrency", element)
                val currencyCode = getValue("targetName", element)
                val ratePerEuro = getValue("exchangeRate", element)

                parsedDataList.add("$currencyName ($currencyCode): $ratePerEuro")
            }
        }
    }

    private fun getValue(tagName: String, element: Element): String {
        val nodeList = element.getElementsByTagName(tagName).item(0).childNodes
        val node = nodeList.item(0)
        return node.nodeValue
    }

    fun getParsedDataList(): List<String> {
        return parsedDataList
    }
}