package service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class CsvToStringArrayReaderService {
    fun read(file: MultipartFile): List<Array<String>> {
        var `in`: InputStream? = null
        return try {
            val bootstrapSchema = CsvSchema.emptySchema().withColumnSeparator(';')
            val mapper = CsvMapper()
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY)
            `in` = file.inputStream
            val it = mapper.readerFor(
                Array<String>::class.java
            ).with(bootstrapSchema).readValues<Array<String>>(`in`)
            it.readAll()
        } finally {
            `in`?.close()
        }
    }
}
