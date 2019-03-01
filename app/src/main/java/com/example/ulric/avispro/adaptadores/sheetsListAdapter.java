package com.example.ulric.avispro.adaptadores;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
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
import com.example.ulric.avispro.modelos.Personaje;
import com.example.ulric.avispro.modelos.Usuario;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.grpc.internal.SharedResourceHolder;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class sheetsListAdapter extends RecyclerView.Adapter<sheetsListAdapter.sheetsListHolder> {

  private int layout;
  private List<Personaje> data;
  private OnItemClickListener listener;

  private Context contexto;

  private int position;


  public sheetsListAdapter(Context contexto, int layout, List<Personaje> data,
                           OnItemClickListener listener) {
    this.layout = layout;
    this.data = data;
    this.listener = listener;
    this.contexto = contexto;
  }

  /**
   * @param viewGroup, conjunto de vistas (RecyclerView) donde se añadirán las del layout que se
   *                   nos pasó en el constructor.
   * @param viewType,  define el tipo de la nueva vista
   * @return un objeto de tipo sheetsListHolder
   */
  @NonNull
  @Override
  public sheetsListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    // Devolvemos un holder tras inflar el layout del contexto
    return new sheetsListHolder(LayoutInflater.from(viewGroup.getContext())
        .inflate(this.layout, viewGroup, false));
  }

  /**
   * @param sheetsListHolder, recibe un determinado holder
   * @param pos,              identifica un determinado ítem dentro del conjunto de datos que maneja el adaptador.
   */
  @Override
  public void onBindViewHolder(@NonNull sheetsListHolder sheetsListHolder, int pos) {

    // Asociamos al holder el ítem (película) y el listener que deberá estar
    // atento a si el usuario selecciona ese elemento.
    sheetsListHolder.bindItem(this.data.get(pos), this.listener);
  }

  /**
   * @return int, número de elementos gestionados por el adaptador.
   */
  @Override
  public int getItemCount() {
    // Devolvemos el número de elementos que está gestionando el adaptador,
    // y que se encuentran almacenados en la propiedad DATA.
    return this.data.size();
  }

  public int getPosition() {
    return this.position;
  }

  public class sheetsListHolder extends RecyclerView.ViewHolder {

    private ImageView avatar;
    private TextView nombre;
    private TextView nivel;
    private TextView raza;

    public sheetsListHolder(@NonNull final View personajeView) {
      super(personajeView);

      avatar = personajeView.findViewById(R.id.iavatar);
      nombre = personajeView.findViewById(R.id.characterName);
      nivel = personajeView.findViewById(R.id.characterLevel);
      raza = personajeView.findViewById(R.id.characterRace);

      //
      final ConstraintLayout layout = personajeView.findViewById(R.id.characterData);
      float dp = 33f;
      Resources r = contexto.getResources();
      DisplayMetrics metrics = r.getDisplayMetrics();
      final float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
      final ViewTreeObserver observer = layout.getViewTreeObserver();
      final ViewTreeObserver.OnGlobalLayoutListener vto = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          if (raza.getWidth() != (layout.getWidth() - avatar.getWidth() - (int) px)) {
            raza.setWidth(layout.getWidth() - avatar.getWidth() - (int) px);
            Log.d("Log", "Width: " + raza.getWidth() + " - Width: " + (layout.getWidth() - avatar.getWidth()));
          }
        }
      };

      observer.addOnGlobalLayoutListener(vto);

      personajeView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          position = getAdapterPosition();
          return false;
        }
      });

    }


    public void bindItem(final Personaje personaje, final OnItemClickListener listener) {

      nombre.setText(personaje.getNombre());
      nivel.setText(String.format("%s %s", "Nivel", personaje.getNivel()));
      raza.setText(String.format("%s (%s)", personaje.getRaza(), personaje.getCultura()));
      if (!personaje.getAvatar().equals("")) {
        Uri avatarUri = personaje.conseguirAvatarUri(contexto);
        Picasso.with(contexto).load(avatarUri)
            .transform(new CropCircleTransformation()).fit().centerCrop().into(avatar);
      }

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onItemClick(personaje, getAdapterPosition());
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
    void onItemClick(Personaje character, int position);
  }

  /**
   * Borrar el elemento, personaje, de la posición 'pos' de la lista
   *
   * @param pos,     posición del elemento dentro de la lista
   * @param usuario, usuario al que pertenece el elemento, personaje.
   */
  public void borrarPersonaje(int pos, Usuario usuario) {
    usuario.borrarPersonaje(data.get(pos).getIdPersonaje());
    data.get(pos).borrarPersonaje();
    data.remove(pos);
    notifyDataSetChanged();
  }

}
