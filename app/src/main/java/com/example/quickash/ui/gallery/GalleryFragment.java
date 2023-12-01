package com.example.quickash.ui.gallery;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickash.Login;
import com.example.quickash.R;
import com.example.quickash.databinding.FragmentGalleryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    TextInputEditText editText, editText1, editText2;
    TextInputLayout textInputLayout,textInputLayout1, textInputLayout2, textInputLayout3,textInputLayout4,textInputLayout5;
    TextView textView3, textView1,textView2, hide,hide1,hide2;
    String uid, email, username, pw;
    Button button, button1, button2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_myaccount);
        button=root.findViewById(R.id.btnconfirm);
        button1=root.findViewById(R.id.btnconfirm1);
        button2=root.findViewById(R.id.btnconfirm2);
        textInputLayout=root.findViewById(R.id.txtname);
        textInputLayout2=root.findViewById(R.id.txtbankno);
        textInputLayout3=root.findViewById(R.id.txtpassword);
        textInputLayout4=root.findViewById(R.id.txtconfirmpassword);
        textView1=root.findViewById(R.id.edit_name);
        textView3=root.findViewById(R.id.edit_pw);
        editText=root.findViewById(R.id.pangalan);
        editText1=root.findViewById(R.id.edit_pw1);
        editText2=root.findViewById(R.id.edit_pw2);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email=dataSnapshot.child("Users").child(uid).child("email").getValue(String.class);
                username=dataSnapshot.child("Users").child(uid).child("name").getValue(String.class);
                pw=dataSnapshot.child("Users").child(uid).child("password").getValue(String.class);
                textInputLayout2.setHint(email);
                textInputLayout.getEditText().setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayout.setEnabled(true);
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayout3.setEnabled(true);
                textInputLayout4.setEnabled(true);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!validateName()) {
                    return;
                }
                button1.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!validateName()) {
                    return;
                }
                button1.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!validateName()) {
                    return;
                }
                button1.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider.getCredential(email,pw);
                if (!validateName()) {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.btnconfirm),"Field can't be empty", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }else {
                    textInputLayout.setEnabled(false);
                    String adminadmin = textInputLayout.getEditText().getText().toString();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("name", adminadmin);
                    databaseReference.updateChildren(updates);
                    Toast.makeText(getActivity(), "Edit Successful", Toast.LENGTH_LONG).show();
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider.getCredential(email,pw);
                if (!validatePassword() | !validateConfirmPassword()) {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.btnconfirm1),"Field can't be empty", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }else {
                    textInputLayout3.setEnabled(false);
                    textInputLayout4.setEnabled(false);
                    String password = textInputLayout4.getEditText().getText().toString();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("password", password);
                    databaseReference.updateChildren(updates);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_LONG).show();
                                                    Toast.makeText(getActivity(), "Login Again", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(getActivity(), Login.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(getActivity(), "Error Password not Updated", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "Error Auth Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    Toast.makeText(getActivity(), "Edit Successful", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    private boolean validateName() {
        String name = textInputLayout.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String password = textInputLayout3.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            return false;
        } else if (textInputLayout3.getEditText().length() < 8) {
            textInputLayout3.setError("Password too weak");
            return false;

        } else {
            textInputLayout3.setError(null);
            return true;
        }
    }


    private boolean validateConfirmPassword() {
        String confirmpassword = textInputLayout4.getEditText().getText().toString().trim();
        String password = textInputLayout3.getEditText().getText().toString().trim();

        if (confirmpassword.isEmpty()) {
            return false;

        } else if (password.isEmpty()) {
            return false;

        } else if (!confirmpassword.equals(password)) {
            textInputLayout4.setError("Password does not match");
            return false;

        } else {
            textInputLayout3.equals(textInputLayout4);
            {
                textInputLayout4.setError(null);
                return true;
            }
        }
    }
}