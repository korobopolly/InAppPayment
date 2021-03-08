package com.nugury.qrpass.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.nugury.qrpass.Interface.ProductClickListener;
import com.nugury.qrpass.MainActivity;
import com.nugury.qrpass.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {


    MainActivity mainActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;

    public ProductAdapter(MainActivity mainActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.mainActivity = mainActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainActivity.getBaseContext())
                .inflate(R.layout.layout_product_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_product.setText(skuDetailsList.get(position).getTitle());

        //product click
        holder.setProductClickListener(new ProductClickListener() {
            @Override
            public void onProductClickListener(View view, int position) {
                //launch billing flow
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(position))
                        .build();
                billingClient.launchBillingFlow(mainActivity,billingFlowParams);
            }
        });

    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_product;

        ProductClickListener ProductClickListener;

        public void setProductClickListener(ProductClickListener iProductClickListener) {
            this.ProductClickListener = iProductClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product=(TextView)itemView.findViewById((R.id.txt_product_name));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ProductClickListener.onProductClickListener(v,getAdapterPosition());
        }
    }
}
