package com.example.ulric.avispro.adaptadores;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Habilidad;
import com.example.ulric.avispro.modelos.Personaje;

import java.util.ArrayList;
import java.util.List;

public class skillsListAdapter  extends RecyclerView.Adapter<skillsListAdapter.skillsListHolder> {
  private int layout;
  private List<Habilidad> data;
  private skillsListAdapter.OnItemClickListener listener;

  private Context contexto;
  private int position;


  public skillsListAdapter(Context contexto, int layout, List<Habilidad> data,
                           skillsListAdapter.OnItemClickListener listener) {
    this.layout =   layout;
    this.data =     data;
    this.listener = listener;
    this.contexto = contexto;
  }

    /**
     * @param viewGroup, conjunto de vistas (RecyclerView) donde se añadirán las del layout que se
     * nos pasó en el constructor.
     * @param viewType, define el tipo de la nueva vista
     * @return un objeto de tipo skillsListHolder
     */
    @NonNull
    @Override
    public skillsListAdapter.skillsListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
      // Devolvemos un holder tras inflar el layout del contexto
      return new skillsListAdapter.skillsListHolder(LayoutInflater.from(viewGroup.getContext())
                 .inflate(this.layout, viewGroup, false));
    }

    /**
     * Asociar el holder con el ítem identificado por su posición
     * @param holder, recibe un determinado holder
     * @param pos, identifica un determinado ítem dentro del conjunto de datos que maneja el adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull skillsListAdapter.skillsListHolder holder, int pos) {
      // Asociamos al holder el ítem (habilidad) y el listener que deberá estar
      // atento a si el usuario selecciona ese elemento.
      holder.bindItem(this.data.get(pos), this.listener) ;
    }

    /**
     * @return int, número de elementos gestionados por el adaptador.
     */
    @Override
    public int getItemCount() { return this.data.size() ; }

    /**
     * @return int, la actual posición dentro de la lista de un elemento
     */
    public int getPosition(){ return this.position; }

    public class skillsListHolder extends RecyclerView.ViewHolder {

      private TextView total, base, bp, bs, nombre;

      public skillsListHolder(@NonNull final View habilidadView) {
        super(habilidadView);

        total =  habilidadView.findViewById(R.id.skillTotalValue);
        base =   habilidadView.findViewById(R.id.skillBaseValue);
        bp =     habilidadView.findViewById(R.id.skillPrimaryBonusValue);
        bs =     habilidadView.findViewById(R.id.skillSecundaryBonusValue);
        nombre = habilidadView.findViewById(R.id.skillName);

        habilidadView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            position = getAdapterPosition();
            return false;
          }
        });
      }

      /**
       * Ponemos la información de la habilidad en la vista y la asociamos a un listener
       * @param habilidad, habilidad que contiene la información
       * @param listener, listener pata la pulsación del item
       */
      public void bindItem(final Habilidad habilidad, final skillsListAdapter.OnItemClickListener listener) {

        Log.d("Item Nombre: ",String.format("%s", habilidad.getNombre()));
        nombre.setText(String.format("%s", habilidad.getNombre()));
        base.setText(String.format("%s", habilidad.getValorBase()));
        total.setText(String.format("%s", habilidad.obtenerValorTotal()));
        bp.setText(String.format("%s", habilidad.obtenerValorBonusPrincipal()));
        bs.setText(String.format("%s", habilidad.obtenerValorBonusSecundario()));
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(habilidad, getAdapterPosition());
          }
        });
      }
    }

    /**
     * Implementamos una interface que nos permitirá definir el evento que responderá
     * a una pulsación del usuario sobre uno de los ítems del RecyclerView. El evento
     * recibirá el ítem sobre el que hemos pulsado y la posición de éste en el adaptador
     */
    public interface OnItemClickListener {
      void onItemClick(Habilidad habilidad, int position) ;
    }

  /**
   * Borrar un elemento, habilidad, del listado
   * @param pos, posición del elemento dentro de la lista.
   * @param p, personaje al que está asociado la habilidad
   */
    public void borrarHabilidad(int pos, Personaje p){
      p.borrarHabilidadPersonaje(data.get(pos).getIdHabilidadPersonaje());
      data.get(pos).borrarHabilidad();
      data.remove(pos);
      notifyItemRemoved(pos);
    }

}
