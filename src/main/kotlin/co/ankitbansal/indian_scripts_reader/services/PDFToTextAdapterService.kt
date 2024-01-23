package co.ankitbansal.indian_scripts_reader.services

import co.ankitbansal.indian_scripts_reader.controller.PDFRequestBody
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class PDFToTextAdapterService {

    fun getText(resource: ClassPathResource = ClassPathResource("nirakaar-mann-hindi (1).pdf")): String {
        val inputStream: InputStream = resource.getInputStream()
        return getText(
            inputStream = inputStream,
            all = true,
            ignoreText = listOf(),
        )
    }

    fun exampleConversion() {
        val fileurl = "https://drive.google.com/uc?export=download&id=1ptThpoT3qgws84zqtRi_GnWvSrq95X-U"
        // 40 साहिब बन्दगी
        // आवे न जावे मरे न जन्मे सोई सत्यपुरुष हमारा है 39
        val ignoreText = listOf("आवे न जावे मरे न जन्मे सोई सत्यपुरुष हमारा है", "साहिब बन्दगी")

        saveInPath(
            body = PDFRequestBody(
                url = fileurl,
                all = true,
                ignoreText = ignoreText,
                title = "nirakaar-mann-hindi"
            ),

            )

    }

    fun getText(body: PDFRequestBody): String {
        val website = URL(body.url)
        return getText(
            inputStream = website.openStream(),
            all = body.all,
            pageEnd = body.pageEnd,
            pageStart = body.pageStart,
            ignoreText = body.ignoreText,
        )
    }

    fun downloadAndSaveInPath(
        body: PDFRequestBody,
        localPath: String = "C:\\Users\\Ankit Bansal\\IdeaProjects\\indian_scripts_reader\\assets"
    ) {
        val website = URL(body.url)
        val pdfFile = "$localPath\\${body.title}.pdf"
        downloadFile(website, pdfFile)
        val text = getText(
            inputStream = FileInputStream(pdfFile),
            all = body.all,
            pageEnd = body.pageEnd,
            pageStart = body.pageStart,
            ignoreText = body.ignoreText,
        )
        val fullText =
            StringBuilder("[//]: #(" + body.url + ")\n#" + body.title + "\n${body.ignoreText.joinToString(" ")}\n" + text)
        val path: Path =
            Paths.get("$localPath\\${body.title}.md")
        Files.write(path, fullText.toString().toByteArray(StandardCharsets.UTF_16))
    }

    fun saveInPath(
        body: PDFRequestBody,
        localPath: String = "C:\\Users\\Ankit Bansal\\IdeaProjects\\indian_scripts_reader\\assets"
    ) {
        val website = URL(body.url)
        val text = getText(
            inputStream = website.openStream(),
            all = body.all,
            pageEnd = body.pageEnd,
            pageStart = body.pageStart,
            ignoreText = body.ignoreText,
        )
        val fullText =
            StringBuilder("[//]: #(" + body.url + ")\n#" + body.title + "\n${body.ignoreText.joinToString(" ")}\n" + text)
        val path: Path =
            Paths.get("$localPath\\${body.title}.txt")
        Files.write(path, fullText.toString().toByteArray(StandardCharsets.UTF_16))
    }

    fun getFromLocalPath(
        path: String,
        all: Boolean = true,
        pageStart: Int = 0,
        pageEnd: Int = 200,
        ignoreText: List<String> = listOf(),
        lang: String = "hin"
    ): String {
        val inputStream: InputStream = FileInputStream(path)
        return getText(
            inputStream = inputStream,
            all = all,
            pageEnd = pageEnd,
            pageStart = pageStart,
            ignoreText = ignoreText,
        )
    }

    // hin+eng
    fun getText(
        inputStream: InputStream,
        all: Boolean = true,
        pageStart: Int = 0,
        pageEnd: Int = 200,
        ignoreText: List<String> = listOf(),
        lang: String = "hin"
    ): String {
        try {
            val document: PDDocument = PDDocument.load(inputStream)
            val pdfRenderer = PDFRenderer(document)
            var i = 0
            var fileTextData = StringBuilder()
            var start = 0
            var end = document.numberOfPages
            if (!all) {
                start = pageStart
                end = pageEnd
            }
            for (page in start until end) {
                val bim = pdfRenderer.renderImageWithDPI(page, 400F, ImageType.RGB)
                val tesseract = Tesseract()
                tesseract.setDatapath("src/main/resources/tessdata")
                tesseract.setLanguage(lang) //+eng
                var text = tesseract.doOCR(bim)
                var splitTextArr = text.split('\n')
                var isIgnore = false
                for (k in ignoreText) {
                    if (splitTextArr[0].contains(k)) {
                        isIgnore = true
                    }
                }
//                val ignoredTexts = ignoreText.filter { it in splitTextArr[0] }
//                println("${ignoreText.size} $ignoreText")
//                println("${ignoredTexts.size} ${String(splitTextArr[0].toByteArray(StandardCharsets.UTF_16),StandardCharsets.UTF_16)} $ignoredTexts")
                if (isIgnore) {
                    splitTextArr = splitTextArr.drop(1)// .joinToString("\n")
                    println(
                        "count $i ${
                            String(
                                splitTextArr[0].toByteArray(StandardCharsets.UTF_16),
                                StandardCharsets.UTF_16
                            )
                        } deleted"
                    )
                } else {
                    println(
                        "count $i ${
                            String(
                                splitTextArr[0].toByteArray(StandardCharsets.UTF_16),
                                StandardCharsets.UTF_16
                            )
                        } not"
                    )
                }

                var newArr: List<String> = splitTextArr //mutableListOf()
                var newString: String = ""

//                for (j in splitTextArr) {
//                    if (j.endsWith(" ")) {
//                        newString = newString.plus(j)
//                    } else {
//                        newArr= newArr.plus(newString)
//                        newString = j
//                    }
//                }
//                println(newArr.size)
                text = newArr.joinToString("\n")
                fileTextData.append(text)
                i += 1
            }

            // Close the PDF document.
            document.close()
            return fileTextData.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun downloadFile(url: URL, outputFilePathName: String) {
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFilePathName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }
}