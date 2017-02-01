package miage.projetindustriel.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import miage.projetindustriel.R;
import miage.projetindustriel.model.Musique;
import miage.projetindustriel.utility.Constantes;
import miage.projetindustriel.utility.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicPlayerFragment.OnFragmentMusicPlayerInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {


    private OnFragmentMusicPlayerInteractionListener mListener;

    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private ImageView imgBackground;
    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private final String TAG = getClass().getSimpleName();
    private Utilities utils;
    private int currentSongIndex = 0; // indice de la musique encours
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean mpIsPlayerRelease = true;
    private ArrayList<Musique> playList = new ArrayList<>();
    private View rootView;

    private static final String LIST_MUSIQUE_ARGS = "listMusiqueAlbum";
    private static final String POS_MUSIQUE_SELECTED_ARGS = "posSelectedMusique";

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MusicPlayerFragment newInstance(ArrayList<Musique> listMusique, int positionSelectedMusique) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_MUSIQUE_ARGS, listMusique);
        args.putInt(POS_MUSIQUE_SELECTED_ARGS, positionSelectedMusique);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playList = getArguments().getParcelableArrayList(LIST_MUSIQUE_ARGS);
            currentSongIndex = getArguments().getInt(POS_MUSIQUE_SELECTED_ARGS);
            //Musique[] musiques = (Musique[]) getArguments().getParcelableArray(MUSIQUE_ARGS);
            //listMusique.addAll(Arrays.asList(musiques));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_player, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        // All player buttons
        imgBackground = (ImageView) rootView.findViewById(R.id.img_background);
        btnPlay = (ImageButton) rootView.findViewById(R.id.btnPlay);
        btnNext = (ImageButton) rootView.findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) rootView.findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) rootView.findViewById(R.id.btnPlaylist);
        btnRepeat = (ImageButton) rootView.findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) rootView.findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) rootView.findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) rootView.findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) rootView.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) rootView.findViewById(R.id.songTotalDurationLabel);

        mp = new MediaPlayer();
        utils = new Utilities();
        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important
        //Jouer la musique selectionnee
        playMusique(currentSongIndex);


        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(
                                R.drawable.btn_play);
                    }
                } else {
                    // Resume song
                    if (mp != null) {
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });


        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (playList.size() - 1)) {
                    playMusique(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playMusique(0);
                    currentSongIndex = 0;
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playMusique(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playMusique(playList.size() - 1);
                    currentSongIndex = playList.size() - 1;
                }

            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getActivity(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getActivity(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getActivity(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getActivity(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

        /**
         * Button Click event for Play list click event
         * Launches list activity which displays list of songs
         * */
        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*Intent intent = new Intent(MusicPlayerActivity.this, PlayListPlayerMenuActivity.class);

                intent.putParcelableArrayListExtra(PlayListActivity.PLAYLIST_EXTRA, playList);
                startActivityForResult(intent, RESULT_OK);*/

               /* Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
                startActivityForResult(i, 100);*/
            }
        });

        return rootView;
    }

    private String buildMusiqueUrl(String urlMusique) {

        Uri builtUri = Uri.parse(Constantes.BASE_URL_SERVER);
        builtUri = Uri.withAppendedPath(builtUri, urlMusique).buildUpon()
                .build();

        return builtUri.toString();
    }

    /**
     * Function to play a song
     *
     * @param positionMusique - index of song
     */

    private void playMusique(int positionMusique) {

        Musique musique = playList.get(positionMusique);
        String url = buildMusiqueUrl(musique.getUrlSon());
        String urlImage = playList.get(positionMusique).getCoverPhoto();
        //Log.v("UrlImage", urlImage);
        Glide.with(this).load(urlImage)
                .placeholder(R.drawable.adele)
                .into(imgBackground);

        try {
            mp.reset();
            mp.setDataSource(url);
            mp.prepare();
            mp.start();
            mpIsPlayerRelease = false;
            // Displaying Song title
            String songTitle = musique.getTitre();
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            if (mpIsPlayerRelease) {
                return;
            }
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * Lorsque la lecture d'une musique se termine
     * si repeat est ACTIVE jouer la meme musique
     * si shuffle est ACTIVE jouer une musique aleatoirement
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playMusique(currentSongIndex);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((playList.size() - 1) - 0 + 1) + 0;
            playMusique(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (playList.size() - 1)) {
                playMusique(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                playMusique(0);
                currentSongIndex = 0;
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentMusicPlayerInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMusicPlayerInteractionListener) {
            mListener = (OnFragmentMusicPlayerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(null != mp){
            mp.release();
            mpIsPlayerRelease = true;
        }

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
    public interface OnFragmentMusicPlayerInteractionListener {
        // TODO: Update argument type and name
        void onFragmentMusicPlayerInteraction(String uri);
    }

}
