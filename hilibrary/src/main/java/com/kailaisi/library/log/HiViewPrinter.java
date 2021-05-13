package com.kailaisi.library.log;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kailaisi.library.R;

import java.util.ArrayList;
import java.util.List;

import static com.kailaisi.library.log.HiLogConfig.MAX_LEN;

/**
 * 描述：在应用中的打印信息，跟踪trace
 * <p/>作者：wu
 * <br/>创建时间：2021-05-12:20:40
 */
public class HiViewPrinter implements HiLogPrinter {
    private RecyclerView recyclerView;
    private LogAdapter adapter;

    private HiViewPrinterProvider printerProvider;

    public HiViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        recyclerView = new RecyclerView(rootView.getContext());
        adapter = new LogAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        printerProvider = new HiViewPrinterProvider(rootView, recyclerView);
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String msg) {
        adapter.addItem(new HiLogMo(System.currentTimeMillis(),level,tag,msg));
        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    /**
     * 获取控制面板
     */
    @NonNull
    public HiViewPrinterProvider getPrinterProvider() {
        return printerProvider;
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private List<HiLogMo> logs = new ArrayList<>();

        void addItem(HiLogMo item) {
            logs.add(item);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hilog, parent, false);
            return new LogViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            HiLogMo item = logs.get(position);
            int color = getHighLightColor(item.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);

            holder.tagView.setText(item.getFlattened());
            holder.messageView.setText(item.log);
        }

        private int getHighLightColor(int level) {
            switch (level) {
                case HiLogType.V:
                    return 0xffbbbbbb;
                case HiLogType.D:
                    return Color.WHITE;
                case HiLogType.I:
                    return 0xff6a8759;
                case HiLogType.W:
                    return 0xffbbb529;
                case HiLogType.E:
                    return 0xffff6b68;
                default:
                    return 0xffffff00;
            }
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tagView;
        TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tv_tag);
            messageView = itemView.findViewById(R.id.tv_message);
        }
    }
}
