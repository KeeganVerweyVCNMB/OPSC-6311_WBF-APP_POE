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

public class wbf_stock_adapter_m extends RecyclerView.Adapter<wbf_stock_adapter_m.MyViewHolder>
{
    //Setting Variables
    String ItemDescM[],ItemNameM[],PriceM[],DateCreatedM[],AmountM[],CatIDM[];
    String ImgURLM[];
    Context contextM;

    //Pulling data from item view class
    public wbf_stock_adapter_m(Context ctM, String dataItemDescM[], String dataItemNameM[], String dataImgM[], String dataPriceM[], String dataDateCreatedM[],
                             String dataAmountM[], String dataCatIDM[])
    {
        contextM = ctM;
        ItemNameM = dataItemDescM;
        ItemDescM = dataItemNameM;
        ImgURLM = dataImgM;
        PriceM = dataPriceM;
        DateCreatedM = dataDateCreatedM;
        AmountM = dataAmountM;
        CatIDM = dataCatIDM;
    }

    //Layout holder for each item row
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contextM);
        View ola = inflater.inflate(R.layout.item_row_m, parent, false);
        return  new MyViewHolder(ola);
    }

    @Override
    public void onBindViewHolder(@NonNull  wbf_stock_adapter_m.MyViewHolder holder, int position)
    {
        holder.sProdNameM.setText(ItemDescM[position]);
        holder.sProdDescM.setText(ItemNameM[position]);
        Picasso.with(contextM).load(ImgURLM[position]).into(holder.ProdImageM);

        //Getting data for each item row
        holder.mainLayoutM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(contextM, wbf_item_view.class);
                intent.putExtra("ItemDesc",ItemDescM[position]);
                intent.putExtra("ItemName",ItemNameM[position]);
                intent.putExtra("images",ImgURLM[position]);
                intent.putExtra("Price",PriceM[position]);
                intent.putExtra("DateFound",DateCreatedM[position]);
                intent.putExtra("Amount",AmountM[position]);
                intent.putExtra("CatID",CatIDM[position]);
                contextM.startActivity(intent);
            }
        });
    }

    //Getting item count for all item(row)
    @Override
    public int getItemCount() {
        return ItemDescM.length;
    }

    //Setting data to UI for each item row
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView sProdNameM, sProdDescM;
        ImageView ProdImageM;
        ConstraintLayout mainLayoutM;

        public MyViewHolder(@NonNull View itemViewM)
        {
            super(itemViewM);
            sProdNameM = itemViewM.findViewById(R.id.ProductNameM);
            sProdDescM = itemViewM.findViewById(R.id.ProductDescM);
            ProdImageM = itemViewM.findViewById(R.id.ProductImgM);
            mainLayoutM = itemViewM.findViewById(R.id.mainLayoutM);
        }
    }
}


