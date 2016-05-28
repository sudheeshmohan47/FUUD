package com.foodu.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodu.android.R;
import com.foodu.android.Widgets.CustomThinTextView;
import com.foodu.android.login.BaseFragments.BaseLoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sudheesh on 16-May-16.
 */
public class WelcomeFragment extends BaseLoginFragment {
    @BindView(R.id.txtWelcome)
    CustomThinTextView txtWelcome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);


        ButterKnife.bind(this,rootView);

        Spanned text = Html.fromHtml("Welcome<br/>to <b>FUUD</b>");
        txtWelcome.setText(text);

        return rootView;
    }
}
