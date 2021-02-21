# Spotify tools

A set of tools designed to make everyday life with Spotify easier. Based on https://github.com/thelinmichael/spotify-web-api-java

Currently, it copies the contents of the "Discover Weekly" playlist to a new playlist discover-weekly-YYYYMMDD.
As Spotify creates that playlist every Monday, that date is always the monday.
If a playlist with such a name already exists, nothing will be done.

Compile and install with

`gradle installDist`

Copy `spotify.properties.dist` to `spotify.properties` and adapt it accordingly. Run it with

`build/install/SpotifyTools/bin/SpotifyTools`
