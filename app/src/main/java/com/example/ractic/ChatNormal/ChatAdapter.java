package com.example.ractic.ChatNormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ractic.ChatNormal.Message;
import com.example.ractic.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ChatAdapter extends ArrayAdapter<Message> {
    Context context;
    ArrayList<Message> messages;
    View v;
    View viewForEdit;
    public ChatAdapter(@NonNull Context context, ArrayList<Message> messages) {
        super(context, R.layout.item_message,messages);
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = this.messages.get(position);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        v = inflater.inflate(R.layout.item_message, null, false);
        TextView textViewName =v.findViewById(R.id.textViewforName);
        TextView textViewMsg =v.findViewById(R.id.textViewforMsg);

        textViewName.setText(message.getName());
        textViewMsg.setText(message.getMessage());


        return v;
    }


}
