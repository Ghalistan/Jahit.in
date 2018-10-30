package protel.jahitin.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import protel.jahitin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PakaianCustomFragment extends Fragment implements View.OnClickListener {
    RelativeLayout Jenis_tombol, Gender_tombol, Kain_tombol;
    ExpandableRelativeLayout Jenis_data, Gender_data, Kain_data;
    TextView tvJenis, tvGender, tvKain;

    public PakaianCustomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pakaian_custom, container, false);
        setJenis(view);
        setGender(view);
        setKain(view);
        return view;
    }

    private void setKain(View view) {
        RelativeLayout Kain_title = view.findViewById(R.id.jkain_layout);
        Kain_title.setOnClickListener(this);
        RelativeLayout Katun = view.findViewById(R.id.katun_kain);
        Katun.setOnClickListener(this);
        RelativeLayout Spandex = view.findViewById(R.id.spandex_kain);
        Spandex.setOnClickListener(this);

        Kain_tombol = view.findViewById(R.id.kain_expand_icon);
        tvKain = view.findViewById(R.id.tvKain);
        Kain_data = view.findViewById(R.id.kain_expandable);
        Kain_data.collapse();
    }

    private void setGender(View view) {
        RelativeLayout Gender_title = view.findViewById(R.id.gender_layout);
        Gender_title.setOnClickListener(this);
        RelativeLayout Male = view.findViewById(R.id.male_gender);
        Male.setOnClickListener(this);
        RelativeLayout Female = view.findViewById(R.id.female_gender);
        Female.setOnClickListener(this);

        Gender_tombol = view.findViewById(R.id.gender_expand_icon);
        tvGender = view.findViewById(R.id.tvGender);
        Gender_data = view.findViewById(R.id.gender_expandable);
        Gender_data.collapse();
    }

    private void setJenis(View view) {
        RelativeLayout Jenis_title =  view.findViewById(R.id.jpakaian_layout);
        Jenis_title.setOnClickListener(this);
        RelativeLayout Kaos = view.findViewById(R.id.Kaos_pcustom);
        Kaos.setOnClickListener(this);
        RelativeLayout Kemeja = view.findViewById(R.id.Kemeja_pcustom);
        Kemeja.setOnClickListener(this);
        RelativeLayout Polo = view.findViewById(R.id.Polo_pcustom);
        Polo.setOnClickListener(this);

        Jenis_tombol = view.findViewById(R.id.jenis_expand_icon);
        tvJenis = view.findViewById(R.id.tvJenis);
        Jenis_data = view.findViewById(R.id.jenis_expandable);
        Jenis_data.collapse();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Jenis Pakaian
            case R.id.jpakaian_layout:
                Jenis_data.toggle();
                break;
            case R.id.Kaos_pcustom:
                tvJenis.setText("Kaos");
                Jenis_data.collapse();
                break;
            case R.id.Kemeja_pcustom:
                tvJenis.setText("Kemeja");
                Jenis_data.collapse();
                break;
            case R.id.Polo_pcustom:
                tvJenis.setText("Polo");
                Jenis_data.collapse();
                break;
            //Gender
            case R.id.gender_layout:
                Gender_data.toggle();
                break;
            case R.id.male_gender:
                tvGender.setText("Laki-Laki");
                Gender_data.collapse();
                break;
            case R.id.female_gender:
                tvGender.setText("Perempuan");
                Gender_data.collapse();
                break;
            //Kain
            case R.id.jkain_layout:
                Kain_data.toggle();
                break;
            case R.id.katun_kain:
                tvKain.setText("Katun");
                Kain_data.collapse();
                break;
            case R.id.spandex_kain:
                tvKain.setText("Spandex");
                Kain_data.collapse();
                break;
        }
    }
}
