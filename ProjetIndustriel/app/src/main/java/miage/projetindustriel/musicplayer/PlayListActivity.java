package miage.projetindustriel.musicplayer;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import miage.projetindustriel.R;
import miage.projetindustriel.activities.AlbumsMainActivity;
import miage.projetindustriel.adapter.MusiqueAdapter;
import miage.projetindustriel.connexion.FetchMusicsAlbum;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Musique;
import miage.projetindustriel.utility.GridSpacingItemDecoration;


public class PlayListActivity extends AppCompatActivity implements FetchMusicsAlbum.AsyncMusiqueAlbumData,
        RecycleViewItemClickListener {


    private String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private ImageView backdrop;
    private MusiqueAdapter adapterMusique;
    ArrayList<Musique> listMusique;
    public static final String POS_MUSIC_TO_PLAY = "posMusicInPlayList";
    public static final String PLAYLIST_EXTRA = "playlist";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        listMusique = new ArrayList<>();
        adapterMusique = new MusiqueAdapter(this, listMusique);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        backdrop = (ImageView)findViewById(R.id.backdrop);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMusique);
        adapterMusique.setRecycleViewItemClickListener(this);
        /*try {
            Glide.with(this).load(R.drawable.cover).into(backdrop);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       /* recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //L'implementation de cette methode est faite dans
                //la methode onClick en bas
                Log.v(TAG, "playlist " +v.getClass().getName());
            }
        });
*/

        //intent execute lorsqu'on clique sur un album pour
        //afficher la playlist
        Intent intentFromAlbumMainActivity = getIntent();
        if(intentFromAlbumMainActivity.hasExtra(AlbumsMainActivity.ALBUM_ID_EXTRA)){
            int idAlbum = intentFromAlbumMainActivity.getIntExtra(AlbumsMainActivity.ALBUM_ID_EXTRA,0);
            if(idAlbum != 0){
                FetchMusicsAlbum fetchMusicsAlbum = new FetchMusicsAlbum();
                fetchMusicsAlbum.delegateMusiqueAlbumData = this;
                fetchMusicsAlbum.execute(idAlbum);
            }
        }

        //intent execute lorsqu'on clique sur une musique
        //lorsque la playlist est affiche a partir du menu du lecteur
       /* Intent intentFromMusicPlayer = getIntent();
        if(null != intentFromMusicPlayer) {
            if (intentFromMusicPlayer.hasExtra(PlayListActivity.PLAYLIST_EXTRA)) {

                ArrayList<Musique> playList = new ArrayList<>();
                playList = intentFromMusicPlayer.getExtras()
                        .getParcelableArrayList(PlayListActivity.PLAYLIST_EXTRA);
                listMusique.addAll(playList);
                adapterMusique.notifyDataSetChanged();
                recyclerView.setTag("recycleViewFromMenu");
                //int posSelectedMusic = intentFromPlayer.getIntExtra(PlayListActivity.POS_MUSIC_TO_PLAY,0);
                Log.v(TAG, playList.size() + "musique dans la playlist " );
            }
        }*/

	}

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onLoadMusiqueAlbumDataFinish(Musique[] musiques) {
        if (null == musiques){
            Toast.makeText(this,"Aucune musique trouvée", Toast.LENGTH_LONG);
            return;
        }
        Random random = new Random();
        int randomIndice = random.nextInt(musiques.length);
        String urlRandomImageBackdrop = musiques[randomIndice].getCoverPhoto();

        Glide.with(this).load(urlRandomImageBackdrop).centerCrop()
                .placeholder(R.drawable.cover_img_album_placeholder)
                .into(backdrop);
        Log.v(TAG, "nombre de musique album = " + musiques.length);
        listMusique.addAll(Arrays.asList(musiques));
        adapterMusique.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {

       /* Musique musique = listMusique.get(position);
        Log.v(TAG, "Recycleview music " + musique.getId() + musique.getTitre());
        Log.v(TAG, "Recycleview music from Menu ? " + ((RecyclerView)view.getParent()).getTag() + "type = " + view.getParent().getClass());


        Object tagRecyclerView = ((RecyclerView)view.getParent()).getTag();
        if(null != tagRecyclerView){
            int posSelectedMusic = position;
            Log.v(TAG,"position = " + posSelectedMusic);
            Intent in = new Intent(this, MusicPlayerActivity.class);
            in.putExtra(POS_MUSIC_TO_PLAY, posSelectedMusic);
            setResult(RESULT_OK, in);
            Log.v(TAG, " in setResult");
            finish();
        }else{*/
            //ouvrir le lecteur et jouer la musique selectionnee
            Intent intent = new Intent(this, MusicPlayerActivity.class);
            intent.putExtra(POS_MUSIC_TO_PLAY, position);
            intent.putParcelableArrayListExtra(PLAYLIST_EXTRA, listMusique);
            startActivity(intent);
        //}

    }
}
