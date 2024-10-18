package me.xii69.vstaffchat.shared.config

import com.moandjiezana.toml.Toml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

abstract class ConfigModel(
    private val path: Path,
    private val clazz: Class<*>,
    private val fileName: String
) {
    lateinit var configuration: Toml

    fun load() {
        val folder = path.toFile()
        val file = File(folder, fileName)

        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        if (!file.exists()) {
            clazz.getResourceAsStream("/" + file.name).use { input ->
                if (input != null) Files.copy(input, file.toPath()) else file.createNewFile()
            }
        }

        configuration = Toml().read(file)
        init()
    }

    abstract fun init()
}
