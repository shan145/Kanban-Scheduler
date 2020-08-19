package com.example.kanbanscheduler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.room_db.Topic;

import java.util.List;

public class TopicGridAdapter extends RecyclerView.Adapter<TopicGridAdapter.TopicViewHolder> {
    private List<Topic> mTopicList;
    private LayoutInflater mInflator;
    private ClickListener mClickListener;

    public TopicGridAdapter(Context context) {
        this.mInflator = LayoutInflater.from(context);
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

    public Topic getTopicAtPosition(int position) { return mTopicList.get(position); }
    public void setTopics(List<Topic> newTopicList) {
        mTopicList = newTopicList;
        notifyDataSetChanged();
    }

    /*
     * Interface to allow calling of edit button in Fragments
     */
    public interface ClickListener {
        void onClicked(int pos, Context context);
    }
    public void setClickListener (TopicGridAdapter.ClickListener listener){
        mClickListener = listener;
    }

    class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTopicName;
        public TopicViewHolder(View itemView, TopicGridAdapter adapter) {
            super(itemView);
            mTopicName = itemView.findViewById(R.id.topic);
            itemView.setOnClickListener(this);
        }

        void bindTo(Topic currentTopic) {
            mTopicName.setText(currentTopic.getTopicName());
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClicked(getAdapterPosition(), view.getContext());
        }
    }
}
