package protel.jahitin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianJadiFragment extends Fragment implements View.OnClickListener {


    public PakaianJadiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pakaian_jadi, container, false);
        // Inflate the layout for this fragment

        Toolbar bottomToolbar = view.findViewById(R.id.bottom_toolbar_pembelian);
        ((AppCompatActivity)getActivity()).setSupportActionBar(bottomToolbar);
        Button btnCheckout = bottomToolbar.findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        
    }
}
