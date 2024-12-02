package at.rueckgr.spotify.main

import at.rueckgr.spotify.util.ApiFactory
import org.apache.logging.log4j.kotlin.logger
import java.util.*

class RecentlyPlayed {
    fun run(): Int {
        val logger = logger("RecentlyPlayed")

        val spotifyApi = ApiFactory.create()

        logger.info("Fetching playlists of user")
        var before: Date? = null
        while (true) {
            val recentlyPlayedTracks = when (before) {
                null -> spotifyApi.currentUsersRecentlyPlayedTracks.build().execute()
                else -> spotifyApi.currentUsersRecentlyPlayedTracks.before(before).build().execute()
            }

            recentlyPlayedTracks.items.forEach { item ->
                val track = item.track
                val artists = track.artists.joinToString(separator = ", ", transform = { it.name })
                logger.info("Played at ${item.playedAt}: $artists -- ${item.track.name}")
            }
            if (recentlyPlayedTracks.items.isEmpty()) {
                break
            }

            before = recentlyPlayedTracks.items.last().playedAt
        }

        return 0
    }
}
