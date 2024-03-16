package org.kshrd;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts/")
public class PostController {

    private final List<Post> posts = new ArrayList<>();
    private int postIdCounter = 1;

    @PostMapping
    public ResponseEntity<Post> insertPost(@RequestBody Post post) {
        post.setId(postIdCounter++);
        post.setCreationDate(LocalDateTime.now());
        posts.add(post);
        String message = "Post created successfully";
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        String message = "Get all posts successfully";
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id) {
        Optional<Post> optionalPost = posts.stream().filter(p -> p.getId() == id).findFirst();
        return optionalPost.map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/byTitle")
    public ResponseEntity<List<Post>> getPostsByTitle(@RequestParam String title) {
        List<Post> foundPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTitle().equalsIgnoreCase(title)) {
                foundPosts.add(post);
            }
        }
        return ResponseEntity.ok(foundPosts);
    }

    @GetMapping("/byAuthor")
    public ResponseEntity<List<Post>> getPostsByAuthor(@RequestParam String author) {
        List<Post> foundPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getAuthor().equalsIgnoreCase(author)) {
                foundPosts.add(post);
            }
        }
        return ResponseEntity.ok(foundPosts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            if (post.getId() == id) {
                updatedPost.setId(id);
                updatedPost.setCreationDate(post.getCreationDate());
                posts.set(i, updatedPost);
                return ResponseEntity.ok(updatedPost);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        posts.removeIf(post -> post.getId() == id);
        return ResponseEntity.noContent().build();
    }
}
