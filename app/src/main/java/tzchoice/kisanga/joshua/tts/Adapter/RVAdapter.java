package tzchoice.kisanga.joshua.tts.Adapter;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tzchoice.kisanga.joshua.tts.Pojo.Tp;
import tzchoice.kisanga.joshua.tts.R;

public class RVAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Tp> mTransitPasses;
    private List<Tp> mOrignTransitPasses;



    public RVAdapter(List<Tp> mTransitPasses) {
        this.mTransitPasses = mTransitPasses;
        this.mOrignTransitPasses = mTransitPasses;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        final Tp model = mTransitPasses.get(i);
        itemViewHolder.bind(model);

    }

    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tp_list_row, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mTransitPasses.size();
    }

    public void setFilter(List<Tp> transitPasses){
        mTransitPasses = new ArrayList<>();
        mTransitPasses.addAll(transitPasses);
        notifyDataSetChanged();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.product_detail:

                    Log.d("onMenuItemClick", "onMenuItemClick: " + position);
                    productDetail();
                    return true;
                case R.id.route_checkpoint:

                    return true;
                case R.id.report_change:

                default:
            }
            return false;
        }

        private void  productDetail() {

        }


    }



}
