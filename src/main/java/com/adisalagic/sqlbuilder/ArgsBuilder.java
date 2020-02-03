package com.adisalagic.sqlbuilder;

public class ArgsBuilder {
	private String where = "";
	private boolean hasWhere = false;

	/**
	 * Adds condition.<br>
	 * DOES NOT OVERWRITE THE LAST CONDITION! See {@link ArgsBuilder#clean()}
	 * @param con Condition, where arguments should be like <code>?</code>
	 * @param firstTime if <code>true</code> add "WHERE" to condition
	 * @param objs Arguments
	 * @throws IllegalArgumentException if number of arguments less then <code>?</code>
	 * @return {@link ArgsBuilder} with condition
	 */
	public ArgsBuilder addCondition(String con, boolean firstTime, Object... objs) {
		String[] args = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			args[i] = objs[i].toString();
			args[i] = args[i].replaceAll("'", "\\\\\\\\\'");
		}
		con = con.replaceAll("'", "");
		if (firstTime) {
			hasWhere = true;
			where = " WHERE ";
		}
		where += replaceChars(con, '?', args);
		return this;
	}

	/**
	 * Replaces symbol with String
	 * @param str String that will be used
	 * @param ch Symbol that will be replaced
	 * @param res Array of strings that will replace symbol
	 * @throws IndexOutOfBoundsException If arguments less then <code>?</code>
	 * @return Replaced string
	 */
	private String replaceChars(String str, char ch, String[] res) {
		boolean isField = true;
		str = str.replaceAll("IS", "=");
		char[] chars = str.toCharArray();
		char[] simbols = {'>', '<', '=', '!'};
		for (int i = 0, j = 0; i < str.length(); i++){
			for (int k = 0; k < simbols.length; k++) {

				if (!isField){
					break;
				}
				if (chars[i] == simbols[k]){
					isField = false;
				}

			}
			if (chars[i] == ch){
				chars[i] = 'ﷻ';
				str = new String(chars);
				if (!isField){
					str = str.replaceAll("ﷻ", "'" + res[j] + "'");
				}else {
					str = str.replaceAll("ﷻ", res[j]);
				}
				chars = str.toCharArray();
				if (res.length == j + 1){
					return str;
				}else {
					j++;
				}
			}
		}
		return null;
	}

	/**
	 * Adds OR
	 * @return {@link ArgsBuilder}
	 */
	public ArgsBuilder Or(){
		where += " OR ";
		return this;
	}

	/**
	 * Adds AND
	 * @return {@link ArgsBuilder}
	 */
	public ArgsBuilder And(){
		where += " AND ";
		return this;
	}

	/**
	 * Should be used in 'Select' statement. See {@link SQLBuilder#Select(String[], String[])}
	 * @param table The table for INNER JOIN
	 * @param firstFields The first argument of condition
	 * @param secondField The second argument of condition
	 * @return {@link ArgsBuilder} with this condition
	 */
	public ArgsBuilder innerJoin(String table, String firstFields, String secondField){
		where += String.format(" INNER JOIN %s ON %s=%s", table, firstFields, secondField);
		return this;
	}

	/**
	 * Cleans condition
	 */
	public void clean(){
		where = "";
		hasWhere = false;
	}

	public String toString() {
		return where;
	}

	/**
	 * @return usage of 'WHERE'
	 */
	public boolean hasWhere() {
		return hasWhere;
	}

}


//SELECT * FROM Animal WHERE Dog AND Cat > 5