package miage.projetindustriel.connexion;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import miage.projetindustriel.model.Musique;
import miage.projetindustriel.utility.Constantes;


/**
 * Created by utilisateur on 08/01/2017.
 */

public class FetchMusicsAlbum extends AsyncTask<Integer, Void, Musique[]> {

    private String TAG = getClass().getSimpleName();

    public AsyncMusiqueAlbumData delegateMusiqueAlbumData = null;

    public interface AsyncMusiqueAlbumData {
        public void onLoadMusiqueAlbumDataFinish(Musique musiques[]);
    }

    private String buildMusiqueUrl(int idAlbum) {

        Uri builtUri = Uri.parse(Constantes.BASE_URL_SERVER);
        builtUri = Uri.withAppendedPath(builtUri, Constantes.URL_GET_MUSIC_ALBUM).buildUpon()
                .appendQueryParameter(Constantes.ALBUM_ID_PARAM, Integer.toString(idAlbum)).build();

        return builtUri.toString();
    }

    @Override
    protected Musique[] doInBackground(Integer... params) {

        if(null == params){
            return null;
        }
        int idAlbum = params[0];
        String url = buildMusiqueUrl(idAlbum);
        ConnectionHttp conn = new ConnectionHttp(url);
        String jsonStr = conn.readStream(conn.getConnection());

        try {
            return getDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {

            conn.disconnect();
        }

        return null;
    }


    private Musique[] getDataFromJson(String musiquesJsonString) throws JSONException {
        // colonnes a extraire

        if(null == musiquesJsonString){
            return null;
        }
        Log.v(TAG, musiquesJsonString);
        final String result = "result";
        final String ID = "id";
        final String ALBUM_ID = "albumId";
        final String TITRE = "titre";
        final String DUREE = "duree";
        final String COVER_PHOTO = "coverImage";
        final String URL_SON = "url";

        JSONObject jsonObject = new JSONObject(musiquesJsonString);
        JSONObject resultObject = jsonObject.getJSONObject("result");
        JSONArray arrayMusiques = resultObject.getJSONArray("musiques");
        int nbMusique = arrayMusiques.length();

        Musique musiques[] = new Musique[nbMusique];
        for (int i=0; i<nbMusique; i++ ) {
            JSONObject musique = arrayMusiques.getJSONObject(i);

            int id = musique.getInt(ID);
            int idAlbum = musique.getInt(ALBUM_ID);
            String titre = musique.getString(TITRE);
            String duree = musique.getString(DUREE);
            String coverPhoto = Constantes.BASE_URL_SERVER + musique.getString(COVER_PHOTO);
            String urlSon = musique.getString(URL_SON);

            musiques[i] = new Musique(id,idAlbum,titre,duree,coverPhoto,urlSon);
        }
        return musiques;
    }

    @Override
    protected void onPostExecute(Musique[] musiques) {
        if(null == musiques){
            return;
        }
        delegateMusiqueAlbumData.onLoadMusiqueAlbumDataFinish(musiques);
    }
}
