package com.example.instagram.service;

import com.example.instagram.dto.request.CreatePostRequest;
import com.example.instagram.dto.response.LikeResponse;
import com.example.instagram.dto.response.PageResponse;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.exception.ApiException;
import com.example.instagram.model.Like;
import com.example.instagram.model.LikeId;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.repository.CommentRepository;
import com.example.instagram.repository.LikeRepository;
import com.example.instagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public PostResponse createPost(CreatePostRequest request, User author) {
        Post post = Post.builder()
                .author(author)
                .imageUrl(request.getImageUrl())
                .caption(request.getCaption())
                .build();

        post = postRepository.save(post);
        return PostResponse.from(post, author, 0, 0, false);
    }

    public PageResponse<PostResponse> getFeed(int page, int size, User currentUser) {
        Page<Post> postsPage = postRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));

        List<PostResponse> posts = postsPage.getContent().stream()
                .map(post -> {
                    long likesCount = likeRepository.countByPostId(post.getId());
                    long commentsCount = commentRepository.countByPostId(post.getId());
                    boolean isLiked = likeRepository.existsById(new LikeId(currentUser.getId(), post.getId()));
                    return PostResponse.from(post, post.getAuthor(), likesCount, commentsCount, isLiked);
                })
                .toList();

        return PageResponse.from(postsPage, posts);
    }

    public PostResponse getPost(String postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Post not found"));

        long likesCount = likeRepository.countByPostId(postId);
        long commentsCount = commentRepository.countByPostId(postId);
        boolean isLiked = likeRepository.existsById(new LikeId(currentUser.getId(), postId));

        return PostResponse.from(post, post.getAuthor(), likesCount, commentsCount, isLiked);
    }

    @Transactional
    public void deletePost(String postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Post not found"));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", "You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    public LikeResponse likePost(String postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Post not found"));

        LikeId likeId = new LikeId(currentUser.getId(), postId);

        if (likeRepository.existsById(likeId)) {
            throw new ApiException(HttpStatus.CONFLICT, "CONFLICT", "Post already liked");
        }

        Like like = Like.builder()
                .id(likeId)
                .user(currentUser)
                .post(post)
                .build();

        likeRepository.save(like);

        long likesCount = likeRepository.countByPostId(postId);
        return LikeResponse.builder()
                .postId(postId)
                .likesCount(likesCount)
                .build();
    }

    public LikeResponse unlikePost(String postId, User currentUser) {
        if (!postRepository.existsById(postId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Post not found");
        }

        LikeId likeId = new LikeId(currentUser.getId(), postId);

        if (!likeRepository.existsById(likeId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Like not found");
        }

        likeRepository.deleteById(likeId);

        long likesCount = likeRepository.countByPostId(postId);
        return LikeResponse.builder()
                .postId(postId)
                .likesCount(likesCount)
                .build();
    }
}
