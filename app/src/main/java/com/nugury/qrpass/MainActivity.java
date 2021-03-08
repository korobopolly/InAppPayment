package com.nugury.qrpass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.nugury.qrpass.Adapter.ProductAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    
    BillingClient billingClient;

    Button loadProduct;
    RecyclerView recyclerproduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setBillingClient();

        //view
        loadProduct = (Button)findViewById(R.id.btn_load_product);
        recyclerproduct = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerproduct.setHasFixedSize(true);
        recyclerproduct.setLayoutManager(new LinearLayoutManager(this));

        //event
        loadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(billingClient.isReady()){
                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("paycheck1","paycheck2"))
                            .setType(BillingClient.SkuType.INAPP)
                        .build();

                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                                loadProductToRecyclerView(list);
                            else
                                Toast.makeText(MainActivity.this, "Cannot query product", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Toast.makeText(MainActivity.this, "Billing client not ready", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProductToRecyclerView(List<SkuDetails> list) {
        ProductAdapter adapter=new ProductAdapter(this,list,billingClient);
        recyclerproduct.setAdapter(adapter);
    }

    private void setBillingClient() {
        billingClient= BillingClient.newBuilder(this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, ""+billingResult, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(MainActivity.this, "You are disconnect from Billing", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        Toast.makeText(this, "Purchases item:"+list.size(), Toast.LENGTH_SHORT).show();

    }
}