package com.example.travelexperts.BusinessLayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelexperts.ApplicationLayer.AddProductActivity;
import com.example.travelexperts.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Suvanjan Shrestha
 * Date: 02/10/2020
 * TravelExperts Android App
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private Cursor cursor;
    List<RecyclerViewData> recyclerViewDataList = new ArrayList<>();
    LayoutInflater inflater;
    Listener listener;


    public ProductAdapter(Context context, List<RecyclerViewData> productList1){
        this.context = context;
        this.recyclerViewDataList = productList1;
        this.listener = (Listener) context;
        inflater = LayoutInflater.from(context);
    }

    //View holder for the Products recycler view
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName, tvSupplierName;
        public ImageButton btnEditProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tv_productName);
            tvSupplierName = itemView.findViewById(R.id.tv_supplierName);
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);

        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_item,parent,false);
        return new ProductViewHolder(view);
    }


    //Populate the list
    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        holder.tvProductName.setText(recyclerViewDataList.get(position).ProdName);
        holder.tvSupplierName.setText(recyclerViewDataList.get(position).SupName);

        holder.btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddProductActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return recyclerViewDataList.size();
    }


}
