package senta.nilesh.autocalc.activity;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.ItemDTO;

public class ReportActivity extends AppCompatActivity {
    private ObservableArrayList<ItemDTO> rcvList = new ObservableArrayList<>();
    private TextView tvN1, tvN2, tvN3, tvN4;
    private float n1 = 0.0f, n2 = 0.0f, n3 = 0.0f, n4 = 0.0f;
    private String[] savedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Report");

        if (getIntent().getExtras() != null) {
            rcvList.addAll((Collection<? extends ItemDTO>) getIntent().getExtras().getSerializable("DATA"));
        }
        savedList = getResources().getStringArray(R.array.user_array);
        tvN1 = (TextView) findViewById(R.id.tv_n1);
        tvN2 = (TextView) findViewById(R.id.tv_n2);
        tvN3 = (TextView) findViewById(R.id.tv_n3);
        tvN4 = (TextView) findViewById(R.id.tv_n4);

        calculate();

        tvN1.setText(savedList[0] + "\n" + new DecimalFormat("##.##").format(n1));
        tvN2.setText(savedList[1] + "\n" + new DecimalFormat("##.##").format(n2));
        tvN3.setText(savedList[2] + "\n" + new DecimalFormat("##.##").format(n3));
        tvN4.setText(savedList[3] + "\n" + new DecimalFormat("##.##").format(n4));

    }

    private void calculate() {
        for (ItemDTO item : rcvList) {
            List<String> users = Arrays.asList(item.getUsersIncluded().split("\\s*,\\s*"));

            for (String s : users) {
                if (s.equals(savedList[0]) && !s.equals(item.getPayBy())) {
                    n1 -= (item.getAmt() / users.size());
                } else if (s.equals(savedList[1]) && !s.equals(item.getPayBy())) {
                    n2 -= (item.getAmt() / users.size());
                } else if (s.equals(savedList[2]) && !s.equals(item.getPayBy())) {
                    n3 -= (item.getAmt() / users.size());
                } else if (s.equals(savedList[3]) && !s.equals(item.getPayBy())) {
                    n4 -= (item.getAmt() / users.size());
                }
            }

            if (item.getPayBy().equals(savedList[0])) {
                if (users.contains(item.getPayBy()))
                    n1 += (item.getAmt() / users.size()) * (users.size() - 1);
                else
                    n1 += (item.getAmt() / users.size()) * (users.size());
            } else if (item.getPayBy().equals(savedList[1])) {
                if (users.contains(item.getPayBy()))
                    n2 += (item.getAmt() / users.size()) * (users.size() - 1);
                else
                    n2 += (item.getAmt() / users.size()) * (users.size());
            } else if (item.getPayBy().equals(savedList[2])) {
                if (users.contains(item.getPayBy()))
                    n3 += (item.getAmt() / users.size()) * (users.size() - 1);
                else
                    n3 += (item.getAmt() / users.size()) * (users.size());
            } else if (item.getPayBy().equals(savedList[3])) {
                if (users.contains(item.getPayBy()))
                    n4 += (item.getAmt() / users.size()) * (users.size() - 1);
                else
                    n4 += (item.getAmt() / users.size()) * (users.size());
            }
        }
    }
}

