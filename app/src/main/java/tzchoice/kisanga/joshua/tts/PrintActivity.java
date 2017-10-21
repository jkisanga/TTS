package tzchoice.kisanga.joshua.tts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.telpo.tps550.api.printer.ThermalPrinter;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;

public class PrintActivity extends AppCompatActivity {

    private static String printVersion;
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

    private  String strStatus, strIrregularity, strProduct,strQuantity,strUnit,strValue,strAction, strActionAmount,strReceipt,strInspector,strCreatedAt,strTPNO, strCheckpoint;

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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrintActivity.this);
                    alertDialog.setTitle(R.string.operation_result);
                    alertDialog.setMessage(getString(R.string.LowBattery));
                    alertDialog.setPositiveButton(getString(R.string.dialog_comfirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                    break;
                case PRINTPAPERWALK:
                    new paperWalkPrintThread().start();
                    break;
                case PRINTCONTENT:
                    new contentPrintThread().start();
                    break;
                case MAKER:
                    new MakerThread().start();
                    break;
                case CANCELPROMPT:
                    if (progressDialog != null && !PrintActivity.this.isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    break;
                case EXECUTECOMMAND:
                    new executeCommand().start();
                    break;
                case OVERHEAT:
                    AlertDialog.Builder overHeatDialog = new AlertDialog.Builder(PrintActivity.this);
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
                    Toast.makeText(PrintActivity.this, "Print Error!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    FloatingActionButton fabPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new MyHandler();
        IntentFilter pIntentFilter = new IntentFilter();
        pIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        pIntentFilter.addAction("android.intent.action.BATTERY_CAPACITY_EVENT");
        registerReceiver(printReceive, pIntentFilter);

        Bundle bundle = getIntent().getExtras();
        strStatus = bundle.getString(Constant.KEY_STATUS);
        strCheckpoint = bundle.getString(Constant.KEY_CHECKPOINT);
        strTPNO = bundle.getString(Constant.KEY_TPNO);
        strIrregularity = bundle.getString(Constant.KEY_IRREGULARITY);
        strProduct = bundle.getString(Constant.KEY_PRODUCT);
        strQuantity = bundle.getString(Constant.KEY_QUANTITY);
        strUnit = bundle.getString(Constant.KEY_UNIT);
        strValue = bundle.getString(Constant.KEY_VALUE);
        strAction = bundle.getString(Constant.KEY_ACTION);
        strActionAmount = bundle.getString(Constant.KEY_ACTION_AMOUNT);
        strReceipt = bundle.getString(Constant.KEY_RECEIPT_NO);
        strInspector = bundle.getString(Constant.KEY_INSPECTOR);
        strCreatedAt = bundle.getString(Constant.KEY_DATE);
        leftDistance = 5;
        lineDistance = 3;

        printContent = "\n ***************************** \n\n" +

                "\n Tanzania Forest Services (TFS) Agency  \n\n" +
                "\n Box 40832, Nyerere Road \n\n" +
                "\n MPINGO HOUSE \n\n" +
                "\n Dar es Salaam, Tanzania \n\n" +
                "\n Phone: (255) (022) 2864249 \n\n" +
                "\n Email: mpingo@tfs.go.tz \n\n\n" +
                "\n Inspection Receipt \n\n" +
                "\n **************************** \n\n" +
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
                "********** TFS *************";

        wordFont = 2;
        printGray = 4;
        if (leftDistance > MAX_LEFT_DISTANCE) {
            Toast.makeText(PrintActivity.this, getString(R.string.outOfLeft), Toast.LENGTH_LONG).show();
            return;
        } else if (lineDistance > 255) {
            Toast.makeText(PrintActivity.this, getString(R.string.outOfLine), Toast.LENGTH_LONG).show();
            return;
        } else if (wordFont > 4 || wordFont < 1) {
            Toast.makeText(PrintActivity.this, getString(R.string.outOfFont), Toast.LENGTH_LONG).show();
            return;
        } else if (printGray < 0 || printGray > 12) {
            Toast.makeText(PrintActivity.this, getString(R.string.outOfGray), Toast.LENGTH_LONG).show();
            return;
        }
        if (printContent == null || printContent.length() == 0) {
            Toast.makeText(PrintActivity.this, getString(R.string.empty), Toast.LENGTH_LONG).show();
            return;
        }
        if (LowBattery == true) {
            handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0, null));
        } else {
            if (!nopaper) {
                progressDialog = ProgressDialog.show(PrintActivity.this, getString(R.string.bl_dy), getString(R.string.printing_wait));
                handler.sendMessage(handler.obtainMessage(PRINTCONTENT, 1, 0, null));
            } else {
                Toast.makeText(PrintActivity.this, getString(R.string.ptintInit), Toast.LENGTH_LONG).show();
            }
        }
        Intent intent = new Intent(PrintActivity
                .this,
                MainActivity.class);
        startActivity(intent);

        finish();
        //
    }

    private void noPaperDlg() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(PrintActivity.this);
        dlg.setTitle(getString(R.string.noPaper));
        dlg.setMessage(getString(R.string.noPaperNotice));
        dlg.setCancelable(false);
        dlg.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThermalPrinter.stop(PrintActivity.this);
            }
        });
        dlg.show();
    }

    private class paperWalkPrintThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(PrintActivity.this);
                ThermalPrinter.reset();
                ThermalPrinter.walkPaper(paperWalk);
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
                ThermalPrinter.stop(PrintActivity.this);
            }
        }
    }

    private class contentPrintThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(PrintActivity.this);
                ThermalPrinter.reset();
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
                    ThermalPrinter.walkPaper(100);
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
                ThermalPrinter.stop(PrintActivity.this);
            }
        }
    }

    private class MakerThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(PrintActivity.this);
                ThermalPrinter.reset();
                ThermalPrinter.searchMark(Integer.parseInt("50"),
                        Integer.parseInt("40"));
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
                ThermalPrinter.stop(PrintActivity.this);
            }
        }
    }

    private class executeCommand extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(PrintActivity.this);
                ThermalPrinter.reset();
                ThermalPrinter.sendCommand("commandi");
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                    nopaper = true;
                } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                    handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0, null));
                } else {
                    handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0, null));
                }
            } finally {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0, null));
                if (nopaper) {
                    handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0, null));
                    nopaper = false;
                    return;
                }
                ThermalPrinter.stop(PrintActivity.this);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (progressDialog != null && !PrintActivity.this.isFinishing()) {
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
}
