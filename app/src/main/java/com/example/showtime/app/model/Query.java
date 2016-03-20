package com.example.showtime.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "queries")
public class Query {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;

    @DatabaseField
    private String query;

    public Query(){}

    public Query(String query) {
        this.query = query;
    }

    public int getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public  void setId(int id) {
        this.id = id;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String toString() {
        return query;
    }
}
