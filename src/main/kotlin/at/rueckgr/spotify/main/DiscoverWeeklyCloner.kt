package at.rueckgr.spotify.main

import at.rueckgr.spotify.util.ApiFactory
import org.apache.logging.log4j.kotlin.logger
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class DiscoverWeeklyCloner {
    fun run(): Int {
        val logger = logger("DiscoverWeeklyCloner")

        val sourcePlaylistName = "Discover Weekly"
        val destinationPlaylistName = buildDestinationName()

        logger.info("Source playlist name: $sourcePlaylistName")
        logger.info("Destination playlist name: $destinationPlaylistName")

        val spotifyApi = ApiFactory.create()

        logger.info("Fetching playlists of user")
        val playlistsRequest = spotifyApi.listOfCurrentUsersPlaylists.build()
        val playlists = playlistsRequest.execute()
        val total = playlists.total
        logger.info("Total playlists of user: $total")
        var playlistsProcessed = 0
        var sourcePlaylist: PlaylistSimplified? = null
        while (playlistsProcessed < total) {
            val playlistsToProcess =
                spotifyApi.listOfCurrentUsersPlaylists.limit(50).offset(playlistsProcessed).build().execute()
            playlistsProcessed += playlistsToProcess.items.size

            val possibleSourcePlaylist =
                playlistsToProcess.items
                    .filterNotNull()
                    .find { playlist -> playlist.name.equals(sourcePlaylistName) }
            if (possibleSourcePlaylist != null) {
                logger.info("Found source playlist with name $sourcePlaylistName: ${possibleSourcePlaylist.id}")
                sourcePlaylist = possibleSourcePlaylist
            }

            val destinationPlaylist = playlistsToProcess.items
                .filterNotNull()
                .find { playlist -> playlist.name.equals(destinationPlaylistName) }
            if (destinationPlaylist != null) {
                logger.info("Destination playlist already exists")
                return 0
            }
        }

        if (sourcePlaylist == null) {
            logger.error("Playlist $sourcePlaylistName not found")
            return 2
        }

        val userId = spotifyApi.currentUsersProfile.build().execute().id
        logger.info("Determined userId: $userId")
        val destinationPlaylist =
            spotifyApi.createPlaylist(userId, destinationPlaylistName).collaborative(false).public_(false).build()
                .execute()
        logger.info("Created destination playlist: ${destinationPlaylist.id}")

        val sourcePlaylistItems = spotifyApi.getPlaylistsItems(sourcePlaylist.id).limit(50).build().execute()
        logger.info("Fetched ${sourcePlaylistItems.items.size} items from source playlist")

        val playlistItems = sourcePlaylistItems.items.map { item -> item.track.uri }.toTypedArray()
        spotifyApi.addItemsToPlaylist(destinationPlaylist.id, playlistItems).build().execute()
        logger.info("Added ${playlistItems.size} items to destination playlist")

        return 0
    }

    private fun buildDestinationName(): String {
        val localDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val date = localDate.format(DateTimeFormatter.ofPattern("YYYYMMdd"))
        return "discover-weekly-$date"
    }
}
