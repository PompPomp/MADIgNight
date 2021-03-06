package sg.edu.np.ignight.Blog;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.ignight.R;

public class BlogViewHolder extends RecyclerView.ViewHolder {
    TextView desc, location;
    View viewItem;
    TextView likes, comments;
    ImageView commentButton, likesButton, blogImg, editBlogButton;

    public BlogViewHolder(View item){
        super(item);
        desc = item.findViewById(R.id.blogDesc);
        location = item.findViewById(R.id.location);
        blogImg = item.findViewById(R.id.blogImg);
        likes = item.findViewById(R.id.likesCount);
        comments = item.findViewById(R.id.commentsCount);
        commentButton = item.findViewById(R.id.commentButton);
        likesButton = item.findViewById(R.id.likeButton);
        editBlogButton = item.findViewById(R.id.editBlogButton);
        viewItem = item;
    }

}
