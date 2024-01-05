import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This class manages, stores, and plays audio content such as songs, podcasts and audiobooks. 
 */
public class Library
{
	private ArrayList<Song> 			songs; 
	private ArrayList<AudioBook> 	audiobooks;
	private ArrayList<Playlist> 	playlists; 
	
  //private ArrayList<Podcast> 	podcasts;
	
	// Public methods in this class set errorMesg string 
	// Error Messages can be retrieved from main in class MyAudioUI by calling  getErrorMessage()
	// In assignment 2 we will replace this with Java Exceptions
	String errorMsg = "";
	
	public String getErrorMessage()
	{
		return errorMsg;
	}

	public Library()
	{
		songs 			= new ArrayList<Song>(); 
		audiobooks 	= new ArrayList<AudioBook>(); ;
		playlists   = new ArrayList<Playlist>();
	  //podcasts		= new ArrayList<Podcast>(); ;
	}
	/*
	 * Download audio content from the store. Since we have decided (design decision) to keep 3 separate lists in our library
	 * to store our songs, podcasts and audiobooks (we could have used one list) then we need to look at the type of
	 * audio content (hint: use the getType() method and compare to Song.TYPENAME or AudioBook.TYPENAME etc)
	 * to determine which list it belongs to above
	 * 
	 * Make sure you do not add song/podcast/audiobook to a list if it is already there. Hint: use the equals() method
	 * If it is already in a list, set the errorMsg string and return false. Otherwise add it to the list and return true
	 * See the video
	 */
	public boolean download(AudioContent content)
	{
		// variable to keep track of whether the content is already downloaded or not
		boolean contentIsDownloaded = false;
		
		// use switch case to handle the 2 possibilities
		switch (content.getType()) {
			case "SONG":
				// loop through the songs
				for (int i = 0; i < songs.size(); i ++) {
					// if the current song is equal to the song input
					if (songs.get(i).equals(content)) {
						// set contentIsDownloaded to true
						contentIsDownloaded = true;
						
						// no longer need to loop
						break;
					}
				}

				// if the song was not alredy downloaded
				if (!contentIsDownloaded) {
					// add it to the songs array list
					songs.add((Song) content);
					return true;
				}

				break;
		
			case "AUDIOBOOK":
				// loop through the audiobooks
				for (int i = 0; i < audiobooks.size(); i ++) {
					// if the current audiobook is equal to the audiobook input
					if (audiobooks.get(i).equals(content)) {
						// set contentIsDownloaded to true
						contentIsDownloaded = true;
						
						// no longer need to loop
						break;
					}
				}
				
				// if the audiobook was not already downloaded
				if (!contentIsDownloaded) {
					// add it to the audiobooks array list
					audiobooks.add((AudioBook) content);
					return true;
				}

				break;
	
		}
		
		// if the content is already downloaded set the correct error message
		this.errorMsg = content.getType().charAt(0) + content.getType().substring(1).toLowerCase() + " already downloaded";
		return false;
	}
	
	// Print Information (printInfo()) about all songs in the array list
	public void listAllSongs()
	{
		for (int i = 0; i < songs.size(); i++)
		{
			int index = i + 1;
			System.out.print("" + index + ". ");
			songs.get(i).printInfo();
			System.out.println();	
		}
	}
	
	// Print Information (printInfo()) about all audiobooks in the array list
	public void listAllAudioBooks()
	{
		// loop through the audiobooks
		for (int i = 0; i < audiobooks.size(); i ++) {
			// 1 index it
			int index = i + 1;
			
			// print out the information needed in proper format
			System.out.print("" + index + ". ");
			audiobooks.get(i).printInfo();
			System.out.println();
		}
	}
	
  // Print Information (printInfo()) about all podcasts in the array list
	public void listAllPodcasts()
	{
		
	}
	
  // Print the name of all playlists in the playlists array list
	// First print the index number as in listAllSongs() above
	public void listAllPlaylists()
	{
		// loop through the playlists
		for (int i = 0; i < playlists.size(); i ++) {
			// 1 index it
			int index = i + 1;
			
			// print out the information needed in proper format
			System.out.print("" + index + ". " + playlists.get(i).getTitle());
			System.out.println();
		}
	}
	
  // Print the name of all artists. 
	public void listAllArtists()
	{ 
		// First create a new (empty) array list of string
		ArrayList<String> artists = new ArrayList<String>();

		// Go through the songs array list add the artist name to the new arraylist only if it is
		for (int i = 0; i < songs.size(); i++) {
			// add the artist name to the new arraylist only if it is not already there
			if (!artists.contains(songs.get(i).getArtist())) {
				artists.add(songs.get(i).getArtist());
			}
		}

		// print the artists names by looping through the artists
		for (int i = 0; i < artists.size(); i++) {
			System.out.println((i+1) + ". " + artists.get(i));
		}
	}

