package com.example.ulric.avispro.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Personaje;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class sheetAdapter extends RecyclerView.Adapter<sheetAdapter.sheetHolder> {

  private int layout;
  private List<Personaje> data;
  private OnItemClickListener listener;

  private Context contexto;


  public sheetAdapter(Context contexto, int layout, List<Personaje> data, OnItemClickListener listener) {
    this.layout = layout;
    this.data = data;
    this.listener = listener;
    this.contexto = contexto;
  }

  /**
   * @param viewGroup
   * conjunto de vistas (RecyclerView) donde se añadirán las del layout que se nos pasó
   * en el constructor.
   * @param viewType
   * define el tipo de la nueva vista
   * @return
   * un objeto de tipo sheetHolder
   */
  @NonNull
  @Override
  public sheetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    // Obtenemos en primer lugar el inflador, que necesitará conocer el contexto
    // en el que vamos a inyectar los elementos de nuestro layout.
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext()) ;

    // Obtenido el inflador, inflamos el layout, esto es, añadimos las vistas de
    // éste a las que ya teníamos en viewGroup, generando una nueva vista.
    View vista = inflater.inflate(this.layout, null) ;

    // Creamos nuestro holder y le proporcionamos la vista obtenida anteriormente.
    sheetHolder holder = new sheetHolder(vista) ;

    // Devolvemos el holder.
    return holder ;
  }

  /**
   * @param sheetHolder
   * recibe un determinado holder
   * @param pos
   * identifica un determinado ítem dentro del conjunto de datos que maneja el
   * adaptador.
   */
  @Override
  public void onBindViewHolder(@NonNull sheetHolder sheetHolder, int pos) {

    // Asociamos al holder el ítem (película) y el listener que deberá estar
    // atento a si el usuario selecciona ese elemento.
    sheetHolder.bindItem(this.data.get(pos), this.listener) ;
  }

  /**
   * @return
   */
  @Override
  public int getItemCount() {
    // Devolvemos el número de elementos que está gestionando el adaptador,
    // y que se encuentran almacenados en la propiedad DATA.
    return this.data.size() ;
  }

  public class sheetHolder extends RecyclerView.ViewHolder {

    private ImageView avatar;
    private TextView  nombre;
    private TextView  nivel;
    private TextView  raza;

    public sheetHolder(@NonNull View itemView) {
      super(itemView);

      avatar = itemView.findViewById(R.id.iavatar);
      nombre = itemView.findViewById(R.id.characterName);
      nivel =  itemView.findViewById(R.id.characterLevel);
      raza =   itemView.findViewById(R.id.characterRace);
    }
    
    
    public void bindItem(final Personaje item, final OnItemClickListener listener) {

      Log.d("Item Nombre: ",item.getNombre()+"");
      nombre.setText(item.getNombre());
      nivel.setText("Nivel "+item.getNivel());
      raza.setText(item.getRaza());



      /* if (item.getAvatar() != ""){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Picasso.with(contexto)
            .load("http://image.tmdb.org/t/p/w185" + item.getAvatar())
            .into(avatar) ;

      } else {

      }*/


      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) { listener.onItemClick(item, getAdapterPosition());
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
    void onItemClick(Personaje character, int position) ;
  }
}
