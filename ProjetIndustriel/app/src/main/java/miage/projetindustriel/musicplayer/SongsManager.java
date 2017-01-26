package miage.projetindustriel.musicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
    // chemin storage externe
    final String MEDIA_PATH = new String("/storage/extSdCard");
    //chemin storage interne
    //final String MEDIA_PATH = new Environment().getExternalStorageDirectory().getPath();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";
    private String MP3Pattern = ".MP3";

    // Constructor
    public SongsManager(){

    }

    /**
     * Function to read all mp3 files from storage
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList() {
        System.out.println(MEDIA_PATH);
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        //scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }


    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            HashMap<String, String> songMap = new HashMap<String, String>();
            songMap.put("songTitle",
                    song.getName().substring(0, (song.getName().length() - 4)));
            //songMap.put("songPath", song.getPath());
            songMap.put("songPath", "https://dl.dropboxusercontent.com/s/lyld02lf11eipn6/Sleep%20Away.mp3");

            // Adding each song to SongList
            songsList.add(songMap);
        }
    }
}
