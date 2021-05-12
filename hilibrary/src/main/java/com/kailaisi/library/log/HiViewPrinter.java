package com.kailaisi.library.log;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kailaisi.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：在应用中的打印信息，跟踪trace
 * <p/>作者：wu
 * <br/>创建时间：2021-05-12:20:40
 */
public class HiViewPrinter implements HiLogPrinter {
    private RecyclerView recyclerView;
    private LogAdapter adapter;

    public HiViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        // TODO: 2021-05-12
        this.recyclerView = new RecyclerView(rootView.getContext());
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String msg) {

    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private List<HiLogMo> logs = new ArrayList<>();

        void addItem(HiLogMo item) {
            logs.add(item);
            notifyItemInserted(logs.size() - 1);
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
            int color=getHighLightColor(item.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);

            holder.tagView.setText(item.getFlattened());
            holder.messageView.setText(item.log);
        }

        private int getHighLightColor(int level) {
            switch (level) {
                case HiLogType.V:
                    return 0xbbbbbb;
                case HiLogType.D:
                    return 0xffffff;
                case HiLogType.I:
                    return 0x6a8759;
                case HiLogType.W:
                    return 0xbbb529;
                case HiLogType.E:
                    return 0xff6b68;
                default:
                    return 0xffff00;
            }
        }

        @Override
        public int getItemCount() {
            return 0;
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
