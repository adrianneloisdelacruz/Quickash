package com.example.quickash.ui.home;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.metrics.Event;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickash.Buttonadd;
import com.example.quickash.ListAdapter;
import com.example.quickash.R;
import com.example.quickash.Resibo;
import com.example.quickash.Resibo1;
import com.example.quickash.Resibo2;
import com.example.quickash.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends Fragment{

    private  FirebaseAuth auth;
    private static int MAX = 1000;
    private static boolean[] numbers;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4;
    private DatabaseReference databaseReference5;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    FirebaseUser user;
    double aDouble;
    List<String> spinnerlist;
    ArrayAdapter<String> adapter, spinnerAdapter;
    Dialog dialoog, dialog1, dialog2;
    String username, uid, balance, result, hidden, hidden1, hidden2, hidden3, history, counter, outer_user, userfinal, sms, balance1, date, no, yes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        Button cashinbtn = (Button) view.findViewById(R.id.cashin);
        Button cashoutbtn = (Button) view.findViewById(R.id.cashout);
        Button transferbtn = (Button) view.findViewById(R.id.transferbtn);
        TextView name = (TextView) view.findViewById(R.id.namehere);
        TextView hide = (TextView) view.findViewById(R.id.hide);
        TextView hide1 = (TextView) view.findViewById(R.id.hide1);
        ListView listView = (ListView) view.findViewById(R.id.list);
        TextView amounttotal = (TextView) view.findViewById(R.id.amount);
        dialoog = new Dialog(getActivity());
        dialoog.setContentView(R.layout.activity_buttonadd);
        dialog1 = new Dialog(getActivity());
        dialog1.setContentView(R.layout.activity_buttonminus);
        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.activity_buttontransfer);
        ImageView close = (ImageView) dialoog.findViewById(R.id.close);
        Button btnbtn = (Button) dialoog.findViewById(R.id.btnbtn1);
        TextView title1 = (TextView) dialoog.findViewById(R.id.title1);
        EditText amount1 = (EditText) dialoog.findViewById(R.id.amount);
        ImageView close1 = (ImageView) dialog1.findViewById(R.id.close);
        Button btnbtn1 = (Button) dialog1.findViewById(R.id.btnbtn1);
        TextView title11 = (TextView) dialog1.findViewById(R.id.title1);
        EditText amount11 = (EditText) dialog1.findViewById(R.id.amount);
        ImageView close11 = (ImageView) dialog2.findViewById(R.id.close);
        Button btnbtn11 = (Button) dialog2.findViewById(R.id.btnbtn1);
        TextView title111 = (TextView) dialog2.findViewById(R.id.title1);
        TextView namee = (TextView) dialog2.findViewById(R.id.namelol);
        TextView value = (TextView) dialog2.findViewById(R.id.val);
        EditText amount111 = (EditText) dialog2.findViewById(R.id.amount);
        Spinner spinner = (Spinner) dialog2.findViewById(R.id.spinner);
        spinnerlist = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Transaction> userArrayList = new ArrayList<>();
        uid = user.getUid();
        calendar=Calendar.getInstance();
        simpleDateFormat= new SimpleDateFormat("MM-dd-yyyy, HH-mm-ss");
        date = simpleDateFormat.format(calendar.getTime());
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Transactions");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History");
        databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("RecentHistory");
        databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("DateTime");
        databaseReference5 = FirebaseDatabase.getInstance().getReference();
        //        ArrayList<Integer> number = new ArrayList<Integer>();
//        for (int i = 1; i <= 1000; ++i) number.add(i);
//        Collections.shuffle(number);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot list : dataSnapshot.getChildren()) {
                    outer_user = String.valueOf(list.child("user_uid").getValue());
                    spinnerlist.add(outer_user);
                    spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerlist);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);
                }
                sms = spinner.getSelectedItem().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(item.toString());
                databaseReference3.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userfinal = dataSnapshot.child("name").getValue(String.class);
                        namee.setText(userfinal);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(item.toString()).child("Transactions");
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        balance1=dataSnapshot.child("balance").getValue(String.class);
                        hide1.setText(balance1);
                        hidden1 = hide1.getText().toString();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                });
