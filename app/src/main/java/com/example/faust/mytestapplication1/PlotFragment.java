package com.example.faust.mytestapplication1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robertospaziani on 30/04/17.
 */


public class PlotFragment  extends Fragment {


    private int mColumnCount = 1;
    private ImageView profile_image;
    String id_group;
    private FirebaseAuth firebaseAuth;
    private XYPlot plot;
    private GraphView graph;
    Double gen_val=0.0,feb_val=0.0,marz_val=0.0,apr_val=0.0,
            mag_val=0.0,giu_val=0.0,lug_val=0.0,ago_val=0.0,
            set_val=0.0,ott_val=0.0,nov_val=0.0,dic_val=0.0;

    Double mymax=50.0;
    Date date = null;
    int mycount,totcount;
    Spinner scategory;
    Spinner mYearSpinner;
    String category_selected;
    String mYearSelected;
    List<String> mYearsList;
    String category;
    String mYearFromDB;

    private HashMap<String,NomeDovuto> attivita_dovuto;


    public PlotFragment () {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getArguments();

        id_group= b.getString("GROUP_ID");

        firebaseAuth = FirebaseAuth.getInstance();


        attivita_dovuto= new HashMap<>();


    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_plot, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"","gen","feb","mar","apr","mag","giu","lug","ago","set","ott","nov","dic",""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setNumHorizontalLabels(14);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);


      //  initData();

       // plot = (XYPlot) view.findViewById(R.id.plot);
       // plot();


        /////////////
        //adapter

        scategory = (Spinner) view.findViewById(R.id.Category_newexpense);
        mYearSpinner = (Spinner) view.findViewById(R.id.graph_year_spinner);

        //Spiinner Category

        //Log.d("FAST", Integer.toString(Calendar.getInstance().getTime().getYear())); //117 (=> 1900 + 117 = 2017)

        mYearsList = new ArrayList<>();
        for(int i = Calendar.getInstance().getTime().getYear()+1900; i>=1900; i--)
        {
            mYearsList.add(Integer.toString(i));
            //Log.d("FAST", Integer.toString(i));
        }

        //mYearsList.add("Choose a year");

        Categories cts = new Categories();
        //AGGIUNGO LE CATEGORIE
        String s = getString(R.string.category_all);
        cts.setItem(s);
        s = getString(R.string.category_generale);
        cts.setItem(s);
        s = getString(R.string.category_luce);
        cts.setItem(s);
        s = getString(R.string.category_gas);
        cts.setItem(s);
        s = getString(R.string.category_internet);
        cts.setItem(s);

