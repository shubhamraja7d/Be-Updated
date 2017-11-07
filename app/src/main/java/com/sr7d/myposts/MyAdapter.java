package com.sr7d.myposts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

//public class MyAdapter extends ArrayAdapter<Item> {
//
//    ArrayList<Item> animalList = new ArrayList();
//
//    public MyAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
//        super(context, textViewResourceId, objects);
//        animalList = objects;
//    }
//
//    @Override
//    public int getCount() {
//        return super.getCount();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        v = inflater.inflate(R.layout.grid_view_items, null);
//        TextView textView = (TextView) v.findViewById(R.id.textView);
//        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
//        textView.setText(animalList.get(position).getAnimalName());
//        imageView.setImageResource(animalList.get(position).getAnimalImage());
//        return v;
//
//    }
//
//}

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Item> items;

    public MyAdapter(Context c, ArrayList<Item> items) {
        this.c = c;
        this.items = items;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_items,parent,false);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.textTitle.setText(items.get(position).getTitle());
        holder.textDate.setText(items.get(position).getDate());
        holder.textDescription.setText(items.get(position).getDescription());

        PicassoClient.downloadImage(c,items.get(position).getIcon(),holder.imageIcon);
        PicassoClient.downloadImage(c,items.get(position).getImage(),holder.imageImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
