package com.autotest.repository;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.autotest.exception.KellyException;
import com.autotest.model.Steps;

@Repository
public class AutomationRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(AutomationRepository.class);
	private static final String LIMIT = " FETCH NEXT 5 ROWS ONLY";
	
	@Qualifier("mssqlJdbcTemplate")
	@Autowired
	private JdbcTemplate mssqlJdbcTemplate;
	
	@Qualifier("oracleJdbcTemplate")
	@Autowired
	private JdbcTemplate oracleJdbcTemplate;
	
	@Qualifier("mssqlEdwsqaJdbcTemplate")
	@Autowired
	private JdbcTemplate mssqlEdwsqaJdbcTemplate;
	
	public List<Map<String, Object>> countList(Steps steps) throws KellyException {
		try {
//			return jdbcTemplate.queryForObject(steps.get(0).getStepQuery(), Integer.class);  // for sqlserver
//			return jdbcTemplate.queryForList(formulateQuery(steps.getStepQuery()));
			
//			return jdbcTemplate.queryForList("select emp_id from kellyservices.employee union all select id from kellytest.hr");  // same database two schema
//			return jdbcTemplate.queryForList("select emp_id from kellyservices.employee union all select id from SYS.EMPLOYEE");  // different database diff schema
//			return mssqlJdbcTemplate.queryForList("select * from MDMInbound.DBO.Inbound_C_L_SERVICE_BURDEN_RATES");  // for oracle only
			return queryForListDs(steps);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new KellyException(e.getMessage(), e.getCause());
		}

	}
	
	public int count(Steps steps) throws KellyException {
		try {
//			return jdbcTemplate.queryForObject(formulateQuery(steps.getStepQuery()), Integer.class);
//			oracleJdbcTemplate.queryForObject("select COUNT(*) from Ksn_Absence_Notified_By", Integer.class);
//			return oracleJdbcTemplate.queryForObject("select count(*) from MDMInbound.DBO.Inbound_C_L_SERVICE_BURDEN_RATES", Integer.class);
			return queryForObjectDs(steps);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new KellyException(e.getMessage());
		}

	}
	
	private String formulateQuery(final String query) {
		if (query.contains("lt;")) {
			query.replaceAll("&amp;", "");
			return query.replaceAll("&amp;lt;", "<");
		}
		
		if (query.contains("gt;")) {
			query.replaceAll("&amp;", "");
			return query.replaceAll("&amp;gt;", ">");
		}
		
		return query;
	}
	
	private int queryForObjectDs(Steps steps) {
		try {
			try {
//				mssqlEdwsqaJdbcTemplate.queryForObject("select count(*) from etlauto.Ksn_Absence_Notified_By", Integer.class);
//				return mssqlJdbcTemplate.queryForObject("select count(*) from OdsFlLegacyJobCodeHierarchy", Integer.class);
				return mssqlJdbcTemplate.queryForObject(formulateQuery(steps.getStepQuery()), Integer.class);
			} catch (Exception e) {
//				return oracleJdbcTemplate.queryForObject("select COUNT(*) from OdsFlLegacyJobCodeHierarchy", Integer.class);
				return oracleJdbcTemplate.queryForObject(formulateQuery(steps.getStepQuery()), Integer.class);
			}
		} catch (Exception e) {
//			return mssqlEdwsqaJdbcTemplate.queryForObject("select count(*) from OdsFlLegacyJobCodeHierarchy", Integer.class);
			return mssqlEdwsqaJdbcTemplate.queryForObject(formulateQuery(steps.getStepQuery()), Integer.class);
		}
		
	}
	
	private List<Map<String, Object>> queryForListDs(Steps steps) {
		try {
			try {
//				mssqlJdbcTemplate.queryForObject("select count(*) from MDMInbound.DBO.Inbound_C_L_SERVICE_BURDEN_RATES", Integer.class);
				return mssqlJdbcTemplate.queryForList(formulateQuery(steps.getStepQuery()));
			} catch (Exception e) {
//				mssqlEdwsqaJdbcTemplate.queryForObject("select count(*) from OdsFlLegacyJobCodeHierarchy", Integer.class);
//				return oracleJdbcTemplate.queryForList("select * from Ksn_Absence_Notified_By" + LIMIT);
				return oracleJdbcTemplate.queryForList(formulateQuery(steps.getStepQuery()));
			}
		} catch (Exception e) {
//			return mssqlEdwsqaJdbcTemplate.queryForList("select * from etl.stgksn_customer_status");
			return mssqlEdwsqaJdbcTemplate.queryForList(formulateQuery(steps.getStepQuery()));
		}

	}

}
