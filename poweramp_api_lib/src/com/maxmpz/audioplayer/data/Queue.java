/*
Copyright (C) 2011 Maksim Petrov

Redistribution and use in source and binary forms, with or without
modification, are permitted for widgets, plugins, applications and other software
which communicate with PowerAMP application on Android platform.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE FOUNDATION OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.maxmpz.audioplayer.data;

import android.net.Uri;

public class Queue {
	public static final String TABLE = "queue";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/queue");
	
   	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.queue";
	public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.queue";
	
	public static final String SORT_ORDER = TABLE + ".sort";
	
	public static final String[] LIBRARY_SEARCH_FIELDS = new String[]{ Queue.TABLE + ".title_tag" };

	public static final Uri getContentEntryUri(long id) {
		return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/queue/" + id);
	}
	
	/*
	 * One of the PowerAMPiAPI.Track.Type constants.
	 * Int.
	 */
	public static final String TYPE = "type";
	
	/*
	 * Folder file id (if this entry is a folder file type).
	 * Int.
	 */
	public static final String FOLDER_FILE_ID = "folder_file_id";
	
	/*
	 * Library media id (if this entry is a library file type).
	 * Int. 
	 */
	public static final String LIBRARY_MEDIA_ID = "library_media_id";
	
	/*
	 * Titla tag (filled only for library file type).
	 * String.
	 */
	public static final String TITLE_TAG = "title_tag";
	
	/*
	 * Album tag (filled only for library file type).
	 * String.
	 */
	public static final String ALBUM_TAG = "album_tag";
	
	/*
	 * Artist tag  (filled only for library file type).
	 * String.
	 */
	public static final String ARTIST_TAG = "artist_tag";
	
	/*
	 * Duration in seconds (filled only for library file type).
	 * Int.
	 */
	public static final String DURATION = "duration";
	
	/*
	 * Path to file  (filled only for library file type).
	 * String.
	 */
	public static final String PATH = "path";
	
	/*
	 * 1 if file is played at least once, 0 othersize.
	 * Int.
	 */
	public static final String PLAYED = "played";
	
	

}
