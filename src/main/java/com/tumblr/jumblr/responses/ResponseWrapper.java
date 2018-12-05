package com.tumblr.jumblr.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.Resource;
import com.tumblr.jumblr.types.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseWrapper {

    private JsonElement response;
    private JumblrClient client;

    public void setClient(JumblrClient client) {
        this.client = client;
    }

    public User getUser() {
        return get("user", User.class);
    }

    public Blog getBlog() {
        return get("blog", Blog.class);
    }

    public Post getPost() {
        return get("post", Post.class);
    }

    public Long getId() {
        JsonObject object = (JsonObject) response;
        return object.get("id").getAsLong();
    }

    // NOTE: needs to be duplicated logic due to Java erasure of generic types
    public List<Post> getPosts() {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        List<Post> l = gson.fromJson(object.get("posts"), new TypeToken<List<Post>>() {}.getType());
        for (Post e : l) { e.setClient(client); }
        return l;
    }

    // NOTE: needs to be duplicated logic due to Java erasure of generic types
    public Map<String,Object> getMapPosts() {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        List<Post> l = gson.fromJson(object.get("posts"), new TypeToken<List<Post>>() {}.getType());
        for (Post e : l) { e.setClient(client); }

        Integer totalPosts = object.get("total_posts").getAsInt();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("posts",l);
        return map;
    }

    // NOTE: needs to be duplicated logic due to Java erasure of generic types
    public List<User> getUsers() {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        List<User> l = gson.fromJson(object.get("users"), new TypeToken<List<User>>() {}.getType());
        for (User e : l) { e.setClient(client); }
        return l;
    }

    // NOTE: needs to be duplicated logic due to Java erasure of generic types
    public List<Post> getLikedPosts() {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        List<Post> l = gson.fromJson(object.get("liked_posts"), new TypeToken<List<Post>>() {}.getType());
        for (Post e : l) { e.setClient(client); }
        return l;
    }

    // NOTE: needs to be duplicated logic due to Java erasure of generic types
    public List<Post> getTaggedPosts() {
        Gson gson = gsonParser();
        List<Post> l = gson.fromJson(response.getAsJsonArray(), new TypeToken<List<Post>>() {}.getType());
        for (Post e : l) { e.setClient(client); }
        return l;
    }

    // NOTE: needs to be duplicated logic due to Java erasure of generic types
    public List<Blog> getBlogs() {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        List<Blog> l = gson.fromJson(object.get("blogs"), new TypeToken<List<Blog>>() {}.getType());
        for (Blog e : l) { e.setClient(client); }
        return l;
    }

    /**
     **
     **/

    private <T extends Resource> T get(String field, Class<T> k) {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        T e = gson.fromJson(object.get(field).toString(), k);
        e.setClient(client);
        return e;
    }

    private Gson gsonParser() {
        return new GsonBuilder().
            registerTypeAdapter(Post.class, new PostDeserializer()).
            create();
    }

}
