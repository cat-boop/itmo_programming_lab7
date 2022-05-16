package com.lab7.server.database;

public enum DBRequest {
    CREATE_CLIENT_TABLE("create table if not exists s335111db_clients\n"
            + "(\n"
            + "client_name text not null primary key,\n"
            + "password text not null\n"
            + ");"),
    CREATE_ROUTE_TABLE("create table if not exists routes\n"
            + "(\n"
            + "id bigserial primary key,\n"
            + "client_name text references s335111db_clients(client_name) on delete cascade,\n"
            + "route_name text not null check (char_length(route_name) > 0),\n"
            + "x_coordinate integer not null check (x_coordinate <= 412),\n"
            + "y_coordinate bigint not null,\n"
            + "creation_date timestamp not null,\n"
            + "x_from integer not null,\n"
            + "y_from integer not null,\n"
            + "z_from double precision not null,\n"
            + "name_from text not null check (char_length(name_from) > 0),\n"
            + "x_to integer,\n"
            + "y_to integer,\n"
            + "z_to integer,\n"
            + "name_to text check (char_length(name_to) > 0),\n"
            + "distance double precision check (distance > 1)\n"
            + ");\n"),
    GET_All_ROUTES("select * from routes"),
    ADD_NEW_ROUTE("insert into routes (\"client_name\", \"route_name\", \"x_coordinate\", \"y_coordinate\", \"creation_date\", \"x_from\", \"y_from\", \"z_from\", \"name_from\", \"x_to\", \"y_to\", \"z_to\", \"name_to\", \"distance\")"
            + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    UPDATE_ROUTE("update routes set (\"client_name\", \"route_name\", \"x_coordinate\", \"y_coordinate\", \"creation_date\", \"x_from\", \"y_from\", \"z_from\", \"name_from\", \"x_to\", \"y_to\", \"z_to\", \"name_to\", \"distance\") "
            + "= (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) where id=?"),
    DELETE_CLIENT_ROUTES("delete from routes where client_name=?"),
    DELETE_ROUTE_BY_ID("delete from routes where id=?"),
    DELETE_ALL_GREATER_THAN_DISTANCE_ROUTES("delete from routes where client_name=? and distance>?"),
    DELETE_ALL_LOWER_THAN_DISTANCE_ROUTES("delete from routes where client_name=? and distance<?"),
    CHECK_IF_CLIENT_EXIST("select from s335111db_clients where client_name=?"),
    CHECK_IF_CLIENT_ENTER_RIGHT_PASSWORD("select from s335111db_clients where client_name=? and password=?"),
    REGISTER_NEW_CLIENT("insert into s335111db_clients (\"client_name\", \"password\") values(?, ?)");

    private final String request;
    DBRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
