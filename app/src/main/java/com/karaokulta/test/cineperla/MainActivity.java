package com.karaokulta.test.cineperla;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    LinearLayout fragmentContainer;
    FragmentManager fragmentManager = getFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.BtnCartelera:
                    for(int i = 0; i < fragmentManager.getBackStackEntryCount();i++){
                        fragmentManager.popBackStack();
                    }
                    fragment_cartelera Cartelera = new fragment_cartelera();
                    fragmentManager.beginTransaction().replace(R.id.LyFunciones, Cartelera).commit();
                    return true;
                case R.id.BtnEstrenos:
                    for(int i = 0; i < fragmentManager.getBackStackEntryCount();i++){
                        fragmentManager.popBackStack();
                    }
                    fragment_estrenos Estrenos = new fragment_estrenos();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.LyFunciones, Estrenos).commit();
                    return true;
                case R.id.BtnPerfil:
                    for(int i = 0; i < fragmentManager.getBackStackEntryCount();i++){
                        fragmentManager.popBackStack();
                    }
                    /*SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("correo", "modificado@email.com");
                    editor.commit();*/

                    fragment_profile Perfil = new fragment_profile();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.LyFunciones, Perfil).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("Informacion", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        DataBaseHelper dbh = new DataBaseHelper(this, DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
        SQLiteDatabase db = dbh.getWritableDatabase();
        if(db == null){
            Toast.makeText(getApplicationContext(), "Es null", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "No es null", Toast.LENGTH_SHORT).show();
        }
        db.close();


        fragmentContainer = (LinearLayout) findViewById(R.id.LyFunciones);

        fragment_cartelera Cartelera = new fragment_cartelera();
        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.LyFunciones, Cartelera).addToBackStack(null);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
