package senta.nilesh.autocalc.adapters;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.utils.AppUtils;

/**
 * Created by "Nilesh Senta" on 4/6/2016.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportAdapterHolder> {

    private ObservableArrayList<ItemDTO> rcList;
    private Context context;

    public ReportAdapter(Context context, ObservableArrayList<ItemDTO> rcList) {
        this.rcList = rcList;
        this.context = context;
    }

    @Override
    public ReportAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_list, parent, false);
        return new ReportAdapterHolder(root);
    }

    @Override
    public void onBindViewHolder(ReportAdapterHolder holder, int position) {
           if (rcList.get(position) == null)
            return;

        holder.tvAmt.setText(String.valueOf(new DecimalFormat("##.##").format(rcList.get(position).getAmt())));
        if (rcList.get(position).getAmt() < 0){
            holder.tvAmt.setTextColor(Color.parseColor("#F44336"));
        }else
            holder.tvAmt.setTextColor(Color.parseColor("#4CAF50"));

        holder.tvName.setText(String.valueOf(Character.toUpperCase(rcList.get(position).getUserName().charAt(0))));
        holder.tvUserName.setText(rcList.get(position).getUserName());
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.circle);
        drawable.setColorFilter(getCoolor(rcList.get(position).getUserName().charAt(0)), PorterDuff.Mode.SRC_ATOP);
        holder.tvName.setBackgroundDrawable(drawable);


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

    class ReportAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvUserName, tvAmt;
        private CardView cvItemRow;
        private LinearLayout llMainRow;

        public ReportAdapterHolder(View itemView) {
            super(itemView);

            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
            tvAmt = (TextView) itemView.findViewById(R.id.tv_amt);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
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
