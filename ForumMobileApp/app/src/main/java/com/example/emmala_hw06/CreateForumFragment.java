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

import android.text.format.DateFormat;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CreateForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText forumTitleEditText, descriptionEditText;
    Button buttonSubmit;
    TextView textViewCancel;
    CreateForumListener listener;
    String TAG = "Demo";

    // TODO: Rename and change types of parameters
    private String userId;
    private String userName;

    public CreateForumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreateForumFragment newInstance(String userId, String userName) {
        CreateForumFragment fragment = new CreateForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userId);
        args.putString(ARG_PARAM2, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.newForum));
        forumTitleEditText = view.findViewById(R.id.forumTitleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        textViewCancel = view.findViewById(R.id.textViewCancel);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                listener.popBackToForumList();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.popBackToForumList();
            }
        });
    }

    public void setData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(forumTitleEditText.getText().toString().equals("")){
            builder.setMessage(getString(R.string.emptyForumTitle))
                    .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.create().show();
            return;
        }else if(descriptionEditText.getText().toString().equals("")){
            builder.setMessage(getString(R.string.emptyForumDescription))
                    .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.create().show();
            return;
        }
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        HashMap<String, Object> forum = new HashMap<>();
        forum.put("forumTitle", forumTitleEditText.getText().toString());
        forum.put("name", userName);
        forum.put("userId", userId);
        forum.put("forumDescription", descriptionEditText.getText().toString());
        String date = new SimpleDateFormat("dd/MM/yyyy hh.mm aa", Locale.US).format(new Date());
        forum.put("time", date);
        store.collection("forums")
                .add(forum)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: ");
                            Log.d("Demo", "onSuccess: ");
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (CreateForumListener) context;
    }

    interface CreateForumListener{
        void popBackToForumList();
    }
}