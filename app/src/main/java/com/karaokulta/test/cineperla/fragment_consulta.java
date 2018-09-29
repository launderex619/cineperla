package com.karaokulta.test.cineperla;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_consulta extends Fragment {
    ArrayList<String> lista = new ArrayList<>();
    int Total=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lista.clear();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consulta, container, false);
        ListView lvItems = (ListView) view.findViewById(R.id.LvConsultas);

        try {
            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
            SQLiteDatabase db = dbh.getReadableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery(" SELECT * FROM boleto", null);                           //Cerramos la base de datos
                if (c != null && c.moveToNext()) {
                    while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                        Total +=Integer.parseInt(c.getString(5));
                        lista.add(c.getString(0));
                        c.moveToNext();
                    }
                }
                db.close();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        lvItems.setAdapter(new CustomAdapter(getContext()));

        return view;
    }

    class CustomAdapter extends BaseAdapter {
        int i = 0;
        Context context;

        CustomAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_consulta, parent, false);
            TextView codigo = (TextView) view.findViewById(R.id.TvCodigo);
            TextView funcion = (TextView) view.findViewById(R.id.TvFuncion);
            TextView usuario = (TextView) view.findViewById(R.id.TvUsuario);
            TextView boletos = (TextView) view.findViewById(R.id.TvCantidadBoletos);
            CheckBox compras = (CheckBox) view.findViewById(R.id.Tv);
            TextView precio = (TextView) view.findViewById(R.id.Precio);
            try {
                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                SQLiteDatabase db = dbh.getReadableDatabase();
                if (db != null) {
                    Cursor c = db.rawQuery(" SELECT * FROM boleto WHERE codigo = '" + lista.get(position) + "'", null);                           //Cerramos la base de datos
                    if (c != null && c.moveToNext()) {
                        codigo.setText(c.getString(0));
                        funcion.setText(c.getString(1));
                        usuario.setText(c.getString(2));
                        boletos.setText(c.getString(3));
                        precio.setText(c.getString(5));
                        compras.setText("Compra online");
                        if (c.getString(4) == "0") {
                            compras.setChecked(false);
                        }
                        else{
                            compras.setChecked(true);
                        }
                    }
                    //Cerramos la base de datos
                    db.close();
                    //getFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            return view;
        }

        /*        class CustomAdapterFuncion extends BaseAdapter{

                    Context context;
                    String data;

                    ArrayList<String> date = new ArrayList<>();
                    CustomAdapterFuncion(Context c, String d){
                        context = c;
                        data = d;
                    }

                    @Override
                    public int getCount() {
                        int i = 0;
                        try {
                            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                            SQLiteDatabase db = dbh.getReadableDatabase();
                            if (db != null) {
                                Cursor c = db.rawQuery(" SELECT hora FROM funcion WHERE fk_pelicula = '" + data + "'", null);                           //Cerramos la base de datos
                                if (c != null && c.moveToNext()) {
                                    while (!c.isAfterLast()) {
                                        date.add(c.getString(0));
                                        i++;
                                        c.moveToNext();
                                    }
                                }
                                db.close();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        return i;
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.item_funcion_funcion, parent,false);
                        TextView hora = (TextView)view.findViewById(R.id.hora);
                        TextView fecha = (TextView)view.findViewById(R.id.fecha);


                        String[] dato = date.get(position).split(" ");
                        fecha.setText(dato[0]);
                        hora.setText(dato[1]);

                        return view;
                    }
                }*/
        public Bitmap rotateBitmapOrientation(String photoFilePath) {
            // Create and configure BitmapFactory
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoFilePath, bounds);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
            // Read EXIF Data
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
            // Rotate Bitmap
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
            // Return result
            return rotatedBitmap;
        }
    }


}
