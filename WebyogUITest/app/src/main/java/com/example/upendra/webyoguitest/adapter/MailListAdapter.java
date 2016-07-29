package com.example.upendra.webyoguitest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upendra.webyoguitest.R;
import com.example.upendra.webyoguitest.model.MailListItem;

import java.util.Collections;
import java.util.List;

public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.MyViewHolder> {
    List<MailListItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public MailListAdapter(Context context, List<MailListItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mail_item_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MailListItem current = data.get(position);
        holder.mailTitle.setText(current.getMailTitle());
        holder.mailSubject.setText(current.getMailSubject());
        holder.mailBody.setText(current.getMailBody());
        if (current.getMailStar()) {
            holder.starredView.setImageResource(R.drawable.starred);
        } else {
            holder.starredView.setImageResource(R.drawable.unstarred);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mailTitle;
        TextView mailSubject;
        TextView mailBody;
        ImageView starredView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mailTitle = (TextView) itemView.findViewById(R.id.mailTitle);
            mailBody = (TextView) itemView.findViewById(R.id.mailBody);
            mailSubject = (TextView) itemView.findViewById(R.id.mailSubject);
            starredView = (ImageView) itemView.findViewById(R.id.mailStar);
        }
    }
}
