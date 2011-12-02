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

import com.maxmpz.powerampapilib.R;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

// PowerAMP Version!
/*
 * Contains eq presets.
 */
public final class EqPresets implements BaseColumns {
	public static final String TABLE = "eq_presets";
	
    public static final Uri CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/eq_presets");
    
    /*
     * Predefined preset number (see res/values/arrays/eq_preset_labels) or NULL for custom preset.
     * Int.
     */
    public static final String PRESET = "preset";
    
    /*
     * Eq preset string.
     * String. 
     */
    public static final String _DATA = "_data";
    
    /*
     * Custom preset name or null for predefined preset.
     * String.
     */
    public static final String NAME = "name";
    
    /*
     * 1 if preset is bound to speaker, 0 otherwise.
     * Int.
     */
    public static final String BIND_TO_SPEAKER = "bind_to_speaker";
    
    /*
     * 1 if preset is bound to wired headset, 0 otherwise.
     * Int.
     */
    public static final String BIND_TO_WIRED = "bind_to_wired";
    
    /*
     * 1 if preset is bound to bluetooth audio output, 0 otherwise.
     * Int.
     */
    public static final String BIND_TO_BT = "bind_to_bt";
    

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.eq_preset";
	public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.eq_preset";
	
	// Should map to array/eq_preset_labels
	public static final int PRESET_BASS = 1;
	public static final int PRESET_BASS_EXTREME = 2;
	public static final int PRESET_BASS_TREBLE = 3;
	public static final int PRESET_TREBLE = 4;
	public static final int PRESET_FLAT = 5;
	public static final int PRESET_CLASSICAL = 6;
	public static final int PRESET_DANCE = 7;
	public static final int PRESET_ROCK = 8;
	public static final int PRESET_TECHNO = 9;
	public static final int PRESET_PHONE_SPEAKER = 10;
	
	public static final String getPresetName(Context context, String name, int preset) {
		CharSequence[] eqPresetsLabels = context.getResources().getTextArray(R.array.eq_preset_labels);
		if(!TextUtils.isEmpty(name)) {
			return name;
		} else 	if(eqPresetsLabels != null && preset >= 0 && preset < eqPresetsLabels.length) {
			return eqPresetsLabels[preset].toString();
		} else {
			return context.getString(R.string.unknown);
		}
	}
	
	
	/*
	 * Contains per song eq bindings.
	 */
	public static final class Songs implements BaseColumns {
		public static final String TABLE = "eq_preset_songs";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AudioPlayerProvider.AUTHORITY + "/eq_preset_songs");
		
		/*
		 * One of the PowerAMPiAPI.Track.Type constants.
		 * Int.
		 */
		public static final String TYPE = "type";
		
		/*
		 * Either folder_file row id or system media id, depending on type.
		 * Int. 
		 */
		public static final String FILE_ID = "file_id";
		
		/*
		 * Eq preset id.
		 * Int.
		 */
		public static final String PRESET_ID = "preset_id";
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maxmpz.eq_preset_song";
		public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maxmpz.eq_preset_song";
	}
}
