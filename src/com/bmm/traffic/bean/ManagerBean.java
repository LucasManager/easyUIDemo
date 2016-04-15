package com.bmm.traffic.bean;

import java.sql.SQLException;
import java.util.Vector;

import com.bmm.bean.common.BaseBean;
import com.bmm.traffic.util.PublicTools;

public class ManagerBean extends BaseBean {

	/* 主界面中, 查询所有树形结构内父级数据的sql */
	private static final String S_SQL_SEARCHLIST = "select "
			+ "a.ID, BK_LEVEL, CODE, PARENT_ID, NO_WAVE1_MIN, NO_WAVE1_MAX,WAVE1_MIN, WAVE1_MAX, "
			+ "'' as NO_WAVE1_LAST_USED_ID, "
			+ "'' as WAVE1_LAST_USED_ID, "
			+ "ISDELETE,DESC_CN,APL_OFFICE "
			+ "from "
			+ "T_TRAFFIC_BOOKING_RANGE a left join webedi.T_BOOKING_PARTY b on a.CODE=b.BOOKING_PARTY_CODE "
			+ "where CODE !='default' and (ISDELETE is null or ISDELETE <> 1) and BK_LEVEL=? and (lower(code) like ? or DESC_CN like ?) "
			+ "order by CODE ";
	/* 主界面中, 查询所有树形结构内二级数据的sql */
	private static final String S_SQL_SEARCHCHILDLIST = "select "
			+ "a.ID, BK_LEVEL, CODE, PARENT_ID, NO_WAVE1_MIN, NO_WAVE1_MAX, "
			+ "WAVE1_MIN, WAVE1_MAX, ISDELETE, DESC_CN, "
			+ "case when bk_level=5 then NO_WAVE1_LAST_USED_ID else (select NO_WAVE1_LAST_USED_ID from T_TRAFFIC_BOOKING_RANGE "
			+ "where parent_id=a.id and bk_level=a.bk_level + 1 and CODE='default' and (ISDELETE<>1 or ISDELETE is null))end as NO_WAVE1_LAST_USED_ID, "
			+ "case when bk_level=5 then WAVE1_LAST_USED_ID else (select WAVE1_LAST_USED_ID from T_TRAFFIC_BOOKING_RANGE "
			+ "where parent_id=a.id and bk_level=a.bk_level + 1 and CODE='default' and (ISDELETE<>1 or ISDELETE is null))end as WAVE1_LAST_USED_ID "
			+ "from "
			+ "T_TRAFFIC_BOOKING_RANGE a left join webedi.T_BOOKING_PARTY b on a.CODE=b.BOOKING_PARTY_CODE "
			+ "where PARENT_ID=?";

	
	/* 编辑页面中, 根据Code值查询单条BusinessPartner数据的sql */
	private static final String S_SQL_SEARCHBP = "SELECT t.ID,BK_LEVEL,CODE,JUDGE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_MIN,WAVE1_MAX,WAVE1_LAST_USED_ID,ISDELETE,DESC_CN,APL_OFFICE FROM T_TRAFFIC_BOOKING_RANGE t left join webedi.T_BOOKING_PARTY b on t.CODE=b.BOOKING_PARTY_CODE  where t.CODE= ? and ISDELETE is null ";
	/* 编辑页面中, 根据Code值和ParentId查询单条Origin数据的sql */
	private static final String S_SQL_SEARCHORIGIN = "SELECT ID,BK_LEVEL,CODE,JUDGE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_MIN,WAVE1_MAX,WAVE1_LAST_USED_ID,ISDELETE FROM T_TRAFFIC_BOOKING_RANGE t where t.CODE= ? and t.PARENT_ID =? and ISDELETE is null ";
	/* 编辑页面中, 根据Code值查询单条Location数据的sql */
	private static final String S_SQL_SEARCHLOCATION = "SELECT ID,BK_LEVEL,CODE,JUDGE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_MIN,WAVE1_MAX,WAVE1_LAST_USED_ID,ISDELETE FROM T_TRAFFIC_BOOKING_RANGE t where t.CODE= ? and t.bk_level='3' and ISDELETE is null ";
	/* 编辑页面中, 查询所有Location的id及name的sql */
	private static final String S_SQL_LOCATION = "SELECT cast(' ' as nvarchar2(15))  comid, cast('ALL' as nvarchar2(15)) text from dual t  union SELECT DISTINCT t.APL_OFFICE as comid,t.APL_OFFICE as text from webedi.T_BOOKING_PARTY t where t.APL_OFFICE is not null";
	/* 编辑页面中, 查询所有BusinessPartner的id及name的sql */
	private static final String S_SQL_BPCODE = "SELECT t.BOOKING_PARTY_CODE as comid,t.DESC_CN as text from webedi.T_BOOKING_PARTY t where t.APL_OFFICE = ? ORDER BY DESC_CN";
	/* 编辑页面中, 查询所有Origin的id及name的sql */
	private static final String S_SQL_ORIGIN = "select unloc_cd as comid,location as text from webedi.t_ref_location where iso_country_cd = 'CN' ORDER BY location";

