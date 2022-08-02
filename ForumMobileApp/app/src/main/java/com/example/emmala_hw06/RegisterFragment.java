/* Assignment: HW06
   File Name: Emmala_HW06
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package com.example.emmala_hw06;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    EditText nameEditText, editTextEmail, EditTextPassword;
    Button submitButton;
    TextView cancel;
    private FirebaseAuth auth;
    RegisterListener registerListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.newAccountLabel));
        nameEditText = view.findViewById(R.id.nameEditText);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        EditTextPassword = view.findViewById(R.id.EditTextPassword);
        submitButton = view.findViewById(R.id.submitButton);
        cancel = view.findViewById(R.id.cancelTextView);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString(), email = editTextEmail.getText().toString(), password = EditTextPassword.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if(name.equals("")){
                    builder.setMessage(getString(R.string.emptyName))
                            .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.create().show();
                }
                else if(email.equals("")){
                    builder.setMessage(getString(R.string.emptyEmail))
                            .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.create().show();
                }else if(password.equals("")){
                    builder.setMessage(getString(R.string.emptyPassword))
                            .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.create().show();
                }else {
                    auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        String userId = task.getResult().getUser().getUid();
                                        FirebaseUser user = task.getResult().getUser();
                                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                        user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    registerListener.gotoForums(userId, name);
                                                }
                                            }
                                        });
                                    }else{
                                        builder.setMessage(task.getException().getMessage())
                                                .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                        builder.create().show();
                                    }
                                }
                            });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerListener.gotoLogin();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        registerListener = (RegisterListener) context;
    }

    interface RegisterListener{
        void gotoForums(String userId, String userName);
        void gotoLogin();
    }
}