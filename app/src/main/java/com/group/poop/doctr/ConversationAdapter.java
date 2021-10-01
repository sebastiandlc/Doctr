package com.group.poop.doctr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

//used to populate conversation_list
public class ConversationAdapter extends ArrayAdapter<ConversationListFiller> {

    public ConversationAdapter(Context context, ArrayList<ConversationListFiller> metaData) {
        super(context, R.layout.conversation, metaData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ConversationListFiller metaData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.conversation, parent, false);
        }

        TextView user = (TextView) convertView.findViewById(R.id.conversation_user);
        TextView time = (TextView) convertView.findViewById(R.id.conversation_time);
        TextView message = (TextView) convertView.findViewById(R.id.conversation_last_message);
        if(metaData.last != null){
            user.setText(metaData.nameToShow);
            time.setText(new Date(metaData.last.getTime()).toString());
            message.setText(metaData.last.getContent());

        }

        return convertView;
    }
}
