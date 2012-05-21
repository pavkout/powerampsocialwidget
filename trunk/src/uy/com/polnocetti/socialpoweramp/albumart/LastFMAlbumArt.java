package uy.com.polnocetti.socialpoweramp.albumart;

import uy.com.polnocetti.socialpoweramp.ButtonWidget;
import android.util.Log;
import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;

public class LastFMAlbumArt implements AlbumArtManager {

	private String userAgent = "tst";
	private static LastFMAlbumArt instance = null;

	protected LastFMAlbumArt() {

	}

	public static LastFMAlbumArt getInstance() {
		if (instance == null) {
			instance = new LastFMAlbumArt();
		}
		return instance;
	}

	@Override
	public String getAlbumUrl(String artist, String album) {

		try {
			Caller.getInstance().setCache(null);
			Caller.getInstance().setUserAgent(userAgent);
			Caller.getInstance().setDebugMode(true);

			String key = "fa1dff1e2e1548a0f326c9bc9e3bfbfb";
			Album lAlbum = Album.getInfo(artist, album, key);

			if (lAlbum != null) {
				return lAlbum.getImageURL(ImageSize.EXTRALARGE);
			} else{
				return "";
			}
		} catch (Exception e) {
			if (e != null){
				Log.e(ButtonWidget.TAG, e.getMessage());
			}
			return "";
		}

	}
}
