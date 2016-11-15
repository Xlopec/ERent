package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.presenter.model.MessageModel;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.SpaceDecorator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.graphics.Color.WHITE;

public final class ChatActivity extends AppCompatActivity {

    private RecyclerView messagesContainer;
    private ActionMenuView chatMenu;

    private final Adapter adapter;

    private static final class MessageHolder extends RecyclerView.ViewHolder {

        private final TextView timestampTextView;
        private final TextView bodyTextView;
        private final Space space;

        MessageHolder(View itemView) {
            super(itemView);
            timestampTextView = (TextView) itemView.findViewById(R.id.message_timestamp);
            bodyTextView = (TextView) itemView.findViewById(R.id.message_body);
            space = (Space) itemView.findViewById(R.id.message_spacing);
        }

        void setDirection(MessageModel.Direction direction) {

            final View holder = itemView.findViewById(R.id.message_body_holder);
            final Context context = holder.getContext();

            if (direction == MessageModel.Direction.RIGHT) {
                bodyTextView.setTextColor(WHITE);
                space.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                );
                holder.setBackgroundResource(R.drawable.chat_message_bg_blue);

            } else if (direction == MessageModel.Direction.LEFT) {
                bodyTextView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextB));
                space.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 0f)
                );
                holder.setBackgroundResource(R.drawable.chat_message_bg_gray);

            } else throw new IllegalArgumentException(
                    String.format("Illegal message direction, was %s", direction));
        }

        void setTimestamp(String timestamp) {
            timestampTextView.setText(timestamp);
        }

        void setBody(String body) {
            bodyTextView.setText(body);
        }

    }

    private class Adapter extends RecyclerView.Adapter<MessageHolder> {

        private final ArrayList<MessageModel> content;

        Adapter() {
            content = new ArrayList<>();
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MessageHolder(LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_message, parent, false));
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {

            final MessageModel model = content.get(position);

            holder.setTimestamp(model.getTimestamp());
            holder.setBody(model.getBody());
            holder.setDirection(model.getDirection());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        void addItem(MessageModel model) {
            content.add(model);
        }

        void addItem(@NotNull Collection<MessageModel> models) {
            content.addAll(models);
        }

    }

    public ChatActivity() {
        adapter = new Adapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        chatMenu = (ActionMenuView) toolbar.findViewById(R.id.chat_menu);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        final SpaceDecorator decorator = new SpaceDecorator();
        final int sidePadding = ImageUtils.dpToPx(5);//convert to dp later

        decorator.setBottom(ImageUtils.dpToPx(15));
        decorator.setLeft(sidePadding);
        decorator.setRight(sidePadding);

        messagesContainer = (RecyclerView) findViewById(R.id.chat_messages_container);
        messagesContainer.addItemDecoration(decorator);
        messagesContainer.setLayoutManager(new LinearLayoutManager(this));
        messagesContainer.setAdapter(adapter);

        adapter.addItem(Arrays.asList(
                new MessageModel("11/14/2016", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus aliquam in turpis ac dictum. Mauris eget tempor leo. Nullam gravida quam quis congue fringilla. Nam faucibus arcu ac sollicitudin posuere. Sed fermentum urna sit amet turpis maximus, eget lacinia velit commodo.", MessageModel.Direction.RIGHT),
                new MessageModel("11/14/2016", "Hello there", MessageModel.Direction.RIGHT),
                new MessageModel("11/14/2016", "Aliquam ut auctor mi. Aliquam ex sem, consectetur vitae ligula aliquet, dictum facilisis arcu. Sed auctor commodo sodales.", MessageModel.Direction.LEFT),
                new MessageModel("11/14/2016", "Nullam consectetur malesuada tellus sit amet pellentesque", MessageModel.Direction.LEFT),
                new MessageModel("11/14/2016", "Phasellus tincidunt erat quam", MessageModel.Direction.LEFT),
                new MessageModel("11/14/2016", "Sed sed lorem nisl.", MessageModel.Direction.RIGHT),
                new MessageModel("11/14/2016", "Aenean pellentesque magna sed ante lobortis, ac aliquet felis scelerisque", MessageModel.Direction.RIGHT),
                new MessageModel("11/14/2016", "Nunc malesuada efficitur nunc, et vehicula ante pretium nec", MessageModel.Direction.LEFT),
                new MessageModel("11/14/2016", "In ut ex porta", MessageModel.Direction.LEFT),
                new MessageModel("11/14/2016", "fames ac ante ipsum primis in faucibus. Quisque", MessageModel.Direction.RIGHT)
        ));

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, chatMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if(id == R.id.action_refresh) {
        }

        return true;
    }

}
