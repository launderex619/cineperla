package com.karaokulta.test.cineperla;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_sala extends Fragment {
    boolean[] Lugares = new boolean[20];
    Button comprar;
    TextView boletitos;
    TextView precioTotal;
    int Boletos;
    int BoletosEnUso = 0;
    int BoletosRestantes;

    String codigof;
    String peliculaf;
    String sala;
    String asientos;
    String boletos;
    String cantidadBoletos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        codigof = bundle.getString("codigof", "null");
        peliculaf = bundle.getString("peliculaf", "null");
        sala = bundle.getString("sala", "null");
        asientos = bundle.getString("asientos", "null");
        boletos = bundle.getString("boletos", "null");
        cantidadBoletos = bundle.getString("cantidadBoletos", "" + "null");

        View view = inflater.inflate(R.layout.fragment_sala, container, false);
        comprar = (Button) view.findViewById(R.id.comprar);
        boletitos = (TextView) view.findViewById(R.id.TvCantidadBoletosSala);
        precioTotal = (TextView) view.findViewById(R.id.TvCostoSala);
        for (int i = 0; i < 20; i++) {
            Lugares[i] = false;
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        final String correo = prefs.getString("correo", "null");
        String contrasena = prefs.getString("contrasena", "null");


        Boletos = Integer.parseInt(cantidadBoletos);
        BoletosRestantes = Integer.parseInt(boletos);

        // Boletos = 20;
        // asientos="0|6|8|9|15|19";
        String lugares[] = asientos.split("\\|");
        if (!asientos.equals("null") && !asientos.equals("30")) {
            for (int i = 0; i < lugares.length; i++) {
                for (int j = 0; j < 20; j++) {
                    if (lugares[i].equals("" + j)) {
                        Lugares[j] = true;
                    }
                }
            }
        }
        if (20 - lugares.length < Boletos) {
            Toast.makeText(getActivity(), "No quedan lugares", Toast.LENGTH_SHORT).show();
            comprar.setVisibility(View.GONE);
        }
        //boletos = "3";

       /* Toast.makeText(getActivity(), bundle.getString("cantidadBoletos", "null"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), bundle.getString("peliculaf", "null"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), bundle.getString("codigof", "null"), Toast.LENGTH_SHORT).show();
*/
        boletitos.setText(cantidadBoletos);
        precioTotal.setText("" + (Boletos * 40));
        GridView GvItems = (GridView) view.findViewById(R.id.GvAsientos);
        GvItems.setAdapter(new CustomAdapter(getContext()));

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == comprar) {
                    if (BoletosEnUso < Boletos) {
                        Toast.makeText(getActivity(), "Llena todos los lugares", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            DataBaseHelper dbh = new DataBaseHelper(getActivity(), DataBaseCommands.DATABASE_NAME, null, DataBaseCommands.DATABASE_VERSION);
                            SQLiteDatabase db = dbh.getReadableDatabase();
                            if (db != null) {
                                Cursor c = db.rawQuery(" SELECT credito, premium, codigo FROM usuario WHERE correo ='" + correo + "'", null);
                                if (c != null && c.moveToNext()) {
                                    if (c.getInt(0) >= (Boletos * 40)) {
                                        if (c.getInt(1) == 1) {
                                            int a = (int) ((Boletos * 40) * .05);
                                            db.execSQL("UPDATE usuario SET puntos = " + a + " " +
                                                    "WHERE correo = '" + correo + "'");
                                        }

                                        for (int i = 0; i < 20; i++) {
                                            if (Lugares[i]) {
                                                asientos += "|" + i;
                                            }
                                        }
                                        int a = (int) ((Boletos * 40));
                                        db.execSQL("UPDATE usuario SET credito = credito - " + a + " " +
                                                "WHERE correo = '" + correo + "'");

                                        db.execSQL("UPDATE funcion SET asientos = '" + asientos + "', " +
                                                "boletos = " + (BoletosRestantes - Boletos) + " " +
                                                "WHERE codigo = " + Integer.parseInt(codigof));

                                        db.execSQL("INSERT INTO boleto (fk_funcion, fk_usuario, cantidad_boletos, compra_en_linea, precio_total) " +
                                                "VALUES (" + Integer.parseInt(codigof) + ", " + c.getInt(2) + ", " + Boletos + ", " + 1 + ", " + (Boletos * 40) + ")");
                                        Toast.makeText(getActivity(), "!Comprado con exito!", Toast.LENGTH_SHORT).show();
                                        getFragmentManager().popBackStack();
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "No tienes suficiente saldo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Debes tener sesion para comprar", Toast.LENGTH_SHORT).show();
                                }
                            }
                            db.close();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        return view;
    }


    class CustomAdapter extends BaseAdapter {

        Context context;
        Button btnAsiento;
        boolean asientoOcupado = true;

        CustomAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            return 20;
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
        public View getView(final int position, View view, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_sala, parent, false);

            btnAsiento = (Button) view.findViewById(R.id.btnAsiento);
            if (Lugares[position]) {
                btnAsiento.setPressed(true);
            }

            btnAsiento.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (!Lugares[position]) {
                            if (v.isPressed()) {
                                BoletosEnUso--;
                                Lugares[position] = false;
                                v.setPressed(!v.isPressed());
                            } else {
                                if (BoletosEnUso == Boletos) {
                                    Toast.makeText(getActivity(), "Ya puedes comprar", Toast.LENGTH_SHORT).show();
                                } else {
                                    BoletosEnUso++;
                                    Lugares[position] = true;
                                    v.setPressed(!v.isPressed());
                                }
                            }
                        }
                    }
                    return true;
                }

            });
            return view;
        }
    }
}