	/* 往T_TRAFFIC_BOOKING_RANGE插入一条新的数据的sql */
	private static final String S_SQL_INSERT = "INSERT INTO T_TRAFFIC_BOOKING_RANGE (ID,BK_LEVEL,CODE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,WAVE1_MIN,WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_LAST_USED_ID) VALUES (SEQ_T_TRAFFIC_BOOKING_RANGE.nextval,?,?,?,?,?,?,?,?,?)";
	/* 追加一条Default数据的sql */
	private static final String S_SQL_INSERT_DEFAULT = "INSERT INTO T_TRAFFIC_BOOKING_RANGE (ID,BK_LEVEL,CODE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,WAVE1_MIN,WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_LAST_USED_ID) SELECT SEQ_T_TRAFFIC_BOOKING_RANGE.nextval,?,'default',ID,?,?,?,?,?,? FROM T_TRAFFIC_BOOKING_RANGE WHERE code = ?";
	/*  */
	private static final String S_SQL_UPDATE = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_LAST_USED_ID=?,WAVE1_LAST_USED_ID=? where id=(select PARENT_ID from T_TRAFFIC_BOOKING_RANGE where id=?)";
	/* 将旧的数据的ISDELETE设为1, 保存旧的日志 */
	private static final String S_SQL_UPDATESTATUS = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_MAX=NO_WAVE1_LAST_USED_ID,WAVE1_MAX=WAVE1_LAST_USED_ID,ISDELETE='1' where id=?";
	/* 修改Default的值的sql */
	private static final String S_SQL_UPDATEDEFAULT = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_MIN=?,NO_WAVE1_MAX=?,WAVE1_MIN=?,WAVE1_MAX=?,NO_WAVE1_LAST_USED_ID=?,WAVE1_LAST_USED_ID=?  where PARENT_ID=? and code='default'";
	/* 修改所有子节点的ParentId值 */
	private static final String S_SQL_UPDATEPARTENDID = "UPDATE T_TRAFFIC_BOOKING_RANGE set PARENT_ID=(select max(id) from T_TRAFFIC_BOOKING_RANGE where code=? and ISDELETE is null) where PARENT_ID=?";
	private static final String S_SQL_REMOVE = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_MAX=NO_WAVE1_LAST_USED_ID,WAVE1_MAX=WAVE1_LAST_USED_ID,ISDELETE='1' where id in (%s)";

	private String locationcode;
	private String bpcode;
	private String code;
	private String parentid;
	private String parentcode;
	private String level;
	private String wavemax;
	private String wavemin;
	private String wavelast;
	private String nowavemax;
	private String nowavemin;
	private String nowavelast;
	private String type;

	public ManagerBean() {
		locationcode = "";
		bpcode = "";
		level = "";
		wavemax = "";
		wavemin = "";
		nowavemax = "";
		nowavemin = "";
	}

	/**
	 * 根据Code值加载对应的Location数据
	 * 
	 * @param page
	 * @param rows
	 * @return vec 结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> getLoadlocation(int page, int rows) throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_SEARCHLOCATION;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = code;
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 查询所有父级信息
	 * 
	 * @param page
	 * @param rows
	 * @return vec 结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> getLoadList(int page, int rows) throws SQLException, Exception {
		Vector<?> vec = null; // 结果集
		String sql = S_SQL_SEARCHLIST;
		String[] parameter = null;
		parameter = new String[3];
		parameter[0] = level;
		// 对于Code值忽略大小写
		parameter[1] = "%" + code.toLowerCase() + "%";
		parameter[2] = "%" + code.toLowerCase() + "%";
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 查询所有子级信息
	 * 
	 * @return vec 结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> getLoadChildList(int page, int rows) throws SQLException, Exception {
		Vector<?> vec = null; // 结果集
		String sql = S_SQL_SEARCHCHILDLIST;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = parentid;
		// 判断是否做筛选,如果有对应的查询条件,则将将查询条件拼进SQL语句的WHERE条件中
		if ("Active".equals(type)) {
			sql += " and ISDELETE is null ";
		}
		sql += " ORDER BY CODE,ISDELETE DESC,a.ID DESC";
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 根据ParentId返回所属的子节点信息
	 * 
	 * @param page
	 * @param rows
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 * 
	 *             public Vector<?> getLoadDefaultList(int page, int rows)
	 *             throws SQLException, Exception { // 结果集 Vector<?> vec = null;
	 *             String sql = S_SQL_SEARCHCHILDLIST; String[] parameter =
	 *             null; parameter = new String[1]; parameter[0] = parentid; if
	 *             ("valid".equals(type)) { sql += " and ISDELETE is null "; }
	 *             sql += " ORDER BY CODE,ISDELETE DESC,ID DESC"; vec =
	 *             extractJSONArray(sql, parameter, page, rows); return vec; }
	 */

