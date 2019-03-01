package com.example.ulric.avispro.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ulric.avispro.R;
import com.example.ulric.avispro.modelos.Combatiente;

import java.util.List;

public class selectorListWithImageAdapter extends ArrayAdapter<Combatiente> {

  private Context contexto;
  private int layout;
  private List<Combatiente> items;

  public selectorListWithImageAdapter(Context contexto, int resource, List<Combatiente> objects) {
    super(contexto, resource, objects);
    this.contexto = contexto;
    this.layout = resource;
    this.items = objects;
  }

  /**
   * Creación de una vista
   * @param position, posición del elemento en el array de elementos
   * @param convertView, la antigua vista para reusarla si es necesario
   * @param parent, elemento padre de la vista
   * @return
   */
  public View getCustomView(int position, View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

    ImageView avatar = view.findViewById(R.id.character_avatar);
    TextView name =    view.findViewById(R.id.character_name);

    Combatiente item = items.get(position);
    name.setText(item.getNombre());
    if (item.tieneAvatar()) {
      avatar.setImageURI(Uri.parse(item.getAvatar()));
    }

    return view;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }
}
