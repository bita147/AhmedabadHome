package senta.nilesh.autocalc.activity;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.adapters.ReportAdapter;
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.utils.AppPref;

public class ReportActivity extends AppCompatActivity {
    private ObservableArrayList<ItemDTO> rcvUserList = new ObservableArrayList<>();
    private ObservableArrayList<ItemDTO> rcvList = new ObservableArrayList<>();
    private ReportAdapter adp;
    private String[] userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Report");

        if (getIntent().getExtras() != null) {
            rcvList.addAll((Collection<? extends ItemDTO>) getIntent().getExtras().getSerializable("DATA"));
        }

        RecyclerView rcvDailyList = (RecyclerView) findViewById(R.id.rcv_report_list);
        userName = AppPref.get(this).getUserList().toArray(new String[AppPref.get(this).getUserList().size()]);
        int userSize = AppPref.get(this).getUserList().size();

        addData(userSize);
        calculate();

        adp = new ReportAdapter(this, rcvUserList);

        rcvDailyList.setLayoutManager(new LinearLayoutManager(this));
        rcvDailyList.setAdapter(adp);

    }

    private void addData(int userSize) {
        for (int i = 0; i < userSize; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setUserName(userName[i]);
            rcvUserList.add(itemDTO);
        }
    }

    private void calculate() {
        for (ItemDTO item : rcvList) {
            List<String> users = Arrays.asList(item.getUsersIncluded().split("\\s*,\\s*"));

            for (String s : users) {
                for (ItemDTO u : rcvUserList)
                    if (s.equals(u.getUserName()) && !s.equals(item.getPayBy())) {
                        u.setAmt(u.getAmt() - (item.getAmt() / users.size()));
                    }
            }

            for (ItemDTO u : rcvUserList)
                if (item.getPayBy().equals(u.getUserName())) {
                    if (users.contains(item.getPayBy()))
                        u.setAmt(u.getAmt() + ((item.getAmt() / users.size()) * (users.size() - 1)));
                    else
                        u.setAmt(u.getAmt() + ((item.getAmt() / users.size()) * (users.size())));
                }
        }
    }


//    private ObservableArrayList<ItemDTO> rcvList = new ObservableArrayList<>();
//    private TextView tvN1, tvN2, tvN3, tvN4;
//    private float n1 = 0.0f, n2 = 0.0f, n3 = 0.0f, n4 = 0.0f;
//    private String[] savedList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_report);
//        setTitle("Report");
//
//        if (getIntent().getExtras() != null) {
//            rcvList.addAll((Collection<? extends ItemDTO>) getIntent().getExtras().getSerializable("DATA"));
//        }
//        savedList = AppPref.get(this).getUserList().toArray(new String[AppPref.get(this).getUserList().size()]);
//        tvN1 = (TextView) findViewById(R.id.tv_n1);
//        tvN2 = (TextView) findViewById(R.id.tv_n2);
//        tvN3 = (TextView) findViewById(R.id.tv_n3);
//        tvN4 = (TextView) findViewById(R.id.tv_n4);
//
//        calculate();
//
//        tvN1.setText(savedList[0] + "\n" + new DecimalFormat("##.##").format(n1));
//        tvN2.setText(savedList[1] + "\n" + new DecimalFormat("##.##").format(n2));
//        tvN3.setText(savedList[2] + "\n" + new DecimalFormat("##.##").format(n3));
//        tvN4.setText(savedList[3] + "\n" + new DecimalFormat("##.##").format(n4));
//
//    }
//
//    private void calculate() {
//        for (ItemDTO item : rcvList) {
//            List<String> users = Arrays.asList(item.getUsersIncluded().split("\\s*,\\s*"));
//
//            for (String s : users) {
//                if (s.equals(savedList[0]) && !s.equals(item.getPayBy())) {
//                    n1 -= (item.getAmt() / users.size());
//                } else if (s.equals(savedList[1]) && !s.equals(item.getPayBy())) {
//                    n2 -= (item.getAmt() / users.size());
//                } else if (s.equals(savedList[2]) && !s.equals(item.getPayBy())) {
//                    n3 -= (item.getAmt() / users.size());
//                } else if (s.equals(savedList[3]) && !s.equals(item.getPayBy())) {
//                    n4 -= (item.getAmt() / users.size());
//                }
//            }
//
//            if (item.getPayBy().equals(savedList[0])) {
//                if (users.contains(item.getPayBy()))
//                    n1 += (item.getAmt() / users.size()) * (users.size() - 1);
//                else
//                    n1 += (item.getAmt() / users.size()) * (users.size());
//            } else if (item.getPayBy().equals(savedList[1])) {
//                if (users.contains(item.getPayBy()))
//                    n2 += (item.getAmt() / users.size()) * (users.size() - 1);
//                else
//                    n2 += (item.getAmt() / users.size()) * (users.size());
//            } else if (item.getPayBy().equals(savedList[2])) {
//                if (users.contains(item.getPayBy()))
//                    n3 += (item.getAmt() / users.size()) * (users.size() - 1);
//                else
//                    n3 += (item.getAmt() / users.size()) * (users.size());
//            } else if (item.getPayBy().equals(savedList[3])) {
//                if (users.contains(item.getPayBy()))
//                    n4 += (item.getAmt() / users.size()) * (users.size() - 1);
//                else
//                    n4 += (item.getAmt() / users.size()) * (users.size());
//            }
//        }
//    }
}

