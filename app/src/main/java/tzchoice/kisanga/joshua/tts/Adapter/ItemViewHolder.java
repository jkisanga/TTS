package tzchoice.kisanga.joshua.tts.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import tzchoice.kisanga.joshua.tts.Constant;
import tzchoice.kisanga.joshua.tts.ResultActivity;
import tzchoice.kisanga.joshua.tts.Pojo.Tp;
import tzchoice.kisanga.joshua.tts.ProductDetailActivity;
import tzchoice.kisanga.joshua.tts.R;
import tzchoice.kisanga.joshua.tts.RouteActivity;

public class ItemViewHolder extends RecyclerView.ViewHolder {

   public TextView listTPNo, listDateOfIssue, listExpiryDate,product, listStatus,
           listTpIssuedTo, listTpLicenseNo,listTpBillNo, listTpForm,
           listTpTo, listTpVehicleNo,
           listOfficer,listOfficerPhone,listForest,listHummerNo, listTpDesc;
    public CardView cardView;
    public FloatingActionButton btnProductDetail, btnRouteDetail, btnCargoIssue;

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        cardView = (CardView) itemView.findViewById(R.id.tp_cardView);
        listTpIssuedTo = (TextView) itemView.findViewById(R.id.list_tp_issued_to);
        listTPNo = (TextView) itemView.findViewById(R.id.list_tp_number);
        listTpLicenseNo = (TextView) itemView.findViewById(R.id.list_license_no);
        listTpBillNo = (TextView) itemView.findViewById(R.id.list_bill_no);
        listTpForm = (TextView) itemView.findViewById(R.id.list_from_village);
        listTpTo = (TextView) itemView.findViewById(R.id.list_to_village);
        listTpVehicleNo = (TextView) itemView.findViewById(R.id.list_vehicle_reg_no);
        listDateOfIssue = (TextView) itemView.findViewById(R.id.list_date_of_issue);
        listExpiryDate = (TextView) itemView.findViewById(R.id.list_expiry_date);
        listOfficer = (TextView) itemView.findViewById(R.id.list_officer);
        listOfficerPhone = (TextView) itemView.findViewById(R.id.list_officer_phone);
        listForest = (TextView) itemView.findViewById(R.id.list_forest);
        listHummerNo = (TextView) itemView.findViewById(R.id.list_hummer_no);
        listTpDesc = (TextView) itemView.findViewById(R.id.list_tp_desc);
        btnProductDetail = (FloatingActionButton) itemView.findViewById(R.id.btn_product_detail);
        btnRouteDetail = (FloatingActionButton) itemView.findViewById(R.id.btn_checkpoint_route);
        btnCargoIssue = (FloatingActionButton) itemView.findViewById(R.id.btn_cargo_issue);
        btnProductDetail.setLabelText("product detail");
      //  btnProductDetail.setColorNormal();

    }


    public void bind(final Tp transitPass) {
        listTPNo.setText("TP No : " + transitPass.getTpNo());
        listTpIssuedTo.setText("Issued To : " + transitPass.getIssuedTo());
        listTpLicenseNo.setText("License No : " + transitPass.getLicenseNo());
        listTpBillNo.setText("Bill No : " + transitPass.getBillNo());
        listTpForm.setText("Source : " + transitPass.getFrom());
        listTpTo.setText("Destination : " + transitPass.getTo());
        listTpVehicleNo.setText("Vehicle No : " + transitPass.getVehicleRegNo());
        listDateOfIssue.setText("Issued on : " + transitPass.getDateOfIssued());
        listExpiryDate.setText("Expiry on : " + transitPass.getExpiryDate());
        listOfficer.setText("Issuer Officer : " + transitPass.getOfficer());
        listOfficerPhone.setText("Officer No : " + transitPass.getOfficerPhoneNo());
        listForest.setText("Source Forest : " + transitPass.getForest());
        listHummerNo.setText("Hummer No : " + transitPass.getHummerNo());
       // listStatus.setText("TP Status : " + transitPass.getStatus());
        listTpDesc.setText("More Desc : " + transitPass.getDesc());

//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Context context = view.getContext();
//                Intent i = new Intent(context, InspectionActivity.class);
//                i.putExtra(Constant.KEY_TPID, transitPass.getId());
//                i.putExtra(Constant.KEY_TPNO, transitPass.getTpNo());
//                context.startActivity(i);
//            }
//        });

        btnProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra(Constant.KEY_TPID, transitPass.getId());
                i.putExtra(Constant.KEY_TPNO, transitPass.getTpNo());
                i.putExtra(Constant.KEY_CLIENT, transitPass.getIssuedTo());
                context.startActivity(i);
            }
        });

        btnRouteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent i = new Intent(context, RouteActivity.class);
                i.putExtra(Constant.KEY_TPID, transitPass.getId());
                i.putExtra(Constant.KEY_TPNO, transitPass.getTpNo());
                i.putExtra(Constant.KEY_CLIENT, transitPass.getIssuedTo());
                context.startActivity(i);
            }
        });

        btnCargoIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent i = new Intent(context, ResultActivity.class);
                i.putExtra(Constant.KEY_TPID, transitPass.getId());
                i.putExtra(Constant.KEY_TPNO, transitPass.getTpNo());
                i.putExtra(Constant.KEY_CLIENT, transitPass.getIssuedTo());
                context.startActivity(i);
            }
        });


    }




    }

