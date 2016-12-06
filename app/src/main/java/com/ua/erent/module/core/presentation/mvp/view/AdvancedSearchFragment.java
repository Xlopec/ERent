package com.ua.erent.module.core.presentation.mvp.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableDialogFragment;
import com.ua.erent.module.core.presentation.mvp.component.SearchComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ISearchPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ISearchView;
import com.ua.erent.module.core.presentation.mvp.view.util.CompoundImage;
import com.ua.erent.module.core.presentation.mvp.view.util.DefaultAnimatorCallback;
import com.ua.erent.module.core.util.MyTextUtil;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;

public final class AdvancedSearchFragment extends InjectableDialogFragment<AdvancedSearchFragment, ISearchPresenter>
        implements ISearchView {

    @BindView(R.id.search_back_btn)
    protected ImageButton backButton;

    @BindView(R.id.search_go_btn)
    protected ImageButton searchButton;

    @BindView(R.id.search_query_field)
    protected EditText queryEditText;

    @BindView(R.id.search_categories_container)
    protected RecyclerView categoriesContainer;

    @BindView(R.id.search_brands_container)
    protected RecyclerView brandsContainer;

    @BindView(R.id.search_regions_container)
    protected RecyclerView regionsContainer;

    @BindView(R.id.search_price_range)
    protected RangeSeekBar<Integer> priceRange;

    private final CategoryAdapter categoryAdapter;
    private final BrandsAdapter brandsAdapter;
    private final RegionsAdapter regionsAdapter;

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

            //90, 180
            halfAnimatorOut = ObjectAnimator.ofFloat(image, View.ROTATION_Y, -90, 0);
            halfAnimatorOut.setDuration(HALF_ANIM_DURATION);
//0, 90
            outAnim = ObjectAnimator.ofFloat(image, View.ROTATION_Y, -180, -90);
            outAnim.setDuration(HALF_ANIM_DURATION);
// -90, 0
            halfAnimatorIn = ObjectAnimator.ofFloat(image, View.ROTATION_Y, 90, 180);
            halfAnimatorIn.setDuration(HALF_ANIM_DURATION);
//-180, -90
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

    private abstract class AbstractAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

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
        }

    }

    public AdvancedSearchFragment() {
        super(SearchComponent.class);
        categoryAdapter = new CategoryAdapter();
        brandsAdapter = new BrandsAdapter();
        regionsAdapter = new RegionsAdapter();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriesContainer.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoriesContainer.setAdapter(categoryAdapter);

        brandsContainer.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        brandsContainer.setAdapter(brandsAdapter);

        regionsContainer.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        regionsContainer.setAdapter(regionsAdapter);
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
    public void onDetach() {
        super.onDetach();
    }

    @NotNull
    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void setCategories(@NotNull Collection<CategoryModel> categories) {
        categoryAdapter.setItems(categories);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void setBrands(@NotNull Collection<BrandModel> brands) {
        brandsAdapter.setItems(brands);
        brandsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRegions(@NotNull Collection<RegionModel> regions) {
        regionsAdapter.setItems(regions);
        regionsAdapter.notifyDataSetChanged();
    }

}
