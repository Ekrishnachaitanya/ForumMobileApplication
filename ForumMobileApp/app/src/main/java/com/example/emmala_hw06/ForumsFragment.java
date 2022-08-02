/* Assignment: HW06
   File Name: Emmala_HW06
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package com.example.emmala_hw06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ForumsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String userId, userName;
    Button logoutButton, newForumButton;
    RecyclerView recyclerView;
    ArrayList<Forum> forums;
    LinearLayoutManager layoutManager;
    ForumsListener forumsListener;
    ForumAdapter forumAdapter;

    public ForumsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    public static ForumsFragment newInstance(String userId, String userName) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userId);
        args.putString(ARG_PARAM2, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutButton = view.findViewById(R.id.logoutButton);
        newForumButton = view.findViewById(R.id.newForumButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        getActivity().setTitle(getString(R.string.forumsTitle));
        forums = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        getData();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                forumsListener.gotoLogin();
            }
        });
        newForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forumsListener.gotoCreateForum(userId, userName);
            }
        });
    }

    public void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(getString(R.string.forumsLabel))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        forums.clear();
                        for(QueryDocumentSnapshot document: value){
                            Forum forum = new Forum(document.getId(), document.get("forumTitle").toString(), document.get("name").toString(), document.get("userId").toString(), document.get("forumDescription").toString(), document.get("time").toString());
                            forums.add(forum);
                            if(document.contains("likeUsers")){
                                forum.setLikeUsers((List<String>) document.get("likeUsers"));
                            }
                        }
                        forumAdapter = new ForumAdapter(forums);
                        recyclerView.setAdapter(forumAdapter);
                    }
                });
    }

    class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder>{

        ArrayList<Forum> forums;

        ForumAdapter(ArrayList<Forum> forums){
            this.forums = forums;
        }

        @NonNull
        @Override
        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(getContext()).inflate(R.layout.forum_layout, parent, false);
            return new ForumViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
            Forum forum = forums.get(position);
            holder.forumTitle.setText(forum.getForumTitle());
            holder.forumCreator.setText(forum.getName());
            holder.forumDescription.setText(forum.getForumDescription());
            holder.dateTimeTextView.setText(forum.getLikeUsers().size()+"Likes | "+forum.getTime());
            holder.forum = forum;
            if(forum.getUserId().equals(userId)){
                holder.binImageView.setVisibility(View.VISIBLE);
            }else{
                holder.binImageView.setVisibility(View.GONE);
            }
            if(forum.likeUsers.contains(userId))
                holder.imageView.setImageResource(R.drawable.like_favorite);
            else{
                holder.imageView.setImageResource(R.drawable.like_not_favorite);
            }
        }

        @Override
        public int getItemCount() {
            return this.forums.size();
        }

        class ForumViewHolder extends RecyclerView.ViewHolder {
            TextView forumTitle, forumCreator, forumDescription, dateTimeTextView;
            ImageView binImageView, imageView;
            Forum forum;
            ConstraintLayout forumLayout;
            public ForumViewHolder(@NonNull View itemView) {
                super(itemView);
                forumTitle = itemView.findViewById(R.id.forumTitle);
                forumDescription = itemView.findViewById(R.id.forumDescription);
                forumCreator = itemView.findViewById(R.id.forumCreator);
                dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
                binImageView = itemView.findViewById(R.id.binImageView);
                imageView = itemView.findViewById(R.id.imageView);
                forumLayout = itemView.findViewById(R.id.forumLayout);
                forumLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forumsListener.gotoForumDetails(forum.getId(), userName);
                    }
                });
                binImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore store = FirebaseFirestore.getInstance();
                        store.collection("forums").document(forum.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        forumAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore store = FirebaseFirestore.getInstance();
                        if(forum.getLikeUsers().contains(userId)){
                            forum.likeUsers.remove(userId);
                            store.collection("forums").document(forum.getId())
                                    .update("likeUsers", forum.getLikeUsers())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            imageView.setImageResource(R.drawable.like_not_favorite);
                                        }
                                    });
                        }else{
                            forum.likeUsers.add(userId);
                            store.collection("forums").document(forum.getId())
                                    .update("likeUsers", forum.getLikeUsers())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            imageView.setImageResource(R.drawable.like_favorite);
                                        }
                                    });
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        forumsListener = (ForumsListener) context;
    }

    interface ForumsListener{
        void gotoCreateForum(String userId, String userName);
        void gotoForumDetails(String forumId, String userName);
        void gotoLogin();
    }
}