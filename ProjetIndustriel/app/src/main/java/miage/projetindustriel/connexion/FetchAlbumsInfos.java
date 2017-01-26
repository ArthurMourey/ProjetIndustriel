package miage.projetindustriel.connexion;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import miage.projetindustriel.model.Album;
import miage.projetindustriel.utility.Constantes;


/**
 * Created by utilisateur on 08/01/2017.
 */

public class FetchAlbumsInfos extends AsyncTask<Void, Void, Album[]> {

    private String TAG = getClass().getSimpleName();

    public AsyncAlbumData delegateAlbumData = null;

    public interface AsyncAlbumData {
        public void onLoadAlbumDataFinish(Album albums[]);
    }

    private String buildAlbumUrl() {

        Uri builtUri = Uri.parse(Constantes.BASE_URL_SERVER);
        builtUri = Uri.withAppendedPath(builtUri, Constantes.URL_GET_ALL_ALBUM).buildUpon()
                .build();

        return builtUri.toString();
    }

    @Override
    protected Album[] doInBackground(Void... params) {

        Log.v(TAG, buildAlbumUrl());
        ConnectionHttp conn = new ConnectionHttp(buildAlbumUrl());
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


    private Album[] getDataFromJson(String albumJsonString) throws JSONException {
        // colonnes a extraire

        if(null == albumJsonString){
            return null;
        }
        Log.v(TAG, albumJsonString);
        final String result = "result";
        final String ID = "id";
        final String NOM = "nom";
        final String COVER_IMAGE = "coverImage";
        final String DATE_SORTIE = "dateSortie";
        final String NB_SONS = "nbSons";

        Log.v(TAG + "getDataFromJson : ", albumJsonString);
        JSONObject jsonObject = new JSONObject(albumJsonString);
        JSONObject resultObject = jsonObject.getJSONObject("result");
        JSONArray arrayAlbums = resultObject.getJSONArray("albums");
        int nbAlbums = arrayAlbums.length();

        Album albums[] = new Album[nbAlbums];
        for (int i=0; i<nbAlbums; i++ ) {
            JSONObject album = arrayAlbums.getJSONObject(i);

            int id = album.getInt(ID);
            String nom = album.getString(NOM);
            String coverImage = Constantes.BASE_URL_SERVER + album.getString(COVER_IMAGE);
            String dateSortie = album.getString(DATE_SORTIE);
            int nbSons = album.getInt(NB_SONS);

            albums[i] = new Album(id,nom,coverImage,dateSortie,nbSons);
        }
        return albums;
    }

    @Override
    protected void onPostExecute(Album[] albums) {
        if(null == albums){
            return;
        }
        delegateAlbumData.onLoadAlbumDataFinish(albums);
    }
}