        s = getString(R.string.category_cibo);
        cts.setItem(s);
        s = getString(R.string.category_regali);
        cts.setItem(s);


        ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, cts.getList());
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, mYearsList);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        scategory.setAdapter(adapterCat);
        mYearSpinner.setAdapter(adapterYear);

        //Listener

        scategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterC, View v,
                                       int position, long id) {
                // On selecting a spinner item
                //id_currency = adapterC.getItemAtPosition(position).toString();
                // Showing selected spinner item
                //prova
                scategory.setSelection(position);

                graph.removeAllSeries();
                category_selected = (String) scategory.getSelectedItem();

                if(category_selected.equals("Generale") || category_selected.equals("General")) {
                    category_selected = "Generale";
                }

                if(category_selected.equals("Luce") || category_selected.equals("Light")) {
                    category_selected = "Luce";
                }

                if(category_selected.equals("Cibo") || category_selected.equals("Food")) {
                    category_selected = "Cibo";
                }

                if(category_selected.equals("Gift") || category_selected.equals("Regalo")) {
                    category_selected = "Regalo";
                }

                initData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterC, View v,
                                       int position, long id) {
                // On selecting a spinner item
                //id_currency = adapterC.getItemAtPosition(position).toString();
                // Showing selected spinner item
                //prova
                mYearSpinner.setSelection(position);

                graph.removeAllSeries();
                mYearSelected = (String) mYearSpinner.getSelectedItem();

                initData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        category_selected = (String) scategory.getSelectedItem();

        if(category_selected.equals("Generale") || category_selected.equals("General")) {
            category_selected = "Generale";
        }

        if(category_selected.equals("Luce") || category_selected.equals("Light")) {
            category_selected = "Luce";
        }

        if(category_selected.equals("Cibo") || category_selected.equals("Food")) {
            category_selected = "Cibo";
        }

        if(category_selected.equals("Gift") || category_selected.equals("Regalo")) {
            category_selected = "Regalo";
        }

        //////////////////////////////

        mYearSelected = (String) mYearSpinner.getSelectedItem();


        /////////////////////////////

        final AppCompatActivity myactivity = (android.support.v7.app.AppCompatActivity) view.getContext();

        profile_image = (com.makeramen.roundedimageview.RoundedImageView) myactivity.findViewById(R.id.row1_image1);

        profile_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


            }
        });

        getandSetImage();






        final Button bGlobal = (Button) myactivity.findViewById(R.id.bGlobal);
        final Button bGroups = (Button) myactivity.findViewById(R.id.bGroups);
        final Button bActivities = (Button) myactivity.findViewById(R.id.bActivities);


        return view;
    }



    private void getandSetImage() {

        //getImage of user

        String url ;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id_group).child("Image");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //if != null SET

                final String image = dataSnapshot.getValue(String.class);

                if (image != null) {

                    if (!image.contains("http")) {
                        try {
                            Bitmap imageBitmaptaken = decodeFromFirebaseBase64(image);
                            //Bitmap imageCirle = getclip(imageBitmaptaken);
                            profile_image.setImageBitmap(imageBitmaptaken);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {


                        Picasso.with(getContext())
                                .load(image)
                                .fit()
                                .centerCrop()
                                .into(profile_image);


                    }


                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }


    private void initData() {

        attivita_dovuto.clear();


        DatabaseReference databaseReference;

        mycount=0;
        totcount=0;

        databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(id_group).child("Activities");

        //Read content data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                //Per ogni attivita
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    final String id = (String) postSnapshot.getKey();
                    final String nome = (String) postSnapshot.child("Name").getValue(String.class);
                   category = (String) postSnapshot.child("Category").getValue(String.class); //todo inserire categoria nel DB groups

                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String s = postSnapshot.child("Date").getValue(String.class);
                    try {
                        mYearFromDB = Integer.toString((format.parse(s)).getYear()+1900);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d("FAST", "mYearFromDB: " + mYearFromDB);

                    if(category.equals("Generale") || category.equals("General")) {
                        category = "Generale";
                    }

                    if(category.equals("Luce") || category.equals("Light")) {
                        category = "Luce";
                    }


                    if(category.equals("Payment") || category.equals("Pagamento")) {
                        category = "Pagamento";
                    }

                    if(category.equals("Cibo") || category.equals("Food")) {
                        category = "Cibo";
                    }

                    if(category.equals("Gift") || category.equals("Regalo")) {
                        category = "Regalo";
                    }



                if(category.equals("Pagamento")==false && category.equals("Payment")==false
                        && mYearFromDB.equals(mYearSelected)) {

                    if(category_selected.equals("All")==true || category_selected.equals("Tutte")) {

                        totcount++;


                        FirebaseDatabase.getInstance().getReference("Activities").child(postSnapshot.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mycount++;

                                        Double total = null;


                                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                        date = null;
                                        try {
                                            String s = dataSnapshot.child("Date").getValue(String.class);
                                            date = format.parse(s);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        total = dataSnapshot.child("Owner").child(firebaseAuth.getCurrentUser().getUid()).child("Total").getValue(Double.class);


                                        if (total == null) {

                                            total = dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Total").getValue(Double.class);


                                        }

                                        if (total == null) {
                                            total = 0.0;
                                        }

                                        NomeDovuto iniziale = new NomeDovuto(nome, total);
                                        iniziale.setId(id);
                                        iniziale.setCategory(category);
                                        iniziale.setDate(date);
                                        attivita_dovuto.put(id, iniziale);


                                        if (mycount == totcount) {

                                            ///Now I have to set the graph
                                            clearValue();
                                            setValue();


                                            graph.getViewport().setXAxisBoundsManual(true);
                                            graph.getViewport().setMinX(0);
                                            graph.getViewport().setMaxX(13);

                                            // set manual Y bounds
                                            graph.getViewport().setYAxisBoundsManual(true);
                                            graph.getViewport().setMinY(0);
                                            graph.getViewport().setMaxY(mymax);


                                            // graph.getLegendRenderer().setVisible(true);


                                            //adesso posso lavorare con il grafico
                                            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{

                                                    new DataPoint(1, gen_val),
                                                    new DataPoint(2, feb_val),
                                                    new DataPoint(3, marz_val),
                                                    new DataPoint(4, apr_val),
                                                    new DataPoint(5, mag_val),
                                                    new DataPoint(6, giu_val),
                                                    new DataPoint(7, lug_val),
                                                    new DataPoint(8, ago_val),
                                                    new DataPoint(9, set_val),
                                                    new DataPoint(10, ott_val),
                                                    new DataPoint(11, nov_val),
                                                    new DataPoint(12, dic_val)
                                            });

                                            series.setSpacing(20);
                                            // draw values on top
                                            series.setDrawValuesOnTop(true);
                                            series.setValuesOnTopColor(Color.DKGRAY);

                                            graph.addSeries(series);





                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    }//all

                    else{

                        //Non è all

                        if(category_selected.equals(category)){

                            totcount++;


                            FirebaseDatabase.getInstance().getReference("Activities").child(postSnapshot.getKey())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {


                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mycount++;

                                            Double total = null;


                                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                            date = null;
                                            try {
                                                String s = dataSnapshot.child("Date").getValue(String.class);
                                                date = format.parse(s);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            total = dataSnapshot.child("Owner").child(firebaseAuth.getCurrentUser().getUid()).child("Total").getValue(Double.class);


                                            if (total == null) {

                                                total = dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Total").getValue(Double.class);


                                            }

                                            if (total == null) {
                                                total = 0.0;
                                            }

                                            NomeDovuto iniziale = new NomeDovuto(nome, total);
                                            iniziale.setId(id);
                                            iniziale.setCategory(category);
                                            iniziale.setDate(date);
                                            attivita_dovuto.put(id, iniziale);


                                            if (mycount == totcount) {

                                                ///Now I have to set the graph
                                                clearValue();
                                                setValue();


                                                graph.getViewport().setXAxisBoundsManual(true);
                                                graph.getViewport().setMinX(0);
                                                graph.getViewport().setMaxX(13);

                                                // set manual Y bounds
                                                graph.getViewport().setYAxisBoundsManual(true);
                                                graph.getViewport().setMinY(0);
                                                graph.getViewport().setMaxY(mymax);


                                                // graph.getLegendRenderer().setVisible(true);


                                                //adesso posso lavorare con il grafico
                                                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{

                                                        new DataPoint(1, gen_val),
                                                        new DataPoint(2, feb_val),
                                                        new DataPoint(3, marz_val),
                                                        new DataPoint(4, apr_val),
                                                        new DataPoint(5, mag_val),
                                                        new DataPoint(6, giu_val),
                                                        new DataPoint(7, lug_val),
                                                        new DataPoint(8, ago_val),
                                                        new DataPoint(9, set_val),
                                                        new DataPoint(10, ott_val),
                                                        new DataPoint(11, nov_val),
                                                        new DataPoint(12, dic_val)
                                                });

                                                series.setSpacing(20);
                                                // draw values on top
                                                series.setDrawValuesOnTop(true);
                                                series.setValuesOnTopColor(Color.DKGRAY);

                                                graph.addSeries(series);





                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });





                        }






                    }

                       }//category



                }




            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    private void clearValue() {

        mymax=50.00;
        gen_val=0.0;
        feb_val=0.0;
                marz_val=0.0;
                apr_val=0.0;

                mag_val=0.0;
                giu_val=0.0;
                lug_val=0.0;
                ago_val=0.0;
                set_val=0.0;
                ott_val=0.0;
                nov_val=0.0;
                dic_val=0.0;
    }

    private void setValue() {

        //usare i double e attivita_dovuto

        Date date = new Date();
        int year = date.getYear();


        for(NomeDovuto nm : attivita_dovuto.values()){


            if(year==nm.getDate().getYear()){
                //se la spesa e' relativa a quest'anno

                switch (nm.getDate().getMonth()){

                    case 0:
                        gen_val+=nm.getDovuto();
                        if(mymax<gen_val){
                            mymax=gen_val+10;
                        }
                        break;

                    case 1:
                        feb_val+=nm.getDovuto();
                        if(mymax<feb_val){
                            mymax=feb_val+10;
                        }
                        break;

                    case 2:
                        marz_val+=nm.getDovuto();
                        if(mymax<marz_val){
                            mymax=marz_val+10;
                        }
                        break;

                    case 3:
                        apr_val+=nm.getDovuto();
                        if(mymax<apr_val){
                            mymax=apr_val+10;
                        }
                        break;

                    case 4:
                        mag_val+=nm.getDovuto();
                        if(mymax<mag_val){
                            mymax=mag_val+10;
                        }
                        break;

                    case 5:
                        giu_val+=nm.getDovuto();
                        if(mymax<giu_val){
                            mymax=giu_val+10;
                        }
                        break;

                    case 6:
                        lug_val+=nm.getDovuto();
                        if(mymax<lug_val){
                            mymax=lug_val+10;
                        }
                        break;

                    case 7:
                        ago_val+=nm.getDovuto();
                        if(mymax<ago_val){
                            mymax=ago_val+10;
                        }
                        break;

                    case 8:
                        set_val+=nm.getDovuto();
                        if(mymax<set_val){
                            mymax=set_val+10;
                        }
                        break;

                    case 9:
                        ott_val+=nm.getDovuto();
                        if(mymax<ott_val){
                            mymax=ott_val+10;
                        }
                        break;


                    case 10:
                        nov_val+=nm.getDovuto();
                        if(mymax<nov_val){
                            mymax=nov_val+10;
                        }
                        break;

                    case 11:
                        dic_val+=nm.getDovuto();
                        if(mymax<dic_val){
                            mymax=dic_val+10;
                        }
                        break;



                }





            }


        }





    }


    private void plot(){

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
        Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);

        // add an "dash" effect to the series2 line:
        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {

                // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {


            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });






    }


    private void plot2(){

        // init example series data
        /*
        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 2.0d)
                , new GraphViewData(2, 1.5d)
                , new GraphViewData(3, 2.5d)
                , new GraphViewData(4, 1.0d)
        });

        GraphView graphView = new LineGraphView(
                this // context
                , "GraphViewDemo" // heading
        );
        graphView.addSeries(exampleSeries); // data
        */

    }










}