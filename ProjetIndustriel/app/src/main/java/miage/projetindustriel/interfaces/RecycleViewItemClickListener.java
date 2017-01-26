package miage.projetindustriel.interfaces;

import android.view.View;

/**
 * Created by utilisateur on 09/01/2017.
 */

/**
 * Interface permettant d'ajouter des cliks sur les items du RecycleView
 * car cela n'existe pas par defaut
 */
public interface RecycleViewItemClickListener {
    void onClick(View view, int position);
}
