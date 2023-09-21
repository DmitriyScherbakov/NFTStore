package com.example.nftstore;

import static android.widget.Toast.makeText;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.DB.MyCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

public class NftViewPage extends AppCompatActivity {

    private String URL, avatar_url;

    private ImageView avatar, main_image;
    private TextView coll_name, nft_name, owner_name, price, sell_title, qstn_title;

    private ImageButton buy_btn, sell_btn, conf_buy_btn, conf_sell_btn;

    private EditText confirm_field_buy, confirm_field_sell, enter_price_field;

    private int priceInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nft_view_page);

        buy_btn = findViewById(R.id.buy_button);
        sell_btn = findViewById(R.id.sell_button);
        price = findViewById(R.id.price);
        sell_title = findViewById(R.id.sell_title);

        avatar = findViewById(R.id.avatar_image);
        main_image = findViewById(R.id.image);
        coll_name = findViewById(R.id.collection_name);
        nft_name = findViewById(R.id.nft_name);
        owner_name = findViewById(R.id.owner_name);
        price = findViewById(R.id.price);

        URL = DBQuery.g_selected_URL;
        Picasso.get().load(URL).into(main_image);
        coll_name.setText(getIntent().getStringExtra("coll_name"));
        nft_name.setText(getIntent().getStringExtra("nft_name"));
        owner_name.setText(getIntent().getStringExtra("owner_name"));
        price.setText("Buy now for " + getIntent().getStringExtra("price") + "$");

        priceInt = Integer.parseInt(getIntent().getStringExtra("price"));

        for (int i = 0; i<DBQuery.g_usersList.size(); i++){
            if (Objects.equals(DBQuery.g_usersList.get(i).getNickname(), getIntent().getStringExtra("owner_name"))){
                avatar_url = DBQuery.g_usersList.get(i).getAvatar_url();
                Picasso.get().load(avatar_url).into(avatar);
            }
        }

        for (int i = 0; i<DBQuery.g_nftList.size(); i++){
            if (Objects.equals(DBQuery.g_nftList.get(i).getName().trim().toUpperCase(),
                    nft_name.getText().toString().trim().toUpperCase())){
                if (DBQuery.g_nftList.get(i).isOnSale()){
                    buy_btn.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    sell_btn.setVisibility(View.GONE);
                    sell_title.setVisibility(View.GONE);
                }
                else{
                    buy_btn.setVisibility(View.GONE);
                    price.setVisibility(View.GONE);
                    sell_btn.setVisibility(View.VISIBLE);
                    sell_title.setVisibility(View.VISIBLE);
                }
            }
        }

        sell_btn.setOnClickListener(view -> openSellWindow());
        buy_btn.setOnClickListener(view -> openBuyWindow());
    }

    private void openBuyWindow() {
        Dialog dialog;
        dialog = new Dialog(NftViewPage.this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View buy_window = inflater.inflate(R.layout.buy_window, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(buy_window);
        dialog.setCancelable(true);

//        confirm_field = buy_window.findViewById(R.id.confirmField);
        conf_buy_btn = buy_window.findViewById(R.id.confirm_buy_button);
        qstn_title = buy_window.findViewById(R.id.question_title);

        qstn_title.setText("Are you sure you want to buy " + nft_name.getText().toString() + "?");

        conf_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nftNameStr = nft_name.getText().toString();
                String priceStr = getIntent().getStringExtra("price");
                String URLStr = DBQuery.g_selected_URL;
                String collNameStr = coll_name.getText().toString();
                String ownerNameStr = DBQuery.myProfile.getNickname();

                if (priceInt > Integer.parseInt(DBQuery.myProfile.getBalance())){
                    Toast.makeText(NftViewPage.this, "You don't have enough funds!",Toast.LENGTH_SHORT).show();
                    return;
                }

                DBQuery.buyingNFT(URLStr, collNameStr, nftNameStr, false, ownerNameStr, priceStr, new MyCompleteListener() {
                    @Override
                    public void onSuccess(){
                        DBQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Intent intent = new Intent(NftViewPage.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            @Override
                            public void onFailure() {
                                dialog.dismiss();
                                makeText(NftViewPage.this, "SMTH went wrong, try again!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onFailure() {
                        dialog.dismiss();
                        makeText(NftViewPage.this, "SMTH went wrong, try again!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void openSellWindow() {
        Dialog dialog;
        dialog = new Dialog(NftViewPage.this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View sell_window = inflater.inflate(R.layout.sell_window, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(sell_window);
        dialog.setCancelable(true);

        confirm_field_sell = sell_window.findViewById(R.id.confirmField);
        enter_price_field = sell_window.findViewById(R.id.enterPriceField);
        conf_sell_btn = sell_window.findViewById(R.id.confirm_sell_button);
        qstn_title = sell_window.findViewById(R.id.question_title);

        qstn_title.setText("Are you sure you want to sell " + nft_name.getText().toString() + "?");

        conf_sell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nftNameStr = nft_name.getText().toString();
                String URLStr = DBQuery.g_selected_URL;
                String collNameStr = coll_name.getText().toString();
                String ownerNameStr = DBQuery.myProfile.getNickname();
                String confirm_field_sellStr = confirm_field_sell.getText().toString();
                String price_fieldStr = enter_price_field.getText().toString();
                int price_fieldInt = Integer.parseInt(price_fieldStr);

                if (price_fieldInt < 1 && price_fieldStr.length() == 0){
                    Toast.makeText(NftViewPage.this, "Enter price of the NFT to confirm the sale", Toast.LENGTH_SHORT).show();
                }

                if (!confirm_field_sellStr.trim().toUpperCase().equals(nftNameStr.trim().toUpperCase())){
                    Toast.makeText(NftViewPage.this, "Enter the name of the NFT to confirm the sale", Toast.LENGTH_SHORT).show();
                    return;
                }

                int balanceInt = Integer.parseInt(DBQuery.myProfile.getBalance());
                balanceInt = balanceInt + price_fieldInt;
                String balanceStr = String.valueOf(balanceInt);
                DBQuery.myProfile.setBalance(balanceStr);

                DBQuery.sellingNFT(URLStr, collNameStr, nftNameStr, true, ownerNameStr, price_fieldStr, new MyCompleteListener() {
                    @Override
                    public void onSuccess(){
                        DBQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Intent intent = new Intent(NftViewPage.this, Library.class);
                                startActivity(intent);
                                finish();
                            }
                            @Override
                            public void onFailure() {
                                dialog.dismiss();
                                Toast.makeText(NftViewPage.this, "SMTH went wrong, try again!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onFailure() {
                        dialog.dismiss();
                        makeText(NftViewPage.this, "SMTH went wrong, try again!",
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        dialog.show();
    }




//     DBQuery.buyingNFT(URLStr, collNameStr, nftNameStr, false, ownerNameStr, priceStr, new OnCompleteListener<QuerySnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<QuerySnapshot> task){
//            if (task.isSuccessful()){
//                DBQuery.loadData(new MyCompleteListener() {
//                    @Override
//                    public void onSuccess() {
//                        dialog.dismiss();
//                        Intent intent = new Intent(NftViewPage.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                    @Override
//                    public void onFailure() {
//                        dialog.dismiss();
//                        makeText(NftViewPage.this, "SMTH went wrong, try again!",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            else{
//                dialog.dismiss();
//                Toast.makeText(NftViewPage.this, "Error",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    });
}