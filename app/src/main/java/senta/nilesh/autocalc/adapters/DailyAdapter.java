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
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.listeners.ListOperationListener;
import senta.nilesh.autocalc.utils.AppPref;
import senta.nilesh.autocalc.utils.AppUtils;

/**
 * Created by "Nilesh Senta" on 4/6/2016.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyAdapterHolder> {

    private ObservableArrayList<ItemDTO> rcList;
    private Context context;
    private ListOperationListener listener;

    public DailyAdapter(Context context, ObservableArrayList<ItemDTO> rcList, ListOperationListener listener) {
        this.rcList = rcList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public DailyAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list, parent, false);
        return new DailyAdapterHolder(root);
    }

    @Override
    public void onBindViewHolder(final DailyAdapterHolder holder, final int position) {
        if (rcList.get(position) == null)
            return;

        holder.tvDesc.setText(rcList.get(position).getItemDesc());
        holder.tvDate.setText(rcList.get(position).getBuyDate());
        holder.tvAmt.setText(String.valueOf(rcList.get(position).getAmt()));
        holder.tvName.setText(String.valueOf(Character.toUpperCase(rcList.get(position).getPayBy().charAt(0))));
        holder.tvUserInclude.setText(rcList.get(position).getUsersIncluded());
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.circle);
        drawable.setColorFilter(getCoolor(rcList.get(position).getPayBy().charAt(0)), PorterDuff.Mode.SRC_ATOP);
        holder.tvName.setBackgroundDrawable(drawable);
        if (rcList.get(position).getUserName().equals(AppPref.get(context).getUserProfileDTO().getUserName())) {
            holder.ivDelete.setVisibility(View.VISIBLE);
//            holder.cvItemRow.setCardElevation(0);
//            holder.cvItemRow.setCardBackgroundColor(Color.parseColor("#FAFAFA"));
        } else {
            holder.ivDelete.setVisibility(View.GONE);
//            holder.cvItemRow.setCardElevation(2);
//            holder.cvItemRow.setCardBackgroundColor(Color.WHITE);
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onDeleteItemClick(rcList.get(position));
            }
        });
        holder.llMainRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(v, rcList.get(position));
            }
        });

        if (AppUtils.isTodayDate(rcList.get(position).getKey())){
            holder.cvItemRow.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_card_background));
        }else{
            holder.cvItemRow.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return rcList.size();
    }

    class DailyAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvDesc, tvDate, tvAmt, tvUserInclude;
        private TextView tvName;
        private ImageView ivDelete;
        private CardView cvItemRow;
        private LinearLayout llMainRow;

        public DailyAdapterHolder(View itemView) {
            super(itemView);

            tvDesc = (TextView) itemView.findViewById(R.id.tv_description);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvAmt = (TextView) itemView.findViewById(R.id.tv_amt);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.ic_delete);
            tvUserInclude = (TextView) itemView.findViewById(R.id.tv_user_included);
            cvItemRow = (CardView) itemView.findViewById(R.id.cv_item_row);
            llMainRow = (LinearLayout) itemView.findViewById(R.id.ll_main_view);
        }
    }

    private int getCoolor(char character) {
        switch (Character.toLowerCase(character)) {
            case 'a':
                return ContextCompat.getColor(context, R.color.a);
            case 'b':
                return ContextCompat.getColor(context, R.color.b);
            case 'c':
                return ContextCompat.getColor(context, R.color.c);
            case 'd':
                return ContextCompat.getColor(context, R.color.d);
            case 'e':
                return ContextCompat.getColor(context, R.color.e);
            case 'f':
                return ContextCompat.getColor(context, R.color.f);
            case 'g':
                return ContextCompat.getColor(context, R.color.g);
            case 'h':
                return ContextCompat.getColor(context, R.color.h);
            case 'i':
                return ContextCompat.getColor(context, R.color.i);
            case 'j':
                return ContextCompat.getColor(context, R.color.j);
            case 'k':
                return ContextCompat.getColor(context, R.color.k);
            case 'l':
                return ContextCompat.getColor(context, R.color.l);
            case 'm':
                return ContextCompat.getColor(context, R.color.m);
            case 'n':
                return ContextCompat.getColor(context, R.color.n);
            case 'o':
                return ContextCompat.getColor(context, R.color.o);
            case 'p':
                return ContextCompat.getColor(context, R.color.p);
            case 'q':
                return ContextCompat.getColor(context, R.color.q);
            case 'r':
                return ContextCompat.getColor(context, R.color.r);
            case 's':
                return ContextCompat.getColor(context, R.color.s);
            case 't':
                return ContextCompat.getColor(context, R.color.t);
            case 'u':
                return ContextCompat.getColor(context, R.color.u);
            case 'v':
                return ContextCompat.getColor(context, R.color.v);
            case 'w':
                return ContextCompat.getColor(context, R.color.w);
            case 'x':
                return ContextCompat.getColor(context, R.color.x);
            case 'y':
                return ContextCompat.getColor(context, R.color.y);
            case 'z':
                return ContextCompat.getColor(context, R.color.z);


        }
        return 0;
    }
}
