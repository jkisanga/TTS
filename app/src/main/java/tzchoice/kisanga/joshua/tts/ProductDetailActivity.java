package tzchoice.kisanga.joshua.tts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.tts.Adapter.ProductAdapter;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.Pojo.Product;
import tzchoice.kisanga.joshua.tts.Pojo.Tp;
import tzchoice.kisanga.joshua.tts.Pojo.TransitPass;

public class ProductDetailActivity extends AppCompatActivity {
    private  FloatingActionButton fabInspect;
    private ProgressDialog pDialog;
    private String TPNo, Client;
    int  TPId;
    private SQLiteHandler db;
    private ProductAdapter productAdapter;
    private RecyclerView  recyclerViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            TPNo = bundle.getString(Constant.KEY_TPNO);
            TPId = bundle.getInt(Constant.KEY_TPID);
            Client = bundle.getString(Constant.KEY_CLIENT);
            db = new SQLiteHandler(this.getApplicationContext());
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TP NO : " + TPNo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

// Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        recyclerViewProduct = (RecyclerView) findViewById(R.id.tp_product_recycler_view);

         fabInspect = (FloatingActionButton) findViewById(R.id.fab_product_detail);



        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(layoutManager);
        sendTPno(TPNo);
    }



    private void sendTPno(final String tpNo) {
        pDialog.setMessage("loading data ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        try {

            Call<TransitPass> call = service.sendTpNoToServer(tpNo);

            call.enqueue(new Callback<TransitPass>() {
                @Override
                public void onResponse(Response<TransitPass> response, Retrofit retrofit) {
                    if(response.isSuccess() && response.body() != null) {

                        hideDialog();

                        try {
                            TransitPass transitPass = response.body();
                            final Tp tp = transitPass.getTp();
                            if (transitPass.getCheckpoints().equals("Inspected")) {
                                fabInspect.setVisibility(View.GONE);
                            } else {
                                fabInspect.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(ProductDetailActivity.this, InspectionActivity.class);
                                        i.putExtra(Constant.KEY_TPID, tp.getId());
                                        i.putExtra(Constant.KEY_TPNO, tp.getTpNo());
                                        i.putExtra(Constant.KEY_CLIENT, Client);
                                        startActivity(i);
                                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                });
                            }
                            List<Product> products = transitPass.getProducts();

                            productAdapter = new ProductAdapter(products);
                            productAdapter.notifyDataSetChanged();
                            recyclerViewProduct.setAdapter(productAdapter);
                            recyclerViewProduct.setHasFixedSize(true);
                            recyclerViewProduct.addItemDecoration(new DividerItemDecoration(ProductDetailActivity.this, LinearLayoutManager.VERTICAL));
                        }catch (Exception e){
                            message("Server issue !");
                            hideDialog();
                        }

                    }else{
                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container_tp)).commit();
                        message("TP No unavailable !");
                        hideDialog();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                    message("Server issue !");
                    hideDialog();
                }
            });
        }catch (Exception e){
            message("Server issue !");
            hideDialog();
        }

    }

    private void message(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}