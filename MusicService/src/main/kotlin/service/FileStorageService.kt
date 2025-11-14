package org.example.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Service
class FileStorageService {
    // Получаем текущий рабочий каталог
    private val projectDir = System.getProperty("user.dir")

    // Указываем путь к папке внутри каталога проекта
    val basePath = "$projectDir/Storage"

    fun saveFile(file: MultipartFile): String {
        val filename = UUID.randomUUID().toString() + "_" + file.originalFilename
        val path = Paths.get(basePath, filename)
        val parentDir = path.parent
        if (!Files.exists(parentDir)) {
            Files.createDirectories(parentDir)
        }
        Files.copy(file.inputStream, path)
        return path.toString()
    }

    fun readFile(path: String): ByteArray {
        return Files.readAllBytes(Paths.get(path))
    }

    fun deleteFile(path: String) {
        Files.deleteIfExists(Paths.get(path))
    }
}