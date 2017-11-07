package com.sr7d.myposts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ShubhamRaja on 05-Nov-17.
 */

public class FirePage {

    public static int TOTAL_NUM_ITEMS;
    public static final int ITEMS_PER_PAGE = 5;
    public static int ITEMS_REMAINING;
    public static  int LAST_PAGE;

    Context c;
    //    String DB_URL;
    RecyclerView rv;

    FirebaseDatabase database;
    DatabaseReference myRef,myRef2;
    int page;

    ArrayList<Item> items = new ArrayList<>();
    MyAdapter adapter;


    //    public FireBaseClient(Context c, String DB_URL, RecyclerView rv) {
    public FirePage(Context c, RecyclerView rv, int page) {
        this.c =  c;
//        this.DB_URL = DB_URL;
        this.rv = rv;
        this.page = page;

        database = FirebaseDatabase.getInstance();
        myRef = database.getInstance().getReference();


    }
    //    //save
//    public void saveOnline(String name, String url){
//        Item i = new Item();
//        i.setName(name);
//        i.setUrl(url);
//        myRef.child("items").push().setValue(i);
//
//    }
    //retrieve
    public void refreshData(){

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                TOTAL_NUM_ITEMS = ((int) dataSnapshot.getChildrenCount());
                ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
                LAST_PAGE = TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;

                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TOTAL_NUM_ITEMS = ((int) dataSnapshot.getChildrenCount());
                ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
                LAST_PAGE = TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  void getUpdates(DataSnapshot dataSnapshot)
    {

//        int startItem = page*ITEMS_PER_PAGE+1;
        int startItem = TOTAL_NUM_ITEMS - page*ITEMS_PER_PAGE;
        int limit = TOTAL_NUM_ITEMS - (page+1)*ITEMS_PER_PAGE;
        int NUMOFDATA = ITEMS_PER_PAGE;

        items.clear();
        if (page == LAST_PAGE && ITEMS_REMAINING > 0) {
//            for (int in = startItem; in < startItem + ITEMS_REMAINING; in++) {
            for (int in = startItem; in > 0; in--) {
                Item i = dataSnapshot.child("Post" + in).getValue(Item.class);
                items.add(i);
            }
        } else {
//            for (int in = startItem; in < startItem + NUMOFDATA; in++) {
            for (int in = startItem; in > limit; in--) {
                Item i = dataSnapshot.child("Post" + in).getValue(Item.class);
                items.add(i);
            }
        }

        if (items.size() > 0) {
            adapter = new MyAdapter(c, items);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(c, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

}
