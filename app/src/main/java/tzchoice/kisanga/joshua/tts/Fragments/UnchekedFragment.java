package tzchoice.kisanga.joshua.tts.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tzchoice.kisanga.joshua.tts.PrintActivity;
import tzchoice.kisanga.joshua.tts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnchekedFragment extends Fragment {


    public UnchekedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_uncheked, container, false);

        return view;
    }

}