	/**
	 * 根据Code值查询Bp及同父级的Default
	 * 
	 * @param page
	 * @param rows
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> loadBusinessPartner(int page, int rows) throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_SEARCHBP;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = code;
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 根据Code值和ParentId查询Origin及同父级的Default
	 * 
	 * @param page
	 * @param rows
	 * @return vec 结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> loadOrigin(int page, int rows) throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_SEARCHORIGIN;
		String[] parameter = null;
		parameter = new String[2];
		parameter[0] = code;
		parameter[1] = parentid;
		vec = extractJSONArray(sql, parameter, page, rows);
		return vec;
	}

	/**
	 * 查询Location下拉列表的所有数据
	 * 
	 * @return vec 查询结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> getLocationCombo() throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_LOCATION;
		String[] parameter = null;
		vec = extractComboArray(sql, parameter, "comid", "text");
		return vec;
	}

	/**
	 * 查询Bp下拉列表的所有数据
	 * 
	 * @return vec 查询结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> getBpCombo() throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null; 
		String sql = S_SQL_BPCODE;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = locationcode;
		if (locationcode == null || "".equals(locationcode)) {
			sql += " OR 1=1 ";
		}
		vec = extractComboArray(sql, parameter, "comid", "text");
		return vec;
	}

	/**
	 * 查询Origin下拉列表的所有数据
	 * 
	 * @return vec 查询结果集
	 * @throws SQLException
	 * @throws Exception
	 */
	public Vector<?> getOriginCombo() throws SQLException, Exception {
		// 结果集
		Vector<?> vec = null;
		String sql = S_SQL_ORIGIN;
		String[] parameter = null;
		vec = extractComboArray(sql, parameter, "comid", "text");
		return vec;
	}

	/**
	 * 新增Location信息 在新增完一条Location后, 要生成一条BL#范围跟他一样的Default值
	 * 
	 * @param type	类型, 通过常量值来区分本次是新增Location还是新增该Location对应的Default值
	 * @throws Exception
	 */
	public void addLocation(String type) throws Exception {
		if (!"default".equals(type)) {
			// 添加location信息
			String sql = S_SQL_INSERT;
			String[] parameter = null;
			parameter = new String[9];
			parameter[0] = level;
			parameter[1] = code;
			parameter[2] = parentid;
			parameter[3] = nowavemin;
			parameter[4] = nowavemax;
			parameter[5] = wavemin;
			parameter[6] = wavemax;
			parameter[7] = nowavelast;
			parameter[8] = wavelast;
			update(sql, parameter);
		} else {
			// 添加location节点下default信息
			String sql = S_SQL_INSERT_DEFAULT;
			String[] parameter = null;
			parameter = new String[8];
			parameter[0] = level;
			parameter[1] = nowavemin;
			parameter[2] = nowavemax;
			parameter[3] = wavemin;
			parameter[4] = wavemax;
			parameter[5] = nowavelast;
			parameter[6] = wavelast;
			parameter[7] = code;
			update(sql, parameter);
		}
	}

	/**
	 * 修改Location信息, 不直接做修改操作, 而是将原来的记录保存, 
	 * 	同时用修改后的数据创建一条新的记录, 并将原本所有的子集的ParentId全部指向新的Location
	 * 
	 * @param id  对应的id主键
	 * @throws Exception
	 */
	public void updateLocation(String id) throws Exception {
		// 先将location状态改为删除状态
		String sql = S_SQL_UPDATESTATUS;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = id;
		update(sql, parameter);
		// 添加新的Location信息
		addLocation("");
		// 修改location下的子节点的parentId
		sql = S_SQL_UPDATEPARTENDID;
		parameter = new String[2];
		parameter[0] = id;
		parameter[1] = code;
		update(sql, parameter);
	}

	/**
	 * 修改Default信息, 不直接做修改操作, 而是将原来的记录保存, 将ISDELETE设为1, 将lastUseId设为MaxId
	 * 	同时用修改后的数据创建一条新的记录
	 * 
	 * @param id	对应的id主键
	 * @throws Exception
	 */
	public void updateDefault(String id) throws Exception {
		// 将default状态改为删除状态
		String sql = S_SQL_UPDATESTATUS;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = id;
		update(sql, parameter);
		// 将default的父节点信息
//		sql = S_SQL_UPDATE;
//		parameter = new String[3];
//		parameter[0] = nowavelast;
//		parameter[1] = wavelast;
//		parameter[2] = id;
//		update(sql, parameter);
		// 添加default信息
		sql = S_SQL_INSERT;
		parameter = new String[9];
		parameter[0] = level;
		parameter[1] = code;
		parameter[2] = parentid;
		parameter[3] = nowavemin;
		parameter[4] = nowavemax;
		parameter[5] = wavemin;
		parameter[6] = wavemax;
		parameter[7] = nowavelast;
		parameter[8] = wavelast;
		update(sql, parameter);
	}

