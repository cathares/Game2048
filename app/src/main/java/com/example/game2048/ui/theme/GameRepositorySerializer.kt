package com.example.game2048.ui.theme

import androidx.datastore.core.Serializer
import com.example.game2048.GameRepository
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object GameRepositorySerializer: Serializer<GameRepository> {
    override val defaultValue: GameRepository
        get() = GameRepository()

    override suspend fun readFrom(input: InputStream): GameRepository {
        return try{
            Json.decodeFromString(
                deserializer = GameRepository.serializer(),
                string = input.readBytes().decodeToString()
            )

        }
        catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: GameRepository, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = GameRepository.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}