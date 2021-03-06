package com.apps.akkaber.model;

import java.io.Serializable;
import java.util.List;

public class WayModel implements Serializable {
    private String product_id;
    private String title;
    private String price;
    private boolean isselected;

    public String getProduct_id() {
        return product_id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }
}
