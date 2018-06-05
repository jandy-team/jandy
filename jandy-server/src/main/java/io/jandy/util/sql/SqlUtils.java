package io.jandy.util.sql;

import org.apache.commons.lang3.StringUtils;

/**
 * @author JCooky
 * @since 2016-08-04
 */
public class SqlUtils {
  public static String toValue(Object cell) {
    if (cell instanceof String) {
      return "'" + escapeSql(cell.toString()) + "'";
    } else if (cell instanceof SubQuery) {
      return ((SubQuery) cell).toSql();
    } else if (cell instanceof Number) {
      return cell.toString();
    } else if (cell instanceof Boolean) {
      return (Boolean) cell ? "1" : "0";
    } else if (cell == null) {
      return "null";
    } else {
      throw new IllegalArgumentException();
    }
  }

  private static String escapeSql(String str) {
    if (str == null) {
      return null;
    }
    return StringUtils.replace(str, "'", "''");
  }
}
