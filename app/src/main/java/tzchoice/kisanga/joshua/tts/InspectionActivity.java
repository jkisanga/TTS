package tzchoice.kisanga.joshua.tts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.telpo.tps550.api.printer.ThermalPrinter;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import info.hoang8f.widget.FButton;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.Pojo.TpInspection;

import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT;
import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_CHECKPOINT_ID;
import static tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler.KEY_USERNAME;

public class InspectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    //Printing
    private final int NOPAPER = 3;
    private final int LOWBATTERY = 4;
    private final int PRINTPAPERWALK = 8;
    private final int PRINTCONTENT = 9;
    private final int CANCELPROMPT = 10;
    private final int OVERHEAT = 12;
    private final int MAKER = 13;
    private final int EXECUTECOMMAND = 15;
    private final int PRINTERR = 11;

    private String Result;
    private Boolean nopaper = false;
    private boolean LowBattery = false;
    private String picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/logo.bmp";
    private  String strStatus, strIrregularity, strProduct,strQuantity,strUnit,strValue,strAction, strActionAmount,strReceipt,strInspector,strCreatedAt,strTPNO, strCheckpoint,strDesc;

    public static int paperWalk;
    public static String printContent;
    private int leftDistance = 0;
    private int lineDistance;
    private int wordFont;
    private int printGray;
    private ProgressDialog progressDialog;
    private final static int MAX_LEFT_DISTANCE = 255;
    ProgressDialog dialog;
    MyHandler handler;
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NOPAPER:
                    noPaperDlg();
                    break;
                case LOWBATTERY:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspectionActivity.this);
                    alertDialog.setTitle(R.string.operation_result);
                    alertDialog.setMessage(getString(R.string.LowBattery));
                    alertDialog.setPositiveButton(getString(R.string.dialog_comfirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                    break;

                case PRINTCONTENT:
                    new contentPrintThread().start();
                    break;

                case CANCELPROMPT:
                    if (progressDialog != null && !InspectionActivity.this.isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    break;

                case OVERHEAT:
                    AlertDialog.Builder overHeatDialog = new AlertDialog.Builder(InspectionActivity.this);
                    overHeatDialog.setTitle(R.string.operation_result);
                    overHeatDialog.setMessage(getString(R.string.overTemp));
                    overHeatDialog.setPositiveButton(getString(R.string.dialog_comfirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    overHeatDialog.show();
                    break;
                default:
                    Toast.makeText(InspectionActivity.this, "Print Error!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }





    String TPNo, SelectedIrre, SelectedAct, SecectedProd, SelectedUnit, Client;
    int  TPId;
    private ProgressDialog pDialog;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    FButton FbtnAdd, FbtnSubmint;

    EditText  txtQuantity,Value,Amount, ReceiptNo, txtMoreDesc;

    Spinner spIrregularities, spActions, spProduct, spUnit;

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inspection);
        dismissKeyboard(this);
        db = new SQLiteHandler(getApplicationContext());
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Printings
        handler = new MyHandler();
        IntentFilter pIntentFilter = new IntentFilter();
        pIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        pIntentFilter.addAction("android.intent.action.BATTERY_CAPACITY_EVENT");
        registerReceiver(printReceive, pIntentFilter);

        Bundle bundle = getIntent().getExtras();

        TPNo = bundle.getString(Constant.KEY_TPNO);
        Client =  "To : " + bundle.getString(Constant.KEY_CLIENT);
        TPId = bundle.getInt(Constant.KEY_TPID);

        //EditText
        Value = (EditText) findViewById(R.id.edt_value);
        txtQuantity = (EditText) findViewById(R.id.edt_volume);
        txtMoreDesc = (EditText) findViewById(R.id.edt_more_desc);
        Amount = (EditText) findViewById(R.id.edt_amount);
        ReceiptNo = (EditText) findViewById(R.id.edt_receipt_no);

        //FButton
        FbtnAdd = (FButton) findViewById(R.id.fbtn_add);
        FbtnSubmint = (FButton) findViewById(R.id.fbtn_submit);
        FbtnSubmint.setOnClickListener(this);
        FbtnAdd.setOnClickListener(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TP NO : " + TPNo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        spIrregularities = (Spinner) findViewById(R.id.sp_irregularities);
        spActions = (Spinner) findViewById(R.id.sp_action);
        spProduct = (Spinner) findViewById(R.id.sp_product);
        spUnit = (Spinner) findViewById(R.id.sp_unit);

       // savepic();
        loadActionSpinnerData();
        loadProductSpinnerData();
        loadIrregularitySpinnerData();
        loadUnitSpinnerData();
    }

    private void sendInspectionDataToServer() {
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        int selectedId=radioGroup.getCheckedRadioButtonId();
        if(selectedId > 0){
            radioButton=(RadioButton)findViewById(selectedId);
            radioButton.getText();
            if(radioButton.getText().toString().equals("OK")){
                sendOKResult();
            }else{
                collectIrregularities();
                    Intent i = new Intent(InspectionActivity.this, MainActivity.class);
                    startActivity(i);
                finish();

            }
        }else{

            message("You Must Choose if is Ok or Not");
        }
    }


    private void collectIrregularities() {

        pDialog.setMessage("sending ...");
        showDialog();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        TpInspection inspection = new TpInspection();

        inspection.setCheckpointId(Integer.valueOf(db.getUserDetails().get(KEY_CHECKPOINT_ID)));
        inspection.setInspectedBy(db.getUserDetails().get(KEY_USERNAME));
        inspection.setDesc(txtMoreDesc.toString());
        inspection.setIrregularity(SelectedIrre);

        inspection.setProduct(SecectedProd);
        inspection.setUnit(SelectedUnit);
        //
        String value = Value.getText().toString().trim();
        if(value.isEmpty() || value.length() == 0 || value.equals("") || value == null){

            inspection.setValue(0.00);
        }else {
            inspection.setValue(Double.valueOf(Value.getText().toString()));
        }

        //
        String amount = Amount.getText().toString().trim();
        if(amount.isEmpty() || amount.length() == 0 || amount.equals("") || amount == null){
            inspection.setActionAmount(0.00);
        }else {
            inspection.setActionAmount(Double.valueOf(Amount.getText().toString()));
        }

        //
        String quantity = txtQuantity.getText().toString().trim();

        if(quantity.isEmpty() || quantity.length() == 0 || quantity.equals("") || quantity == null){
            inspection.setQuantity(0.00);
        }else {
            inspection.setQuantity(Double.valueOf(txtQuantity.getText().toString()));
        }
        inspection.setAction(SelectedAct);
        inspection.setReceiptNo(ReceiptNo.getText().toString());
        inspection.setStatus("NOT OK");
        inspection.setTpId(TPId);
        inspection.setDesc(txtMoreDesc.getText().toString());
        try {

            Call<TpInspection> call = service.sendTpInspection(inspection);
            call.enqueue(new Callback<TpInspection>() {
                @Override
                public void onResponse(Response<TpInspection> response, Retrofit retrofit) {
                    if(response.isSuccess()) {
//                        message("data sent. You can fill another");
                        Amount.setText("");
                        Value.setText("");
                        txtQuantity.setText("");
                        ReceiptNo.setText("");
                        txtMoreDesc.setText("");
                        spIrregularities.setSelection(0);
                        spProduct.setSelection(0);
                        spActions.setSelection(0);
                        spUnit.setSelection(0);
                        TpInspection result = response.body();

                        printReceipt(result);


                        hideDialog();
                    }else{

                        message("check the internet");
                        hideDialog();
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    message("Server issue, check internet or try again");
                    hideDialog();
                }
            });
        }catch (Exception e){
            pDialog.setMessage("sending ...");
            showDialog();
            message("Server issue, check internet or try again");
            hideDialog();
        }

    }

    private void printReceipt(TpInspection result) {
        strCheckpoint = "Checkpoint : " + db.getUserDetails().get(KEY_CHECKPOINT);
        strTPNO = "TP No : " + TPNo;
        strStatus = "Cargo status : " + result.getStatus();
        strIrregularity =  "Irregularity : " + result.getIrregularity();
        strProduct =  "Product : " + result.getProduct();
        strQuantity =  "Quantity : " + result.getQuantity();
        strUnit = "Unit : " + result.getUnit();
        if(result.getValue() == null){
            strValue = "Value : " + result.getValue();
        }else {
            strValue = "Value : " + formatDecimal(String.valueOf(result.getValue())) + " TZS";
        }
        strAction = "Action : " + result.getAction();
        if(result.getActionAmount() == null){
            strActionAmount = "Amount : " + result.getActionAmount();
        }else {
            strActionAmount = "Amount : " + formatDecimal(String.valueOf(result.getActionAmount())) + " TZS";
        }
        strReceipt =  "Receipt No : " + result.getReceiptNo();
        strInspector = "Inspector : " + result.getInspectedBy();
        strCreatedAt = "Date : " + result.getCreatedAt();
        strDesc = "More : " + result.getDesc();

        leftDistance = 5;
        lineDistance = 6;

        printContent = "\n --- start of receipt --- \n\n" +

                "\n Tanzania Forest Services (TFS) Agency  \n\n" +
                "\n Box 40832, Nyerere Road \n\n" +
                "\n Dar es Salaam, Tanzania \n\n" +
                "\n Phone: (255) (022) 2864249 \n\n" +
                "\n Email: mpingo@tfs.go.tz \n" +
                "\n  \n" +
                "\n" + Client + "\n\n" +
                "\n \n" +
                "\n Inspection Receipt \n\n" +
                "\n ------------------ \n\n" +

                "\n" + strCheckpoint + "\n\n" +
                "\n" + strTPNO + "\n\n" +
                "\n" + strStatus + "\n\n" +
                "\n" + strIrregularity + "\n\n" +
                "\n" + strProduct + "\n\n" +
                "\n" + strQuantity + "\n\n" +
                "\n" + strUnit + "\n\n" +
                "\n" + strValue + "\n\n" +
                "\n" + strAction + "\n\n" +
                "\n" + strActionAmount + "\n\n" +
                "\n" + strReceipt + "\n\n" +
                "\n" + strInspector + "\n\n" +
                "\n" + strCreatedAt + "\n\n\n" +
                "\n" + strDesc + "\n\n\n" +
                "   --- end of receipt ---   \n\n\n\n\n\n";

        wordFont = 2;
        printGray = 12;
        if (leftDistance > MAX_LEFT_DISTANCE) {
            Toast.makeText(InspectionActivity.this, getString(R.string.outOfLeft), Toast.LENGTH_LONG).show();
            return;
        } else if (lineDistance > 255) {
            Toast.makeText(InspectionActivity.this, getString(R.string.outOfLine), Toast.LENGTH_LONG).show();
            return;
        } else if (wordFont > 4 || wordFont < 1) {
            Toast.makeText(InspectionActivity.this, getString(R.string.outOfFont), Toast.LENGTH_LONG).show();
            return;
        } else if (printGray < 0 || printGray > 12) {
            Toast.makeText(InspectionActivity.this, getString(R.string.outOfGray), Toast.LENGTH_LONG).show();
            return;
        }
        if (printContent == null || printContent.length() == 0) {
            Toast.makeText(InspectionActivity.this, getString(R.string.empty), Toast.LENGTH_LONG).show();
            return;
        }
        if (LowBattery == true) {
            handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0, null));
        } else {
            if (!nopaper) {
                progressDialog = ProgressDialog.show(InspectionActivity.this, getString(R.string.bl_dy), getString(R.string.printing_wait));
                handler.sendMessage(handler.obtainMessage(PRINTCONTENT, 1, 0, null));
            } else {
                Toast.makeText(InspectionActivity.this, getString(R.string.ptintInit), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void message(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void sendOKResult() {


        pDialog.setMessage("sending ...");
        showDialog();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);


        final TpInspection inspection = new TpInspection();
        inspection.setCheckpointId(Integer.valueOf(db.getUserDetails().get(KEY_CHECKPOINT_ID)));
        inspection.setInspectedBy(db.getUserDetails().get(KEY_USERNAME));
        inspection.setStatus("OK");
        inspection.setTpId(TPId);

        try {

            Call<TpInspection> call = service.sendTpInspection(inspection);
            call.enqueue(new Callback<TpInspection>() {
                @Override
                public void onResponse(Response<TpInspection> response, Retrofit retrofit) {
                    if(response.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "Inspection sent", Toast.LENGTH_SHORT).show();


                        TpInspection result = response.body();

                        printReceipt(result);

                        Intent intent = new Intent(InspectionActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        hideDialog();
                        finish();
                    }else{

                        message("wait a moment and try again");
                        hideDialog();
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    message("check internet");
                hideDialog();
                }
            });
        }catch (Exception e){
            message("try again");
            hideDialog();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadIrregularitySpinnerData() {
        // database handler


        // Spinner Drop down elements
        List<String> irregularities = db.irregularities();

        HintSpinner<String> hintSpinner = new HintSpinner<>(
                spIrregularities,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, R.string.irregularities_hint_text, irregularities),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        SelectedIrre = spIrregularities.getSelectedItem().toString();
                    }
                });
        hintSpinner.init();
    }
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadUnitSpinnerData() {
        // database handler


        // Spinner Drop down elements
        List<String> units = db.units();

        HintSpinner<String> hintSpinner = new HintSpinner<>(
                spUnit,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, R.string.unit_hint_text, units),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        SelectedUnit = spUnit.getSelectedItem().toString();
                    }
                });
        hintSpinner.init();
    }

    private void loadActionSpinnerData() {
        // database handler

        // Spinner Drop down elements
        List<String> actionList = db.actions();

        HintSpinner<String> hintSpinner = new HintSpinner<>(
                spActions,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, R.string.action_hint_text, actionList),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        SelectedAct = spActions.getSelectedItem().toString();
                    }
                });
        hintSpinner.init();
    }
    private void loadProductSpinnerData() {
        // database handler

        // Spinner Drop down elements
        List<String> productList = db.products();

        HintSpinner<String> hintSpinner = new HintSpinner<>(
                spProduct,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, R.string.product_hint_text, productList),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        SecectedProd = spProduct.getSelectedItem().toString();
                    }
                });
        hintSpinner.init();
    }

    @Override
    public void onClick(View view) {
        FButton fb = (FButton) view;
        switch (fb.getId()){
            case R.id.fbtn_submit:
                sendInspectionDataToServer();
                break;
            case R.id.fbtn_add:
                collectIrregularities();
                message("data sent. You can fill another");
               // FbtnSubmint.setEnabled(false);
               // FbtnView.setEnabled(true);
                break;
//            case R.id.fbtn_view:
//                Intent i = new Intent(InspectionActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
//                break;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


    //Printings

    private void noPaperDlg() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(InspectionActivity.this);
        dlg.setTitle(getString(R.string.noPaper));
        dlg.setMessage(getString(R.string.noPaperNotice));
        dlg.setCancelable(false);
        dlg.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThermalPrinter.stop(InspectionActivity.this);
            }
        });
        dlg.show();
    }



    private class contentPrintThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(InspectionActivity.this);
                ThermalPrinter.reset();
