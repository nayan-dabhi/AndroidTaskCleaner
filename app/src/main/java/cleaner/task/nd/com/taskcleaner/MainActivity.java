package cleaner.task.nd.com.taskcleaner;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Activity mActivity;
    Context mContext;
    ActivityManager actvityManager;

    SwipeRefreshLayout mSwipeRefresh;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    List<ActivityManager.RunningAppProcessInfo> mListProcesses;
    DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActivity = this;
        mContext = this.getApplicationContext();
        mListProcesses = new ArrayList<>();

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProcessData();
            }
        });

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        // Log.e("processName ", mListProcesses.get(position).processName);
                    }

                    public void onLongClickItem(View v, int position) {
                        try {
                            Log.e("processName ", mListProcesses.get(position).processName);

                            android.os.Process.killProcess(mListProcesses.get(position).pid);
                            android.os.Process.sendSignal(mListProcesses.get(position).pid, android.os.Process.SIGNAL_KILL);
                            actvityManager.killBackgroundProcesses(mListProcesses.get(position).processName);

                            loadProcessData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadProcessData();
    }

    private void loadProcessData() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }

        // using Activity service to list all process
        actvityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        // list all running process
        mListProcesses = actvityManager.getRunningAppProcesses();

        mAdapter = new DataAdapter(mListProcesses);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
