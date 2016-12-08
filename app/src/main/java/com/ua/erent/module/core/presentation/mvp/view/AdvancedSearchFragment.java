package com.ua.erent.module.core.presentation.mvp.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableDialogFragment;
import com.ua.erent.module.core.presentation.mvp.component.SearchComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ISearchPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ISearchView;
import com.ua.erent.module.core.presentation.mvp.view.util.CompoundImage;
import com.ua.erent.module.core.presentation.mvp.view.util.DefaultAnimatorCallback;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;
import com.ua.erent.module.core.util.MyTextUtil;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

public final class AdvancedSearchFragment extends InjectableDialogFragment<AdvancedSearchFragment, ISearchPresenter>
        implements ISearchView {

    @BindView(R.id.search_back_btn)
    protected ImageButton backButton;

    @BindView(R.id.search_go_btn)
    protected ImageButton searchButton;

    @BindView(R.id.search_query_field)
    protected EditText queryEditText;

    @BindView(R.id.search_categories_sub_title)
    protected TextView categoriesSubTextView;

    @BindView(R.id.search_categories_progress)
    protected ProgressBar categoriesProgressBar;

    @BindView(R.id.search_categories_container)
    protected RecyclerView categoriesContainer;

    @BindView(R.id.search_brands_sub_title)
    protected TextView brandsSubTextView;

    @BindView(R.id.search_brands_progress)
    protected ProgressBar brandsProgressBar;

    @BindView(R.id.search_brands_container)
    protected RecyclerView brandsContainer;

    @BindView(R.id.search_regions_sub_title)
    protected TextView regionsSubTextView;

    @BindView(R.id.search_regions_progress)
    protected ProgressBar regionsProgressBar;

    @BindView(R.id.search_regions_container)
    protected RecyclerView regionsContainer;

    @BindView(R.id.search_price_range)
    protected RangeSeekBar<Integer> priceRange;

    @BindView(R.id.search_views_switcher)
    protected ViewSwitcher viewSwitcher;

    @BindView(R.id.search_result_item_container)
    protected RecyclerView resultRecycleView;

    private final CategoryAdapter categoryAdapter;
    private final BrandsAdapter brandsAdapter;
    private final RegionsAdapter regionsAdapter;
    private final ResultAdapter resultAdapter;

    private static final class ViewHolder extends RecyclerView.ViewHolder {

        private static final long HALF_ANIM_DURATION = 150L;

        private final CompoundImage image;
        private final TextView name;

        private final ObjectAnimator halfAnimatorOut;
        private final ObjectAnimator outAnim;

        private final ObjectAnimator halfAnimatorIn;
        private final ObjectAnimator inAnim;

        ViewHolder(View itemView) {
            super(itemView);
            image = (CompoundImage) itemView.findViewById(R.id.item_image);
            name = (TextView) itemView.findViewById(R.id.item_full_name);

            halfAnimatorOut = ObjectAnimator.ofFloat(image, View.ROTATION_Y, -90, 0);
            halfAnimatorOut.setDuration(HALF_ANIM_DURATION);

            outAnim = ObjectAnimator.ofFloat(image, View.ROTATION_Y, -180, -90);
            outAnim.setDuration(HALF_ANIM_DURATION);

            halfAnimatorIn = ObjectAnimator.ofFloat(image, View.ROTATION_Y, 90, 180);
            halfAnimatorIn.setDuration(HALF_ANIM_DURATION);

            inAnim = ObjectAnimator.ofFloat(image, View.ROTATION_Y, 0, 90);
            inAnim.setDuration(HALF_ANIM_DURATION);
        }

        void setup(final String title) {

            final String shortName = String.valueOf(title.charAt(0));

            name.setText(title);
            image.setText(shortName);
            image.setChecked(false);
            itemView.setOnClickListener(v -> {

                if (image.isChecked()) {

                    outAnim.addListener(new DefaultAnimatorCallback() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            image.setChecked(false);
                            image.setText(shortName);
                            halfAnimatorOut.start();
                            outAnim.removeAllListeners();
                        }
                    });
                    outAnim.start();
                } else {

                    inAnim.addListener(new DefaultAnimatorCallback() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            image.setChecked(true);
                            image.setText(null);
                            halfAnimatorIn.start();
                            inAnim.removeAllListeners();
                        }
                    });
                    inAnim.start();
                }
            });
        }

    }

    private abstract class AbstractAdapter<T extends Parcelable> extends RecyclerView.Adapter<ViewHolder> {

        protected final ArrayList<T> data;

        AbstractAdapter() {
            data = new ArrayList<>();
            setHasStableIds(true);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View v = LayoutInflater.from(AdvancedSearchFragment.this.getContext())
                    .inflate(R.layout.search_selectable_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        void setItems(@NotNull Collection<T> items) {
            data.clear();
            data.addAll(items);
        }

        void onSaveState(Bundle out) {
            out.putParcelableArrayList(getClass().getSimpleName(), data);
        }

        void onRestoreState(Bundle state) {
            final ArrayList<T> arr = state.getParcelableArrayList(getClass().getSimpleName());

            if (arr != null) {
                data.addAll(arr);
            }
        }

    }

    private class CategoryAdapter extends AbstractAdapter<CategoryModel> {

        CategoryAdapter() {
            super();
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final CategoryModel categoryModel = data.get(position);
            final String title = MyTextUtil.capitalize(categoryModel.getTitle());

            holder.setup(title);
            holder.image.setOnCheckedChangeListener((buttonView, isChecked) ->
                    presenter.onCategorySelectionChanged(categoryModel.getId(), isChecked));
        }

    }

    private class BrandsAdapter extends AbstractAdapter<BrandModel> {

        BrandsAdapter() {
            super();
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final BrandModel brandModel = data.get(position);
            final String title = MyTextUtil.capitalize(brandModel.getName());

            holder.setup(title);
            holder.image.setOnCheckedChangeListener((buttonView, isChecked) ->
                    presenter.onCategorySelectionChanged(brandModel.getId(), isChecked));
        }

    }

    private class RegionsAdapter extends AbstractAdapter<RegionModel> {

        RegionsAdapter() {
            super();
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final RegionModel regionModel = data.get(position);
            final String title = MyTextUtil.capitalize(regionModel.getName());

            holder.setup(title);
            holder.image.setOnCheckedChangeListener((buttonView, isChecked) ->
                    presenter.onCategorySelectionChanged(regionModel.getId(), isChecked));
        }

    }

    private class ResultAdapter extends RecyclerView.Adapter<TypedViewHolder> {

        private final List<RecyclerItem> data;

        ResultAdapter() {
            data = new ArrayList<>(15);
            setHasStableIds(true);
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).getType().getValId();
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public TypedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final ContentType type = ContentType.forId(viewType);
            final int rootId = type == ContentType.CONTENT ? R.layout.good_item : R.layout.progress_item;

            return new ItemHolder(LayoutInflater.from(getContext()).inflate(rootId, parent, false), type);
        }

        @Override
        public void onBindViewHolder(TypedViewHolder mHolder, int position) {

            if (mHolder.getType() == ContentType.CONTENT) {

                final ItemModel model = data.get(position).getPayload();
                final ItemHolder holder = (ItemHolder) mHolder;

                holder.setId(model.getId());
                holder.setAvatarBm(model.getUserAvatar());

                if (!model.getGallery().isEmpty()) {
                    holder.setPhotoBm(model.getGallery().iterator().next());
                }

                holder.getTitle().setText(model.getTitle());
                holder.getSubTitle().setText(getContext().getString(R.string.items_sub_title,
                        model.getRegion(),
                        model.getPrice(),
                        model.getUsername()));

                holder.getDescription().setText(model.getDescription());
               // holder.getTitle().setOnClickListener(v -> presenter.onItemClicked(model.getId()));
               // holder.getDescription().setOnClickListener(v -> presenter.onItemClicked(model.getId()));
              //  holder.getActionMenu().setOnClickListener(v -> showItemPopup(v, model.getId()));
            }
        }

        @Override
        public void onViewAttachedToWindow(TypedViewHolder mHolder) {

            if (mHolder.getType() == ContentType.CONTENT) {

                final ItemHolder holder = (ItemHolder) mHolder;

                if (holder.getAvatarBm() != null) {
                    loadInto(holder.getAvatarBm(), holder.getAvatar(), R.drawable.ic_account_circle_def_24dp);
                }

                if (holder.getPhotoBm() != null) {
                    holder.getPhoto().setVisibility(View.VISIBLE);
              //      holder.getPhoto().setOnClickListener(v -> presenter.onPhotoClicked(holder.getId(), holder.getPhoto()));
                    loadInto(holder.getPhotoBm(), holder.getPhoto(), R.drawable.image_placeholder_photo);
                }
            }
        }

        @Override
        public void onViewDetachedFromWindow(TypedViewHolder mHolder) {
            if (mHolder.getType() == ContentType.CONTENT) {

                final ItemHolder holder = (ItemHolder) mHolder;

                holder.setPhotoBm(null);
                holder.setAvatarBm(null);
                holder.getAvatar().setImageResource(R.drawable.ic_account_circle_def_24dp);
                holder.getPhoto().setImageDrawable(null);
                holder.getPhoto().setVisibility(View.GONE);
            }
        }

        void setItems(@NotNull Collection<ItemModel> items) {
            data.clear();
            data.addAll(fromModels(items));
        }

        void addBegin(@NotNull Collection<ItemModel> items) {
            data.addAll(0, fromModels(items));
        }

        void addEnd(@NotNull Collection<ItemModel> items) {
            data.addAll(fromModels(items));
        }

        void addLoaderStart() {

            if (data.isEmpty() || data.get(0).getType() != ContentType.LOADER) {
                data.add(0, new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderStart() {

            if (!data.isEmpty() && data.get(0).getType() == ContentType.LOADER) {
                data.remove(0);
                return true;
            }

            return false;
        }

        void addLoaderEnd() {

            final int lastIndx = data.size() - 1;

            if (data.isEmpty() || data.get(lastIndx).getType() != ContentType.LOADER) {
                data.add(new RecyclerItem(ContentType.LOADER, null));
            }
        }

        boolean removeLoaderEnd() {

            final int lastIndx = data.size() - 1;

            if (!data.isEmpty() && data.get(lastIndx).getType() == ContentType.LOADER) {
                data.remove(lastIndx);
                return true;
            }

            return false;
        }

        private Collection<RecyclerItem> fromModels(Collection<ItemModel> itemModels) {
            final Collection<RecyclerItem> result = new ArrayList<>(itemModels.size());
            for (final ItemModel itemModel : itemModels) {
                result.add(new RecyclerItem(ContentType.CONTENT, itemModel));
            }
            return result;
        }

        private void loadInto(IFutureBitmap futureBitmap, ImageView view, @DrawableRes int defaultId) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            final WeakReference<ImageView> avatarRef = new WeakReference<>(view);

            futureBitmap.fetch(params.width, params.height, AdvancedSearchFragment.this.getContext())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            bm -> {
                                if (avatarRef.get() != null)
                                    view.setImageBitmap(bm);
                            },
                            th -> {
                                if (avatarRef.get() != null)
                                    view.setImageResource(defaultId);
                            });
        }

    }

    public AdvancedSearchFragment() {
        super(SearchComponent.class);
        categoryAdapter = new CategoryAdapter();
        brandsAdapter = new BrandsAdapter();
        regionsAdapter = new RegionsAdapter();
        resultAdapter = new ResultAdapter();
    }

    public static AdvancedSearchFragment newInstance(String param1, String param2) {
        AdvancedSearchFragment fragment = new AdvancedSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advanced_search, container, false);
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        ScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                final int lastVisiblePos = layoutManager.findLastVisibleItemPosition();
                final int lastPos = recyclerView.getAdapter().getItemCount() - 1;

                if(lastVisiblePos == lastPos) {
                    presenter.onNextResult();
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if (savedInstanceState != null) {
            categoryAdapter.onRestoreState(savedInstanceState);
            brandsAdapter.onRestoreState(savedInstanceState);
            regionsAdapter.onRestoreState(savedInstanceState);
        }*/

        categoriesContainer.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoriesContainer.setAdapter(categoryAdapter);

        brandsContainer.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        brandsContainer.setAdapter(brandsAdapter);

        regionsContainer.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        regionsContainer.setAdapter(regionsAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        resultRecycleView.setLayoutManager(layoutManager);
        resultRecycleView.setAdapter(resultAdapter);
        resultRecycleView.addOnScrollListener(new ScrollListener(layoutManager));

        searchButton.setOnClickListener(v -> presenter.onSearch(queryEditText.getText().toString()));
    }

    @Override
    public void onStart() {
        super.onStart();

        final Dialog dialog = getDialog();

        if (dialog != null) {

            final int width = ViewGroup.LayoutParams.MATCH_PARENT;
            final int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       /* categoryAdapter.onSaveState(outState);
        brandsAdapter.onSaveState(outState);
        regionsAdapter.onSaveState(outState);*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NotNull
    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showCategoriesProgress() {
        categoriesProgressBar.setVisibility(View.VISIBLE);
        categoriesSubTextView.setVisibility(View.GONE);
        categoriesContainer.setVisibility(View.GONE);
    }

    @Override
    public void showCategoriesFailure() {
        categoriesProgressBar.setVisibility(View.GONE);
        categoriesSubTextView.setVisibility(View.VISIBLE);
        categoriesContainer.setVisibility(View.GONE);
    }

    @Override
    public void setCategories(@NotNull Collection<CategoryModel> categories) {
        categoryAdapter.setItems(categories);
        categoryAdapter.notifyDataSetChanged();
        categoriesProgressBar.setVisibility(View.GONE);
        categoriesSubTextView.setVisibility(View.GONE);
        categoriesContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBrandsProgress() {
        brandsProgressBar.setVisibility(View.VISIBLE);
        brandsSubTextView.setVisibility(View.GONE);
        brandsContainer.setVisibility(View.GONE);
    }

    @Override
    public void showBrandsFailure() {
        brandsProgressBar.setVisibility(View.GONE);
        brandsSubTextView.setVisibility(View.VISIBLE);
        brandsContainer.setVisibility(View.GONE);
    }

    @Override
    public void setBrands(@NotNull Collection<BrandModel> brands) {
        brandsAdapter.setItems(brands);
        brandsAdapter.notifyDataSetChanged();
        brandsProgressBar.setVisibility(View.GONE);
        brandsSubTextView.setVisibility(View.GONE);
        brandsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRegionsProgress() {
        regionsProgressBar.setVisibility(View.VISIBLE);
        regionsSubTextView.setVisibility(View.GONE);
        regionsContainer.setVisibility(View.GONE);
    }

    @Override
    public void showRegionsFailure() {
        regionsProgressBar.setVisibility(View.GONE);
        regionsSubTextView.setVisibility(View.VISIBLE);
        regionsContainer.setVisibility(View.GONE);
    }

    @Override
    public void setRegions(@NotNull Collection<RegionModel> regions) {
        regionsAdapter.setItems(regions);
        regionsAdapter.notifyDataSetChanged();
        regionsProgressBar.setVisibility(View.GONE);
        regionsSubTextView.setVisibility(View.GONE);
        regionsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult() {
        final int id = viewSwitcher.getCurrentView().getId();

        if(id != R.id.search_result_container) {
            viewSwitcher.showNext();
        }
    }

    @Override
    public void showSearch() {

        final int id = viewSwitcher.getCurrentView().getId();

        if(id == R.id.search_result_container) {
            viewSwitcher.showNext();
        }
    }

    @Override
    public void setResult(@NotNull Collection<ItemModel> items) {
        final boolean removed = resultAdapter.removeLoaderStart();

        if (removed) {
            resultAdapter.notifyItemRemoved(0);
        }
        resultAdapter.addBegin(items);
        resultAdapter.notifyItemRangeInserted(0, items.size());
    }

}
