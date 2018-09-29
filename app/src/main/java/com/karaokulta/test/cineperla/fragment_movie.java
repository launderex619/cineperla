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
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 */

public class fragment_movie extends Fragment implements View.OnClickListener {
    ArrayList<String> lista = new ArrayList<>();
    public Bundle bundle = new Bundle();

    Button btnPagar;
    ImageView Img_movie;
    TextView Movie_Title;
    TextView TvDuracion;
    TextView TvClasificacion;
    TextView TvSipnosis;
    TextView TvSipnosisInformation;
    TextView TvDirector;
    TextView TvDirectorInformation;
    TextView TvActoresInformation;
    Button btnResta;
    TextView TvCantidadBoletos;
    Button btnSuma;
    CardView pago;
    ListView Funciones;
    RelativeLayout container2;

    int Boletos = 1;
    String codigo;
    String fk_pelicula;
    String sala;
    String asientos;
    String boletos;
    String hora;
    String codigoP;
    String nombreP;
    String genero;
    String director;
    String clasificacion;
    String actores;
    String duracion;
    String filepath;
    String sipnosis;
    View vistaAnterior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_movie, container, false);
        lista.clear();
        container2 = (RelativeLayout) view.findViewById(R.id.container2);
        btnPagar = (Button) view.findViewById(R.id.btnPagar);
        Img_movie = (ImageView) view.findViewById(R.id.Img_movie);
        Movie_Title = (TextView) view.findViewById(R.id.Movie_title);
        TvDuracion = (TextView) view.findViewById(R.id.TvDuracionMovie);
        TvClasificacion = (TextView) view.findViewById(R.id.TvClasificacion);
        TvSipnosis = (TextView) view.findViewById(R.id.TvSipnosis);
        TvSipnosisInformation = (TextView) view.findViewById(R.id.TvSipnosisInformation);
        TvDirector = (TextView) view.findViewById(R.id.TvDirector);
        TvDirectorInformation = (TextView) view.findViewById(R.id.TvDirectorInformation);
        TvActoresInformation = (TextView) view.findViewById(R.id.TvActoresInformation);
        btnSuma = (Button) view.findViewById(R.id.btnSuma);
        btnResta = (Button) view.findViewById(R.id.btnResta);
        TvCantidadBoletos = (TextView) view.findViewById(R.id.TvCantidadBoletos);
        pago = (CardView) view.findViewById(R.id.pago);
        Funciones = (ListView) view.findViewById(R.id.LvFunciones);
        pago.setVisibility(GONE);

        TvCantidadBoletos.setText("" + Boletos);

        Bundle bundle2 = getArguments();
        if (bundle2 != null) {
            codigoP = bundle2.getString("codigo");
            nombreP = bundle2.getString("nombre");
            genero = bundle2.getString("genero");
            director = bundle2.getString("director");
            clasificacion = bundle2.getString("clasificacion");
            actores = bundle2.getString("actores");
            duracion = bundle2.getString("duracion");
            filepath = bundle2.getString("filepath");
            sipnosis = bundle2.getString("sipnosis");
        }

        try {
            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
            SQLiteDatabase db = dbh.getReadableDatabase();
            //Si hemos abierto correctamente la base de datos
            if (db != null) {
                Cursor c = db.rawQuery(" SELECT * FROM funcion where fk_pelicula = '" + nombreP + "'", null);                           //Cerramos la base de datos
                if (c != null && c.moveToNext()) {
                    codigo = c.getString(0);
                    fk_pelicula = c.getString(1);
                    sala = c.getString(2);
                    asientos = c.getString(3);
                    boletos = c.getString(4);
                    hora = c.getString(5);
                }
                //Cerramos la base de datos
                db.close();
                //getFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        if (filepath.startsWith("content")) {
            try {
                Uri photoUri = Uri.parse(filepath);
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                Img_movie.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 800, 1000, false));
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            File imageFile = new File(filepath);
            if (imageFile.exists()) {
                Bitmap myBitmap = rotateBitmapOrientation(filepath);
                Img_movie.setImageBitmap(myBitmap);
            }
        }
        Movie_Title.setText(nombreP);
        TvDuracion.setText(duracion);
        TvClasificacion.setText(clasificacion);
        TvSipnosisInformation.setText(sipnosis);
        TvDirectorInformation.setText(director);
        TvActoresInformation.setText(actores);

        Funciones.setAdapter(new CustomAdapterFuncion(getContext(), nombreP));
        container2.getLayoutParams().height = 160 * lista.size();
        container2.getLayoutParams().width = 800;
        Funciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                    SQLiteDatabase db = dbh.getReadableDatabase();
                    if (db != null) {
                        Cursor c = db.rawQuery(" SELECT * FROM funcion WHERE codigo = '" + lista.get(position) + "'", null);                           //Cerramos la base de datos
                        if (c != null && c.moveToNext()) {
                            codigo = c.getString(0);
                            fk_pelicula = c.getString(1);
                            sala = c.getString(2);
                            asientos = c.getString(3);
                            boletos = c.getString(4);
                            hora = c.getString(5);
                        }
                        db.close();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }

                String selectedItem = "" + position;  //aqui voy a hacer la consulta a la base de datos
                bundle.putString("codigof", codigo );
                bundle.putString("peliculaf", fk_pelicula);
                bundle.putString("sala", sala );
                bundle.putString("asientos", asientos);
                bundle.putString("boletos", boletos );
                bundle.putString("cantidadBoletos", "" + Boletos );
                pago.setVisibility(View.VISIBLE);
                TextView selAnt;
                if (vistaAnterior == null) {
                    selAnt = (TextView) view.findViewById(R.id.Selected);
                } else {
                    selAnt = (TextView) vistaAnterior.findViewById(R.id.Selected);
                }
                selAnt.setVisibility(GONE);
                TextView sel = (TextView) view.findViewById(R.id.Selected);
                sel.setVisibility(View.VISIBLE);
                vistaAnterior = view;
            }
        });

        btnPagar.setOnClickListener(this);
        btnResta.setOnClickListener(this);
        btnSuma.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == btnPagar) {
            fragment_sala fragmentSala = new fragment_sala();
            fragmentSala.setArguments(bundle);
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.LyFunciones, fragmentSala).addToBackStack(null).commit();
        } else if (v == btnResta) {
            if (Boletos != 1) {
                Boletos--;
                TvCantidadBoletos.setText("" + Boletos);
                bundle.putString("cantidadBoletos", "" + Boletos );
            }
        } else if (v == btnSuma) {
            if (Boletos != 5) {
                Boletos++;
                TvCantidadBoletos.setText("" + Boletos);
                bundle.putString("cantidadBoletos", "" + Boletos );
            }
        }
    }

    class CustomAdapterFuncion extends BaseAdapter {

        Context context;
        String data;

        ArrayList<String> date = new ArrayList<>();

        CustomAdapterFuncion(Context c, String d) {
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
                            lista.add(c.getString(0));
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_funcion_funcion, parent, false);
            TextView hora = (TextView) view.findViewById(R.id.hora);
            TextView fecha = (TextView) view.findViewById(R.id.fecha);

            String[] dato = date.get(position).split(" ");
            fecha.setText(dato[0]);
            hora.setText(dato[1]);

            return view;
        }
    }


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
