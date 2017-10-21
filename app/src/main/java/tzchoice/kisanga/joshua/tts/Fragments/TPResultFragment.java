package tzchoice.kisanga.joshua.tts.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.hoang8f.widget.FButton;
import tzchoice.kisanga.joshua.tts.Adapter.CheckpointAdapter;
import tzchoice.kisanga.joshua.tts.Adapter.ProductAdapter;
import tzchoice.kisanga.joshua.tts.Constant;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.R;

/**
 * Created by user on 3/11/2017.
 */

public class TPResultFragment extends Fragment {
    private String TPNo;
    private SQLiteHandler db;
    TextView TPNO, 	countryOfOrigin, consignmentNo,IssuedOn,ExpiryOn,From,
            To,VehicleNo;
   // private RecyclerView recyclerViewRoute, recyclerViewProduct ;
    private CheckpointAdapter mAdapter;
    private ProductAdapter productAdapter;
    FButton fButtonInspect;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tp_result, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            TPNo = bundle.getString(Constant.KEY_TPNO);
            db = new SQLiteHandler(getActivity().getApplicationContext());
        }
// Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        TPNO = (TextView) view.findViewById(R.id.txt_tp_no);
       // issuedTo = (TextView) view.findViewById(R.id.txt_issued_to);
        countryOfOrigin = (TextView) view.findViewById(R.id.txt_country_of_origin);
        consignmentNo = (TextView) view.findViewById(R.id.txt_consignment_no);
        IssuedOn = (TextView) view.findViewById(R.id.txt_issued);
        ExpiryOn = (TextView) view.findViewById(R.id.txt_expiry_on);
        From = (TextView) view.findViewById(R.id.txt_from);
        To = (TextView) view.findViewById(R.id.txt_to);
        VehicleNo = (TextView) view.findViewById(R.id.txt_vehicle_no);
       // recyclerView = (RecyclerView) view.findViewById(R.id.tp_route_recycler_view);
       // recyclerViewProduct = (RecyclerView) view.findViewById(R.id.tp_product_recycler_view);
        fButtonInspect = (FButton) view.findViewById(R.id.fbtn_inspect);

//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
//        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerViewProduct.setLayoutManager(layoutManager1);
      //  sendTPno(TPNo);
        return  view;
    }


//
//    private void sendTPno(final String tpNo) {
//        pDialog.setMessage("loading data ...");
//        showDialog();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constant.url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
//
//        try {
//
//            Call<TransitPass> call = service.sendTpNoToServer(tpNo);
//
//            call.enqueue(new Callback<TransitPass>() {
//                @Override
//                public void onResponse(Response<TransitPass> response, Retrofit retrofit) {
//                    if(response.isSuccess() && response.body() != null) {
//
//                        hideDialog();
//
//                        try {
//                            TransitPass transitPass = response.body();
//                            final Tp tp = transitPass.getTp();
//
//                            TPNO.setText("TP No : " + tp.getTpNo());
//                            consignmentNo.setText("Consignment No : " + tp.getConsignmentNo());
//                            countryOfOrigin.setText("Country of Origin : " + tp.getCountryOfOrigin());
//                            IssuedOn.setText("Date Issued : " + tp.getDateOfIssued());
//                            ExpiryOn.setText("Date of Expiry : " + tp.getExpiryDate());
//                            From.setText("From : " + tp.getFrom());
//                            To.setText("To : " + tp.getTo());
//                            VehicleNo.setText("Vehicle Reg No : " + tp.getVehicleRegNo());
//
//                            if (transitPass.getCheckpoints().equals("Inspected")) {
//                                fButtonInspect.setVisibility(View.GONE);
//                            } else {
//                                fButtonInspect.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Intent i = new Intent(getActivity(), InspectionActivity.class);
//                                        i.putExtra(Constant.KEY_TPID, tp.getId());
//                                        i.putExtra(Constant.KEY_TPNO, tp.getTpNo());
//                                        startActivity(i);
//                                    }
//                                });
//
//                            }
//                            List<Product> products = transitPass.getProducts();
//                            List<Checkpoint> checkpoints = transitPass.getCheckpoints();
//
//
//                            productAdapter = new ProductAdapter(products);
//                            productAdapter.notifyDataSetChanged();
//                            recyclerViewProduct.setAdapter(productAdapter);
//                            recyclerView.setHasFixedSize(true);
//
//                            mAdapter = new CheckpointAdapter(checkpoints);
//                            mAdapter.notifyDataSetChanged();
//                            recyclerView.setAdapter(mAdapter);
//                            recyclerView.setHasFixedSize(true);
//
//                            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//                            recyclerViewProduct.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//                        }catch (Exception e){
//                            message("Server issue !");
//                            hideDialog();
//                        }
//
//                    }else{
//                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container_tp)).commit();
//                        message("TP No unavailable !");
//                        hideDialog();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//
//                    message("Server issue !");
//                    hideDialog();
//                }
//            });
//        }catch (Exception e){
//            message("Server issue !");
//            hideDialog();
//        }
//
//    }
//
//    private void message(String s) {
//        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//    }
//
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

}
