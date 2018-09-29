package com.karaokulta.test.cineperla;


import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_pelicula extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout LyModificar;
    Button btnAgregar;
    Button btnModificar;
    Button btnEliminar;
    TextView TvGenero;
    TextView TvClasificacion;
    TextView TvAddPelicula;
    EditText TvSipnosis;
    AutoCompleteTextView ActvNombre;
    AutoCompleteTextView ActvDirector;
    EditText EtActores;
    EditText EtDuracion;
    Button btnAdd;
    Button btnEdit;
    Button btnDelete;
    View view;
    ImageView ImgMovie;
    ImageView ImgMovieLibrary;
    Spinner SpGeneros;
    Spinner SpPeliculas;
    Spinner SpClasificacion;
    public String photoFileName = "photo.jpg";
    public final String APP_TAG = "CinePerla";
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    final static int PICK_PHOTO_CODE = 1046;
    File photoFile;
    String path;
    boolean Galery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pelicula, container, false);
        btnAgregar = (Button) view.findViewById(R.id.btnAgregar);
        btnModificar = (Button) view.findViewById(R.id.btnModificar);
        btnEliminar = (Button) view.findViewById(R.id.btnEliminar);
        TvGenero = (TextView) view.findViewById(R.id.TvGenero);
        TvClasificacion = (TextView) view.findViewById(R.id.TvClasificacion);
        ActvNombre = (AutoCompleteTextView) view.findViewById(R.id.ActvNombre);
        ActvDirector = (AutoCompleteTextView) view.findViewById(R.id.ActvDirector);
        EtActores = (EditText) view.findViewById(R.id.EtActores);
        EtDuracion = (EditText) view.findViewById(R.id.EtDuracion);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        ImgMovie = (ImageView) view.findViewById(R.id.ImgMovie);
        ImgMovieLibrary = (ImageView) view.findViewById(R.id.ImgMovieLibrary);
        SpGeneros = (Spinner) view.findViewById(R.id.SpGeneros);
        SpClasificacion = (Spinner) view.findViewById(R.id.SpClasificacion);
        SpPeliculas = (Spinner) view.findViewById(R.id.SpPeliculas);
        LyModificar = (LinearLayout) view.findViewById(R.id.LyModificar);
        TvAddPelicula = (TextView) view.findViewById(R.id.TvAddPelicula);
        TvSipnosis = (EditText) view.findViewById(R.id.EtSipnosis);

        //SpGeneros.setAdapter(ada);
        btnDelete.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);

        btnAgregar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        SpGeneros.setOnItemSelectedListener(this);
        SpClasificacion.setOnItemSelectedListener(this);
        SpPeliculas.setOnItemSelectedListener(this);
        ImgMovie.setOnClickListener(this);
        ImgMovieLibrary.setOnClickListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == SpClasificacion) {
            TvClasificacion.setText(parent.getItemAtPosition(position).toString());
        } else if (parent == SpGeneros) {
            TvGenero.setText(parent.getItemAtPosition(position).toString());
        } else if (parent == SpPeliculas) {
            try {
                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                SQLiteDatabase db = dbh.getReadableDatabase();
                //Si hemos abierto correctamente la base de datos
                if (db != null) {
                    //Insertamos los datos en la tabla Usuarios
                    Cursor c = db.rawQuery(" SELECT * FROM pelicula where nombre = '" + SpPeliculas.getItemAtPosition(position) + "'", null);                           //Cerramos la base de datos
                    if (c != null && c.moveToNext()) {
                        ActvNombre.setText(c.getString(1));
                        TvGenero.setText(c.getString(2));
                        ActvDirector.setText(c.getString(3));
                        TvClasificacion.setText(c.getString(4));
                        EtActores.setText(c.getString(5));
                        EtDuracion.setText(c.getString(6));
                        path = c.getString(7);
                        if (path.startsWith("content")) {
                            ImgMovie.setImageDrawable(null);
                            try {
                                Uri photoUri = Uri.parse(path);
                                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                                ImgMovieLibrary.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, ImgMovieLibrary.getWidth(), ImgMovieLibrary.getHeight(), false));
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ImgMovieLibrary.setImageDrawable(null);
                            File imageFile = new File(path);
                            if (imageFile.exists()) {
                                Bitmap myBitmap = rotateBitmapOrientation(path);
                                ImgMovie.setImageBitmap(myBitmap);
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
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAgregar) {
            LyModificar.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            TvAddPelicula.setText("Añadir pelicula");
            Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
        } else if (v == btnModificar) {
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            TvAddPelicula.setText("Modificar pelicula");
            LyModificar.setVisibility(View.VISIBLE);
            try {
                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                SQLiteDatabase db = dbh.getReadableDatabase();
                //Si hemos abierto correctamente la base de datos
                if (db != null) {
                    //Insertamos los datos en la tabla Usuarios
                    Cursor c = db.rawQuery(" SELECT * FROM pelicula", null);                           //Cerramos la base de datos
                    if (c != null && c.moveToNext()) {
                        ArrayAdapter<String> adapter;
                        List<String> list;
                        list = new ArrayList<String>();
                        while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                            list.add(c.getString(1));
                            c.moveToNext();
                        }
                        adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpPeliculas.setAdapter(adapter);
                    }
                    //Cerramos la base de datos
                    db.close();
                    //getFragmentManager().popBackStack();

                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        } else if (v == btnEliminar) {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            TvAddPelicula.setText("Eliminar pelicula");
            LyModificar.setVisibility(View.VISIBLE);
            try {
                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                SQLiteDatabase db = dbh.getReadableDatabase();
                //Si hemos abierto correctamente la base de datos
                if (db != null) {
                    //Insertamos los datos en la tabla Usuarios
                    Cursor c = db.rawQuery(" SELECT * FROM pelicula", null);                           //Cerramos la base de datos
                    if (c != null && c.moveToNext()) {
                        ArrayAdapter<String> adapter;
                        List<String> list;
                        list = new ArrayList<String>();
                        while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                            list.add(c.getString(1));
                            c.moveToNext();
                        }
                        adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SpPeliculas.setAdapter(adapter);
                    }
                    //Cerramos la base de datos
                    db.close();
                    //getFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else if (v == btnAdd) {
            String nombre = ActvNombre.getText().toString();
            String genero = TvGenero.getText().toString();
            String clasificacion = TvClasificacion.getText().toString();
            String director = ActvDirector.getText().toString();
            String Duracion = EtDuracion.getText().toString();
            String actores = EtActores.getText().toString();
            String Sipnosis = TvSipnosis.getText().toString();
            int duracion;
            if (EtDuracion.getText().toString().length() == 0) {
                duracion = 0;
            } else {
                duracion = Integer.parseInt(EtDuracion.getText().toString());
            }
            if (nombre.length() > 1) {
                if (director.length() > 2) {
                    if (actores.length() > 2) {
                        if (duracion > 1) {
                            if (Sipnosis.length() > 2) {
                                if (path != null) {
                                    try {
                                        DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                                        SQLiteDatabase db = dbh.getWritableDatabase();
                                        //Si hemos abierto correctamente la base de datos
                                        if (db != null) {
                                            //Insertamos los datos en la tabla Usuarios
                                            db.execSQL("INSERT INTO pelicula (nombre, genero, director, clasificacion, actores, duracion, filePath, sipnosis) " +
                                                    "VALUES ('" + nombre + "', '" + genero + "', '" + director + "', '" + clasificacion + "', '" + actores + "', " + duracion + ", '" + path + "', '" + Sipnosis + "')");
                                            //Cerramos la base de datos
                                            db.close();
                                            Toast.makeText(getActivity(), "Registrado", Toast.LENGTH_SHORT).show();
                                            //getFragmentManager().popBackStack();
                                            ActvNombre.setText("");
                                            TvGenero.setText("");
                                            TvClasificacion.setText("");
                                            ActvDirector.setText("");
                                            EtDuracion.setText("");
                                            EtActores.setText("");
                                            TvSipnosis.setText("");
                                            path = null;
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(this).attach(this).commit();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    TvSipnosis.setError("Escribe una sipnosis correcta");
                                }
                            } else {
                                Toast.makeText(getActivity(), "Debes seleccionar una imagen", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            EtDuracion.setError("no hay pelicula con duracion menor a 1 minuto");
                        }
                    } else {
                        EtActores.setError("Nombres no validos");
                    }
                } else {
                    ActvDirector.setError("Nombre invalido");
                }
            } else {
                ActvNombre.setError("Nombre invalido");
            }

        } else if (v == btnEdit) {
            String nombre = ActvNombre.getText().toString();
            String genero = TvGenero.getText().toString();
            String clasificacion = TvClasificacion.getText().toString();
            String director = ActvDirector.getText().toString();
            String Duracion = EtDuracion.getText().toString();
            String actores = EtActores.getText().toString();
            String Sipnosis = TvSipnosis.getText().toString();

            int duracion;
            if (EtDuracion.getText().toString().length() == 0) {
                duracion = 0;
            } else {
                duracion = Integer.parseInt(EtDuracion.getText().toString());
            }
            if (nombre.length() > 1) {
                if (director.length() > 2) {
                    if (actores.length() > 2) {
                        if (duracion > 1) {
                            if (Sipnosis.length() > 2) {
                                if (path != null) {
                                    try {
                                        DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                                        SQLiteDatabase db = dbh.getWritableDatabase();
                                        //Si hemos abierto correctamente la base de datos
                                        if (db != null) {
                                            //Insertamos los datos en la tabla Usuarios
                                            db.execSQL("UPDATE pelicula SET nombre = '" + nombre + "', " +
                                                    "genero = '" + genero + "' , " +
                                                    "director = '" + director + "', " +
                                                    "clasificacion = '" + clasificacion + "', " +
                                                    "actores = '" + actores + "', " +
                                                    "duracion = " + duracion + ", " +
                                                    "filePath = '" + path + "', " +
                                                    "sipnosis  = '"+Sipnosis+"' "+
                                                    "WHERE nombre = '" + SpPeliculas.getItemAtPosition(SpPeliculas.getSelectedItemPosition()) + "'");
                                            //Cerramos la base de datos
                                            db.close();
                                            Toast.makeText(getActivity(), "Actualizado", Toast.LENGTH_SHORT).show();
                                            //getFragmentManager().popBackStack();
                                            ActvNombre.setText("");
                                            TvGenero.setText("");
                                            TvClasificacion.setText("");
                                            ActvDirector.setText("");
                                            EtDuracion.setText("");
                                            EtActores.setText("");
                                            TvSipnosis.setText("");
                                            path = null;
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.detach(this).attach(this).commit();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    TvSipnosis.setError("Escribe una sipnosis correcta");
                                }
                            } else {
                                Toast.makeText(getActivity(), "Debes seleccionar una imagen", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            EtDuracion.setError("no hay pelicula con duracion menor a 1 minuto");
                        }
                    } else {
                        EtActores.setError("Nombres no validos");
                    }
                } else {
                    ActvDirector.setError("Nombre invalido");
                }
            } else {
                ActvNombre.setError("Nombre invalido");
            }

        } else if (v == btnDelete) {
            if (SpPeliculas.getCount() > 0) {
                try {
                    DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                    SQLiteDatabase db = dbh.getWritableDatabase();
                    //Si hemos abierto correctamente la base de datos
                    if (db != null) {
                        //Insertamos los datos en la tabla Usuarios
                        db.execSQL("DELETE FROM pelicula WHERE nombre = '" + SpPeliculas.getItemAtPosition(SpPeliculas.getSelectedItemPosition()) + "'");
                        //Cerramos la base de datos
                        db.close();
                        Toast.makeText(getActivity(), "Eliminado", Toast.LENGTH_SHORT).show();
                        //getFragmentManager().popBackStack();
                        ActvNombre.setText("");
                        TvGenero.setText("");
                        TvClasificacion.setText("");
                        ActvDirector.setText("");
                        EtDuracion.setText("");
                        EtActores.setText("");
                        path = null;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(this).attach(this).commit();
                    }
                    Toast.makeText(getActivity(), "Peliculas restantes: " + (SpPeliculas.getCount() - 2), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v == ImgMovie) {
            onLaunchCamera(v);
        } else if (v == ImgMovieLibrary) {
            onPickPhoto(true);
        }
    }


    public void onPickPhoto(boolean galery) {
        Galery = galery;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    private void onLaunchCamera(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(ActvNombre.getText().toString() + ".jpg");
        Uri fileProvider = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.appFolder), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = rotateBitmapOrientation(photoFile.getAbsolutePath());
                path = photoFile.getAbsolutePath();
                ImgMovie.setImageBitmap(Bitmap.createScaledBitmap(takenImage, ImgMovie.getWidth(), ImgMovie.getHeight(), false));
            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICK_PHOTO_CODE) {
            if (data != null) {
                try {
                    Uri photoUri = data.getData();
                    path = photoUri.toString();
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    ImgMovieLibrary.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, ImgMovieLibrary.getWidth(), ImgMovieLibrary.getHeight(), false));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
