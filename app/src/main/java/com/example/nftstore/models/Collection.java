package com.example.nftstore.models;

public class Collection {

    String  name, all_time_volume, collection_image, col_id, creator_name,  floor_price, amount_of_nft;

    public Collection(String name, String all_time_volume, String collection_image, String col_id, String creator_name, String floor_price, String amount_of_nft) {
        this.name = name;
        this.all_time_volume = all_time_volume;
        this.collection_image = collection_image;
        this.col_id = col_id;
        this.creator_name = creator_name;
        this.floor_price = floor_price;
        this.amount_of_nft = amount_of_nft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAll_time_volume() {
        return all_time_volume;
    }

    public void setAll_time_volume(String all_time_volume) {
        this.all_time_volume = all_time_volume;
    }

    public String getCollection_image() {
        return collection_image;
    }

    public void setCollection_image(String collection_image) {
        this.collection_image = collection_image;
    }

    public String getCol_id() {
        return col_id;
    }

    public void setCol_id(String col_id) {
        this.col_id = col_id;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getFloor_price() {
        return floor_price;
    }

    public void setFloor_price(String floor_price) {
        this.floor_price = floor_price;
    }

    public String getAmount_of_nft() {
        return amount_of_nft;
    }

    public void setAmount_of_nft(String amount_of_nft) {
        this.amount_of_nft = amount_of_nft;
    }
}
