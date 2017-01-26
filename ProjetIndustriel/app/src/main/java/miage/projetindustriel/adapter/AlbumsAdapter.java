package miage.projetindustriel.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import miage.projetindustriel.R;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Album;


public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder>{

    private Context mContext;
    private List<Album> albumList;
    private RecycleViewItemClickListener recycleViewItemClickListener;

    private String TAG = getClass().getSimpleName();

    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    //setter du listener du recycle view
    public void setRecycleViewItemClickListener(RecycleViewItemClickListener recycleViewItemClickListener) {
        this.recycleViewItemClickListener = recycleViewItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getNom());
        holder.count.setText(album.getNbSons() + " sons");

        // loading album cover using Glide library
        String urlImage = album.getCoverImage();
        Glide.with(mContext).load(urlImage).placeholder(R.drawable.cover_img_album_placeholder).into(holder.coverImage);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Afficher le popup menu Lorsqu'on clique sur les 3 points d'un album
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener pour les items du popup menu
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Ajouter aux favoris", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Musique suivante", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    /**
     * View holder des items du recycle view avec un click listener
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, count;
        public ImageView coverImage, overflow;

        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            coverImage = (ImageView) view.findViewById(R.id.cover_image);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(null != recycleViewItemClickListener){
                //un delegate vers la classe implementant l'interface RecycleViewItemClickListener
                recycleViewItemClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

}