	// Delete a song from the library (i.e. the songs list) - 
	// also go through all playlists and remove it from any playlist as well if it is part of the playlist
	public boolean deleteSong(int index)
	{
		// check if the user inputted a valid song index
		if (index > 0 && index <= songs.size()) {
			// make an object to hold what song we want to delete
			Song toDel = songs.get(index - 1);
			// remove the song from the songs arraylist (1 indexed)
			songs.remove(index - 1);

			// loop through the playlists
			for (int i = 0; i < playlists.size(); i ++) {
				// make an array list each time of the content of current playlist
				ArrayList<AudioContent> currPlayList = playlists.get(i).getContent();
				
				// if the sond is in the current playlist
				if (currPlayList.contains(toDel)) {
					// remove the song from this playlist (make sure to 1 index it)
					playlists.get(i).deleteContent(currPlayList.indexOf(toDel) + 1);
				}
			}

			return true;
		} else { // incase of an invalid song index
			// set the correct error message
			this.errorMsg = "Song Not Found";
			return false;
		}

	}
	
  //Sort songs in library by year
	public void sortSongsByYear()
	{
		// Use Collections.sort() and pass songs arraylist and SongYearComparator() so it sorts it in ascending order based on year
		Collections.sort(songs, new SongYearComparator());
	
	}
  // Write a class SongYearComparator that implements
	// the Comparator interface and compare two songs based on year
	private class SongYearComparator implements Comparator<Song>
	{
		@Override
		// take 2 Song attribiutes
		public int compare(Song a, Song b) {
			// sort them by their year
			// it will automatically sort in ascending order based on whether the returned value is positive or negative
			return a.getYear() - b.getYear();
		}
	}

	// Sort songs by length
	public void sortSongsByLength()
	{
		// Use Collections.sort() and pass songs arralist and SongLengthComparator() so it sorts it in ascending order based on length
		Collections.sort(songs, new SongLengthComparator());
	}
  // Write a class SongLengthComparator that implements
	// the Comparator interface and compare two songs based on length
	private class SongLengthComparator implements Comparator<Song>
	{
		@Override
		// take 2 Song attributes
		public int compare(Song a, Song b) {
			// sort them by their length
			// it will automatically sort in ascending order based on whether the returned value is positive or negative
			return a.getLength() - b.getLength();
		}
	}

	// Sort songs by title Y
	public void sortSongsByName()
	{
	  // Use Collections.sort()
		// class Song should implement the Comparable interface
		// see class Song code

		// just need to pass songs arraylist since it will default to compareTo() behaviour for sorting defined in Song class
		Collections.sort(songs);
	}

	
	
	/*
	 * Play Content
	 */
	
	// Play song from songs list
	public boolean playSong(int index)
	{
		if (index < 1 || index > songs.size())
		{
			errorMsg = "Song Not Found";
			return false;
		}
		songs.get(index-1).play();
		return true;
	}
	
	// Play podcast from list (specify season and episode)
	// Bonus
	public boolean playPodcast(int index, int season, int episode)
	{
		return false;
	}
	
	// Print the episode titles of a specified season
	// Bonus 
	public boolean printPodcastEpisodes(int index, int season)
	{
		return false;
	}
	
	// Play a chapter of an audio book from list of audiobooks
	public boolean playAudioBook(int index, int chapter)
	{
		// if inputted audiobook index is not valid
		if (index < 1 || index > audiobooks.size()) {
			// set the correct error message
			this.errorMsg = "Audio Book Not Found";
			return false;
		}
		
		// if inputted audiobook index is valid
		// set the currentChapter to the passed chapter input (1 indexed)
		audiobooks.get(index - 1).selectChapter(chapter);
		// play the audio book at the specified index (1 indexed)
		audiobooks.get(index -1).play();
		return true;

	}
	
	// Print the chapter titles (Table Of Contents) of an audiobook
	// see class AudioBook
	public boolean printAudioBookTOC(int index)
	{
		// if inputted audiobook index is not valid
		if (index < 1 || index > audiobooks.size()) {
			// set the correct error message
			this.errorMsg = "Audio Book Not Found";
			return false;
		}
		
		// if inputted audiobook index is valid
		// call the printTOC() method on the audiobook at the specified index
		audiobooks.get(index - 1).printTOC();
		return true;
	}
	
  /*
   * Playlist Related Methods
   */
	
