package com.example.quickash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Statistics extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseReference databaseReference;
    String uid, cas;
    FirebaseUser user;
    String[] months = {"January","February","March","April"};
    int[] salary = {16000, 20000, 30000, 50000};
    int size = 0;
    float s, s1 , s2;
    String string, string1, string2;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        pieChart = findViewById(R.id.activity_main_piechart);
        textView = findViewById(R.id.de);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History");



        setupPieChart();
        loadPieChartData();
    }


    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Transaction");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get total available quest
                        string = String.valueOf(dataSnapshot.child("CashIn").getChildrenCount());
                        string1 = String.valueOf(dataSnapshot.child("CashOut").getChildrenCount());
                        string2 = String.valueOf(dataSnapshot.child("Transfer").getChildrenCount());
                        float xHours = Float.parseFloat(string);
                        float xHours1 = Float.parseFloat(string1);
                        float xHours2 = Float.parseFloat(string2);
                        s += xHours;
                        s1 += xHours1;
                        s2 += xHours2;
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        if (s != 0){
                            entries.add(new PieEntry(s, "Cash In"));
                        }
                        if (s1 != 0){
                            entries.add(new PieEntry(s1, "Cash Out"));
                        }
                        if (s2 != 0){
                            entries.add(new PieEntry(s2, "Transfer"));
                        }

                        ArrayList<Integer> colors = new ArrayList<>();
                        for (int color: ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "Transaction Category");
                        dataSet.setColors(colors);

                        PieData data = new PieData(dataSet);
                        data.setDrawValues(true);
                        data.setValueFormatter(new PercentFormatter(pieChart));
                        data.setValueTextSize(12f);
                        data.setValueTextColor(Color.BLACK);
                        pieChart.setData(data);
                        pieChart.invalidate();
                        pieChart.animateY(1400, Easing.EaseInOutQuad);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}

//    private void setupChartView() {
//        Pie pie = AnyChart.pie();
//        List<DataEntry> dataEntries = new ArrayList<>();
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    cas = String.valueOf(dataSnapshot1.getValue());
//                    String[] words = cas.split(":");
//
//                    for (int i=0; i<words.length; i++){
//                        dataEntries.add(new ValueDataEntry(months[i], salary[i]));
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        pie.data(dataEntries);
//        pie. title("Salary");
//        anyChartView.setChart(pie);
//    }
//}