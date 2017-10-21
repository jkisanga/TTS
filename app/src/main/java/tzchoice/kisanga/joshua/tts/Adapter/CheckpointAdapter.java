package tzchoice.kisanga.joshua.tts.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tzchoice.kisanga.joshua.tts.Pojo.Checkpoint;
import tzchoice.kisanga.joshua.tts.R;

/**
 * Created by user on 2/28/2017.
 */

public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.MyViewHolder> {

        List<Checkpoint> checkpoints;

public CheckpointAdapter(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
        }

@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_route_list_row, parent,false);

        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(MyViewHolder holder, int position) {
final Checkpoint checkpoint = checkpoints.get(position);
        holder.checkpointName.setText("CP Name : " + checkpoint.getName());
        holder.checkpointStatus.setText("Status : " + checkpoint.getStatus());
        holder.inspectedBy.setText("Inpected By : " + checkpoint.getInspectedBy());

        }



@Override
public int getItemCount() {
        return checkpoints.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView checkpointName, checkpointStatus, inspectedBy;
    LinearLayout linearLayout;
    public MyViewHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.route_list_row);
        checkpointName = (TextView) itemView.findViewById(R.id.txt_checkpoint_name);
        checkpointStatus = (TextView) itemView.findViewById(R.id.txt_checkpoint_status);
        inspectedBy = (TextView) itemView.findViewById(R.id.txt_inspected_by);


    }
}
}

