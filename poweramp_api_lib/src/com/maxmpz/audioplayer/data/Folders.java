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

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class Folders implements BaseColumns {
	private static final String TAG = "Folders";
	
    public static final Uri CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folders");
    public static final Uri HIER_CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/hier_folders");

	public static final Uri getContentEntryUri(long id) {
		return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folders/" + id);
	}
	public static final Uri getContentEntryHierUri(long id) {
		return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/hier_folders/" + id);
	}
	
	
	/*
	 * volume_id (fatID) of the storage.
	 * Int.
	 */
	public static final String VOLUME_ID = "volume_id";

	/*
	 * Short name of the folder.
	 * String.
	 */
	public static final String NAME = "name";

	/*
	 * Short path of the parent folder.
	 * String.
	 */
	public static final String PARENT_NAME = "parent_name";

	/*
	 * Full path of the folder.
	 * String.
	 */
	public static final String PATH = "path";

	/*
	 * Folder album art/thumb image (short name).
	 * String.
	 */
	public static final String THUMB = "thumb";

	/*
	 * One of the THUMB_* constants.
	 * Int.
	 */
	public static final String THUMB_STATUS = "thumb_status";

	/*
	 * Number of files in a folder.
	 * Int.
	 */
	public static final String NUM_FILES = "num_files";
	
	/*
	 * Int.
	 */
	public static final String DIR_MODIFIED_AT = "dir_modified_at";
	
	/*
	 * Int.
	 */
	public static final String UPDATED_AT = "updated_at";
	/*
	 * Id of the parent folder or 0 for "root" folders.
	 * Int.
	 */
	public static final String PARENT_ID = "parent_id";	
	/*
	 * Number of child subfolders.
	 * Int.
	 */
	public static final String NUMB_SUBFOLDERS = "num_subfolders";
	

   	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.folder";
	public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.folder";
   	public static final String HIER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.hier_folder";
	public static final String HIER_ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.hier_folder";
    
    public static final String TABLE = "folders";
    
    public static final int THUMB_EXISTS_NOT_PROCESSED = 10;
    public static final int THUMB_PROCESSED = 1;
    public static final int THUMB_NO_THUMB = 0;
    
    public static final int THUMB_SIZE_DIPS = 64;
	public static final String THUMB_EXT = "pamp";
	public static final String THUMB_FILE = "albumart." + THUMB_EXT; // IMPORTANT: keep in sync with ThumbDirScanner.EXTS
	
	public static final String[] SEARCH_FIELDS = new String[] {
		TABLE + ".name" 
	};

	// TODO: support mod time desc, mod time asc modes ?
	//private static final String[] SORT_ORDER_FIELDS = new String[] { "folders.path" };
	
	//public static final String SORT_ORDER_TRACK_FILENAME =
	
	public static final String SORT_NAME = Folders.TABLE + ".name COLLATE NOCASE";
	public static final String SORT_PATH = Folders.TABLE + ".path COLLATE NOCASE";
	public static final String SORT_CREATED_AT = Folders.TABLE + ".dir_modified_at DESC";
	

    public static final class Files implements BaseColumns {
    	public static final String TABLE = "folder_files";
    	
    	public static final Uri getContentUri(long folderId) {
    		// NOTE: folder is specifically singular so URI differs and I don't get unwanted on data changed updates for folders when files are changed.
    		return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folder/" + folderId + "/files");  
    	}
    	
    	public static final Uri getHierContentUri(long folderId) {
    		// NOTE: folder is specifically singular so URI differs and I don't get unwanted on data changed updates for folders when files are changed.
    		return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folder/" + folderId + "/hier_files");  
    	}

    	public static final Uri getContentEntryUri(long fileId) {
    		return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/files/" + fileId);
    	}
    	
    	/** All files, joined with the folders. */
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/files");
    	
    	/** Special uri used for notification - invalidates any /folder/ * /files */
    	public static final Uri FOLDER_CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folder");
    	
    	/*
    	 * Short filename.
    	 * String.
    	 */
    	public static final String NAME = "name";
    	
    	/*
    	 * Track number (extracted from filename).
    	 * Int.
    	 */
    	public static final String TRACK_NUMBER = "track_number";
    	
    	/*
    	 * Track name without number.
    	 * String.
    	 */
    	public static final String NAME_WITHOUT_NUMBER = "name_without_number";
    	
    	/*
    	 * One of the TAG_* constants.
    	 * Int.
    	 */
    	public static final String TAG_STATUS = "tag_status";
    	
    	/*
    	 * Track # tag.
    	 * Int.
    	 */
    	public static final String TRACK_TAG = "track_tag";
    	
    	/*
    	 * Parent folder id.
    	 * Int.
    	 */
    	public static final String FOLDER_ID = "folder_id";
    	
    	/*
    	 * Artist tag.
    	 * String.
    	 */
    	public static final String ARTIST_TAG = "artist_tag";
    	
    	/*
    	 * Album tag.
    	 * String.
    	 */
    	public static final String ALBUM_TAG = "album_tag";
    	
    	/*
    	 * Title tag.
    	 * String.
    	 */
    	public static final String TITLE_TAG = "title_tag";
    	
    	/*
    	 * Duration in seconds.
    	 * Int.
    	 */
    	public static final String DURATION = "duration";
    	
    	/*
    	 * Int.
    	 */
    	public static final String UPDATED_AT = "updated_at";
    	
    	/*
    	 * One of the file types - see PowerAMPiAPI.Track.FileType class.
    	 */
    	public static final String FILE_TYPE = "file_type";
    	
    	/*
    	 * Int.
    	 */
    	public static final String FILE_CREATED_AT = "file_created_at"; 
    	
    	
    	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.folder_file";
    	public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.folder_file";
    
		
		
		public static final String[] SEARCH_FIELDS = new String[] {
			TABLE + ".title_tag", TABLE + ".name" 
		};
		
		
		public static final int TAG_NOT_SCANNED = 0;
		public static final int TAG_SCANNED = 1;
		
		public static final String SORT_TRACK_FILENAME = Folders.Files.TABLE + ".track_number, " + Folders.Files.TABLE + ".name_without_number COLLATE NOCASE";
		public static final String SORT_FILENAME_WITHOUT_NUMBER = Folders.Files.TABLE + ".name_without_number COLLATE NOCASE";
		public static final String SORT_FILENAME_PLAIN = Folders.Files.TABLE + ".name COLLATE NOCASE";
		public static final String SORT_FILE_CREATED_AT = Folders.Files.TABLE + ".file_created_at DESC";
    }
		
    public static final class Playlists implements BaseColumns {
		public static final String TABLE = "folder_playlists";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folder_playlists");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.folder_playlist";
		public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.folder_playlist";
		
		
		/*
		 * Name of the playlist.
		 * String.
		 */
		public static final String NAME = "name"; 
		
		
		public static final String SORT_ORDER = TABLE + ".name";		
		
		
		
		public static final class Entries implements BaseColumns {
			public static final String TABLE = "folder_playlist_entries";
			public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.folder_playlist_entries";
			
			public static final String SORT_ORDER = TABLE + ".sort";

			public static Uri getContentUri(long playlistId) {
				return Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/folder_playlists/" + playlistId + "/entries");
			}
			
			/*
			 * Actual id of the file in folder_files table.
			 * Int.
			 */
			public static final String FOLDER_FILE_ID = "folder_file_id";
			
			/*
			 * Folder Playlist id.
			 * Int.
			 */
			public static final String PLAYLIST_ID = "playlist_id";
			
			/*
			 * Sort order.
			 * Int.
			 */
			public static final String SORT = "sort";
		}
    }
}