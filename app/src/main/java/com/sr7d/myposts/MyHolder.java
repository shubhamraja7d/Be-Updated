package com.sr7d.myposts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ShubhamRaja on 05-Nov-17.
 */

public class MyHolder extends RecyclerView.ViewHolder{

    TextView textTitle,textDate,textDescription;
    ImageView imageIcon,imageImage;

    public MyHolder(View itemView) {
        super(itemView);

        textTitle = (TextView) itemView.findViewById(R.id.textTitle);
        textDate = (TextView) itemView.findViewById(R.id.textDate);
        textDescription = (TextView) itemView.findViewById(R.id.textDescription);

        imageIcon = (ImageView) itemView.findViewById(R.id.imageIcon);
        imageImage = (ImageView) itemView.findViewById(R.id.imageImage);

    }

}
