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
import android.view.View;
import android.view.ViewGroup;
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
import miage.projetindustriel.connexion.FetchMusicsAlbum;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Album;
import miage.projetindustriel.model.Musique;
import miage.projetindustriel.utility.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnAlbumFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment implements FetchAlbumsInfos.AsyncAlbumData,
        FetchMusicsAlbum.AsyncMusiqueAlbumData, RecycleViewItemClickListener {


    private OnAlbumFragmentInteractionListener mListener;

    private final String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;
    private ImageView backdrop;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private View rootView;

    public AlbumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_album, container, false);

        //Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        initCollapsingToolbar();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        backdrop = (ImageView)rootView.findViewById(R.id.backdrop);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getActivity(), albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setRecycleViewItemClickListener(this);

        //Chercher les albums
        FetchAlbumsInfos fetchAlbumsInfos = new FetchAlbumsInfos();
        fetchAlbumsInfos.delegateAlbumData = this;
        fetchAlbumsInfos.execute();

        return rootView;

    }

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


    @Override
    public void onLoadAlbumDataFinish(Album[] albums) {
        if(null == albums){
            Toast.makeText(getActivity(),"Aucun album trouvé", Toast.LENGTH_LONG);
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

    @Override
    public void onLoadMusiqueAlbumDataFinish(Musique[] musiques) {
        if (null == musiques){
            Toast.makeText(getActivity(),"Aucune musique trouvée", Toast.LENGTH_LONG);
            return;
        }
        onAlbumPressed(musiques);

    }

    //Lorsqu'un item du recycle view est clique (ici sur un album)
    @Override
    public void onClick(View view, int position) {

       /* chercher les musiques de l'album selectionnee
        et les envoyes au fragment MusiqueAlbumFragment
        en utilisant le delegate onLoadMusiqueAlbumDataFinish*/
        Album album = albumList.get(position);
        FetchMusicsAlbum fetchMusicsAlbum = new FetchMusicsAlbum();
        fetchMusicsAlbum.delegateMusiqueAlbumData = this;
        fetchMusicsAlbum.execute(album.getId());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onAlbumPressed(Musique[] musique) {
        if (mListener != null) {
            mListener.onFragmentAlbumInteraction(musique);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumFragmentInteractionListener) {
            mListener = (OnAlbumFragmentInteractionListener) context;
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
    public interface OnAlbumFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentAlbumInteraction(Musique[] musique);
    }

}
