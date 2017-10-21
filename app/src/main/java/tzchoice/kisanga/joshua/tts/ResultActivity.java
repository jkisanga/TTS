package tzchoice.kisanga.joshua.tts;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.tts.Adapter.ResultAdapter;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.Pojo.TpInspection;

public class ResultActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private String TPNo,Client;
    int  TPId;
    private SQLiteHandler db;
    private RecyclerView recyclerViewResult;
    private ResultAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_result);

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


        recyclerViewResult = (RecyclerView) findViewById(R.id.tp_result_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewResult.setLayoutManager(layoutManager);
        sendTPno(TPId);

    }
    private void sendTPno(final int tpId) {
        pDialog.setMessage("loading data ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        try {

            Call<List<TpInspection>> call = service.getTpInspection(tpId);

            call.enqueue(new Callback<List<TpInspection>>() {
                @Override
                public void onResponse(Response<List<TpInspection>> response, Retrofit retrofit) {
                    if(response.isSuccess()) {

                        hideDialog();

                        try {
                           List<TpInspection>  tpInspections = response.body();

                            mAdapter = new ResultAdapter(tpInspections);

                            mAdapter.notifyDataSetChanged();
                            recyclerViewResult.setAdapter(mAdapter);
                            recyclerViewResult.setHasFixedSize(true);
                           // recyclerViewResult.addItemDecoration(new DividerItemDecoration(ResultActivity.this, LinearLayoutManager.VERTICAL));
                            Log.d("issue", "onResponse: Issue");

                        }catch (Exception e){
                            Log.d("issue", "onResponse: Issue1");
                            message("Server issue !");
                            hideDialog();
                        }

                    }else{
                        message("Kuna issue");
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
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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