//                System.out.println(item.toString());     //prints the text in spinner item.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=dataSnapshot.child(uid).child("name").getValue(String.class);
                name.setText(username);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        databaseReference1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                balance = dataSnapshot.child("balance").getValue(String.class);
                counter = dataSnapshot.child("counter").getValue(String.class);
                if(aDouble>0.0) {
                    aDouble = Double.valueOf(balance);
                    String strDouble = String.format("%.2f", aDouble);
                    amounttotal.setText(strDouble);
                }else{
                    amounttotal.setText("0.00");
                }
                value.setText(balance);
                hide.setText(counter);
                hidden = hide.getText().toString();
                if (hidden.isEmpty()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Transactions");
                    Map newPost = new HashMap();
                    newPost.put("balance", "0");
                    newPost.put("counter", "1000");
                    databaseReference.setValue(newPost);
//                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History");
//                    Map newPost1 = new HashMap();
//                    newPost1.put("value", "0");
//                    databaseReference1.setValue(newPost1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        databaseReference2.child("Value").addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    history = String.valueOf(dataSnapshot1.getValue());
                    Transaction here = new Transaction(history, R.drawable.ic_baseline_attach_money_24, no);
                    userArrayList.add(here);
                }

                ListAdapter adapter = new ListAdapter(getActivity(), userArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoog.dismiss();
            }
        });

        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        close11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to all delete?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History");
                        dR.removeValue();
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Transactions");
                        Map<String, Object> updates = new HashMap<String,Object>();
                        updates.put("counter", "1000");
                        databaseReference.updateChildren(updates);
                    }});
                adb.show();
            }
        });

        btnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = amount1.getText().toString();
                if (value.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a valid amount. ", Toast.LENGTH_LONG).show();
                } else {
                    hidden = hide.getText().toString();
                    result = amounttotal.getText().toString();
                    double d = Double.valueOf(value);
                    double e = Double.valueOf(result);
                    int f = Integer.valueOf(hidden);
                    d += e;
                    f -= 1;
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(d);
                    String strI = String.valueOf(d);
                    String strI1 = String.valueOf(f);
//                      databaseReference.child("balance").setValue(ServerValue.increment(result));
                    AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                    adb.setTitle("Confirmation");
                    adb.setMessage("Are you sure you want to transaction this amount before you proceed?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Transactions");
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("balance", strI);
                            updates.put("counter", strI1);
                            databaseReference.updateChildren(updates);
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("Value");
                            Map<String, Object> updates1 = new HashMap<String,Object>();
                            updates1.put("value" + strI1, "Cash in to your account +₱" + value);
                            databaseReference1.updateChildren(updates1);
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("CashIn");
                            Map<String, Object> updates2 = new HashMap<String,Object>();
                            updates2.put("value" + strI1, value);
                            databaseReference2.updateChildren(updates2);
                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("DateTime");
                            Map<String, Object> updates3 = new HashMap<String,Object>();
                            updates3.put("value" + strI1, date);
                            databaseReference3.updateChildren(updates3);
                            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("RecentHistory");
                            Map<String, Object> updates4 = new HashMap<String,Object>();
                            updates4.put("value" + strI1, "Cash in");
                            databaseReference4.updateChildren(updates4);
                            Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
//                                adapter.notifyDataSetChanged();
                            dialoog.dismiss();
                            amount1.setText("");
                            aDouble = 1.0;
                            Intent intent = new Intent(getActivity(), Resibo2.class);
                            intent.putExtra("id", uid);
                            intent.putExtra("name", username);
                            intent.putExtra("value", value);
                            startActivity(intent);
                        }});
                    adb.show();
                }
            }
        });

        btnbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = amount11.getText().toString();
                if (value.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a valid amount. ", Toast.LENGTH_LONG).show();
                } else {
                    hidden = hide.getText().toString();
                    result = amounttotal.getText().toString();
                    double d = Double.valueOf(value);
                    double e = Double.valueOf(result);
                    int f = Integer.valueOf(hidden);
                    e -= d;
                    f -= 1;
                    String strI = String.valueOf(e);
                    String strI1 = String.valueOf(f);
                    if (e <= 0) {
                        Toast.makeText(getActivity(), "Your Balance is insufficient", Toast.LENGTH_LONG).show();
                        amounttotal.setText("0");
                    } else {
                        AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                        adb.setTitle("Confirmation");
                        adb.setMessage("Are you sure you want to transaction this amount before you proceed?");
                        adb.setNegativeButton("No", null);
                        adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Transactions");
                                Map<String, Object> updates = new HashMap<String,Object>();
                                updates.put("balance", strI);
                                updates.put("counter", strI1);
                                databaseReference.updateChildren(updates);
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("Value");
                                Map<String, Object> updates1 = new HashMap<String,Object>();
                                updates1.put("value" + strI1, "Cash out to your account -₱" + value);
                                databaseReference1.updateChildren(updates1);
                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("CashOut");
                                Map<String, Object> updates2 = new HashMap<String,Object>();
                                updates2.put("value" + strI1, value);
                                databaseReference2.updateChildren(updates2);
//                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("CashOut");
//                                Map<String, Object> updates2 = new HashMap<String,Object>();
//                                updates2.put("value" + strI1, "Cash out");
//                                databaseReference2.updateChildren(updates2);
                                Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
//                                adapter.notifyDataSetChanged();
                                dialog1.dismiss();
                                amount11.setText("");
                                aDouble = 1.0;
                                Intent intent = new Intent(getActivity(), Resibo1.class);
                                intent.putExtra("id", uid);
                                intent.putExtra("name", username);
                                intent.putExtra("value", value);
                                startActivity(intent);
                            }});
                        adb.show();
                    }
                }
            }
        });

        btnbtn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = amount111.getText().toString();
                sms = spinner.getSelectedItem().toString();
                if (value.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a valid amount. ", Toast.LENGTH_LONG).show();
                } else {
                    hidden = hide.getText().toString();
                    result = amounttotal.getText().toString();
                    hidden1 = hide1.getText().toString();
                    double d = Double.valueOf(value);
                    double e = Double.valueOf(result);
                    int f = Integer.valueOf(hidden);
                    double g = Double.valueOf(hidden1);
                    e -= d;
                    f -= 1;
                    g += d;
                    String strI = String.valueOf(e);
                    String strI1 = String.valueOf(f);
                    String strI2 = String.valueOf(g);
                    hide1.setText(strI2);
                    hidden2 = hide1.getText().toString();
                    amounttotal.setText(strI);
                    hidden3 = amounttotal.getText().toString();
                    if (e <= 0) {
                        Toast.makeText(getActivity(), "Your Balance is insufficient", Toast.LENGTH_LONG).show();
                        amounttotal.setText("0");
                    } else {
                        if(uid.equals(sms)){
                            Toast.makeText(getActivity(), "You can't transfer on your own account", Toast.LENGTH_LONG).show();
                        }else{
                            AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                            adb.setTitle("Confirmation");
                            adb.setMessage("Are you sure you want to transaction this amount before you proceed?");
                            adb.setNegativeButton("No", null);
                            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("Value");
                                    Map<String, Object> updates1 = new HashMap<String,Object>();
                                    updates1.put("value" + strI1, "You Transfer -₱" + value);
                                    databaseReference1.updateChildren(updates1);
                                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Transactions");
                                    Map<String, Object> updates2 = new HashMap<String,Object>();
                                    updates2.put("balance", hidden3);
                                    updates2.put("counter", strI1);
                                    databaseReference2.updateChildren(updates2);
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(sms).child("Transactions");
                                    Map<String, Object> updates = new HashMap<String,Object>();
                                    updates.put("balance", hidden2);
                                    updates.put("counter", strI1);
                                    databaseReference.updateChildren(updates);
                                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(sms).child("History").child("Value");
                                    Map<String, Object> updates3 = new HashMap<String,Object>();
                                    updates3.put("value" + strI1, "Transfer +₱" + value);
                                    databaseReference3.updateChildren(updates3);
                                    DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("Transfer");
                                    Map<String, Object> updates4 = new HashMap<String,Object>();
                                    updates4.put("value" + strI1, value);
                                    databaseReference4.updateChildren(updates4);
                                    DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference().child("Users").child(sms).child("History").child("Transfer");
                                    Map<String, Object> updates5 = new HashMap<String,Object>();
                                    updates5.put("value" + strI1, value);
                                    databaseReference5.updateChildren(updates5);
//                                    DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("History").child("CashOut");
//                                    Map<String, Object> updates4 = new HashMap<String,Object>();
//                                    updates4.put("value" + strI1, "Transfer");
//                                    databaseReference4.updateChildren(updates4);
                                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                                    dialog2.dismiss();
                                    amount111.setText("");
                                    aDouble = 1.0;
                                    Intent intent = new Intent(getActivity(), Resibo.class);
                                    intent.putExtra("id", sms);
                                    intent.putExtra("name", userfinal);
                                    intent.putExtra("value", value);
                                    startActivity(intent);
                                }});
                            adb.show();
                        }
                    }
                }
            }
        });

