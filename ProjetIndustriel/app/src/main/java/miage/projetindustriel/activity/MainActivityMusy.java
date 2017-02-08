package miage.projetindustriel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import miage.projetindustriel.R;
import miage.projetindustriel.fragment.AlbumFragment;
import miage.projetindustriel.fragment.MapFragment;
import miage.projetindustriel.fragment.MusicAlbumFragment;
import miage.projetindustriel.fragment.MusicPlayerFragment;
import miage.projetindustriel.model.Musique;
import miage.projetindustriel.utility.CircleTransform;

public class MainActivityMusy extends AppCompatActivity implements
        AlbumFragment.OnAlbumFragmentInteractionListener,
        MapFragment.OnFragmentMapInteractionListener,
        MusicAlbumFragment.OnFragmentMusicAlbumInteractionListener,
        MusicPlayerFragment.OnFragmentMusicPlayerInteractionListener{

    private final String TAG = getClass().getSimpleName();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imageViewPhoto, imageViewNavHeaderBg;
    private TextView textViewNomPrenom, textViewEmail;
    private Toolbar toolbar;
    //private FloatingActionButton fab;
    MaterialSearchView searchView ;


    //les tags pour identifier les fragments
    private static final String TAG_FRAG_MUSIQUE = "album";
    private static final String TAG_FRAG_MAP = "map";
    private static final String TAG_FRAG_MUSIC_ALBUM = "musicAlbum";
    public static String TAG_FRAG_COURANT = TAG_FRAG_MUSIQUE;

    private boolean chargerFragAccueilOnBackPress = true;
    private boolean fragPlayerAddedToBackStack = false;
    private boolean fragAddedToBackStack = false;
    private Handler mHandler;

    // index du menu courant selectionne dans le nav_drawer
    public static int navMenuSelectedIndex = 0;
    private String[] titresAcitvity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_musy);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.showVoice(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(TAG, "onQueryTextSubmit");
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                //searchView.
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v(TAG, "onQueryTextChange");
                //Toast.makeText(getApplicationContext(), "onQueryTextChange", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //charger ici les titre des musiques depuis la base pour faire
                //des suggestions
                //Toast.makeText(getApplicationContext(), "searchViewShown", Toast.LENGTH_LONG).show();
                Log.v(TAG, "searchViewShown");
            }

            @Override
            public void onSearchViewClosed() {
                Log.v(TAG, "searchViewClose");
                //Toast.makeText(getApplicationContext(), "searchViewClose", Toast.LENGTH_LONG).show();
            }
        });

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        //les elements du navigation header
        navHeader = navigationView.getHeaderView(0);
        textViewNomPrenom = (TextView) navHeader.findViewById(R.id.nom_prenom);
        textViewEmail = (TextView) navHeader.findViewById(R.id.email);
        imageViewPhoto = (ImageView) navHeader.findViewById(R.id.img_photo_profile);
        imageViewNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

        // chargement des titres depuis le fichier resource strings.xml
        titresAcitvity = getResources().getStringArray(R.array.titres_nav_item);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // load nav menu header data
        initialiserDonneesNavigationHeader();

        // initialiser navigation menu
        initialiserNavigationMenu();

        if (savedInstanceState == null) {
            navMenuSelectedIndex = 0;
            TAG_FRAG_COURANT = TAG_FRAG_MUSIQUE;
            afficherFragementSelectedMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private void initialiserDonneesNavigationHeader() {

        textViewNomPrenom.setText("Nom Prénom");
        textViewEmail.setText("email@example.com");
        // loading header background image
        Glide.with(this).load(R.drawable.nav_menu_header_bg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewNavHeaderBg);

        Glide.with(this).load(R.drawable.ic_profile)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewPhoto);
    }


    /**
     * Ajoute le listener sur les menus du navigation
     * drawer
     */
    private void initialiserNavigationMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_music:
                        navMenuSelectedIndex = 0;
                        TAG_FRAG_COURANT = TAG_FRAG_MUSIQUE;
                        break;
                    case R.id.nav_map:
                        navMenuSelectedIndex = 1;
                        TAG_FRAG_COURANT = TAG_FRAG_MAP;
                        break;
                    case R.id.nav_deconnexion:
                        navMenuSelectedIndex = 2;
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_share:
                        //navMenuSelectedIndex = 3;
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_send:
                        //navMenuSelectedIndex = 4;
                        drawer.closeDrawers();
                        break;
                    default:
                        navMenuSelectedIndex = 0;

                }
                //verifie si l'etat est checked sinon changer l'etat
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                afficherFragementSelectedMenu();
                //effacer le backstack
                FragmentManager fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            return;
        } else {
            super.onBackPressed();
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        // Afficher le fragment musique lorsque le button retour est appuye
        // si l'utilisateur se trouve sur un autre fragment
        if (chargerFragAccueilOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navMenuSelectedIndex != 0) {
                navMenuSelectedIndex = 0;
                TAG_FRAG_COURANT = TAG_FRAG_MUSIQUE;
                afficherFragementSelectedMenu();
                return;
            }
        }

        //si le fragment (MusicAlbumFragment ici) a deja ete
        //ajoute apres un backPress on le supprime
        if (fragAddedToBackStack) {
            fragAddedToBackStack = false;
        }

        if (fragPlayerAddedToBackStack) {
            fragPlayerAddedToBackStack = false;
        }

        Log.v(TAG, "finish " + getSupportFragmentManager().getBackStackEntryCount());
        super.onBackPressed();


    }

    /**
     * Charge le fragment correspondant au menu selectionne
     * dans la navigation drawer
     */
    private void afficherFragementSelectedMenu() {

        //Marquer le menu selectionne comme checked
        navigationView.getMenu().getItem(navMenuSelectedIndex).setChecked(true);
        //Affiche le titre correspondant
        getSupportActionBar().setTitle(titresAcitvity[navMenuSelectedIndex]);

        // Si on selectionne le meme menu, ne rien faire
        // just fermer la navigation drawer
        /*Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(TAG_FRAG_COURANT);
        if(null != fragmentById && fragmentByTag != null){
            if(fragmentById.getClass().getSimpleName().equals(fragmentByTag.getClass().getSimpleName())){
                drawer.closeDrawers();
                Log.v(TAG, "FragCourant  = " + TAG_FRAG_COURANT +
                "byId =" + fragmentById.getClass().getSimpleName() +
                "byTag =" + fragmentByTag.getClass().getSimpleName());
                // afficher or masque le button fab
                afficherIconFab();
                return;
            }
        }*/
        //Log.v("fragDisp" , fra.getClass().getSimpleName() + " " + TAG_FRAG_COURANT);
        if (getSupportFragmentManager().findFragmentByTag(TAG_FRAG_COURANT) != null) {
            drawer.closeDrawers();
            Log.v(TAG, "FragCourant not null = " + TAG_FRAG_COURANT);
            // afficher or masque le button fab
            //afficherIconFab();
            //return;
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        //Un remplacement des fragments en utilisant un runnable
        //pour faciliter le chargement dans le cas ou le fragement a
        //beaucoup de donnees a charger
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // remplacer le fragment de l'activity par le fragment selectionne
                Fragment fragment = getFragmentSelectedMenu();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_FRAG_COURANT);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // afficher ou masquer the fab button
        //afficherIconFab();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getFragmentSelectedMenu() {

        switch (navMenuSelectedIndex) {
            case 0:
                AlbumFragment musiqueFragment = new AlbumFragment();
                return musiqueFragment;
            case 1:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
            default:
                return new AlbumFragment();
        }
    }

    /*private void afficherIconFab() {
        if (navMenuSelectedIndex == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }*/

    @Override
    public void onFragmentMapInteraction(String uri) {
       /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        ((AlbumFragment)fragment).afficherParam();
        Log.v("MainActivity ", fragment.getClass().getSimpleName() + " : " + uri);*/
    }

    @Override
    public void onFragmentAlbumInteraction(final Musique[] musiques) {
        Log.v(TAG, "Nbre de musique Album = " + musiques.length);
        //MusicAlbumFragment musicAlbumFragment = MusicAlbumFragment.newInstance(musiques);

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // remplacer le fragment de l'activity par le fragment selectionne
                //Fragment fragment = getFragmentSelectedMenu();
                Fragment fragment = MusicAlbumFragment.newInstance(musiques);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_FRAG_COURANT);
                fragmentTransaction.commitAllowingStateLoss();
                if (!fragAddedToBackStack) {
                    fragmentTransaction.addToBackStack(null);
                    fragAddedToBackStack = true;
                    Log.v(TAG, "finish added " + fragAddedToBackStack);
                }

                //fragmentTransaction.commit();
                TAG_FRAG_COURANT = TAG_FRAG_MUSIC_ALBUM;
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


       /* FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, musicAlbumFragment);
        if (!fragAddedToBackStack) {
            fragmentTransaction.addToBackStack(null);
            fragAddedToBackStack = true;
            Log.v(TAG, "finish added " + fragAddedToBackStack);
        }

        fragmentTransaction.commit();
        TAG_FRAG_COURANT = TAG_FRAG_MUSIC_ALBUM;
        */
    }

    @Override
    public void onFragmentMusicAlbumInteraction(final ArrayList<Musique> listMusique, final int positionMusicSelected) {
        Log.v(TAG, " music " +  positionMusicSelected);
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // remplacer le fragment de l'activity par le fragment selectionne
                //Fragment fragment = getFragmentSelectedMenu();
                Fragment fragment = MusicPlayerFragment.newInstance(listMusique, positionMusicSelected);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragment_container, fragment, "MusicPlayer");
                fragmentTransaction.commitAllowingStateLoss();
                //fragmentTransaction.addToBackStack(null);
                //Log.v(TAG, "finish added fragPlayer Ext " + fragPlayerAddedToBackStack);
                if (!fragPlayerAddedToBackStack) {
                    fragmentTransaction.addToBackStack(null);
                    fragPlayerAddedToBackStack = true;
                    Log.v(TAG, "finish added fragPlayer " + fragPlayerAddedToBackStack);
                }

                //fragmentTransaction.commit();
                //TAG_FRAG_COURANT = TAG_FRAG_MUSIC_ALBUM;
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        };
    }

    @Override
    public void onFragmentMusicPlayerInteraction(String uri) {

    }
}
