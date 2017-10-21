package tzchoice.kisanga.joshua.tts.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import info.hoang8f.widget.FButton;
import tzchoice.kisanga.joshua.tts.Constant;
import tzchoice.kisanga.joshua.tts.R;

public class SearchFragment extends Fragment {

    FButton btnTPSearch;
    EditText inputTPNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search1, container, false);
        inputTPNo = (EditText) view.findViewById(R.id.edt_search_tp);
        btnTPSearch = (FButton) view.findViewById(R.id.btn_search_tp);
        btnTPSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if no view has focus:
                 view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if(inputTPNo.getText().toString().equals("")){
                    massage("Please inter TP Number");
                }else {
                    String TPNO = inputTPNo.getText().toString();
                    Bundle b = new Bundle();
                    b.putString(Constant.KEY_TPNO, TPNO);
                    inputTPNo.setText("");

                    // Check that the activity is using the layout version with the fragment_container FrameLayout
                    if(view.findViewById(R.id.fragment_container_tp) == null)
                    {
                        // if we are being restored from a previous state, then we dont need to do anything and should
                        // return or else we could end up with overlapping fragments.
                        if(savedInstanceState != null)
                            return;

                        // Create an instance of editorFrag
                        TPResultFragment tpResult = new TPResultFragment();
                        tpResult.setArguments(b);
                        getFragmentManager().beginTransaction().remove(tpResult).commit();

                        // add fragment to the fragment container layout
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container_tp, tpResult)

                                .commit();
                    }
                }

            }

        });
        return view;
    }

    private void massage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
