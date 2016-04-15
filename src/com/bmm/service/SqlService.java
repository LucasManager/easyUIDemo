package com.bmm.service;

public class SqlService {

	/**
	 * Range SQL
	 * 
	 */
	/* ��������, ��ѯ�������νṹ�ڸ������ݵ�sql */
	protected static final String S_SQL_SEARCHLIST = "select "
			+ "a.ID, BK_LEVEL, CODE, PARENT_ID, NO_WAVE1_MIN, NO_WAVE1_MAX,WAVE1_MIN, WAVE1_MAX, "
			+ "'' as NO_WAVE1_LAST_USED_ID, '' as WAVE1_LAST_USED_ID, ISDELETE,DESC_CN,APL_OFFICE from "
			+ "T_TRAFFIC_BOOKING_RANGE a left join webedi.T_BOOKING_PARTY b on a.CODE=b.BOOKING_PARTY_CODE "
			+ "where CODE !='default' and (ISDELETE is null or ISDELETE <> 1) and BK_LEVEL=? and (lower(code) like ? or DESC_CN like ?) and (lower(CODE) like ? or lower(APL_OFFICE) like ?) "
			+ "order by CODE ";
	/* ��������, ��ѯ�������νṹ�ڶ������ݵ�sql */
	protected static final String S_SQL_SEARCHCHILDLIST = "select "
			+ "a.ID, BK_LEVEL, CODE, PARENT_ID, NO_WAVE1_MIN, NO_WAVE1_MAX, "
			+ "WAVE1_MIN, WAVE1_MAX, ISDELETE, DESC_CN, "
			+ "case when (bk_level=5 or code='default') then NO_WAVE1_LAST_USED_ID else (select NO_WAVE1_LAST_USED_ID from T_TRAFFIC_BOOKING_RANGE "
			+ "where rownum=1 and parent_id=a.id and bk_level=a.bk_level + 1 and CODE='default' and (ISDELETE<>1 or ISDELETE is null)) end as NO_WAVE1_LAST_USED_ID, "
			+ "case when (bk_level=5 or code='default') then WAVE1_LAST_USED_ID else (select WAVE1_LAST_USED_ID from T_TRAFFIC_BOOKING_RANGE "
			+ "where rownum=1 and  parent_id=a.id and bk_level=a.bk_level + 1 and CODE='default' and (ISDELETE<>1 or ISDELETE is null))end as WAVE1_LAST_USED_ID "
			+ "from " + "T_TRAFFIC_BOOKING_RANGE a left join webedi.T_BOOKING_PARTY b on a.CODE=b.BOOKING_PARTY_CODE "
			+ "where PARENT_ID=? and (lower(code) like ? or DESC_CN like ?)";

