package com.example.android.ep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
 RecyclerView mRecyclerView;
 FirebaseDatabase mFirebaseDatabase;
 DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mosquito Diseases");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder>  firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        mRef) {

                    @Override
                    protected void populateViewHolder(ViewHolder holder, Model model, int position) {
                        holder.setDetails(getApplication(), model.getTitle(), model.getDescription(), model.getImage());
                    }


                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mDescTv = view.findViewById(R.id.rDescriptionTv);
                                ImageView mImageView = view.findViewById(R.id.rImageView);

                                String mTitle = mTitleTv.getText().toString();
                                String mDesc = mDescTv.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

                                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description", mDesc);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}
