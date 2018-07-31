package com.mr_abdali.monitor;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mr_abdali.monitor.chat.MainChat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildActivity extends AppCompatActivity {
    public static final String messag1 = "ParentId";
    public static final String message2 = "childId";

    String ParentId;
    // variable
    private RecyclerView mchildList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    //
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_btn) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // back action  "<-"....
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //---------------------- Recycler view database ------------------------//
        mchildList = (RecyclerView) findViewById(R.id.childlist);
        mchildList.setHasFixedSize(true);
        mchildList.setLayoutManager(new LinearLayoutManager(this));

        ParentId = getIntent().getStringExtra("ParentId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("childlist").child(ParentId);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(ChildActivity.this, "No Child Founded!", Toast.LENGTH_LONG).show();
                }
            }
        };

        // ---------------------- end ----------------------------//




        //TODO floating Action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChildActivity.this, ChildAddActivity.class);
                intent.putExtra(LoginActivity.EXTRA_MESSAGE, ParentId);
                startActivity(intent);
            }
        });



    } // main method end


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item1_id) {
            // your cade is here to implement
            Toast.makeText(getApplicationContext(), "item 1 is clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.item2_id) {
            // your cade is here to implement
            Toast.makeText(getApplicationContext(), "item 2 clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.item3_id) {
            // your cade is here to implement
            Toast.makeText(getApplicationContext(), "item 3 clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.search_id) {
            // your cade is here to implement
            Toast.makeText(getApplicationContext(), "Search icon", Toast.LENGTH_SHORT).show();
        }
        // for action backkkkk
        else if (id == android.R.id.home) {
            // your <code>here</code>
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // onstart....
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Childlist,ChildViewHolder> FBRA = new FirebaseRecyclerAdapter<Childlist, ChildViewHolder>(
                Childlist.class,
                R.layout.single_name,
                ChildViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ChildViewHolder viewHolder, Childlist model, int position) {
                final String child_key = getRef(position).getKey().toString();
                viewHolder.setName(model.getchildName());
                viewHolder.child_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChildActivity.this, com.mr_abdali.monitor.TabActivity.class);
                        startActivity(intent);
                    }
                });
                viewHolder.mchat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ChildActivity.this, MainChat.class);
                        intent.putExtra(messag1, ParentId);
                        intent.putExtra(message2, child_key);
                        startActivity(intent);

                    }
                });
                viewHolder.mdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteChildName(child_key);
                    }
                });
            }
        };
        mchildList.setAdapter(FBRA);

    }

    //TODO Child Item Deletion area
    private void deleteChildName(String child_key) {
        DatabaseReference delete = mDatabase.child(child_key);
        delete.removeValue();
    }

    // end os start method ..
    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView child_name;
        Button mchat, mdelete;

        public ChildViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mchat = (Button) mView.findViewById(R.id.chat);
            mdelete = (Button) mView.findViewById(R.id.btn_delete);
        }

        public void setName(String child) {
            child_name = (TextView) mView.findViewById(R.id.ch);
            child_name.setText(child);
        }
    }

}