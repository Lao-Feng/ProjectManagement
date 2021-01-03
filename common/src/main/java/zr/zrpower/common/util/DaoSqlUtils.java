package zr.zrpower.common.util;

/**
 * 数据库SQL工具类
 * @author lwk
 *
 */
public class DaoSqlUtils {

	/**
	 * 防止SQL注入攻击
	 * @param sql
	 * @return
	 */
	public static String transactSqlInjection(String sql) {
		if (StringUtils.isBlank(sql)) {
			return StringUtils._BLANK;
		}
        return sql.replaceAll("([';]+|(--)+).*", " ");
    }
}