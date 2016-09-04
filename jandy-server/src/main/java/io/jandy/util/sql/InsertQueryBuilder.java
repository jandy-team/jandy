package io.jandy.util.sql;

import io.jandy.domain.data.ClassObject;
import io.jandy.util.sql.conditional.Where;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author JCooky
 * @since 2016-08-04
 */
public class InsertQueryBuilder {
  private String tableName;
  private boolean ignore = false, replace = false;
  private List<Object[]> values = new ArrayList<>();
  private String[] columns;

  public InsertQueryBuilder(boolean replace) {
    this.replace = replace;
  }

  public InsertQueryBuilder ignore() {
    if (replace) throw new IllegalArgumentException("Do not set ignore when replace is true");
    this.ignore = true;
    return this;
  }

  public InsertQueryBuilder into(String tableName) {
    this.tableName = tableName;
    return this;
  }

  public InsertQueryBuilder columns(String... columns) {
    this.columns = columns;
    return this;
  }

  public InsertQueryBuilder value(Object... value) {
    this.values.add(value);
    return this;
  }

  public InsertQueryBuilder values(Stream<Object[]> values) {
    values.forEach(this::value);
    return this;
  }

  public int execute(JdbcTemplate jdbc) {
    return jdbc.update(toSql());
  }

  public String toSql() {
    StringBuilder sql = new StringBuilder(1024);

    if (replace) {
      sql.append("REPLACE INTO ");
    } else if (ignore) {
      sql.append("INSERT IGNORE INTO ");
    } else {
      sql.append("INSERT INTO ");
    }

    sql.append(tableName).append("(").append(StringUtils.join(columns, ',')).append(")").append(" VALUES ");
    sql.append(values.stream().map(row -> "(" + toValue(row) + ")").reduce((s1, s2) -> s1 + ", " +s2).orElse(""));

    return sql.toString();
  }

  private String toValue(Object[] row) {
    return Arrays.stream(row).map(SqlUtils::toValue).reduce((s1, s2) -> s1 + ", " + s2).get();
  }

  public static void main(String[] args) {
    List<ClassObject> classObjects = new ArrayList<>();
    ClassObject c = new ClassObject();
    c.setName(null);
    c.setPackageName(null);
    classObjects.add(c);

    c.setName("test");
    c.setPackageName("test");
    classObjects.add(c);

    c.setName("test2");
    c.setPackageName(null);
    classObjects.add(c);

    InsertQueryBuilder q = Query.insert().ignore().into("prof_class")
        .columns("name", "package_name");
    classObjects.forEach(co -> q.value(co.getName(), co.getPackageName()));
    System.out.println(q.toSql());

    InsertQueryBuilder q0 = Query.replace().ignore().into("prof_class")
        .columns("name", "package_name");
    classObjects.forEach(co -> q0.value(co.getName(), new SubQuery(Query.select().columns("id").from("a").where(Where.eq("name", co.getName())))));
    System.out.println(q0.toSql());
  }
}
