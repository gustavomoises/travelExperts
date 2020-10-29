package com.example.travelexperts.BusinessLayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelexperts.R;

import java.util.List;

/*
 * Author: Suvanjan Shrestha
 * Date: 02/10/2020
 * TravelExperts Android App
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    List<RecyclerViewData> dataList;
    LayoutInflater inflater;

    public ProductAdapter(Context context, List<RecyclerViewData> dataList){
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    //View holder for the Products recycler view
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
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

        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ProductViewHolder(view);
    }


    //Populate the Recyclerview list
    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        RecyclerViewData recyclerViewData = dataList.get(position);

        holder.tvProductName.setText(recyclerViewData.getProdName());
        holder.tvSupplierName.setText(recyclerViewData.getSupName());
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
