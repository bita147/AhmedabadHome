package senta.nilesh.autocalc.adapters;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.WaterBottleDTO;
import senta.nilesh.autocalc.listeners.BottleListOperationListener;
import senta.nilesh.autocalc.utils.AppPref;

/**
 * Created by "Nilesh Senta" on 4/6/2016.
 */
public class BottleAdapter extends RecyclerView.Adapter<BottleAdapter.DailyAdapterHolder> {

    private ObservableArrayList<WaterBottleDTO> rcList;
    private Context context;
    private BottleListOperationListener listener;

    public BottleAdapter(Context context, ObservableArrayList<WaterBottleDTO> rcList, BottleListOperationListener listener) {
        this.rcList = rcList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public DailyAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_bottle_list, parent, false);
        return new DailyAdapterHolder(root);
    }

    @Override
    public void onBindViewHolder(final DailyAdapterHolder holder, final int position) {
        if (rcList.get(position) == null)
            return;

        holder.tvDate.setText(rcList.get(position).getBuyDate());
        holder.tvInsertedBy.setText(String.valueOf(rcList.get(position).getUserName()));
        holder.tvBottleCount.setText(String.valueOf(rcList.get(position).getBottleCount()));
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.circle);
        drawable.setColorFilter(getCoolor(String.valueOf(rcList.get(position).getBottleCount()).charAt(0)), PorterDuff.Mode.SRC_ATOP);
        holder.tvBottleCount.setBackgroundDrawable(drawable);
        if (rcList.get(position).getUserName().equals(AppPref.get(context).getUserProfileDTO().getUserName())) {
            holder.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.ivDelete.setVisibility(View.GONE);
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onDeleteItemClick(rcList.get(position));
            }
        });

//        if (AppUtils.isTodayDate(rcList.get(position).getKey())) {
//            holder.cvItemRow.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_card_background));
//        } else {
//            holder.cvItemRow.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
//        }
    }

    @Override
    public int getItemCount() {
        return rcList.size();
    }

    class DailyAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvInsertedBy, tvDate, tvBottleCount;

        private ImageView ivDelete;
        private CardView cvItemRow;
        private LinearLayout llMainRow;

        public DailyAdapterHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvInsertedBy = (TextView) itemView.findViewById(R.id.tv_inserted_by);
            tvBottleCount = (TextView) itemView.findViewById(R.id.tv_bottle_count);
            ivDelete = (ImageView) itemView.findViewById(R.id.ic_delete);
            cvItemRow = (CardView) itemView.findViewById(R.id.cv_item_row);
            llMainRow = (LinearLayout) itemView.findViewById(R.id.ll_main_view);
        }
    }

    private int getCoolor(char character) {
        switch (Character.toLowerCase(character)) {
            case '1':
                return ContextCompat.getColor(context, R.color.d);
            case '2':
                return ContextCompat.getColor(context, R.color.r);
            case '3':
                return ContextCompat.getColor(context, R.color.n);
            case '4':
                return ContextCompat.getColor(context, R.color.o);
            case '5':
                return ContextCompat.getColor(context, R.color.p);
            case '6':
                return ContextCompat.getColor(context, R.color.q);
            case '7':
                return ContextCompat.getColor(context, R.color.r);
            case '8':
                return ContextCompat.getColor(context, R.color.s);
            case '9':
                return ContextCompat.getColor(context, R.color.t);
        }
        return 0;
    }
}
