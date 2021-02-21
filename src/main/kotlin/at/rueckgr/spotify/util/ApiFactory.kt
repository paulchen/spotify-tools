package at.rueckgr.spotify.util

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.SpotifyHttpManager
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

object ApiFactory {
    private const val PROPERTIES_FILE = "spotify.properties"

    private fun loadProperties(): Properties {
        val properties = Properties()
        properties.load(FileInputStream(PROPERTIES_FILE))
        return properties
    }

    private fun saveProperties(properties: Properties) {
        properties.store(FileOutputStream(PROPERTIES_FILE), null)
    }

    fun create(): SpotifyApi {
        val properties = loadProperties()

        val clientId = properties.getProperty(Property.CLIENT_ID)
        val clientSecret = properties.getProperty(Property.CLIENT_SECRET)
        val redirectUri = SpotifyHttpManager.makeUri("https://rueckgr.at/spotify/")

        val tokenExpiration = if (properties.containsKey(Property.TOKEN_EXPIRATION)) {
            LocalDateTime.ofEpochSecond(properties.getProperty(Property.TOKEN_EXPIRATION).toLong(),0, OffsetDateTime.now().offset)
        }
        else {
            LocalDateTime.now()
        }

        if(!properties.containsKey(Property.ACCESS_TOKEN) || !properties.containsKey(Property.REFRESH_TOKEN)) {
            val spotifyApi = SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build()
            val authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("playlist-read-private,playlist-modify-private")
                .build()
            val uri = authorizationCodeUriRequest.execute()
            println("Please visit this URL and enter the code you received: $uri")
            val authorizationCode = readLine()

            val authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode).build()
            val authorizationCodeCredentials = authorizationCodeRequest.execute()

            saveTokens(spotifyApi, properties, authorizationCodeCredentials)

            return spotifyApi
        }
        else if (tokenExpiration.isBefore(LocalDateTime.now())) {
            val spotifyApi = SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(properties.getProperty(Property.REFRESH_TOKEN))
                .build()
            val authorizationCodeCredentials = spotifyApi.authorizationCodeRefresh().build().execute()

            saveTokens(spotifyApi, properties, authorizationCodeCredentials)

            return spotifyApi
        }

        return SpotifyApi.Builder()
            .setAccessToken(properties.getProperty(Property.ACCESS_TOKEN))
            .setRefreshToken(properties.getProperty(Property.REFRESH_TOKEN))
            .build()
    }

    private fun saveTokens(spotifyApi: SpotifyApi, properties: Properties, authorizationCodeCredentials: AuthorizationCodeCredentials) {
        spotifyApi.accessToken = authorizationCodeCredentials.accessToken
        properties.setProperty(Property.ACCESS_TOKEN, authorizationCodeCredentials.accessToken)

        if (authorizationCodeCredentials.refreshToken != null) {
            spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken
            properties.setProperty(Property.REFRESH_TOKEN, authorizationCodeCredentials.refreshToken)
        }

        val newTokenExpiration = LocalDateTime.now().toEpochSecond(OffsetDateTime.now().offset) + authorizationCodeCredentials.expiresIn
        properties.setProperty(Property.TOKEN_EXPIRATION, newTokenExpiration.toString())

        saveProperties(properties)
    }
}
