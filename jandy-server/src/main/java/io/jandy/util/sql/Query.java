package io.jandy.util.sql;

/**
 * @author JCooky
 * @since 2016-08-04
 */
public class Query {
  public static InsertQueryBuilder insert() {
    return new InsertQueryBuilder(false);
  }

  public static InsertQueryBuilder replace() {
    return new InsertQueryBuilder(true);
  }

  public static SelectQueryBuilder select() {
    return new SelectQueryBuilder();
  }

  public static SubQuery subQuery(SelectQueryBuilder q) {
    return new SubQuery(q);
  }

  public static <ID> BatchUpdateQueryBuilder<ID> update(String table) { return new BatchUpdateQueryBuilder<>(table); }
}
