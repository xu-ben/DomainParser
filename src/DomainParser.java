import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sound.midi.SysexMessage;


public class DomainParser {

	/**
	 * 私有构造函数
	 */
	private DomainParser() {
	}
	
	/**
	 * 全局唯一实例
	 */
	private static DomainParser dp = null;
	
	/**
	 * @return 本类的实例
	 */
	public synchronized static DomainParser getInstance() {
		if (dp == null) {
			dp = new DomainParser();
		}
		return dp;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DomainParser dp = DomainParser.getInstance();
		Scanner cin = new Scanner(System.in);
		while (cin.hasNextLine()) {
			String line = cin.nextLine().trim();
			String ret = dp.getIpv6ofDomain(line);
			StringBuilder sb = new StringBuilder();
			if (ret == null) {
				ret = dp.getIpv4ofDomain(line);
			}
			if (ret == null) {
				continue;
			}
			sb.append(ret);
			sb.append('\t');
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
			System.out.print(sb);
		}
	}
	
	/**
	 * 获取domain的ipv6地址，如有多个，返回其中一个
	 * @param domain 域名
	 */
	public String getIpv6ofDomain(String domain) {
			try {
				InetAddress[] ipArray = InetAddress.getAllByName(domain);
				if (ipArray == null || ipArray.length <= 0) {
					return null;
				}
				for (InetAddress ip : ipArray) {
					if (ip instanceof Inet6Address) {
						return ip.getHostAddress();
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	/**
	 * 获取domain的ipv4地址，如有多个，返回其中一个
	 * @param domain 域名
	 */
	public String getIpv4ofDomain(String domain) {
			try {
				InetAddress[] ipArray = InetAddress.getAllByName(domain);
				if (ipArray == null || ipArray.length <= 0) {
					return null;
				}
				for (InetAddress ip : ipArray) {
					if (ip instanceof Inet4Address) {
						return ip.getHostAddress();
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public static void test() {
		String host = "www.baidu.com";
		Map<String, List<String>> map = getHostIp(host);
		if (map == null) {
			System.err.println("local dns is error!");
			return;
		}
		if (map.containsKey("ipv4")) {
			for (String ip : map.get("ipv4")) {
				System.out.println("ipv4:" + ip);
			}
		}
		if (map.containsKey("ipv6")) {
			for (String ip : map.get("ipv6")) {
				System.out.println("ipv6:" + ip);
			}
		}
		
	}
 
	//java 通过dns解析获取域名的主机ip地址
	public static Map<String, List<String>> getHostIp(String host) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			InetAddress[] ipArray = InetAddress.getAllByName(host);
			for (InetAddress ip : ipArray) {
				if (ip instanceof Inet4Address) {
					if (!(map.containsKey("ipv4"))) {
						List<String> list = new ArrayList<String>();
						map.put("ipv4", list);
					}
					map.get("ipv4").add(ip.getHostAddress());
				} else if (ip instanceof Inet6Address) {
					if (!(map.containsKey("ipv6"))) {
						List<String> list = new ArrayList<String>();
						map.put("ipv6", list);
					}
					map.get("ipv6").add(ip.getHostAddress());
				}
			}
		} catch (Exception e) {
			return null;
		}
		return map;
	}
}
