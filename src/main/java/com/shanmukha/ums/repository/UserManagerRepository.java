package com.shanmukha.ums.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.shanmukha.ums.dto.GetUsersDTO;
import com.shanmukha.ums.query.SQL;

@Repository
public class UserManagerRepository {

	private final JdbcTemplate jdbcTemplate;

    Long totalCount = (long) 0;

    public UserManagerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<GetUsersDTO> getUsers(Pageable page, String name, String username, String role) {
        totalCount = (long) 0;
        RowMapper<GetUsersDTO> mapper = (rs, rowNum) -> {

            GetUsersDTO users = new GetUsersDTO();
            users.setId(rs.getLong("id"));
            users.setName(rs.getString("name"));
            users.setUsername(rs.getString("username"));
            users.setRole(rs.getString("role"));
            users.setEmail(rs.getString("email"));
            users.setPhone(rs.getString("phone"));

            totalCount = rs.getLong("total");
            return users;
        };
        List<GetUsersDTO> tempList = jdbcTemplate.query(SQL.getUsers(page, name, username, role), mapper);
        return new PageImpl<>(tempList, page, totalCount);
    }
}
