package miage.projetindustriel.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import miage.projetindustriel.R;
import miage.projetindustriel.adapter.MusiqueAdapter;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Musique;
import miage.projetindustriel.utility.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicAlbumFragment.OnFragmentMusicAlbumInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicAlbumFragment extends Fragment implements RecycleViewItemClickListener {


    private OnFragmentMusicAlbumInteractionListener mListener;


    private String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private ImageView backdrop;
    private MusiqueAdapter adapterMusique;
    ArrayList<Musique> listMusique = new ArrayList<>();
    public static final String MUSIQUE_ARGS = "musiquesAlbum";
    private View rootView;

    public MusicAlbumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MusicAlbumFragment newInstance(Musique[] musiques) {
        MusicAlbumFragment fragment = new MusicAlbumFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(MUSIQUE_ARGS, musiques);
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Musique[] musiques = (Musique[]) getArguments().getParcelableArray(MUSIQUE_ARGS);
            listMusique.addAll(Arrays.asList(musiques));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_music_album, container, false);

        //Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        initCollapsingToolbar();

        //listMusique = new ArrayList<>();
        Log.v(TAG, "nb Musique "+listMusique.size());
        adapterMusique = new MusiqueAdapter(getActivity(), listMusique);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        backdrop = (ImageView)rootView.findViewById(R.id.backdrop);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMusique);
        adapterMusique.setRecycleViewItemClickListener(this);

        if(null != listMusique){
            //charger l'image du backdrop
            Random random = new Random();
            int randomIndice = random.nextInt(listMusique.size());
            //prendre une image d'une des musiques
            String urlRandomImageBackdrop = listMusique.get(randomIndice).getCoverPhoto();
            Glide.with(this).load(urlRandomImageBackdrop).centerCrop()
                    .placeholder(R.drawable.cover_img_album_placeholder)
                    .into(backdrop);
        }

        return rootView;

    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);

    }*/

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
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

    /*@Override
    public void onLoadMusiqueAlbumDataFinish(Musique[] musiques) {
        if (null == musiques){
            Toast.makeText(getActivity(),"Aucune musique trouvée", Toast.LENGTH_LONG);
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
    }*/

    //Mettre a jour l'adaptater lorsque ce fragment sera charge
    public void updateAdapterMusicAlbum(Musique[] musiques){
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

    //click sur une musique
    @Override
    public void onClick(View view, final int position) {

        Musique musique = listMusique.get(position);
        onMusicAlbumPressed(listMusique, position);

        /*Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // remplacer le fragment de l'activity par le fragment selectionne
                //Fragment fragment = getFragmentSelectedMenu();
                Fragment fragment = MusicPlayerFragment.newInstance(position);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragment_container, fragment, "MusicPlayer");
                fragmentTransaction.commitAllowingStateLoss();
                //fragmentTransaction.addToBackStack(null);
               *//* if (!fragAddedToBackStack) {
                    fragmentTransaction.addToBackStack(null);
                    fragAddedToBackStack = true;
                    Log.v(TAG, "finish added " + fragAddedToBackStack);
                }

                //fragmentTransaction.commit();
                TAG_FRAG_COURANT = TAG_FRAG_MUSIC_ALBUM;*//*
            }
        }

        Handler mHandler = new Handler();
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        };*/
        /*Log.v(TAG, "Recycleview music from Menu ? " + ((RecyclerView)view.getParent()).getTag() + "type = " + view.getParent().getClass());


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
        /*Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra(POS_MUSIC_TO_PLAY, position);
        intent.putParcelableArrayListExtra(PLAYLIST_EXTRA, listMusique);
        startActivity(intent);*/
        //}

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onMusicAlbumPressed(ArrayList<Musique> listMusique, int positionMusicSelected) {
        if (mListener != null) {
            mListener.onFragmentMusicAlbumInteraction(listMusique, positionMusicSelected);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMusicAlbumInteractionListener) {
            mListener = (OnFragmentMusicAlbumInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentMusicAlbumInteractionListener {
        // TODO: Update argument type and name
        void onFragmentMusicAlbumInteraction(ArrayList<Musique> listMusique, int positionMusicSelected);
    }

}