//                ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_MIDDLE);
//                File file = new File(picturePath);
//                if (file.exists()) {
//                    ThermalPrinter.printLogo(BitmapFactory.decodeFile(picturePath));
//                }
                ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
                ThermalPrinter.setLeftIndent(leftDistance);
                ThermalPrinter.setLineSpace(lineDistance);
                if (wordFont == 4) {
                    ThermalPrinter.setFontSize(2);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (wordFont == 3) {
                    ThermalPrinter.setFontSize(1);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (wordFont == 2) {
                    ThermalPrinter.setFontSize(2);
                } else if (wordFont == 1) {
                    ThermalPrinter.setFontSize(1);
                }
                ThermalPrinter.setGray(printGray);
                ThermalPrinter.addString(printContent);
                ThermalPrinter.printString();


                if((SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS510A.ordinal()) ||
                        SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS510D.ordinal() ||
                        SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS580A.ordinal()){
                    ThermalPrinter.walkPaper(10);
                }else {
                    ThermalPrinter.walkPaper(120);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                if (Result.equals("tzchoice.kisanga.joshua.tts.NoPaperException")) {
                    nopaper = true;
                } else if (Result.equals("tzchoice.kisanga.joshua.tts.OverHeatException")) {
                    handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0, null));
                } else {
                    handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0, null));
                }
            } finally {
                handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0, null));
                if (nopaper){
                    handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0, null));
                    nopaper = false;
                    return;
                }
                ThermalPrinter.stop(InspectionActivity.this);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (progressDialog != null && !InspectionActivity.this.isFinishing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        unregisterReceiver(printReceive);
        ThermalPrinter.stop();
        super.onDestroy();
    }

    /* Called when the application resumes */
    @Override
    protected void onResume() {
        super.onResume();
    }

    private final BroadcastReceiver printReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                //TPS390 can not print,while in low battery,whether is charging or not charging
                if(SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS390.ordinal()){
                    if (level * 5 <= scale) {
                        LowBattery = true;
                    } else {
                        LowBattery = false;
                    }
                }else {
                    if (status != BatteryManager.BATTERY_STATUS_CHARGING) {
                        if (level * 5 <= scale) {
                            LowBattery = true;
                        } else {
                            LowBattery = false;
                        }
                    } else {
                        LowBattery = false;
                    }
                }
            }
            //Only use for TPS550_MTK devices
            else if (action.equals("android.intent.action.BATTERY_CAPACITY_EVENT")) {
                int status = intent.getIntExtra("action", 0);
                int level = intent.getIntExtra("level", 0);
                if(status == 0){
                    if(level < 1){
                        LowBattery = true;
                    }else {
                        LowBattery = false;
                    }
                }else {
                    LowBattery = false;
                }
            }
        }
    };

    private void savepic() {
        File file = new File(picturePath);
        if (!file.exists()) {
            InputStream inputStream = null;
            FileOutputStream fos = null;
            byte[] tmp = new byte[1024];
            try {
                inputStream = getApplicationContext().getAssets().open("syhlogo.png");
                fos = new FileOutputStream(file);
                int length = 0;
                while((length = inputStream.read(tmp)) > 0){
                    fos.write(tmp, 0, length);
                }
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("save", e.toString());
            }
//            finally {
//                try {
//                    inputStream.close();
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public static String formatDecimal(String value) {
        DecimalFormat df = new DecimalFormat("#,###,##0.0");
        return df.format(Double.valueOf(value));
    }


}
