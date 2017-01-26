package miage.projetindustriel.activities;

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
import java.util.List;
import java.util.Random;


import miage.projetindustriel.R;
import miage.projetindustriel.adapter.AlbumsAdapter;
import miage.projetindustriel.connexion.FetchAlbumsInfos;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Album;
import miage.projetindustriel.musicplayer.PlayListActivity;
import miage.projetindustriel.utility.GridSpacingItemDecoration;

/**
 * Created by utilisateur on 08/01/2017.
 */

public class AlbumsMainActivity extends AppCompatActivity implements FetchAlbumsInfos.AsyncAlbumData,
        RecycleViewItemClickListener {


    private String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;
    private ImageView backdrop;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    public static final String ALBUM_ID_EXTRA = "albumId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid_albums);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        backdrop = (ImageView)findViewById(R.id.backdrop);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setRecycleViewItemClickListener(this);
        /*try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //L'implementation de cette methode est faite dans
                //la methode onClick en bas
                Log.v(TAG, v.getClass().getName());
            }
        });


        //Chercher les albums
        FetchAlbumsInfos fetchAlbumsInfos = new FetchAlbumsInfos();
        fetchAlbumsInfos.delegateAlbumData = this;
        fetchAlbumsInfos.execute();

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
    public void onLoadAlbumDataFinish(Album[] albums) {
        if(null == albums){
            Toast.makeText(this,"Aucun album trouvé", Toast.LENGTH_LONG);
            return;
        }
        Random random = new Random();
        int randomIndice = random.nextInt(albums.length);
        String urlRandomImageBackdrop = albums[randomIndice].getCoverImage();

        Glide.with(this).load(urlRandomImageBackdrop)
                .placeholder(R.drawable.cover_img_album_placeholder)
                .into(backdrop);
        Log.v(TAG, albums.length + " length");
        albumList.addAll(Arrays.asList(albums));
        adapter.notifyDataSetChanged();
    }

    //Lorsqu'un item du recycle view est clique (ici sur un album)
    @Override
    public void onClick(View view, int position) {

        //To be continued === Lancer l'activity avec les musique de l'album
        //Log.v(TAG, view.getClass().getName() + " pos = " +albumList.get(position).getNom());
        Intent intentPlayList = new Intent(this, PlayListActivity.class);

        Album album = albumList.get(position);
        intentPlayList.putExtra(ALBUM_ID_EXTRA, album.getId());

        startActivity(intentPlayList);
    }
}
