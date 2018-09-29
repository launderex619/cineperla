package com.karaokulta.test.cineperla;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fragment_cartelera extends Fragment {
    ArrayList<String> lista = new ArrayList<>();
    String codigo;
    String nombre;
    String genero;
    String director;
    String clasificacion;
    String actores;
    String duracion;
    String filepath;
    String sipnosis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "Fragment", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_cartelera, container,false);
        ListView lvItems = (ListView) view.findViewById(R.id.LvCartelera);
        lista.clear();

        try {
            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
            SQLiteDatabase db = dbh.getReadableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery(" SELECT * FROM pelicula", null);                           //Cerramos la base de datos
                if (c != null && c.moveToNext()) {
                    while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                        lista.add(c.getString(1));
                        c.moveToNext();
                    }
                }
                db.close();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        lvItems.setAdapter(new CustomAdapter(getContext()));
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                    SQLiteDatabase db = dbh.getReadableDatabase();
                    String NombrePel = lista.get(position);
                    if (db != null) {
                        Cursor c = db.rawQuery(" SELECT * FROM pelicula WHERE nombre = '" + lista.get(position) + "'", null);                           //Cerramos la base de datos
                        if (c != null && c.moveToNext()) {
                            codigo = c.getString(0);
                            nombre = c.getString(1);
                            genero = c.getString(2);
                            director = c.getString(3);
                            clasificacion = c.getString(4);
                            actores = c.getString(5);
                            duracion = c.getString(6);
                            filepath = c.getString(7);
                            sipnosis = c.getString(8);
                            Bundle bundle = new Bundle();
                            bundle.putString("codigo", codigo);
                            bundle.putString("nombre", nombre);
                            bundle.putString("genero", genero);
                            bundle.putString("director", director);
                            bundle.putString("clasificacion", clasificacion);
                            bundle.putString("actores", actores);
                            bundle.putString("duracion", duracion);
                            bundle.putString("filepath", filepath);
                            bundle.putString("sipnosis", sipnosis);
                            fragment_movie fragmentMovie = new fragment_movie();

                            fragmentMovie.setArguments(bundle);
                            android.app.FragmentManager manager = getFragmentManager();
                            manager.beginTransaction().replace(R.id.LyFunciones, fragmentMovie).addToBackStack(null).commit();
                        }
                    }
                    db.close();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    class CustomAdapter extends BaseAdapter{
        int i = 0;
        Context context;
        CustomAdapter(Context c){
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
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_funcion, parent,false);
            LinearLayout Item = (LinearLayout) view.findViewById(R.id.LyItemFuncion);
            ImageView picture = (ImageView) view.findViewById(R.id.ImgFuncion);
            TextView Nombre = (TextView) view.findViewById(R.id.TvNombreFuncion);
            //ListView Lvfunciones = (ListView) view.findViewById(R.id.LvFunciones);
            TextView TvSipnosis = (TextView)view.findViewById(R.id.TvSipnosis);

            String path;
            try {
                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                SQLiteDatabase db = dbh.getReadableDatabase();
                //Si hemos abierto correctamente la base de datos
                if (db != null) {
                    //Insertamos los datos en la tabla Usuarios
                    Cursor c = db.rawQuery(" SELECT * FROM pelicula WHERE nombre = '" + lista.get(position) + "'", null);                           //Cerramos la base de datos
                    if (c != null && c.moveToNext()) {
                       // Lvfunciones.setAdapter(new CustomAdapterFuncion(getContext(), c.getString(1)));
                        TvSipnosis.setText(c.getString(8));
                        Nombre.setText(c.getString(1));
                        path = c.getString(7);
                        if (path.startsWith("content")) {
                            try {
                                Uri photoUri = Uri.parse(path);
                                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                                picture.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 800, 1000, false));
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            File imageFile = new File(path);
                            if(imageFile.exists()){
                                Bitmap myBitmap = rotateBitmapOrientation(path);
                                picture.setImageBitmap(myBitmap);
                            }
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