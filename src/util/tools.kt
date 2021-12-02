package main.util

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

fun sumList(l1 : List<Int>, l2 : List<Int>) : List<Int> {
    return listOf(l1[0] + l2[0], l1[1] + l2[1])
}

fun loadFileAsStream(fileName : String) : InputStreamReader {
    return File(fileName).reader(Charset.forName("ASCII"))
}

fun convertStringToStream(data : String) : InputStreamReader {
    var byteArrayInputStream = ByteArrayInputStream(data.toByteArray(Charset.defaultCharset()))
    return InputStreamReader(byteArrayInputStream)
}