//        btnbtn111.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnbtn11.setVisibility(View.VISIBLE);
//                btnbtn111.setVisibility(View.GONE);
//                String value = amount111.getText().toString();
//                sms = spinner.getSelectedItem().toString();
//                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child("VHOhptFi5dZJTHw15KMyjgo9YfZ2").child("Transactions");
//                            databaseReference2.addValueEventListener(new ValueEventListener() {
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    balance1=dataSnapshot.child("balance").getValue(String.class);
//                                    hide1.setText(balance1);
//                                    hidden1 = hide1.getText().toString();
//                                    int d = Integer.valueOf(value);
//                                    int e = Integer.valueOf(hidden1);
//                                    d += e;
//                                    String strI = String.valueOf(d);
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("VHOhptFi5dZJTHw15KMyjgo9YfZ2").child("Transactions");
//                                    Map<String, Object> updates = new HashMap<String,Object>();
//                                    updates.put("balance", strI);
//                                    databaseReference.updateChildren(updates);
//                                    dialog2.dismiss();
//                                    amount111.setText("");
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
//                                }
//                            });
//            }
//        });

        cashinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialoog.show();
            }
        });

        cashoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
            }
        });

        transferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        return view;
    }

//    private static int getRandomWithoutDuplicates(Random generator) {
//        int randomNum;
//        do {
//            randomNum = generator.nextInt(MAX);
//        } while (numbers[randomNum]);
//        numbers[randomNum] = true;
//        return randomNum;
//    }
}