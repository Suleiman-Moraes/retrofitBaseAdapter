package com.senaigo.retrofit.model;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;

    public Map<String, Object> getMap(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userId", userId);
        map.put("id", id);
        map.put("title", title);
        map.put("body", body);
        return map;
    }

    public static User convertMapToUser(Map<String, Object> map){
        return new User((int) map.get("userId"), (int) map.get("id"), map.get("title") + "", map.get("body") + "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
