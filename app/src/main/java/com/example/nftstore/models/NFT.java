package com.example.nftstore.models;

public class NFT {

    String URL, collection, name, owner, price;
    boolean onSale;

    public NFT(String URL, String collection, String name, String owner, String price, boolean onSale) {
        this.URL = URL;
        this.collection = collection;
        this.name = name;
        this.owner = owner;
        this.price = price;
        this.onSale = onSale;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }
}
