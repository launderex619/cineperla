package com.karaokulta.test.cineperla;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class fragment_funciones extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    LinearLayout LyModificar;
    Button btnAgregar;
    Button btnModificar;
    Button btnEliminar;
    TextView TvAddPelicula;
    TextView TvDia;
    TextView TvHora;
    TextView TvPelicula;
    TextView TvSala;
    Button btnAdd;
    Button btnEdit;
    Button btnDelete;
    View view;
    ImageView ImgHora;
    ImageView ImgDia;
    Spinner SpFunciones;
    Spinner SpPelicula;
    Spinner SpSala;


    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private static final String CERO = "0";
    private static final String BARRA = "-";
    private static final String DOS_PUNTOS = ":";
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    boolean date, time, movie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_funciones, container, false);
        btnAgregar = (Button) view.findViewById(R.id.btnAgregar);
        btnModificar = (Button) view.findViewById(R.id.btnModificar);
        btnEliminar = (Button) view.findViewById(R.id.btnEliminar);

        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        ImgHora = (ImageView) view.findViewById(R.id.ImgHora);
        ImgDia = (ImageView) view.findViewById(R.id.ImgDia);
        SpPelicula = (Spinner) view.findViewById(R.id.SpPelicula);
        SpFunciones = (Spinner) view.findViewById(R.id.SpFunciones);
        SpSala = (Spinner) view.findViewById(R.id.SpSala);
        LyModificar = (LinearLayout) view.findViewById(R.id.LyModificar);
        TvAddPelicula = (TextView) view.findViewById(R.id.TvAddPelicula);
        TvDia = (TextView) view.findViewById(R.id.TvDia);
        TvPelicula = (TextView) view.findViewById(R.id.TvPelicula);
        TvSala = (TextView) view.findViewById(R.id.TvSala);
        TvHora = (TextView) view.findViewById(R.id.TvHora);

        btnDelete.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);

        btnAgregar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        SpFunciones.setOnItemSelectedListener(this);
        SpSala.setOnItemSelectedListener(this);
        ImgHora.setOnClickListener(this);
        ImgDia.setOnClickListener(this);

        try {
            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
            SQLiteDatabase db = dbh.getReadableDatabase();
            //Si hemos abierto correctamente la base de datos
            if (db != null) {
                //Insertamos los datos en la tabla Usuarios
                Cursor c = db.rawQuery(" SELECT * FROM pelicula", null);
                if (c != null && c.moveToNext()) {
                    ArrayAdapter<String> adapter;
                    List<String> list;
                    list = new ArrayList<String>();
                    int i = 0;
                    while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                        list.add(c.getString(1));
                        c.moveToNext();
                        i++;
                    }
                    if (i > 0) {
                        movie = true;
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpPelicula.setAdapter(adapter);
                }
                c = db.rawQuery(" SELECT * FROM funcion", null);
                if (c != null && c.moveToNext()) {
                    ArrayAdapter<String> adapter;
                    List<String> list;
                    list = new ArrayList<String>();
                    int i = 0;
                    while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                        list.add(c.getString(1));
                        c.moveToNext();
                        i++;
                    }
                    if (i > 0) {
                        movie = true;
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpFunciones.setAdapter(adapter);
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


    @Override
    public void onClick(View v) {
        if (v == ImgDia) {
            DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                    final int mesActual = month + 1;
                    //Formateo el día obtenido: antepone el 0 si son menores de 10
                    String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    //Formateo el mes obtenido: antepone el 0 si son menores de 10
                    String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                    //Muestro la fecha con el formato deseado
                    TvDia.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);


                }
                //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                /**
                 *También puede cargar los valores que usted desee
                 */
            }, anio, mes, dia);
            //Muestro el widget
            recogerFecha.show();

        } else if (v == ImgHora) {
            TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //Formateo el hora obtenido: antepone el 0 si son menores de 10
                    String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                    //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                    String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                    //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                    String AM_PM;
                    if (hourOfDay < 12) {
                        AM_PM = "a.m.";
                    } else {
                        AM_PM = "p.m.";
                    }
                    if (AM_PM.equals("p.m.")) {
                        horaFormateada = "" + hourOfDay; // "" + ( Integer.parseInt(horaFormateada) + 12);
                    }
                    //Muestro la hora con el formato deseado
                    TvHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + ":00");
                }
                //Estos valores deben ir en ese orden
                //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                //Pero el sistema devuelve la hora en formato 24 horas
            }, hora, minuto, false);
            recogerHora.show();
        } else if (v == btnAgregar) {
            LyModificar.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        } else if (v == btnModificar) {
            LyModificar.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        } else if (v == btnEliminar) {
            LyModificar.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        } else if (v == btnEdit) {
            if (movie) {
                if (!TvDia.getText().toString().equals("Fecha")) {
                    if (!TvHora.getText().toString().equals("Hora")) {
                        try {
                            String date = TvDia.getText().toString() + " " + TvHora.getText().toString();
                            Boolean whrite = true;
                            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                            SQLiteDatabase db = dbh.getReadableDatabase();
                            //Si hemos abierto correctamente la base de datos
                            if (db != null) {
                                //Insertamos los datos en la tabla Usuarios
                                String a = SpPelicula.getSelectedItem().toString();
                                Cursor c = db.rawQuery(" SELECT * FROM funcion WHERE fk_pelicula ='" + SpPelicula.getSelectedItem() + "'", null);
                                if (c != null && c.moveToNext()) {
                                    while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                                        if (c.getString(5).contains(date) || c.getString(5).equals(date)) {
                                            whrite = false;
                                        }
                                        c.moveToNext();
                                    }
                                }
                                int i = 0;
                                if (whrite) {
                                    i++;
                                    db.execSQL("INSERT INTO funcion (fk_pelicula, sala, asientos, boletos, hora) " +
                                            "VALUES ('" + SpPelicula.getSelectedItem() + "', " + SpSala.getSelectedItemPosition() + ", '" + "30" + "', " + 20 + ", '" + date + "')");
                                    db.execSQL("UPDATE funcion SET fk_pelicula = '" + SpPelicula.getSelectedItem() + "', " +
                                            "sala = " + SpSala.getSelectedItemPosition() + ", " +
                                            "hora = '" + date + "'" +
                                            "WHERE fk_pelicula = '" + SpFunciones.getItemAtPosition(SpFunciones.getSelectedItemPosition()) + "'");
                                    Toast.makeText(getActivity(), "Modificado", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "Ya existe esta funcion", Toast.LENGTH_SHORT).show();
                                }
                                //Cerramos la base de datos
                                db.close();
                                //getFragmentManager().popBackStack();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Selecciona una Hora", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Selecciona una Fecha", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "No hay peliculas", Toast.LENGTH_SHORT).show();
            }
        } else if (v == btnAdd) {
            if (movie) {
                if (!TvDia.getText().toString().equals("Fecha")) {
                    if (!TvHora.getText().toString().equals("Hora")) {
                        try {
                            String date = TvDia.getText().toString() + " " + TvHora.getText().toString();
                            Boolean whrite = true;
                            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                            SQLiteDatabase db = dbh.getReadableDatabase();
                            //Si hemos abierto correctamente la base de datos
                            if (db != null) {
                                //Insertamos los datos en la tabla Usuarios
                                String a = SpPelicula.getSelectedItem().toString();
                                Cursor c = db.rawQuery(" SELECT * FROM funcion WHERE fk_pelicula ='" + SpPelicula.getSelectedItem() + "'", null);
                                if (c != null && c.moveToNext()) {
                                    while (!c.isAfterLast()) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG
                                        if (c.getString(5).contains(date) || c.getString(5).equals(date)) {
                                            whrite = false;
                                        }
                                        c.moveToNext();
                                    }
                                }
                                int i = 0;
                                if (whrite) {
                                    i++;
                                    if (i < 2) {
                                        db.execSQL("INSERT INTO funcion (fk_pelicula, sala, asientos, boletos, hora) " +
                                                "VALUES ('" + SpPelicula.getSelectedItem() + "', " + SpSala.getSelectedItemPosition() + ", '" + "30" + "', " + 20 + ", '" + date + "')");
                                        Toast.makeText(getActivity(), "Ingresado", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Ya existe esta funcion", Toast.LENGTH_SHORT).show();
                                }
                                //Cerramos la base de datos
                                db.close();
                                //getFragmentManager().popBackStack();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Selecciona una Hora", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Selecciona una Fecha", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "No hay peliculas", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == btnDelete) {
            if (SpFunciones.getCount() > 0) {
                try {
                    DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                    SQLiteDatabase db = dbh.getWritableDatabase();
                    //Si hemos abierto correctamente la base de datos
                    if (db != null) {
                        //Insertamos los datos en la tabla Usuarios
                        int id = 0;
                        Cursor c = db.rawQuery(" SELECT * FROM funcion WHERE fk_pelicula ='" + SpPelicula.getSelectedItem() + "' AND sala = '" + TvSala.getText() + "'", null);
                        if (c != null && c.moveToNext()) {
                            id = c.getInt(0);
                        }
                        db.execSQL("DELETE FROM funcion WHERE codigo = " + id);
                        //Cerramos la base de datos
                        db.close();
                        Toast.makeText(getActivity(), "Eliminado", Toast.LENGTH_SHORT).show();
                        //getFragmentManager().popBackStack();
                        TvHora.setText("");
                        TvSala.setText("");
                        TvPelicula.setText("");
                        TvDia.setText("");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(this).attach(this).commit();
                    }
                    Toast.makeText(getActivity(), "Funciones restantes: " + (SpFunciones.getCount() - 1), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == SpFunciones) {
            try {
                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                SQLiteDatabase db = dbh.getReadableDatabase();
                //Si hemos abierto correctamente la base de datos
                if (db != null) {
                    //Insertamos los datos en la tabla Usuarios
                    Cursor c = db.rawQuery(" SELECT * FROM funcion where fk_pelicula = '" + SpFunciones.getItemAtPosition(position) + "'", null);                           //Cerramos la base de datos
                    if (c != null && c.moveToNext()) {
                        TvPelicula.setText(c.getString(1));
                        TvSala.setText("" + c.getString(2));
                        TvHora.setText(c.getString(5));
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
