package com.karaokulta.test.cineperla;


import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_registro extends Fragment implements View.OnClickListener {

    AutoCompleteTextView ActvNombre;
    EditText EtCorreo;
    EditText EtContrasena;
    EditText EtEdad;
    Button btnRegistro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro, container, false);
        ActvNombre = (AutoCompleteTextView) view.findViewById(R.id.ActvNombre);
        EtCorreo = (EditText) view.findViewById(R.id.EtCorreo);
        EtContrasena = (EditText) view.findViewById(R.id.EtContrasena);
        EtEdad = (EditText) view.findViewById(R.id.EtEdad);
        btnRegistro = (Button) view.findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegistro) {
            String nombre = ActvNombre.getText().toString();
            String correo = EtCorreo.getText().toString();
            String contrasena = EtContrasena.getText().toString();
            int edad;
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
                                    db.execSQL("INSERT INTO usuario (nombre, correo, contrasena, puntos, edad, credito, premium) " +
                                            "VALUES ('" + nombre + "', '" + correo  + "', '" + contrasena + "', " + 0 +", "+ edad + ", " + 0 + ", "+ 0 + ")");
                                    //Cerramos la base de datos
                                    db.close();
                                    Toast.makeText(getActivity(), "Registrado", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
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
    }
}
