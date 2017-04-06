package com.example.faust.mytestapplication1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaymentFragment extends Fragment
{
    private String mPaymentId;

    private TextView mAmountLabelTextView;
    private TextView mReceiverLabelTextView;

    private TextView mSenderDetailTextView;
    private TextView mReceiverDetailTextView;
    private TextView mGroupDetailTextView;
    private TextView mAmountDetailTextView;

    private Button mDefaultAmountButton;
    private Button mCustomAmountButton;

    private EditText mCustomAmountEditText;

    private Button mConfirmButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentId = getArguments().getString("expense_id", null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
