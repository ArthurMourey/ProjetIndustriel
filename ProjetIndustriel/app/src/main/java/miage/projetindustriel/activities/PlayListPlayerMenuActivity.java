package miage.projetindustriel.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

import miage.projetindustriel.R;
import miage.projetindustriel.adapter.MusiqueAdapter;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Musique;
import miage.projetindustriel.musicplayer.MusicPlayerActivity;
import miage.projetindustriel.musicplayer.PlayListActivity;
import miage.projetindustriel.utility.GridSpacingItemDecoration;

/**
 * Created by utilisateur on 25/01/2017.
 */

public class PlayListPlayerMenuActivity extends AppCompatActivity implements RecycleViewItemClickListener{

    private final String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private MusiqueAdapter adapterMusique;
    private ArrayList<Musique> listMusique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_player_menu);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        listMusique = new ArrayList<>();
        adapterMusique = new MusiqueAdapter(this, listMusique);
        recyclerView.setAdapter(adapterMusique);
        adapterMusique.setRecycleViewItemClickListener(this);

        Intent intentFromMusicPlayerActivity = getIntent();
        if (null != intentFromMusicPlayerActivity) {
            if (intentFromMusicPlayerActivity.hasExtra(PlayListActivity.PLAYLIST_EXTRA)) {

                ArrayList<Musique> playList = intentFromMusicPlayerActivity.getExtras()
                        .getParcelableArrayList(PlayListActivity.PLAYLIST_EXTRA);
                listMusique.addAll(playList);
                adapterMusique.notifyDataSetChanged();
                //int posSelectedMusic = intentFromPlayer.getIntExtra(PlayListActivity.POS_MUSIC_TO_PLAY,0);
                Log.v(TAG, listMusique.size() + "musique dans la playlist ");
            }
        }

    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(View view, int position) {
        Musique musique = listMusique.get(position);

        Log.v(TAG, "Recycleview music " + musique.getId() + musique.getTitre());

        //Intent in = new Intent(this, MusicPlayerActivity.class);
        Intent in = new Intent();
        in.putExtra(PlayListActivity.POS_MUSIC_TO_PLAY, position);
        setResult(RESULT_OK, in);
        Log.v(TAG, " in setResult");
        finish();
    }
}
