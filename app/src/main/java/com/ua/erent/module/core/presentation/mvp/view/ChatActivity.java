package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.ChatComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.IChatPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.MessageModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.IChatView;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.SpaceDecorator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.graphics.Color.WHITE;

public final class ChatActivity extends InjectableActivity<ChatActivity, IChatPresenter>
        implements IChatView {

    private RecyclerView messagesContainer;
    //private ActionMenuView chatMenu;

    private final Adapter adapter;

    private static final class LoaderHolder extends TypedViewHolder {

        LoaderHolder(@NotNull View itemView) {
            super(itemView, ContentType.LOADER);
        }
    }

    private static final class MessageHolder extends TypedViewHolder {

        private final LinearLayout messageContainer;
        private final TextView timestampTextView;
        private final TextView bodyTextView;
        private final Space space;

        MessageHolder(View itemView) {
            super(itemView, ContentType.CONTENT);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            timestampTextView = (TextView) itemView.findViewById(R.id.message_timestamp);
            bodyTextView = (TextView) itemView.findViewById(R.id.message_body);
            space = (Space) itemView.findViewById(R.id.message_spacing_left);
        }

        void setDirection(MessageModel.Direction direction) {

            final View holder = itemView.findViewById(R.id.message_body_holder);
            final Context context = holder.getContext();

            if (direction == MessageModel.Direction.RIGHT) {
                bodyTextView.setTextColor(WHITE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageContainer.getLayoutParams();

                params.setMargins(ImageUtils.dpToPx(35), 0, 0, 0);

                space.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                );
                holder.setBackgroundResource(R.drawable.chat_message_bg_blue);

            } else if (direction == MessageModel.Direction.LEFT) {
                bodyTextView.setTextColor(ContextCompat.getColor(context, R.color.primaryTextB));

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageContainer.getLayoutParams();

                params.setMargins(0, 0, ImageUtils.dpToPx(35), 0);
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

    private class Adapter extends RecyclerView.Adapter<TypedViewHolder> {

        private final ArrayList<RecyclerItem> content;

        Adapter() {
            content = new ArrayList<>();
            setHasStableIds(true);
        }

        @Override
        public int getItemViewType(int position) {
            return content.get(position).getType().getValId();
        }

        @Override
        public TypedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final ContentType type = ContentType.forId(viewType);
            final LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);

            if(type == ContentType.LOADER)
                return new LoaderHolder(inflater.inflate(R.layout.progress_item, parent, false));

            return new MessageHolder(inflater.inflate(R.layout.chat_message, parent, false));
        }

        @Override
        public void onBindViewHolder(TypedViewHolder holder, int position) {

            if(holder.getType() == ContentType.CONTENT) {

                final MessageModel model = content.get(position).getPayload();
                final MessageHolder messageHolder = (MessageHolder) holder;

                messageHolder.setTimestamp(model.getTimestamp());
                messageHolder.setBody(model.getBody());
                messageHolder.setDirection(model.getDirection());
            }
        }

        @Override
        public long getItemId(int position) {
            return content.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return content.size();
        }

        void setItems(@NotNull Collection<MessageModel> items) {
            content.clear();
            content.addAll(fromModels(items));
        }

        void addBegin(@NotNull Collection<MessageModel> items) {
            content.addAll(0, fromModels(items));
        }

        void addEnd(@NotNull Collection<MessageModel> items) {
            content.addAll(fromModels(items));
        }

        void addLoaderStart() {

            if (content.isEmpty() || content.get(0).getType() != ContentType.LOADER) {
                content.add(0, new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderStart() {

            if (!content.isEmpty() && content.get(0).getType() == ContentType.LOADER) {
                content.remove(0);
                return true;
            }

            return false;
        }

        void addLoaderEnd() {

            final int lastIndx = content.size() - 1;

            if (content.isEmpty() || content.get(lastIndx).getType() != ContentType.LOADER) {
                content.add(new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderEnd() {

            final int lastIndx = content.size() - 1;

            if (!content.isEmpty() && content.get(lastIndx).getType() == ContentType.LOADER) {
                content.remove(lastIndx);
                return true;
            }

            return false;
        }

        private Collection<RecyclerItem> fromModels(Collection<MessageModel> models) {
            final Collection<RecyclerItem> result = new ArrayList<>(models.size());
            for (final MessageModel model : models) {
                result.add(new RecyclerItem(ContentType.CONTENT, model));
            }
            return result;
        }

    }

    public ChatActivity() {
        super(R.layout.activity_chat, ChatComponent.class);
        adapter = new Adapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // chatMenu = (ActionMenuView) toolbar.findViewById(R.id.chat_menu);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
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

        adapter.setItems(Arrays.asList(
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

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);
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
