package co.ankitbansal.indian_scripts_reader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.ResourceBundleMessageSource





@SpringBootApplication
class IndianScriptsReaderApplication  {

}

fun main(args: Array<String>) {
    val messageSource = ResourceBundleMessageSource()
    messageSource.setDefaultEncoding("UTF-16")
    runApplication<IndianScriptsReaderApplication>(*args)

}

