package miage.projetindustriel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import miage.projetindustriel.R;
import miage.projetindustriel.interfaces.RecycleViewItemClickListener;
import miage.projetindustriel.model.Musique;


public class MusiqueAdapter extends RecyclerView.Adapter<MusiqueAdapter.ViewHolder>{

    private Context mContext;
    private List<Musique> listMusique;
    private RecycleViewItemClickListener recycleViewItemClickListener;

    private String TAG = getClass().getSimpleName();

    public MusiqueAdapter(Context mContext, List<Musique> listMusique) {
        this.mContext = mContext;
        this.listMusique = listMusique;

    }

    //setter du listener du recycle view
    public void setRecycleViewItemClickListener(RecycleViewItemClickListener recycleViewItemClickListener) {
        this.recycleViewItemClickListener = recycleViewItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Musique musique = listMusique.get(position);
        holder.titre.setText(musique.getTitre());
        holder.duree.setText(musique.getDuree());

    }

    @Override
    public int getItemCount() {
        return listMusique.size();
    }

    /**
     * View holder des items du recycle view avec un click listener
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titre, duree;


        public ViewHolder(View view) {
            super(view);
            titre = (TextView)view.findViewById(R.id.tv_titre_son);
            duree = (TextView) view.findViewById(R.id.tv_duree_son);
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
