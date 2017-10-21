package tzchoice.kisanga.joshua.tts.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tzchoice.kisanga.joshua.tts.Pojo.TpInspection;
import tzchoice.kisanga.joshua.tts.R;

/**
 * Created by user on 3/26/2017.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    List<TpInspection> tpInspections;

    public ResultAdapter(List<TpInspection> tpInspections) {
        this.tpInspections = tpInspections;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_result_list_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TpInspection tpInspection = tpInspections.get(position);
        holder.checkpointName.setText("CP Name : " + tpInspection.getCheckpointName());
        holder.Status.setText("Status : " + tpInspection.getStatus());
        holder.inspectedBy.setText("Inpected By : " + tpInspection.getInspectedBy());
        holder.textTrregularity.setText("Irregularity : " + tpInspection.getIrregularity());
        holder.product.setText("Product : " + tpInspection.getProduct());
        holder.quantity.setText("Quantity : " + tpInspection.getQuantity());
        holder.unit.setText("Unit : " + tpInspection.getUnit());
        holder.value.setText("Value : " + tpInspection.getValue());
        holder.action.setText("Action : " + tpInspection.getAction());
        holder.action_amount.setText("Amount : " + tpInspection.getActionAmount());
        holder.desc.setText("More Desc : " + tpInspection.getDesc());
        holder.created_at.setText("Inspected on : " + tpInspection.getCreatedAt() );

    }


    @Override
    public int getItemCount() {
        return tpInspections.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView checkpointName, textTrregularity, product,quantity,unit,value,action,action_amount,receipt_no,Status,inspectedBy,
                desc, created_at;

        public MyViewHolder(View itemView) {
            super(itemView);
            checkpointName = (TextView) itemView.findViewById(R.id.r_checkpoint_name);
            Status = (TextView) itemView.findViewById(R.id.r_status);
            inspectedBy = (TextView) itemView.findViewById(R.id.r_inspected_by);
            textTrregularity = (TextView) itemView.findViewById(R.id.re_irregularity);
            product = (TextView) itemView.findViewById(R.id.r_product);
            quantity = (TextView) itemView.findViewById(R.id.r_quantity);
            unit = (TextView) itemView.findViewById(R.id.r_unit);
            value = (TextView) itemView.findViewById(R.id.r_value);
            action = (TextView) itemView.findViewById(R.id.r_action);
            action_amount = (TextView) itemView.findViewById(R.id.r_action_amount);
            desc = (TextView) itemView.findViewById(R.id.r_desc);
            created_at = (TextView) itemView.findViewById(R.id.r_created_at);


        }
    }
}
