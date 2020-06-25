package com.fiesta.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.fiesta.model.vo.Company;
import com.fiesta.model.vo.Review;
import com.fiesta.model.vo.Service;
import com.fiesta.util.ServerInfo;

public class CompanyDaoImpl {
	private DataSource ds;
	
	private static CompanyDaoImpl dao = new CompanyDaoImpl();
	private CompanyDaoImpl() {
		//ds = DataSourceManager.getInstance().getConnection();
		
		//단위테스트
		try {
			Class.forName(ServerInfo.DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			System.out.println("CompanyDaoImpl :: " + e);
		}
	}
	public static CompanyDaoImpl getInstance() {
		return dao;
	}
	
	public Connection getConnection() throws SQLException {
		//return ds.getConnection();

		//단위테스트
		return DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASS);
	}
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(ps!=null) ps.close();
		if(conn!=null) conn.close();
	}
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs!=null) rs.close();
		closeAll(ps, conn);
	}
	
	//작업 영역
	//카테고리를 설정 안 하고 검색할 때
	public ArrayList<Review> lookupCompany(String searchBy, String searchContent) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, sc.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c ");
			query.append("LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("LEFT OUTER JOIN service s ");
			query.append("ON c.com_code = s.com_code ");
			if(searchBy.equals("태그")) {
				query.append("WHERE s.service_tag LIKE ? ");
				//query.append("ORDER BY c.com_code DESC ");
				ps=conn.prepareStatement(query.toString());
				ps.setString(1, "%"+searchContent+"%");
			}else if(searchBy.equals("회사명")) {
				query.append("WHERE c.com_name LIKE ? ");
				//query.append("ORDER BY c.com_code DESC ");
				ps=conn.prepareStatement(query.toString());
				ps.setString(1, "%"+searchContent+"%");
			}else {
				query.append("WHERE (s.service_tag LIKE ? ");
				query.append("OR c.com_name LIKE ?) ");
				//query.append("ORDER BY c.com_code DESC ");
				ps=conn.prepareStatement(query.toString());
				ps.setString(1, "%"+searchContent+"%");
				ps.setString(2, "%"+searchContent+"%");
			}
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;
	}

	public Company lookupCompany(String comEmail) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Company company = null;
		try {
			conn = getConnection();
			String query = "SELECT * FROM company WHERE com_email=?";
			ps = conn.prepareStatement(query);
			//System.out.println("ps completed in lookupCompany");
			
			ps.setString(1, comEmail);
			rs = ps.executeQuery();
			if(rs.next()) {
				company = new Company(
										  rs.getInt("com_code"),
										  comEmail,
										  rs.getString("com_pass"),
										  rs.getString("com_name"),
										  rs.getString("com_tel"),
										  rs.getString("com_addr"),
										  rs.getString("com_img"),
										  rs.getString("com_desc"),
										  rs.getInt("com_count"),
										  rs.getInt("comCategory_code"));  
			//System.out.println(id+ " lookup success");
			}
		} finally {
			closeAll(rs, ps, conn);
		}
		return company;
	}
	
	public ArrayList<Review> lookupCompany(int category, String searchBy, String searchContent) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c ");
			query.append("LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("LEFT OUTER JOIN service s ");
			query.append("ON c.com_code = s.com_code ");
			if(searchBy.equals("태그")) {
				query.append("WHERE c.comCategory_code = ? ");
				query.append("AND s.service_tag LIKE ? ");
				//query.append("ORDER BY c.com_code DESC ");
				ps=conn.prepareStatement(query.toString());
				ps.setInt(1, category);
				ps.setString(2, "%"+searchContent+"%");
			}else if(searchBy.equals("회사명")) {
				query.append("WHERE c.comCategory_code = ? ");
				query.append("AND c.com_name LIKE ? ");
				//query.append("ORDER BY c.com_code DESC ");
				ps=conn.prepareStatement(query.toString());
				ps.setInt(1, category);
				ps.setString(2, "%"+searchContent+"%");
			}else {
				query.append("WHERE c.comCategory_code = ? ");
				query.append("AND (s.service_tag LIKE ? ");
				query.append("OR c.com_name LIKE ?) ");
				//query.append("ORDER BY c.com_code DESC ");
				ps=conn.prepareStatement(query.toString());
				ps.setInt(1, category);
				ps.setString(2, "%"+searchContent+"%");
				ps.setString(3, "%"+searchContent+"%");
			}
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;
	}
	
	public ArrayList<Review> showAllCompany() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();

		try {
			conn=getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, c.com_email, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			//query.append("ORDER BY c.com_code DESC");
			ps=conn.prepareStatement(query.toString());
			//System.out.println(query.toString());
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}

		return list;
	}

	public ArrayList<Review> showAllCompanyByCategory(int category) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, c.com_email, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("WHERE c.comCategory_code = ? ");
			//query.append("ORDER BY c.com_code DESC ");
			
			ps=conn.prepareStatement(query.toString());
			ps.setInt(1, category);
			
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return list;
	}
	
	public ArrayList<Review> sortCompany(String sortBy) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			String query2="";
			/*if(sortBy.equals("최신순")) {
				query2="ORDER BY c.com_code DESC";
			}else if(sortBy.equals("조회순")) {
				query2="GROUP BY c.com_code ORDER BY COUNT(c.com_count) ASC";
			}else*/ if(sortBy.equals("평점순")) {
				query2="GROUP BY r.review_code ORDER BY AVG(r.review_score) ASC";
			}else {
				query2="GROUP BY r.review_code ORDER BY COUNT(r.review_code) ASC";
			}

			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c ");
			query.append("LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("LEFT OUTER JOIN service s ");
			query.append("ON c.com_code = s.com_code ");
			query.append(query2);
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));;
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return list;
	}
	
	public ArrayList<Review> sortCompany(int category, String sortBy) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			String query2="";
			/*if(sortBy.equals("최신순")) {
				query2="ORDER BY c.com_code DESC";
			}else if(sortBy.equals("조회순")) {
				query2="GROUP BY c.com_code ORDER BY COUNT(c.com_count) ASC";
			}else */if(sortBy.equals("평점순")) {
				query2="GROUP BY r.review_code ORDER BY AVG(r.review_score) ASC";
			}else {
				query2="GROUP BY r.review_code ORDER BY COUNT(r.review_code) ASC";
			}

			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c ");
			query.append("LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("LEFT OUTER JOIN service s ");
			query.append("ON c.com_code = s.com_code ");
			query.append("WHERE c.comCategory_code = ? ");
			query.append(query2);
			ps.setInt(1, category);
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return list;
	}
	
	public ArrayList<Review> sortCompany(String searchBy, String searchContent, String sortBy) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			String query2="";
			/*if(sortBy.equals("최신순")) {
				query2="ORDER BY c.com_code DESC";
			}else if(sortBy.equals("조회순")) {
				query2="GROUP BY c.com_code ORDER BY COUNT(c.com_count) ASC";
			}else*/ if(sortBy.equals("평점순")) {
				query2="ORDER BY r.review_code ORDER BY AVG(r.review_score) ASC";
			}else {
				query2="ORDER BY r.review_code ORDER BY COUNT(r.review_code) ASC";
			}

			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_code, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c ");
			query.append("LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("LEFT OUTER JOIN service s ");
			query.append("ON c.com_code = s.com_code ");
			if(searchBy.equals("태그")) {
				query.append("AND s.service_tag LIKE ? ");
				query.append(query2);
				ps=conn.prepareStatement(query.toString());
				ps.setString(1, "%"+searchContent+"%");
			}else if(searchBy.equals("회사명")) {
				query.append("AND c.com_name LIKE ? ");
				query.append("query2");
				ps=conn.prepareStatement(query.toString());
				ps.setString(1, "%"+searchContent+"%");
			}else {
				query.append("AND (s.service_tag LIKE ? ");
				query.append("OR c.com_name LIKE ? ");
				query.append("query2");
				ps=conn.prepareStatement(query.toString());
				ps.setString(1, "%"+searchContent+"%");
				ps.setString(2, "%"+searchContent+"%");
			}
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return list;
	}
	
	public ArrayList<Review> sortCompany(int category, String searchBy, String searchContent, String sortBy) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs =null;
		ArrayList<Review> list = new ArrayList<Review>();
		
		try {
			conn=getConnection();
			String query2="";
			/*if(sortBy.equals("최신순")) {
				query2="ORDER BY c.com_code DESC";
			}else if(sortBy.equals("조회순")) {
				query2="GROUP BY c.com_code ORDER BY COUNT(c.com_count) ASC";
			}else*/ if(sortBy.equals("평점순")) {
				query2="ORDER BY r.review_code ORDER BY AVG(r.review_score) ASC";
			}else {
				query2="ORDER BY r.review_code ORDER BY COUNT(r.review_code) ASC";
			}

			StringBuffer query = new StringBuffer();
			query.append("SELECT c.com_email, c.com_name, c.com_desc, c.com_img, r.review_score, r.review_desc ");
			query.append("FROM company c ");
			query.append("LEFT OUTER JOIN review r ");
			query.append("ON c.com_code = r.com_code ");
			query.append("LEFT OUTER JOIN service s ");
			query.append("ON c.com_code = s.com_code ");
			query.append("WHERE c.comCategory_code = ? ");
			if(searchBy.equals("태그")) {
				query.append("AND s.service_tag LIKE ? ");
				query.append(query2);
				ps=conn.prepareStatement(query.toString());
				ps.setString(2, "%"+searchContent+"%");
			}else if(searchBy.equals("회사명")) {
				query.append("AND c.com_name LIKE ? ");
				query.append("query2");
				ps=conn.prepareStatement(query.toString());
				ps.setString(2, "%"+searchContent+"%");
			}else {
				query.append("AND (s.service_tag LIKE ? ");
				query.append("OR c.com_name LIKE ? ");
				query.append("query2");
				ps=conn.prepareStatement(query.toString());
				ps.setString(2, "%"+searchContent+"%");
				ps.setString(3, "%"+searchContent+"%");
			}
			ps.setInt(1, category);
			rs=ps.executeQuery();
			while(rs.next()) {
				list.add(new Review(rs.getInt("r.review_score"),
						rs.getString("r.review_desc"),
						new Company(rs.getString("c.com_code"),
								rs.getString("c.com_name"),
								rs.getString("c.com_img"),
								rs.getString("c.com_desc"))));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		
		return list;
	}
	
	public void insertService(Service service) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn=  getConnection();
			String query = "INSERT INTO service(service_name, service_desc, service_img, service_tag, com_code) VALUES(?,?,?,?,?)";
			ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement 생성됨...insertService");
			
			ps.setString(1, service.getServiceName());
			ps.setString(2, service.getServiceDesc());
			ps.setString(3, service.getServiceImg());
			ps.setString(4, service.getServiceTag());
			ps.setInt(5, service.getComCode());
			
			System.out.println(ps.executeUpdate()+" row INSERT OK!!");
		}finally{
			closeAll(ps, conn);
		}
		
	}
	
	public void deleteService(int code) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn=  getConnection();
			String query = "DELETE FROM service WHERE service_code=?";
			ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement 생성됨...insertService");
			
			ps.setInt(1, code);
			
			System.out.println(ps.executeUpdate()+" row delete OK!!");
		}finally{
			closeAll(ps, conn);
		}
		
	}

	public ArrayList<Service> showAllService(String companycode) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Service> list = new ArrayList<>();
		try {
			conn = getConnection();
			String query = "SELECT * FROM service WHERE com_email=?";
			ps = conn.prepareStatement(query);
			System.out.println("PreparedStatement....showAllService..");
					
			ps.setString(1, companycode);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Service(rs.getInt("service_code"),
						rs.getString("service_name"),
						rs.getString("service_desc"),
						rs.getString("service_img"),
						rs.getString("service_tag"),
						rs.getInt("com_code")));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;
	}
	
	//단위테스트
	public static void main(String[] args) throws SQLException {
		CompanyDaoImpl dao = CompanyDaoImpl.getInstance();
		
	}
}