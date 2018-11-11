package androidnews.kiloproject.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidnews.kiloproject.R;
import androidnews.kiloproject.bean.data.CommentLevel;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter  extends BaseMultiItemQuickAdapter<CommentLevel, BaseViewHolder> {
    RequestOptions options;
    private final RequestManager glide;
    private Context mContext;
    public static final int LEVEL_ONE = 0;
    public static final int LEVEL_TWO = 1;

    public CommentAdapter(Context Context,RequestManager glide, List<CommentLevel> data) {
        super(data);
        this.glide = glide;
        this.mContext = Context;
        addItemType(LEVEL_ONE, R.layout.comment_level_one);
        addItemType(LEVEL_TWO, R.layout.comment_level_two);
        options = new RequestOptions();
        options.error(R.mipmap.ic_launcher);
    }

    @Override
    protected void convert(final BaseViewHolder helper, CommentLevel item) {
        switch (item.getItemType()){
            case LEVEL_ONE:
            case LEVEL_TWO:
                final CommentLevel data = item;
                helper.setText(R.id.tv_text, data.getText());
                helper.setText(R.id.tv_time, data.getTime());
                helper.setText(R.id.tv_name, data.getName().replace("&nbsp"," "));
                glide.load(data.getImgUrl()).apply(options).into((CircleImageView)helper.getView(R.id.iv_avatar));
                break;
        }
    }
}