	/**
	 * 添加bussiness party信息
	 * 
	 * @param type
	 * @throws Exception
	 */
	public void addbp(String type) throws Exception {
		if (!"default".equals(type)) {
			String sql = S_SQL_INSERT;// 添加bussiness party信息
			String[] parameter = null;
			parameter = new String[9];
			parameter[0] = level;
			parameter[1] = code;
			parameter[2] = parentid;
			parameter[3] = nowavemin;
			parameter[4] = nowavemax;
			parameter[5] = wavemin;
			parameter[6] = wavemax;
			parameter[7] = nowavelast;
			parameter[8] = wavelast;
			update(sql, parameter);
		} else {
			String sql = S_SQL_INSERT_DEFAULT;// 添加bussiness party下的default信息
			String[] parameter = null;
			parameter = new String[8];
			parameter[0] = level;
			parameter[1] = nowavemin;
			parameter[2] = nowavemax;
			parameter[3] = wavemin;
			parameter[4] = wavemax;
			parameter[5] = nowavemin;
			parameter[6] = wavemin;
			parameter[7] = code;
			update(sql, parameter);
		}
	}

	/**
	 * 修改bussiness party信息
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void updatebp(String id) throws Exception {
		// 将bussiness party状态改为删除状态
		String sql = S_SQL_UPDATESTATUS;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = id;
		update(sql, parameter);
		// 添加bussiness party信息
		addbp("");
		// 修改bussiness party下的default信息
		sql = S_SQL_UPDATEDEFAULT;
		parameter = new String[7];
		parameter[0] = nowavemin;
		parameter[1] = nowavemax;
		parameter[2] = wavemin;
		parameter[3] = wavemax;
		parameter[4] = nowavelast;
		parameter[5] = wavelast;
		parameter[6] = id;
		update(sql, parameter);
		// 修改bussiness party下的子节点的paretendid
		sql = S_SQL_UPDATEPARTENDID;
		parameter = new String[2];
		parameter[0] = code;
		parameter[1] = id;
		update(sql, parameter);
	}

	/**
	 * 添加origin信息
	 * 
	 * @throws Exception
	 */
	public void addorign() throws Exception {
		String sql = S_SQL_INSERT;
		String[] parameter = null;
		parameter = new String[9];
		parameter[0] = level;
		parameter[1] = code;
		parameter[2] = parentid;
		parameter[3] = nowavemin;
		parameter[4] = nowavemax;
		parameter[5] = wavemin;
		parameter[6] = wavemax;
		parameter[7] = nowavelast;
		parameter[8] = wavelast;
		update(sql, parameter);
	}

	/**
	 * 修改orign信息
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void updateorign(String id) throws Exception {
		String sql = S_SQL_UPDATESTATUS;
		String[] parameter = null;
		parameter = new String[1];
		parameter[0] = id;
		update(sql, parameter);
		addorign();
	}

	/**
	 * 移除信息
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void remove(String[] id) throws Exception {
		String sql = S_SQL_REMOVE;
		String[] parameter = null;
		sql = String.format(sql, PublicTools.ArrayToString(id));
		sql += " or PARENT_ID in (%s)";
		sql = String.format(sql, PublicTools.ArrayToString(id));
		update(sql, parameter);
	}

	public String getLocationcode() {
		return locationcode;
	}

	public void setLocationcode(String locationcode) {
		this.locationcode = locationcode;
	}

	public String getBpcode() {
		return bpcode;
	}

	public void setBpcode(String bpcode) {
		this.bpcode = bpcode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getWavemax() {
		return wavemax;
	}

	public void setWavemax(String wavemax) {
		this.wavemax = wavemax;
	}

	public String getWavemin() {
		return wavemin;
	}

	public void setWavemin(String wavemin) {
		this.wavemin = wavemin;
	}

	public String getNowavemax() {
		return nowavemax;
	}

	public void setNowavemax(String nowavemax) {
		this.nowavemax = nowavemax;
	}

	public String getNowavemin() {
		return nowavemin;
	}

	public void setNowavemin(String nowavemin) {
		this.nowavemin = nowavemin;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getParentcode() {
		return parentcode;
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWavelast() {
		return wavelast;
	}

	public void setWavelast(String wavelast) {
		this.wavelast = wavelast;
	}

	public String getNowavelast() {
		return nowavelast;
	}

	public void setNowavelast(String nowavelast) {
		this.nowavelast = nowavelast;
	}
}
