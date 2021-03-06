package com.example.upendra.webyoguitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.upendra.webyoguitest.Utils.DividerItemDecoration;
import com.example.upendra.webyoguitest.activities.ComposeMailActivity;
import com.example.upendra.webyoguitest.activities.DetailedMailActivity;
import com.example.upendra.webyoguitest.adapter.MailListAdapter;
import com.example.upendra.webyoguitest.model.MailListItem;
import com.example.upendra.webyoguitest.constants.URLConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment implements URLConstants {

    private RecyclerView recyclerView;
    private MailListAdapter adapter;
    private JSONArray jsonMailData = null;
    private List<MailListItem> mailListItems;
    private String mailData;

    public InboxFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mailData = getArguments().getString("InboxMails");
        View rootView = inflater.inflate(R.layout.fragment_mail_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.mailList);
        adapter = new MailListAdapter(getActivity(), getData(mailData));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mailListItems = getData(mailData);
                Intent myIntent = new Intent(getActivity(), DetailedMailActivity.class);
                myIntent.putExtra("mailID", mailListItems.get(position).getMailID());
                getActivity().startActivity(myIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                adapter.notifyItemChanged(position);
                position = recyclerView.getChildLayoutPosition(view);
                adapter.notifyItemChanged(position);
                Toast.makeText(getContext(), "LONG PRESSED", Toast.LENGTH_SHORT).show();
            }
        }));
        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        assert floatingActionButton != null;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ComposeMailActivity.class);
                getActivity().startActivity(myIntent);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private List<MailListItem> getData(String mailsData) {
        List<MailListItem> data = new ArrayList<>();
        // preparing navigation drawer items
        try {
            jsonMailData = new JSONArray(mailsData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < jsonMailData.length(); i++) {
            try {
                jsonObject = jsonMailData.getJSONObject(i);
                MailListItem mailItem = new MailListItem();
                mailItem.setMailTitle(jsonObject.getString("subject"));
                mailItem.setMailSubject(jsonObject.getString("ts"));
                mailItem.setMailBody(jsonObject.getString("preview"));
                mailItem.setMailID(jsonObject.getString("id"));
                if (jsonObject.getBoolean("isStarred")) {
                    mailItem.setMailStar(true);
                } else {
                    mailItem.setMailStar(false);
                }
                data.add(mailItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

}
