package co.ankitbansal.indian_scripts_reader.controller

import co.ankitbansal.indian_scripts_reader.services.PDFToTextAdapterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/v1/pdf-to-text")
class PdfToTextController(private val pdfService: PDFToTextAdapterService) {
    @PostMapping("/hin")
    @ResponseBody
    fun hindiPdfTotext(@RequestBody body: PDFRequestBody): String {
        return pdfService.getText(body)
    }

    @PostMapping("/hin/save-local")
    fun hindiPdfTotextSaveLocal(@RequestBody body: PDFRequestBody): ResponseEntity<ResponseBodyDto<String>> {
        pdfService.downloadAndSaveInPath(body = body)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/hin/get-local")
    fun hindiPdfTotextFromLocal(@RequestBody body: PDFRequestBody):String {
        return  pdfService.getFromLocalPath(
            path = body.url,
            all = body.all,
            pageStart = body.pageStart,
            pageEnd = body.pageEnd,
            ignoreText = body.ignoreText,
            lang = "hin"
        )
    }
}

data class PDFRequestBody(
    val url: String,
    val title: String = "",
    val pageStart: Int = 0,
    val pageEnd: Int = 100,
    val all: Boolean = true,
    val ignoreText: List<String> = listOf(),
)

data class ResponseBodyDto<T>(val success: Boolean, val data: T)