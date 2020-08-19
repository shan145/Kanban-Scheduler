package com.example.kanbanscheduler.adapters;

import android.content.Context;
import android.graphics.Color;
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
    private int[] colorArray;

    public TopicGridAdapter(Context context) {
        this.mInflator = LayoutInflater.from(context);
        this.colorArray = context.getResources().getIntArray(R.array.rainbow);
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
        holder.bindTo(mCurrent, position);
    }

    @Override
    public int getItemCount() {
        if(mTopicList != null) return mTopicList.size();
        return 0;
    }

    public boolean inTopicList(String string) {
        for(int i = 0; i < mTopicList.size(); i++) {
            if(mTopicList.get(i).getTopicName().equals(string)) {
                return true;
            }
        }
        return false;
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

        void bindTo(Topic currentTopic, int position) {
            mTopicName.setText(currentTopic.getTopicName());
            mTopicName.setBackgroundColor(colorArray[position%colorArray.length]);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClicked(getAdapterPosition(), view.getContext());
        }
    }
}
