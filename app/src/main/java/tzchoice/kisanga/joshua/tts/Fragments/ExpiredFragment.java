package tzchoice.kisanga.joshua.tts.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.tts.Adapter.RVAdapter;
import tzchoice.kisanga.joshua.tts.Constant;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.Helper.SessionManager;
import tzchoice.kisanga.joshua.tts.LoginActivity;
import tzchoice.kisanga.joshua.tts.Pojo.Tp;
import tzchoice.kisanga.joshua.tts.R;
import tzchoice.kisanga.joshua.tts.RetrofitAPI;

import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpiredFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Tp> mTransitPass;
    private RecyclerView recyclerView;
    private RVAdapter adapter;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private SessionManager session;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expired, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDistanceToTriggerSync(40);

        // session manager
        session = new SessionManager(getActivity());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        ListData();
                                        // stopping swipe refresh
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );

            recyclerView = (RecyclerView) view.findViewById(R.id.tp_recycler_view);
// SQLite database handler
        db = new SQLiteHandler(getActivity());
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }



    private void ListData() {
        pDialog.setMessage("loading data ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Call<List<Tp>> call = service.getTPExpired(Integer.parseInt(db.getUserDetails().get(KEY_CHECKPOINT_ID)));
        call.enqueue(new Callback<List<Tp>>() {
            @Override
            public void onResponse(Response<List<Tp>> response, Retrofit retrofit) {
                swipeRefreshLayout.setRefreshing(false);
            hideDialog();
                try {

                    mTransitPass = response.body();

                    adapter = new RVAdapter(mTransitPass);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                }catch (Exception e){
                    e.printStackTrace();
                    message("No Internet, make sure you are connected !");
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                message("Server issue !");
                hideDialog();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("testF", "onFailure: " + t);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final MenuItem item1 = menu.findItem(R.id.action_settings);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(mTransitPass);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

    }



    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Tp> filteredModelList = filter(mTransitPass, newText);

        adapter.setFilter(filteredModelList);
        return true;
    }
    private void message(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Tp> filter(List<Tp> models, String query) {
        query = query.toLowerCase();
        final List<Tp> filteredModelList = new ArrayList<>();
        for (Tp model : models) {

            final String text = model.getTpNo();

            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onRefresh() {
        ListData();
    }

    private void logoutUser() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to sign off?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        //Starting login activity
                        session.setLogin(false);

                        db.deleteUsers();

                        // Launching the login activity
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