	/* �༭ҳ����, ����Codeֵ��ѯ����BusinessPartner���ݵ�sql */
	protected static final String S_SQL_SEARCHBP = "SELECT t.ID,BK_LEVEL,CODE,JUDGE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_MIN,WAVE1_MAX,WAVE1_LAST_USED_ID,ISDELETE,DESC_CN,APL_OFFICE FROM T_TRAFFIC_BOOKING_RANGE t left join webedi.T_BOOKING_PARTY b on t.CODE=b.BOOKING_PARTY_CODE  where t.CODE= ? and ISDELETE is null ";
	/* �༭ҳ����, ����Codeֵ��ParentId��ѯ����Origin���ݵ�sql */
	protected static final String S_SQL_SEARCHORIGIN = "SELECT ID,BK_LEVEL,CODE,JUDGE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_MIN,WAVE1_MAX,WAVE1_LAST_USED_ID,ISDELETE FROM T_TRAFFIC_BOOKING_RANGE t where t.CODE= ? and t.PARENT_ID =? and ISDELETE is null ";
	/* �༭ҳ����, ����Codeֵ��ѯ����Location���ݵ�sql */
	protected static final String S_SQL_SEARCHLOCATION = "SELECT ID,BK_LEVEL,CODE,JUDGE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_MIN,WAVE1_MAX,WAVE1_LAST_USED_ID,ISDELETE FROM T_TRAFFIC_BOOKING_RANGE t where t.CODE= ? and t.bk_level='3' and ISDELETE is null ";
	/* �༭ҳ����, ��ѯ����Location��id��name��sql */
	protected static final String S_SQL_LOCATION = "SELECT cast(' ' as nvarchar2(15))  comid, cast('ALL' as nvarchar2(15)) text from dual t  union SELECT DISTINCT t.APL_OFFICE as comid,t.APL_OFFICE as text from webedi.T_BOOKING_PARTY t where t.APL_OFFICE is not null";
	/* �༭ҳ����, ��ѯ����BusinessPartner��id��name��sql */
	protected static final String S_SQL_BPCODE = "SELECT t.BOOKING_PARTY_CODE as comid,t.DESC_CN as text from webedi.T_BOOKING_PARTY t join webedi.T_REF_OFFICE r on t.OFFICE_ID=r.ID where r.OFFICE = ? ORDER BY nlssort(DESC_CN, 'NLS_SORT=SCHINESE_PINYIN_M')";
	/* �༭ҳ����, ��ѯ����Origin��id��name��sql */
	protected static final String S_SQL_ORIGIN = "select unloc_cd as comid,location as text from webedi.t_ref_location where iso_country_cd = 'CN' ORDER BY location";

	/* ��T_TRAFFIC_BOOKING_RANGE����һ���µ����ݵ�sql */
	protected static final String S_SQL_INSERT = "INSERT INTO T_TRAFFIC_BOOKING_RANGE (ID,BK_LEVEL,CODE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,WAVE1_MIN,WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_LAST_USED_ID) VALUES (SEQ_T_TRAFFIC_BOOKING_RANGE.nextval,?,?,?,?,?,?,?,?,?)";
	/* ׷��һ��Default���ݵ�sql */
	protected static final String S_SQL_INSERT_DEFAULT = "INSERT INTO T_TRAFFIC_BOOKING_RANGE (ID,BK_LEVEL,CODE,PARENT_ID,NO_WAVE1_MIN,NO_WAVE1_MAX,WAVE1_MIN,WAVE1_MAX,NO_WAVE1_LAST_USED_ID,WAVE1_LAST_USED_ID) SELECT SEQ_T_TRAFFIC_BOOKING_RANGE.nextval,?,'default',ID,?,?,?,?,?,? FROM T_TRAFFIC_BOOKING_RANGE WHERE ID = (select max(ID) from T_TRAFFIC_BOOKING_RANGE where code=?)";
	/*  */
	protected static final String S_SQL_UPDATE = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_LAST_USED_ID=?,WAVE1_LAST_USED_ID=? where id=(select PARENT_ID from T_TRAFFIC_BOOKING_RANGE where id=?)";
	/* ���ɵ����ݵ�ISDELETE��Ϊ1, ����ɵ���־ */
	protected static final String S_SQL_UPDATESTATUS = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_MAX=NO_WAVE1_LAST_USED_ID,WAVE1_MAX=WAVE1_LAST_USED_ID,ISDELETE='1' where id=? or (PARENT_ID=? and code='default')";
	/* �޸�Default��ֵ��sql */
	protected static final String S_SQL_UPDATEDEFAULT = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_MIN=?,NO_WAVE1_MAX=?,WAVE1_MIN=?,WAVE1_MAX=?,NO_WAVE1_LAST_USED_ID=?,WAVE1_LAST_USED_ID=?  where PARENT_ID=? and code='default'";
	/* �޸������ӽڵ��ParentIdֵ */
	protected static final String S_SQL_UPDATEPARTENDID = "UPDATE T_TRAFFIC_BOOKING_RANGE set PARENT_ID=(select max(id) from T_TRAFFIC_BOOKING_RANGE where code=? and ISDELETE is null) where PARENT_ID=? and CODE!='default'";
	/* ���ڵ��isdeleteֵ��Ϊ1����ɾ�� */
	protected static final String S_SQL_REMOVE = "UPDATE T_TRAFFIC_BOOKING_RANGE set NO_WAVE1_MAX=NO_WAVE1_LAST_USED_ID,WAVE1_MAX=WAVE1_LAST_USED_ID,ISDELETE='1' where id in (%s) or PARENT_ID in (%s)";

