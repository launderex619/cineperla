package com.karaokulta.test.cineperla;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_profile extends Fragment implements View.OnClickListener {

    Button btnIniciarSesion;
    Button btnRegistrarte;
    View view;
    EditText EtCorreo;
    EditText EtContrasena;
    EditText EtEdad;
    Button btnActualizar;
    Button btnCerrarSesion;
    Button btnRegistro;
    AutoCompleteTextView ActvNombre;
    LayoutInflater Inflater;
    ViewGroup Container;
    CheckBox CbxPremium;
    Bundle SavedInstanceState;
    TextView TvPuntos;
    TextView TvCredito;
    TextView TvNombre;
    Button btnAddCredito;

    int Credito;
    int Puntos;
    int edad;
    int premium;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Inflater = inflater;
        Container = container;
        SavedInstanceState = savedInstanceState;
        // Inflate the layout for this fragment
        SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String correo = prefs.getString("correo", "null");
        Toast.makeText(getActivity(), correo, Toast.LENGTH_SHORT).show();

        if (correo.equals("null")) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            btnIniciarSesion = (Button) view.findViewById(R.id.BtnIniciarSesion);
            btnRegistrarte = (Button) view.findViewById(R.id.BtnRegistrarte);
            EtCorreo = (EditText) view.findViewById(R.id.EtCorreo);
            EtContrasena = (EditText) view.findViewById(R.id.EtContrasena);

            btnIniciarSesion.setOnClickListener(this);
            btnRegistrarte.setOnClickListener(this);
        } else {
            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
            SQLiteDatabase db = dbh.getReadableDatabase();

            view = inflater.inflate(R.layout.fragment_profile_in, container, false);
            ActvNombre = (AutoCompleteTextView) view.findViewById(R.id.ActvNombre);
            EtCorreo = (EditText) view.findViewById(R.id.EtCorreo);
            EtContrasena = (EditText) view.findViewById(R.id.EtContrasena);
            EtEdad = (EditText) view.findViewById(R.id.EtEdad);
            btnActualizar = (Button) view.findViewById(R.id.btnActualizarPerfil);
            btnCerrarSesion = (Button) view.findViewById(R.id.btnCerrarSesion);
            TvPuntos = (TextView) view.findViewById(R.id.TvPuntos);
            TvCredito = (TextView) view.findViewById(R.id.TvSueldo);
            TvNombre = (TextView) view.findViewById(R.id.TvNombre);
            btnAddCredito = (Button) view.findViewById(R.id.btnSuma);
            CbxPremium = (CheckBox) view.findViewById(R.id.CbxPremium);

            TvCredito.setText("0");
            Credito = 0;
            Puntos = 0;
            edad = 0;
            premium = 0;

            btnActualizar.setOnClickListener(this);
            btnCerrarSesion.setOnClickListener(this);
            btnAddCredito.setOnClickListener(this);
            CbxPremium.setOnClickListener(this);

            if (db != null) {
                Cursor c = db.rawQuery(" SELECT * FROM usuario WHERE correo = '" + correo + "'", null);                            //Cerramos la base de datos
                if (c != null && c.moveToNext()) {
                    //Toast.makeText(getActivity(), c.getString(0).toString(), Toast.LENGTH_SHORT).show();.
                    ActvNombre.setText(c.getString(1));         // "nombre text NOT NULL, " +
                    TvNombre.setText(c.getString(1));
                    EtCorreo.setText(c.getString(2));           // "correo text NOT NULL UNIQUE, " +
                    EtContrasena.setText(c.getString(3));       // "contrasena text NOT NULL, " +
                    TvPuntos.setText(c.getString(4));           //"puntos INTEGER NOT NULL, " +
                    EtEdad.setText("" + c.getString(5));        //"edad INTEGER NOT NULL, " +
                    TvCredito.setText("" + c.getString(6));     //credito INTEGER NOT NULL)";
                    premium = c.getInt(7);
                    Credito = Integer.parseInt(TvCredito.getText().toString());
                    Puntos = Integer.parseInt(TvPuntos.getText().toString());
                    edad = Integer.parseInt(EtEdad.getText().toString());
                    if (premium == 1) {
                        CbxPremium.setChecked(true);
                    } else {
                        CbxPremium.setChecked(false);
                    }

                } else {
                    Toast.makeText(getActivity(), "Algo salio mal", Toast.LENGTH_SHORT).show();
                }
                db.close();
            } else {
                //por si algo sale mal
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("correo", "null");
                editor.commit();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == btnIniciarSesion) {
            String correo = EtCorreo.getText().toString();
            String contrasena = EtContrasena.getText().toString();
            if (!correo.contains("@") && correo.length() <= 4) {
                EtCorreo.setError("Correo no valido");
            } else {
                if (contrasena.length() <= 0) {
                    EtContrasena.setError("Contraseña no valida");
                } else {
                    try {
                        DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                        SQLiteDatabase db = dbh.getReadableDatabase();
                        //Si hemos abierto correctamente la base de datos
                        if (db != null) {
                            if(correo.endsWith("perla.com")) {
                                Cursor c = db.rawQuery(" SELECT correo FROM empleado WHERE correo = '" + correo + "'", null);                            //Cerramos la base de datos
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(this).commit();
                                SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("correo", "null");
                                editor.putString("rol", "empleado");
                                editor.commit();
                                Intent intent = new Intent(getActivity(), Empleado.class);
                                startActivity(intent);
                            }
                            else if(correo.equals("admin")){
                                //
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(this).commit();
                                SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("correo", "null");
                                editor.putString("rol", "admin");
                                editor.commit();
                                Intent intent = new Intent(getActivity(), Empleado.class);
                                startActivity(intent);
                            }
                            else{
                                Cursor c = db.rawQuery(" SELECT correo FROM usuario WHERE correo = '" + correo + "'", null);                            //Cerramos la base de datos
                                if ((c != null && c.moveToNext()) && correo.equals(c.getString(0))) {
                                    //Toast.makeText(getActivity(), c.getString(0).toString(), Toast.LENGTH_SHORT).show();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(this).attach(this).commit();
                                    SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("correo", correo);
                                    editor.putString("contrasena", contrasena);
                                    editor.commit();
                                } else {
                                    Toast.makeText(getActivity(), "Correo no registrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                            db.close();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else if (v == btnRegistrarte) {
            //codigo para la vista de registro
            fragment_registro fragmentRegistro = new fragment_registro();
            android.app.FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.LyFunciones, fragmentRegistro).addToBackStack(null).commit();
        } else if (v == btnCerrarSesion) {
            SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("correo", "null");
            editor.commit();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        } else if (v == btnAddCredito) {
            Credito += 10;
            TvCredito.setText("" + Credito);
        } else if (v == btnActualizar) {
            String nombre = ActvNombre.getText().toString();
            String correo = EtCorreo.getText().toString();
            String contrasena = EtContrasena.getText().toString();
            if (EtEdad.getText().toString().length() == 0) {
                edad = 0;
            } else {
                edad = Integer.parseInt(EtEdad.getText().toString());
            }
            if (nombre.length() > 1) {
                if (correo.contains("@") && correo.length() > 3) {
                    if (contrasena.length() > 0) {
                        if (edad > 15 && edad < 110) {
                            try {
                                DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                                SQLiteDatabase db = dbh.getWritableDatabase();
                                //Si hemos abierto correctamente la base de datos
                                if (db != null) {
                                    //Insertamos los datos en la tabla Usuarios
                                    db.execSQL("UPDATE usuario SET nombre ='" + nombre +
                                            "', correo = '" + correo +
                                            "', contrasena = '" + contrasena +
                                            "', puntos = " + Puntos +
                                            ", edad = " + edad +
                                            ", credito = " + Credito +
                                            ", premium = " + premium + " where correo = '" + correo + "'");
                                    //Cerramos la base de datos
                                    Toast.makeText(getActivity(), "Actualizado con exito", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(this).attach(this).commit();
                                    db.close();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            EtEdad.setError("Debes ser mayor de 15 y menor de 110");
                        }
                    } else {
                        EtContrasena.setError("Contraseña no valida");
                    }
                } else {
                    EtCorreo.setError("Correo no valido");
                }
            } else {
                ActvNombre.setError("Nombre invalido");
            }
        }
        if (v == CbxPremium) {
            if (premium == 1) {
                premium = 0;
                CbxPremium.setChecked(false);
                Credito += 50;
                Puntos = 0;
            } else {
                if (Credito >= 50) {
                    premium = 1;
                    CbxPremium.setChecked(true);
                    Credito -= 50;
                } else {
                    CbxPremium.setChecked(false);
                    Toast.makeText(getActivity(), "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                }
            }
            TvCredito.setText("" + Credito);
        }
    }
}
