package com.example.a5imageprocessing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class AlertDialog extends AppCompatDialogFragment {
    String wid;
    String len;
    String accNo;
    String pID;
    String date;

    public AlertDialog(String pID, String accNo, String  wid, String len, String date) {
        this.pID = pID;
        this.accNo = accNo;
        this.wid = wid;
        this.len = len;
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("INFO")
            .setMessage("PLANT ID.: "+ this.pID +"\nACCESSION NO.: "+ this.accNo + "\nLEAF WIDTH: "+ this.wid + " cm\nLEAF HEIGHT: "+ this.len +" cm" + "\nDATE: " + this.date).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }


}