	// Make a new playlist and add to playlists array list
	// Make sure a playlist with the same title doesn't already exist
	public boolean makePlaylist(String title)
	{
		// if the playlist already exists
		if (playlists.contains(new Playlist(title))) {
			// set the correct error message
			this.errorMsg = "Playlist " + title + " Already Exists";
			return false;
		}

		// if the playlist doesn't already exist
		// add a new playlist with the given title to the playlists arraylist
		playlists.add(new Playlist(title));
		return true;
	}
	
	// Print list of content information (songs, audiobooks etc) in playlist named title from list of playlists
	public boolean printPlaylist(String title)
	{
		// if the playlist doesn't exist
		if (!playlists.contains(new Playlist(title))) {
			// set the correct error message
			this.errorMsg = "Playlist " + title + " Not Found";
			return false;
		}

		// if the play list exists
		// print the playlist contents using printContents() method
		// we use "new Playlist(title)" because that creates a temopary playlist object with the given title
		// we can now identify it in the playlists arralylist
		playlists.get(playlists.indexOf(new Playlist(title))).printContents();
		return true;
	}
	
	// Play all content in a playlist
	public boolean playPlaylist(String playlistTitle)
	{
		// if the playlist doesn't exist
		if (!playlists.contains(new Playlist(playlistTitle))) {
			// set the correct error message
			this.errorMsg = "Playlist " + playlistTitle + " Not Found";
			return false;
		}

		// if the play list exists
		// play the playlist using playAll() method
		// we use "new Playlist(playlistTitle)" because that creates a temopary playlist object with the given title
		// we can now identify it in the playlists arralylist
		playlists.get(playlists.indexOf(new Playlist(playlistTitle))).playAll();
		return true;
	}
	
	// Play a specific song/audiobook in a playlist
	public boolean playPlaylist(String playlistTitle, int indexInPL)
	{
		// if the playlist exists
		if (playlists.contains(new Playlist(playlistTitle))) {
			// if the inputted index is valid
			if (playlists.get(playlists.indexOf(new Playlist(playlistTitle))).contains(indexInPL)) {
				// play the specific song/audiobook based on the given index
				playlists.get(playlists.indexOf(new Playlist(playlistTitle))).play(indexInPL);
				return true;
			}
			
			// if the inputted index is invalid
			// set the correct error message
			this.errorMsg = "Audio Not Found In Playlist " + playlistTitle;
			return false;
		}

		// if the playlist doesn't exist
		// set the correct error message
		this.errorMsg = "Playlist " + playlistTitle + " Not Found";
		return false;
	}
	
	// Add a song/audiobook/podcast from library lists at top to a playlist
	// Use the type parameter and compare to Song.TYPENAME etc
	// to determine which array list it comes from then use the given index
	// for that list
	public boolean addContentToPlaylist(String type, int index, String playlistTitle)
	{
		// if the playlist exists
		if (playlists.contains(new Playlist(playlistTitle))) {
			// if the audio type is a song
			if (type.equalsIgnoreCase("SONG")) {
				// if the inputted song index is valid
				if (index > 0 && index <= songs.size()) {
					// add the specified song (1 indexed) to the specified playlist
					playlists.get(playlists.indexOf(new Playlist(playlistTitle))).addContent(songs.get(index - 1));
					return true;
				}

				// if the inputted song index is invalid
				// set the correct error message
				this.errorMsg = "Song Not Found";
				return false;
			
			// if the audio type is an audiobook
			} else if (type.equalsIgnoreCase("AUDIOBOOK")) {
				// if the inputted audiobook index is valid
				if (index > 0 && index <= audiobooks.size()) {
					// add the specified audio book (1 indexed) to the specified playlist
					playlists.get(playlists.indexOf(new Playlist(playlistTitle))).addContent(audiobooks.get(index - 1));
					return true;
				}
				
				// set the correct error message
				this.errorMsg = "AudioBook Not Found";
				return false;
			}
		}

		// if the playlist doesn't exist
		// set the correct error message
		this.errorMsg = "Playlist " + playlistTitle + " Not Found";
		return false;
	}

  // Delete a song/audiobook/podcast from a playlist with the given title
	// Make sure the given index of the song/audiobook/podcast in the playlist is valid 
	public boolean delContentFromPlaylist(int index, String title)
	{
		// if the playlist exists
		if (playlists.contains(new Playlist(title))) {
			// if the inputted index is valid
			if (playlists.get(playlists.indexOf(new Playlist(title))).contains(index)) {
				// delete the specified audio content from the specified playlist
				playlists.get(playlists.indexOf(new Playlist(title))).deleteContent(index);
				return true;
			}

			// if the inputted index is invalid
			// set the correct error message
			this.errorMsg = "Audio Not Found In Playlist " + title;
			return false;
		}

		// if the playlist doesn't exist
		// set the correct error message
		this.errorMsg = "Playlist Not Found";
		return false;
	}
	
}

