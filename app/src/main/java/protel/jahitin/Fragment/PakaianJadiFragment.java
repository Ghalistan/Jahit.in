package protel.jahitin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianJadiFragment extends Fragment {


    public PakaianJadiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pakaian_jadi, container, false);
    }

}
