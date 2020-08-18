package com.example.kanbanscheduler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.room_db.Topic;


import java.util.ArrayList;
import java.util.List;

public class TopicGridAdapter extends RecyclerView.Adapter<TopicGridAdapter.TopicViewHolder> {
    private ArrayList<Topic> mTopicList;
    private LayoutInflater mInflator;

    public TopicGridAdapter(Context context, ArrayList<Topic> topicData) {
        this.mInflator = LayoutInflater.from(context);
        this.mTopicList=topicData;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mTopicView = mInflator.inflate(R.layout.topic_item, parent, false);
        return new TopicViewHolder(mTopicView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic mCurrent = mTopicList.get(position);
        holder.bindTo(mCurrent);
    }

    @Override
    public int getItemCount() {
        if(mTopicList != null) return mTopicList.size();
        return 0;
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView mTopicName;
        public TopicViewHolder(View itemView, TopicGridAdapter adapter) {
            super(itemView);
            mTopicName = itemView.findViewById(R.id.topic);
        }
        void bindTo(Topic currentTopic) {
            mTopicName.setText(currentTopic.getTopicName());
        }
    }
}
