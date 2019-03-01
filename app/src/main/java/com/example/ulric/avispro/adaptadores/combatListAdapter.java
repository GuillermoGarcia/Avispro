package com.example.ulric.avispro.adaptadores;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.actividades.ListActivity;
import com.example.ulric.avispro.modelos.Combate;
import com.example.ulric.avispro.modelos.Combatiente;
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;

import java.util.List;

import io.grpc.internal.SharedResourceHolder;


public class combatListAdapter extends RecyclerView.Adapter<combatListAdapter.combatListHolder> {
  private int layout;
  private List<Combate> data;
  private combatListAdapter.OnItemClickListener listener;

  private Context contexto;

  private int position;

  private String idPersonaje;
  private String idUsuario;

  /**
   * Constructor de la clase tipo adapter
   * @param contexto      contexto del adaptador.
   * @param layout        tipo de layout que se usará para mostrar los elementos.
   * @param data          array con los elementos a mostrar.
   * @param listener      listener a vincular a cada elemento
   * @param idPersonaje   identificador del personaje
   * @param idUsuario     identificador del usuario
   */
  public combatListAdapter(Context contexto, int layout, List<Combate> data,
                           combatListAdapter.OnItemClickListener listener, String idPersonaje,
                           String idUsuario) {
    this.layout = layout;
    this.data = data;
    this.listener = listener;
    this.contexto = contexto;
    this.idPersonaje = idPersonaje;
    this.idUsuario = idUsuario;
  }

  /**
   * @param viewGroup conjunto de vistas (RecyclerView) donde se añadirán las del layout que se
   * nos proporcionó en el constructor.
   * @param viewType define el tipo de la nueva vista
   * @return un objeto de tipo combatListHolder
   */
  @NonNull
  @Override
  public combatListAdapter.combatListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    // Devolvemos un holder tras inflar el layout del contexto
    return new combatListAdapter.combatListHolder(LayoutInflater.from(viewGroup.getContext())
        .inflate(this.layout, viewGroup, false));
  }

  /**
   * Vinculamos cada elemento del array data al holder
   * @param combatListHolder holder a vincular
   * @param pos posición del elemento a vincular
   *
   */
  @Override
  public void onBindViewHolder(@NonNull combatListAdapter.combatListHolder combatListHolder, int pos) {
    combatListHolder.bindItem(this.data.get(pos), this.listener) ;
  }

  /**
   * @return cantidad de elementos gestionados por el adaptador.
   */
  @Override
  public int getItemCount() { return this.data.size() ; }

  /**
   * @return posición del elemento
   */
  public int getPosition(){ return this.position; }

  /**
   * Clase Holder de la lista de combates
   */
  public class combatListHolder extends RecyclerView.ViewHolder {

    private TextView  nombre, nombreMaster, numeroPJ, participa, master;

    /**
     * Holder Class Creator
     * @param combatView view of the holder
     */
    public combatListHolder(@NonNull final View combatView) {
      super(combatView);

      nombreMaster =    combatView.findViewById(R.id.masterName);
      nombre =    combatView.findViewById(R.id.combatName);
      numeroPJ =  combatView.findViewById(R.id.countPJ);
      participa = combatView.findViewById(R.id.joined);
      master =    combatView.findViewById(R.id.master);

      //
      final ConstraintLayout layout = combatView.findViewById(R.id.combatData);
      float dp = 33f;
      Resources r = contexto.getResources();
      DisplayMetrics metrics = r.getDisplayMetrics();
      final float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
      final ViewTreeObserver observer = layout.getViewTreeObserver();
      final ViewTreeObserver.OnGlobalLayoutListener vto = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
        }
      };

      observer.addOnGlobalLayoutListener(vto);

      combatView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          position = getAdapterPosition();
          return false;
        }
      });

    }

    /**
     * Vincular un listener a un elemento
     * @param combate elemento a vincular
     * @param listener listener a vincular
     */
    public void bindItem(final Combate combate, final combatListAdapter.OnItemClickListener listener) {

      Log.d("Item Nombre: ", combate.getNombre() + "");
      nombre.setText(combate.getNombre());
      nombreMaster.setText(combate.getMaster());
      numeroPJ.setText(String.format("%s", combate.getIdPjs().size()));
      if ((idPersonaje != null) && (combate.estaUnido(idPersonaje))) {
        participa.setVisibility(View.VISIBLE);
      }
      if (combate.esMaster(idUsuario)) {
        master.setVisibility(View.VISIBLE);
      }
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) { listener.onItemClick(combate, getAdapterPosition()); }
      });

    }

  }

  /**
   * Implementamos una interface que nos permitirá definir el evento que responderá
   * a una pulsación del usuario sobre uno de los ítems del RecyclerView. El evento
   * recibirá el ítem sobre el que hemos pulsado y la posición de éste en el adaptador
   */
  public interface OnItemClickListener {
    void onItemClick(Combate combate, int position) ;
  }

  /**
   * Borrar personaje de la lista de participantes.
   * @param id, position of the item
   * @param pj, id of the pj to remove
   */
  public void dejarCombate(int id, Combatiente pj){
    data.get(id).dejarCombate(pj);
    data.remove(id);
    notifyItemRemoved(id);
  }
}
