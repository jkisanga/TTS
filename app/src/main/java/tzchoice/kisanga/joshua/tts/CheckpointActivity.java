package tzchoice.kisanga.joshua.tts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import tzchoice.kisanga.joshua.tts.Adapter.CheckpointAdapter;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;

import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT;
import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT_ID;

public class CheckpointActivity extends AppCompatActivity {
    int checkpintId = 0;
    private RecyclerView recyclerView;
    private CheckpointAdapter mAdapter;
    private Button btnInspect;
    private SQLiteHandler db;
    String TPNo,product;
    int TPId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_checkpoint);

        Bundle bundle = getIntent().getExtras();
        TPNo = bundle.getString(Constant.KEY_TPNO);
        TPId = bundle.getInt(Constant.KEY_TPID);
         product = bundle.getString(Constant.KEY_PRODUCT);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TP No : " + TPNo + " Route List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = new SQLiteHandler(getApplicationContext());

        btnInspect = (Button) findViewById(R.id.btn_inspected);
        btnInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckpointActivity.this, InspectionActivity.class);
                intent.putExtra(Constant.KEY_CHECKPOINT_ID, db.getUserDetails().get(KEY_CHECKPOINT_ID));
                intent.putExtra(Constant.KEY_CHECKPOINT, db.getUserDetails().get(KEY_CHECKPOINT));
               // intent.putExtra(Constant.KEY_USER_ID, db.getUserDetails().get(KEY_USER_ID));
                intent.putExtra(Constant.KEY_TPID, TPId);
                intent.putExtra(Constant.KEY_TPNO, TPNo);
                intent.putExtra(Constant.KEY_PRODUCT, product);
                startActivity(intent);
            }
        });
    }




}
