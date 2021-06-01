package com.example.opsctask2app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

public class wbf_stock_adapter extends RecyclerView.Adapter<wbf_stock_adapter.MyViewHolder>
{
    //Setting Variables
    String ItemDesc[],ItemName[],Price[],DateCreated[],Amount[],CatID[];
    String ImgURL[];
    Context context;

    //Pulling data from item view class
    public wbf_stock_adapter(Context ct, String dataItemDesc[], String dataItemName[], String dataImg[], String dataPrice[], String dataDateCreated[],
                             String dataAmount[], String dataCatID[])
    {
        context = ct;
        ItemName = dataItemDesc;
        ItemDesc = dataItemName;
        ImgURL = dataImg;
        Price = dataPrice;
        DateCreated = dataDateCreated;
        Amount = dataAmount;
        CatID = dataCatID;
    }

    //Layout holder for each item row
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View ola = inflater.inflate(R.layout.item_row, parent, false);
        return  new MyViewHolder(ola);
    }

    @Override
    public void onBindViewHolder(@NonNull  wbf_stock_adapter.MyViewHolder holder, int position)
    {
        holder.sProdName.setText(ItemDesc[position]);
        holder.sProdDesc.setText(ItemName[position]);
        Picasso.with(context).load(ImgURL[position]).into(holder.ProdImage);

        //Getting data for each item row
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, wbf_item_view.class);
                intent.putExtra("ItemDesc",ItemDesc[position]);
                intent.putExtra("ItemName",ItemName[position]);
                intent.putExtra("images",ImgURL[position]);
                intent.putExtra("Price",Price[position]);
                intent.putExtra("DateFound",DateCreated[position]);
                intent.putExtra("Amount",Amount[position]);
                intent.putExtra("CatID",CatID[position]);
                context.startActivity(intent);
            }
        });
    }

    //Getting item count for all item(row)
    @Override
    public int getItemCount() {
        return ItemDesc.length;
    }

    //Setting data to UI for each item row
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView sProdName, sProdDesc;
        ImageView ProdImage;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            sProdName = itemView.findViewById(R.id.ProductName);
            sProdDesc = itemView.findViewById(R.id.ProductDesc);
            ProdImage = itemView.findViewById(R.id.ProductImg);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}