	/**
	 * Error SQL
	 * 
	 */
	/* ��ѯ������־sql */
	protected static final String S_SQL_ERROR_SEARCH = "SELECT ID,ERROR_DESC,ERROR_FILE_NAME,to_char(CREATE_DATE,'yyyy-MM-dd  hh24:mi:ss') as CREATE_DATE FROM T_TRAFFIC_CONVERT_ERROR_LOG WHERE MODULES = ? and lower(ERROR_FILE_NAME) LIKE ? ";

	/* ͳ�ƴ�����־����sql */
	protected static final String S_SQL_ERROR_STATISTICS = "SELECT SUM(FILE_QUANTITY) as fileQty , SUM(ERR_FILE_QUANTITY) as errQty , SUM(WBP_FILE_QUANTITY) as wbpQty ,SUM(Q2C_FILE_QUANTITY) as q2cQty FROM T_TRAFFIC_CONVERT_DAILY_LOG WHERE MODULES = ?";

	/**
	 * Email SQL
	 * 
	 */
	/* ��ѯ�ʼ��б�sql */
	protected static final String S_SQL_EMAIL_SEARCH = "SELECT "
			+ "ID, ADDRESS, TITLE, STATUS, MAIL_TYPE,CONTENT as CONTENTCLOB, to_char(CREATEDATE,'yyyy-MM-dd  hh24:mi:ss') as CREATEDATE,SENDINFO "
			+ "from T_NOTIFICATION_EMAIL "
			+ "WHERE 1=1" ;

	/* ��ʱ�첽��ѯ���µ��ʼ���Ϣsql */
	protected static final String S_SQL_EMAIL_REFRESHSTATUS = "SELECT ID, ADDRESS, TITLE, STATUS,CONTENT as CONTENTCLOB, MAIL_TYPE,to_char(CREATEDATE,'yyyy-MM-dd  hh24:mi:ss') as CREATEDATE from T_NOTIFICATION_EMAIL";

	/* �����ռ��˵�ַ��״̬sql */
	protected static final String S_SQL_EMAIL_UPDATE_ADDRESS = "INSERT INTO T_NOTIFICATION_EMAIL(ID,  STATUS, TITLE,ADDRESS,CONTENT, MAIL_TYPE,CREATEDATE) "
			+ "SELECT SEQ_T_NOTIFICATION_EMAIL.nextval as ID,? as status,TITLE,? as address,CONTENT,MAIL_TYPE,SYSDATE as CREATEDATE from T_NOTIFICATION_EMAIL where ID=?";

	/* �����ʼ�״̬sql */
	protected static final String S_SQL_EMAIL_UPDATE_STATUS = "INSERT INTO T_NOTIFICATION_EMAIL(ID,  STATUS, TITLE,ADDRESS,CONTENT, MAIL_TYPE,CREATEDATE) "
			+ "SELECT SEQ_T_NOTIFICATION_EMAIL.nextval as ID,? as status,TITLE,ADDRESS,CONTENT,MAIL_TYPE,SYSDATE as CREATEDATE from T_NOTIFICATION_EMAIL WHERE ID IN (%s)";

	/* ͳ���ʼ�������sql */
	protected static final String S_SQL_EMAIL_STATISTICS = "SELECT STATUS,count(ID) as count FROM T_NOTIFICATION_EMAIL WHERE 1=1 ";

}