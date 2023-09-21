package com.example.nftstore.DB;

import static android.content.ContentValues.TAG;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nftstore.models.Collection;
import com.example.nftstore.models.MyProfile;
import com.example.nftstore.models.NFT;
import com.example.nftstore.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBQuery {

    public static FirebaseFirestore g_firestore;

    public static MyProfile myProfile = new MyProfile("NA", null, null, null, null, null);

    public static List<NFT> g_nftList = new ArrayList<>();

    public static List<Users> g_usersList = new ArrayList<>();

    public static List<Collection> g_collectionList = new ArrayList<>();

    public static int g_selected_col_index = 0;
    public static int g_selected_owner_index = 0;

    public static int g_usersCount = 0;
    public static final int NOT_VISITED = 0;


    public static String g_selected_URL = "";
    public static String g_selected_URL_collection_image = "";


    public static void createUserData(String email, String nickname, MyCompleteListener completeListener) {
        Map<String, Object> userData = new ArrayMap<>();

        userData.put("EMAIL_ID", email);
        userData.put("NICKNAME", nickname);
        userData.put("AVATAR", null);
        userData.put("BALANCE", "1000");

        DocumentReference userDoc = g_firestore
                .collection("USERS")
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getUid()
                );
        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void getUserData(MyCompleteListener completeListener) {
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setNickname(documentSnapshot.getString("NICKNAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                        myProfile.setAvatar_url(documentSnapshot.getString("AVATAR_URL"));
                        myProfile.setBalance(documentSnapshot.getString("BALANCE"));

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void getAllUsersData(MyCompleteListener completeListener) {
        g_usersList.clear();
        g_firestore.collection("USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_usersList.add(new Users(
                                    doc.getString("NICKNAME"),
                                    doc.getString("EMAIL_ID"),
                                    doc.getString("AVATAR_URL"),
                                    doc.getString("BALANCE")
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void createNFTData(String name, String URL, String collection, String owner, boolean onSale, String price, MyCompleteListener completeListener) {
        Map<String, Object> nftData = new ArrayMap<>();

        nftData.put("name", name);
        nftData.put("URL", URL);
        nftData.put("collection", collection);
        nftData.put("owner", owner);
        nftData.put("onSale", onSale);
        nftData.put("price", price);

        DocumentReference nftDoc = g_firestore
                .collection("NFTs")
                .document();
        WriteBatch batch = g_firestore.batch();

        batch.set(nftDoc, nftData);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void createCollectionData(String name, String all_time_volume, String collection_image,
                                            String creator_name, String floor_price, String amount_of_nft, MyCompleteListener completeListener) {
        Map<String, Object> collectionData = new ArrayMap<>();

        collectionData.put("ALL_TIME_VOLUME", all_time_volume);
        collectionData.put("AMOUNT_OF_NFT", amount_of_nft);
        collectionData.put("COLLECTION_IMAGE", collection_image);
        collectionData.put("CREATOR", creator_name);
        collectionData.put("FLOOR_PRICE", floor_price);
        collectionData.put("NAME", name);

        DocumentReference collectionDoc = g_firestore
                .collection("COLLECTIONS")
                .document();
        WriteBatch batch = g_firestore.batch();

        batch.set(collectionDoc, collectionData);


        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadData(MyCompleteListener completeListener) {
        loadNFT(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getAllUsersData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                loadCollections(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        getUsersCount(completeListener);
                                    }

                                    @Override
                                    public void onFailure() {
                                        completeListener.onFailure();
                                    }
                                });
                            }

                            @Override
                            public void onFailure() {
                                completeListener.onFailure();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }

    public static void getUsersCount(MyCompleteListener completeListener) {
        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_usersCount = documentSnapshot.getLong("COUNT").intValue();
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadNFT(MyCompleteListener completeListener) {
        g_nftList.clear();
        g_firestore.collection("NFTs")
//                .whereEqualTo("id", FirebaseFirestore.getInstance())
//                .whereEqualTo("owner", g_usersList.get(g_selected_owner_index).getNickname())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_nftList.add(new NFT(
                                    doc.getString("URL"),
                                    doc.getString("collection"),
                                    doc.getString("name"),
                                    doc.getString("owner"),
                                    doc.getString("price"),
                                    Boolean.TRUE.equals(doc.getBoolean("onSale"))

                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });
    }

    public static void loadCollections(MyCompleteListener completeListener) {
        g_collectionList.clear();
        g_firestore.collection("COLLECTIONS")
//                .whereEqualTo("collection", g_collectionList.get(g_selected_col_index).getDocID())
//                .whereEqualTo("owner", g_usersList.get(g_selected_owner_index).getNickname())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_collectionList.add(new Collection(
                                    doc.getString("NAME"),
                                    doc.getString("ALL_TIME_VOLUME"),
                                    doc.getString("COLLECTION_IMAGE"),
                                    doc.getString("COL_ID"),
                                    doc.getString("CREATOR"),
                                    doc.getString("FLOOR_PRICE"),
                                    doc.getString("AMOUNT_OF_NFT")
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });
    }

    public static void buyingNFT(String URL, String collection, String name, boolean onSale, String owner, String price, MyCompleteListener completeListener) {
        WriteBatch batch = g_firestore.batch();
        Map<String, Object> data = new ArrayMap<>();

        data.put("owner", owner);
        data.put("onSale", onSale);
        data.put("name", name);
        data.put("price", price);
        data.put("URL", URL);
        data.put("collection", collection);

        g_firestore.collection("NFTs")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DocumentReference docRef = g_firestore.collection("NFTs").document(document.getId());
                                docRef.set(data);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                completeListener.onSuccess();
                            }

                            @Override
                            public void onFailure() {
                                System.out.println("344");
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void sellingNFT(String URL, String collection, String name, boolean onSale, String owner, String price, MyCompleteListener completeListener) {
        WriteBatch batch = g_firestore.batch();
        Map<String, Object> data = new ArrayMap<>();

        data.put("owner", owner);
        data.put("onSale", onSale);
        data.put("name", name);
        data.put("price", price);
        data.put("URL", URL);
        data.put("collection", collection);

        g_firestore.collection("NFTs")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DocumentReference docRef = g_firestore.collection("NFTs").document(document.getId());
                                docRef.set(data);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                completeListener.onSuccess();
                            }

                            @Override
                            public void onFailure() {
                                System.out.println("344");
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }
}