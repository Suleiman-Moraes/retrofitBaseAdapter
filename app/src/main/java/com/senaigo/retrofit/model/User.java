package com.senaigo.retrofit.model;


import android.util.Log;

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
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    public Map<String, Object> getMap(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("username", username);
        map.put("email", email);
        map.put("phone", phone);
        map.put("website", website);
        map.put("street", address.getStreet());
        map.put("suite", address.getSuite());
        map.put("city", address.getCity());
        map.put("zipcode", address.getZipcode());
        map.put("lat", address.getGeo().getLat());
        map.put("lng", address.getGeo().getLng());
        map.put("nameCompany", company.getName());
        map.put("catchPhrase", company.getCatchPhrase());
        map.put("bs", company.getBs());
        return map;
    }

    public static User convertMapToUser(Map<String, Object> map){
        Geo geo = new Geo(Double.valueOf(map.get("lat") + ""), Double.valueOf(map.get("lng") + ""));
        Address address = new Address(map.get("street") + "", map.get("suite") + "",
                map.get("city") + "", map.get("zipcode") + "", geo);
        Company company = new Company(map.get("nameCompany") + "", map.get("catchPhrase") + "", map.get("bs") + "");
        User user = new User((int) map.get("id"), map.get("name") + "", map.get("username") + "", map.get("email") + "",
                map.get("phone") + "", map.get("website") + "", address, company);
        return user;
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
