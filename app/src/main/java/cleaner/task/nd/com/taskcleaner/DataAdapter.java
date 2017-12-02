package cleaner.task.nd.com.taskcleaner;

import android.app.ActivityManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.RowViewHolder> {
    private List<ActivityManager.RunningAppProcessInfo> mDataList;

    public class RowViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public RowViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
        }
    }

    public DataAdapter(List<ActivityManager.RunningAppProcessInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        ActivityManager.RunningAppProcessInfo item = mDataList.get(position);

        holder.name.setText(item.processName);
    }

}
