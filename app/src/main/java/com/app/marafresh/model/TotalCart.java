package com.app.marafresh.model;

/**
 * Created by JeanEric on 05/10/2017.
 */

public class TotalCart {

    private int TotalPerITEM;

    public TotalCart() {
    }

    public TotalCart(int totalPerITEM) {
        TotalPerITEM = totalPerITEM;
    }

    public int getTotalPerITEM() {
        return TotalPerITEM;
    }

    public void setTotalPerITEM(int totalPerITEM) {
        TotalPerITEM = totalPerITEM;
    }
}
