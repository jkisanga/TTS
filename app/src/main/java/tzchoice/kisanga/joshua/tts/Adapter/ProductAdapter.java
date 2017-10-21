package tzchoice.kisanga.joshua.tts.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tzchoice.kisanga.joshua.tts.Pojo.Product;
import tzchoice.kisanga.joshua.tts.R;

/**
 * Created by user on 3/20/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tp_product_list_row, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.txtProduct.setText("Product : " + product.getName());
        holder.txtSpecias.setText("Species : " + product.getScientificName());
        holder.txtQuanity.setText("Quantity : " + product.getQuantity());
        holder.txtUnit.setText("Unit : " + product.getUnit());

    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtProduct, txtSpecias, txtQuanity, txtUnit;
        LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.product_list_row);
            txtProduct = (TextView) itemView.findViewById(R.id.txt_product);
            txtSpecias = (TextView) itemView.findViewById(R.id.txt_species);
            txtQuanity = (TextView) itemView.findViewById(R.id.txt_quanity);
            txtUnit = (TextView) itemView.findViewById(R.id.txt_unit);


        }
    }

}
