package com.karaokulta.test.cineperla;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class Empleado extends AppCompatActivity implements View.OnClickListener {

    Button btnAddPelicula;
    Button btnAddFuncion;
    Button btnVenderEntrada;
    Button btnAddUsuario;
    Button btnAddEmpleado;
    Button btnAddPuesto;
    Button btnConsultas;
    Button btnCorte;

    LinearLayout ActionGerente1;
    LinearLayout ActionGerente2;
    LinearLayout LySeccion;
    ScrollView SvSeccion;
    FragmentManager fragmentManager = getFragmentManager();
    boolean Seccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        SharedPreferences prefs = getApplication().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String correo = prefs.getString("correo", "null");
        String rol = prefs.getString("rol", "null");
        Toast.makeText(getApplication(), correo, Toast.LENGTH_SHORT).show();

        btnAddPelicula = (Button) findViewById(R.id.btnPelicula);
        btnAddFuncion = (Button) findViewById(R.id.btnFuncion);
        btnVenderEntrada = (Button) findViewById(R.id.btnVenderEntrada);
        btnAddUsuario = (Button) findViewById(R.id.btnUsuario);
        btnAddEmpleado = (Button) findViewById(R.id.btnEmpleado);
        btnAddPuesto = (Button) findViewById(R.id.btnPuesto);
        btnConsultas = (Button) findViewById(R.id.btnConsultas);
        btnCorte = (Button) findViewById(R.id.btnCorte);
        ActionGerente1 = (LinearLayout) findViewById(R.id.ActionGerente1);
        ActionGerente2 = (LinearLayout) findViewById(R.id.ActionGerente2);
        LySeccion = (LinearLayout) findViewById(R.id.LySeccion);
        SvSeccion = (ScrollView) findViewById(R.id.seccion);

        btnAddPelicula.setOnClickListener(this);
        btnAddFuncion.setOnClickListener(this);
        btnVenderEntrada.setOnClickListener(this);
        btnVenderEntrada.setOnClickListener(this);
        btnAddUsuario.setOnClickListener(this);
        btnAddEmpleado.setOnClickListener(this);
        btnAddPuesto.setOnClickListener(this);
        btnConsultas.setOnClickListener(this);
        btnCorte.setOnClickListener(this);

        if (rol.equals("admin")) {
            ActionGerente1.setVisibility(View.VISIBLE);
            ActionGerente2.setVisibility(View.VISIBLE);
        } else if (rol.equals("empleado")) {
            ActionGerente1.setVisibility(View.GONE);
            ActionGerente2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!Seccion) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Seccion = false;
            SvSeccion.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddPelicula) {
            SvSeccion.setVisibility(View.VISIBLE);
            fragment_pelicula Pelicula = new fragment_pelicula();
            fragmentManager.beginTransaction().replace(R.id.LySeccion, Pelicula).commit();
            Seccion = true;
        } else if (v == btnAddFuncion) {
            SvSeccion.setVisibility(View.VISIBLE);
            fragment_funciones Funcion = new fragment_funciones();
            fragmentManager.beginTransaction().replace(R.id.LySeccion, Funcion).commit();
            Seccion = true;
        } else if (v == btnVenderEntrada) {
            SvSeccion.setVisibility(View.VISIBLE);
            Seccion = true;
        } else if (v == btnAddUsuario) {
            SvSeccion.setVisibility(View.VISIBLE);
            Seccion = true;
        } else if (v == btnAddEmpleado) {
            SvSeccion.setVisibility(View.VISIBLE);
            Seccion = true;
        } else if (v == btnAddPuesto) {
            SvSeccion.setVisibility(View.VISIBLE);
            Seccion = true;
        } else if (v == btnConsultas) {
            SvSeccion.setVisibility(View.VISIBLE);
            Seccion = true;
        } else if (v == btnCorte) {
            SvSeccion.setVisibility(View.VISIBLE);
            fragment_consulta Consulta = new fragment_consulta();
            fragmentManager.beginTransaction().replace(R.id.LySeccion, Consulta).commit();
            Seccion = true;
        }
    }
}
