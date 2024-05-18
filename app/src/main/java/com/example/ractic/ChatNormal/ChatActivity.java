package com.example.ractic.ChatNormal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ractic.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    EditText editTextNmae;
    FloatingActionButton button;
    ListView listView;
    DatabaseReference db;
    DatabaseReference messagesReference;
    ArrayList<Message> messages;
    ArrayAdapter<Message> adapter;
    AlertDialog deleteDialog;
    AlertDialog editDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messages = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference();
        messagesReference = db.child("messages");
        defineViews();
        adapter = new ChatAdapter(this,messages);
        listView.setAdapter(adapter);
        addingFunc();
        readingFunc();
        deletingFunc();
        editingFunc();
    }
    private void addingFunc() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    String key = messagesReference.push().getKey();
                    Message message = new Message(editText.getText().toString(),editTextNmae.getText().toString());
                    message.setKey(key);
                    messagesReference.child(key).setValue(message);
                    editText.getText().clear();
                }
            }
        });
    }
    private void readingFunc() {
        messagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    Message message = snapshot1.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void deletingFunc() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(ChatActivity.this);
                Message pickedMsg = messages.get(position);
                deleteDialog.setTitle("вы точно хотите удалить сообщение?");
                deleteDialog.setMessage(messages.get(position).getMessage());
                deleteDialog.setPositiveButton("удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messagesReference.child(pickedMsg.getKey()).removeValue();
                        messages.remove(pickedMsg);
                        adapter.notifyDataSetChanged();

                    }
                });
                deleteDialog.setNegativeButton("отмена",null);
                AlertDialog deleteAlertDIalog = deleteDialog.create();
                deleteAlertDIalog.show();
                return false;
            }
        });
    }
    private void editingFunc() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message messageForGettingInfo = messages.get(position);
                AlertDialog.Builder editingMsg = new AlertDialog.Builder(ChatActivity.this);
                View viewForEdit = getLayoutInflater().inflate(R.layout.editing_msg,null);
                EditText textForNewName = viewForEdit.findViewById(R.id.titleFOrEditDIalog);
                EditText textForNewMsg = viewForEdit.findViewById(R.id.editTextForText);
                Button buttonForExit = viewForEdit.findViewById(R.id.buttonForExitDIA);
                Button buttonForComp = viewForEdit.findViewById(R.id.buttonForCompDIA);
                textForNewName.setText(messageForGettingInfo.getName());
                textForNewMsg.setText(messageForGettingInfo.getMessage());
                buttonForComp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messagesReference.child(messageForGettingInfo.getKey()).child("name").setValue(textForNewName.getText().toString());
                        messagesReference.child(messageForGettingInfo.getKey()).child("message").setValue(textForNewMsg.getText().toString());
                        editDialog.dismiss();
                    }
                });
                buttonForExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
                editingMsg.setView(viewForEdit);
                editDialog = editingMsg.create();
                editDialog.show();
            }
        });
    }

    private void defineViews() {
        editText = findViewById(R.id.EditTextForMsg);
        button = findViewById(R.id.ButtonForChat);
        listView = findViewById(R.id.ListViewForChat);
        editTextNmae = findViewById(R.id.EditTextForName);
    }
}