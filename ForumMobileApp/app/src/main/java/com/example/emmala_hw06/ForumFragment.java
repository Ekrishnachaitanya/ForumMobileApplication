/* Assignment: HW06
   File Name: Emmala_HW06
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package com.example.emmala_hw06;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView forumTitleTextView, forumNameTextView, forumComments;
    EditText writeCommentEditText;
    Button postButton;
    RecyclerView commentRecyclerView;
    FirebaseFirestore store;
    ArrayList<Comment> comments;
    CommentAdapterFragment adapterFragment;
    String TAG ="Demo";
    LinearLayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String forumId;
    private String userName;

    public ForumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(String forumId, String userName) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, forumId);
        args.putString(ARG_PARAM2, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forumId = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forumTitleTextView = view.findViewById(R.id.forumTitleTextView);
        forumNameTextView = view.findViewById(R.id.forumNameTextView);
        forumComments = view.findViewById(R.id.forumComments);
        writeCommentEditText = view.findViewById(R.id.writeCommentEditText);
        postButton = view.findViewById(R.id.postButton);
        commentRecyclerView = view.findViewById(R.id.commentRecyclerView);
        comments = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(layoutManager);
        store = FirebaseFirestore.getInstance();
        store.collection("forums").document(forumId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess: "+documentSnapshot);
                        forumTitleTextView.setText(documentSnapshot.get("forumTitle").toString());
                        forumNameTextView.setText(documentSnapshot.get("forumDescription").toString());
                    }
                });

        store.collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                comments.clear();
                for(QueryDocumentSnapshot querySnapShot: value){
                    if(querySnapShot.get("forumId") != null && querySnapShot.get("forumId").equals(forumId)){
                        Comment comment = new Comment(querySnapShot.get("commentCreator").toString(), querySnapShot.get("commentText").toString(), querySnapShot.getId(), querySnapShot.get("dateTime").toString());
                        comments.add(comment);
                    }
                }
                forumComments.setText(comments.size()+" comments");
                adapterFragment = new CommentAdapterFragment(comments);
                commentRecyclerView.setAdapter(adapterFragment);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(writeCommentEditText.getText().toString().equals("")){
                    Log.d(TAG, "onClick: inside ");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getString(R.string.okLabel))
                            .setPositiveButton(getString(R.string.okLabel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.create().show();
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                HashMap<String, Object> commentDetail = new HashMap<>();
                commentDetail.put("commentCreator", userName);
                commentDetail.put("commentText", writeCommentEditText.getText().toString());
                commentDetail.put("forumId", forumId);
                commentDetail.put("dateTime", new SimpleDateFormat("dd/MM/yyyy hh.mm aa", Locale.US).format(new Date()));
                db.collection("comments")
                        .add(commentDetail)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                writeCommentEditText.setText("");
                            }
                        });

            }
        });
    }

    class CommentAdapterFragment extends RecyclerView.Adapter<CommentAdapterFragment.CommentViewHolder>{

        ArrayList<Comment> comments;

        CommentAdapterFragment(ArrayList<Comment> comments){
            this.comments = comments;
        }

        @NonNull
        @Override
        public CommentAdapterFragment.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_layout, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentAdapterFragment.CommentViewHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.commentCreator.setText(comment.getCommentCreator());
            holder.commentText.setText(comment.getCommentText());
            holder.dateTextView.setText(comment.getDateTime());
            holder.comment = comment;
            if(comment.getCommentCreator().equals(userName)){
                holder.binIV.setVisibility(View.VISIBLE);
                holder.binIV.setImageResource(R.drawable.rubbish_bin);
            }else{
                holder.binIV.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return this.comments.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder{
            TextView commentCreator, commentText, dateTextView;
            ImageView binIV;
            Comment comment;
            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                commentCreator = itemView.findViewById(R.id.commentCreator);
                commentText = itemView.findViewById(R.id.commentText);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                binIV = itemView.findViewById(R.id.binIV);
                binIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("comments").document(comment.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                    }
                });
            }
        }
    }
}