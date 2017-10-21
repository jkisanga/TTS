package tzchoice.kisanga.joshua.tts.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.tts.Pojo.TransitPass;
import tzchoice.kisanga.joshua.tts.R;

/**
 * Created by user on 2/27/2017.
 */

public class TPAdapter extends RecyclerView.Adapter<TPAdapter.MyViewHolder> {

    private List<TransitPass> transitPasses;
    private List<TransitPass> mOriginalTransitPasses;

        public TPAdapter(List<TransitPass> transitPasses) {
            this.transitPasses = transitPasses;
            this.mOriginalTransitPasses = transitPasses;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_list_row, parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final TransitPass transitPass = transitPasses.get(position);

//            holder.TPNo.setText("TP No : " + transitPass.getTp_no());
//            holder.product.setText("Product : " + transitPass.getProduct());
//            holder.dateOfIssue.setText("Issue on : " + transitPass.getDate_of_issued());
//            holder.expiryDate.setText("Expiry On : " + transitPass.getExpity_date());
//            holder.formDistrict.setText("From : " + transitPass.getFrom_district());
//            holder.toDistrict.setText("To : " + transitPass.getTo_district());
//            holder.quantity.setText("QTY : " + transitPass.getQuantity().toString());
//            holder.status.setText("Status : " + transitPass.getStatus());
//            holder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Context context = view.getContext();
//                    Intent i = new Intent(context, CheckpointActivity.class);
//                    i.putExtra(Constant.KEY_TPID, transitPass.getId());
//                    i.putExtra(Constant.KEY_TPNO, transitPass.getTp_no());
//                    i.putExtra(Constant.KEY_PRODUCT, transitPass.getProduct());
//
//                    context.startActivity(i);
//                }
//            });


        }



        @Override
        public int getItemCount() {
            return transitPasses.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView TPNo, dateOfIssue, expiryDate,formDistrict,toDistrict,product, quantity, status;
            CardView cardView;
            public MyViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.tp_cardView);
//                TPNo = (TextView) itemView.findViewById(R.id.tp_number);
//                dateOfIssue = (TextView) itemView.findViewById(R.id.date_of_issue);
//                expiryDate = (TextView) itemView.findViewById(R.id.expiry_date);
//                formDistrict = (TextView) itemView.findViewById(R.id.txt_from);
//                toDistrict = (TextView) itemView.findViewById(R.id.txt_to);
//                status = (TextView) itemView.findViewById(R.id.txt_tp_status);
            }
            public void bind(TransitPass transitPass) {
//                TPNo.setText(transitPass.getTp_no());

            }
        }
    public void setFilter(List<TransitPass> transitPasses){
        mOriginalTransitPasses = new ArrayList<>();
        mOriginalTransitPasses.addAll(transitPasses);
        notifyDataSetChanged();
    }
}
