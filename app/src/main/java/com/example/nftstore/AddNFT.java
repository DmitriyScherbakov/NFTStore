package com.example.nftstore;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nftstore.DB.DBQuery;
import com.example.nftstore.DB.MyCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

public class AddNFT extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton load_nft, test_button;
    private EditText imageUrl, nft_name, coll_name, price;
    private ImageView image1, image2;
    private CardView cardView;
    private TextView title;
    String imageUrlStr, nft_nameStr, coll_nameStr, priceStr, ownerStr;
    boolean onSaleStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nft);

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        load_nft = findViewById(R.id.load_nft);
        test_button = findViewById(R.id.test_button);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        title = findViewById(R.id.title);
        cardView = findViewById(R.id.card_view);
        imageUrl = findViewById(R.id.urlField);
        nft_name = findViewById(R.id.nameField);
        coll_name = findViewById(R.id.collNameField);
        price = findViewById(R.id.priceField);

        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(imageUrl.getText().toString()).into(image2);
                image2.setVisibility(View.VISIBLE);
                image1.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
            }
        });

        load_nft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageUrlStr = imageUrl.getText().toString().trim();
                nft_nameStr = nft_name.getText().toString().trim();
                coll_nameStr = coll_name.getText().toString().trim();
                priceStr = price.getText().toString().trim();
                ownerStr = DBQuery.myProfile.getNickname();
                onSaleStr = false;

                if (TextUtils.isEmpty(imageUrl.getText().toString().trim())) {
                    Toast.makeText(AddNFT.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(nft_name.getText().toString().trim())) {
                    Toast.makeText(AddNFT.this, "Enter name of NFT", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < DBQuery.g_nftList.size(); i++) {
                    if (Objects.equals(DBQuery.g_nftList.get(i).getName(), nft_name.getText().toString())) {
                        Toast.makeText(AddNFT.this, "This name of NFT already used, try enter another name",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(coll_name.getText().toString().trim())) {
                    Toast.makeText(AddNFT.this, "Enter name of collection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(price.getText().toString().trim())) {
                    Toast.makeText(AddNFT.this, "Enter price", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AddNFT.this, "Success", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < DBQuery.g_nftList.size(); i++) {
                    if (Objects.equals(DBQuery.g_nftList.get(i).getCollection().trim().toUpperCase(), coll_name.getText().toString().trim().toUpperCase()))
                    {
                        DBQuery.createNFTData(nft_nameStr, imageUrlStr, coll_nameStr, ownerStr, onSaleStr, priceStr, new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                DBQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(AddNFT.this, MainActivity.class);
                                        startActivity(intent);
                                        AddNFT.this.finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        makeText(AddNFT.this, "SMTH went wrong, try again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onFailure() {
                                makeText(AddNFT.this, "SMTH went wrong, try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        DBQuery.createNFTData(nft_nameStr, imageUrlStr, coll_nameStr, ownerStr, onSaleStr, priceStr, new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                DBQuery.createCollectionData(coll_nameStr, priceStr, imageUrlStr, ownerStr, priceStr, "1", new MyCompleteListener(){
                                    @Override
                                    public void onSuccess() {
                                        DBQuery.loadData(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                Intent intent = new Intent(AddNFT.this, MainActivity.class);
                                                startActivity(intent);
                                                AddNFT.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                makeText(AddNFT.this, "SMTH went wrong, try again!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure() {
                                        makeText(AddNFT.this, "SMTH went wrong, try again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure() {
                                makeText(AddNFT.this, "SMTH went wrong, try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                }
            }
        });
    }
}