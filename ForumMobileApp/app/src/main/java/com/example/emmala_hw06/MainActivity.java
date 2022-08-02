/* Assignment: HW06
   File Name: Emmala_HW06
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package com.example.emmala_hw06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, RegisterFragment.RegisterListener, ForumsFragment.ForumsListener, CreateForumFragment.CreateForumListener {

    String createForumTag = "createForum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerLayout, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new RegisterFragment())
                .commit();
    }

    @Override
    public void gotoForums(String userId, String userName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, ForumsFragment.newInstance(userId, userName))
                .commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoCreateForum(String userId, String userName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, CreateForumFragment.newInstance(userId, userName))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoForumDetails(String forumId, String userName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerLayout, ForumFragment.newInstance(forumId, userName))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void popBackToForumList() {
        getSupportFragmentManager().popBackStack();
    }
}