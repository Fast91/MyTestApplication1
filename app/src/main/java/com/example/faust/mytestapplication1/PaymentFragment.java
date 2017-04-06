package com.example.faust.mytestapplication1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaymentFragment extends Fragment
{
    private String mBundlePaymentReceiver;
    private String mBundlePaymentGroup;

    private String mReceiver;
    private String mGroup;
    private String mDefaultAmountValue;
    private String mCustomAmountValue;
    private String mAmountValue;

    private boolean mDefaultAmountOptionSelected;
    private boolean mCustomAmountEditTextOptionSelected;
    // se true significa che ho selezionato il bottone con l'importo esatto
    // se è false significa che ho selezionato il bottone custom amount per inserire un importo personalizzato

    //private RelativeLayout mLayout;

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

    private Drawable mDeselectedButtonBackground;
    private Drawable mSelectedButtonBackground;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundlePaymentReceiver = getArguments().getString("expense_receiver", null);
        mBundlePaymentGroup = getArguments().getString("expense_group", null);
        mDefaultAmountOptionSelected = true;
        mCustomAmountEditTextOptionSelected = false;

        // TODO DB etc etc
        mReceiver = mBundlePaymentReceiver;
        mGroup = mBundlePaymentGroup;
        mDefaultAmountValue = "50€"; // da importare dal db
        mCustomAmountValue=mDefaultAmountValue;
        mAmountValue=mDefaultAmountValue;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        wireUpWidgets(view);
        retrieveDataFromDB();
        initWidgets();
        setListeners();

        return view;
    }

    private void wireUpWidgets(View view)
    {
        //mLayout = (RelativeLayout) view.findViewById(R.id.layout_fragment_payment);

        mAmountLabelTextView = (TextView) view.findViewById(R.id.amount_label_payment_tv);
        mReceiverLabelTextView = (TextView) view.findViewById(R.id.receiver_label_payment_tv);

        mSenderDetailTextView = (TextView) view.findViewById(R.id.sender_detail_payment_tv);
        mReceiverDetailTextView = (TextView) view.findViewById(R.id.receiver_detail_payment_tv);
        mGroupDetailTextView = (TextView) view.findViewById(R.id.group_detail_payment_tv);
        mAmountDetailTextView = (TextView) view.findViewById(R.id.amount_detail_payment_tv);

        mDefaultAmountButton = (Button) view.findViewById(R.id.default_amount_button_payment_tv);
        mCustomAmountButton = (Button) view.findViewById(R.id.custom_amount_button_payment_tv);

        mCustomAmountEditText = (EditText) view.findViewById(R.id.custom_amount_edittext_payent_tv);

        mConfirmButton = (Button) view.findViewById(R.id.confirm_button_payment_tv);

        mDeselectedButtonBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.buttonshape_faust, null);
        mSelectedButtonBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.button_pressed_faust, null);
        /*mDeselectedButtonBackground = ContextCompat.getDrawable(getContext(), R.drawable.buttonshape_faust);
        mSelectedButtonBackground = ContextCompat.getDrawable(getContext(), R.drawable.button_pressed_faust);*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDeselectedButtonBackground = view.getResources().getDrawable(R.drawable.buttonshape_faust, getContext().getTheme());
            mSelectedButtonBackground = view.getResources().getDrawable(R.drawable.button_pressed_faust, getContext().getTheme());
        }
        else
        {
            mDeselectedButtonBackground = view.getResources().getDrawable(R.drawable.buttonshape_faust);
            mSelectedButtonBackground = view.getResources().getDrawable(R.drawable.button_pressed_faust);
        }*/

    }

    private void retrieveDataFromDB()
    {
        //TODO qui acquisiamo le informazioni dal db per modificare i widget
    }

    private void initWidgets()
    {
        // INIZIALIZZO I BOTTONI, MI ASSICURO CHE IL CONFIRM SIA DISABILITATO,
        // CHE TUTTI I BOTTONI SIANO VISIBILI E CHE L'EDITTEXT DEL CUSTOM AMOUNT SIA
        // INVISIBILE E DISABILITATO.

        mAmountLabelTextView.setText(mDefaultAmountValue);
        mDefaultAmountButton.setText(mDefaultAmountValue);
        mCustomAmountEditText.setText(mDefaultAmountValue);
        mReceiverLabelTextView.setText(mReceiver);
        mReceiverDetailTextView.setText(mReceiver);
        mGroupDetailTextView.setText(mGroup);

        mDefaultAmountButton.setEnabled(true);
        mCustomAmountButton.setEnabled(true);
        mConfirmButton.setEnabled(true);
        mCustomAmountEditText.setEnabled(true);

        mDefaultAmountButton.setVisibility(View.VISIBLE);
        mCustomAmountButton.setVisibility(View.VISIBLE);
        mConfirmButton.setVisibility(View.VISIBLE);
        mCustomAmountEditText.setVisibility(View.INVISIBLE);

        updateButtonsBackground();
        swapCustomButtonToEditText(false);

        refreshValues();
        refreshButtonsValue();
        refreshDetails();
    }

    private void updateButtonsBackground()
    {
        mDefaultAmountButton.setBackground(mDefaultAmountOptionSelected?mSelectedButtonBackground:mDeselectedButtonBackground);
        mCustomAmountButton.setBackground(!mDefaultAmountOptionSelected?mSelectedButtonBackground:mDeselectedButtonBackground);
        // non devo modificare il background degli altri due bottoni
    }

    private void swapCustomButtonToEditText(boolean toEditText)
    {
        if(toEditText!=mCustomAmountEditTextOptionSelected)
        {
            if (toEditText) {
                mCustomAmountEditTextOptionSelected = true;

                mCustomAmountButton.setEnabled(false);
                mCustomAmountEditText.setVisibility(View.VISIBLE);
                mCustomAmountButton.setVisibility(View.INVISIBLE);
                mCustomAmountEditText.setEnabled(true);
            } else {
                mCustomAmountEditTextOptionSelected = false;

                mCustomAmountEditText.setEnabled(false);
                mCustomAmountButton.setVisibility(View.VISIBLE);
                mCustomAmountEditText.setVisibility(View.INVISIBLE);
                mCustomAmountButton.setEnabled(true);
            }
        }
    }

    private void refreshValues()
    {
        mAmountValue = mDefaultAmountOptionSelected?mDefaultAmountValue:mCustomAmountValue;
    }

    private void refreshButtonsValue()
    {
        //mCustomAmountButton.setText(mCustomAmountValue);
    }

    private void refreshDetails()
    {
        mAmountDetailTextView.setText(mAmountValue);
    }

    private void setListeners()
    {
        // azioni da eseguire:
        // devo gestire la comparsa e scomparsa del bottone e dell'edittext del custom amount
        // devo modificare il valore di mDefaultAmountSelected quando premo uno dei due bottoni di amount
        // e di conseguenza devo aggiornare i background.
        mDefaultAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultAmountOptionSelected = true;
                updateButtonsBackground();
                swapCustomButtonToEditText(false);
                refreshValues();
                refreshDetails();
            }
        });
        mCustomAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultAmountOptionSelected = false;
                updateButtonsBackground();
                swapCustomButtonToEditText(true);
                refreshValues();
                refreshDetails();
            }
        });
        mCustomAmountEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultAmountOptionSelected = false;
                updateButtonsBackground();
                swapCustomButtonToEditText(true);
            }
        });
        mCustomAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mCustomAmountValue=s.toString();
                refreshValues();
                refreshButtonsValue();
                refreshDetails();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO qui il DB deve fare la transazione
                // ...
                updateButtonsBackground();
                swapCustomButtonToEditText(false);
            }
        });
    }
}
