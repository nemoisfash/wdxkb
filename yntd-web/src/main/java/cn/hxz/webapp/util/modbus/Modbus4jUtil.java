package cn.hxz.webapp.util.modbus;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * modbus通讯工具类,采用modbus4j实现
 * 
 * @author lxq
 * @dependencies modbus4j-3.0.3.jar
 * @website https://github.com/infiniteautomation/modbus4j
 */
public class Modbus4jUtil {
	/** Modbus工厂 */
	private static ModbusFactory ModbusFactory;

	static {
		if (ModbusFactory == null) {
			ModbusFactory = new ModbusFactory();
		}
	}

	/**
	 * 获取master
	 * 
	 * @return
	 * @throws ModbusInitException
	 */
	public static ModbusMaster getMaster(String host, int port) throws ModbusInitException {
		IpParameters params = new IpParameters();
		// params.setHost("localhost");//ip地址
		// // params.setHost("192.168.0.122");
		// params.setPort(502);//端口号
		params.setHost(host);
		params.setPort(port);
		ModbusMaster master = ModbusFactory.createTcpMaster(params, false);
		master.init();

		return master;
	}
	/**
	 * 获取tcpmaster
	 * 
	 * @return
	 * @throws ModbusInitException
	 */
	public static ModbusMaster getTcpMaster(String host, int port) throws ModbusInitException {
		IpParameters params = new IpParameters();
		// params.setHost("localhost");//ip地址
		// // params.setHost("192.168.0.122");
		// params.setPort(502);//端口号
		params.setHost(host);
		params.setPort(port);
		ModbusMaster master = ModbusFactory.createTcpMaster(params, false);
		master.init();
		
		return master;
	}

	/**
	 * 读取[01 Coil Status 0x]类型 开关数据
	 * 
	 * @param slaveId
	 *            slaveId
	 * @param offset
	 *            位置
	 * @return 读取值
	 * @throws ModbusTransportException
	 *             异常
	 * @throws ErrorResponseException
	 *             异常
	 * @throws ModbusInitException
	 *             异常
	 */
	public static Boolean readCoilStatus(String host, int port, int slaveId, int offset)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 01 Coil Status
		BaseLocator<Boolean> loc = BaseLocator.coilStatus(slaveId, offset);
		Boolean valu = getMaster(host, port).getValue(loc);
		return valu;
	}

	/**
	 * 读取[02 Input Status 1x]类型 开关数据
	 * 
	 * @param slaveId
	 * @param offset
	 * @return
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static Boolean readInputStatus(String host, int port, int slaveId, int offset)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 02 Input Status
		BaseLocator<Boolean> loc = BaseLocator.inputStatus(slaveId, offset);
		Boolean value = getMaster(host, port).getValue(loc);
		return value;
	}

	/**
	 * 读取[03 Holding Register类型 2x]模拟量数据
	 * 
	 * @param slaveId
	 *            slave Id
	 * @param offset
	 *            位置
	 * @param dataType
	 *            数据类型,来自com.serotonin.modbus4j.code.DataType
	 * @return
	 * @throws ModbusTransportException
	 *             异常
	 * @throws ErrorResponseException
	 *             异常
	 * @throws ModbusInitException
	 *             异常
	 */
	public static Number readHoldingRegister(String host, int port, int slaveId, int offset, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 03 Holding Register类型数据读取
		BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
		Number value = getMaster(host, port).getValue(loc);
		return value;
	}

	/**
	 * 读取[04 Input Registers 3x]类型 模拟量数据
	 * 
	 * @param slaveId
	 *            slaveId
	 * @param offset
	 *            位置
	 * @param dataType
	 *            数据类型,来自com.serotonin.modbus4j.code.DataType
	 * @return 返回结果
	 * @throws ModbusTransportException
	 *             异常
	 * @throws ErrorResponseException
	 *             异常
	 * @throws ModbusInitException
	 *             异常
	 */
	public static Number readInputRegisters(String host, int port, int slaveId, int offset, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 04 Input Registers类型数据读取
		BaseLocator<Number> loc = BaseLocator.inputRegister(slaveId, offset, dataType);
		Number value = getMaster(host, port).getValue(loc);
		return value;
	}

	/**
	 * 批量读取使用方法
	 * 
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static void batchRead(String host, int port)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {

		BatchRead<Integer> batch = new BatchRead<Integer>();

		batch.addLocator(0, BaseLocator.holdingRegister(1, 1, DataType.FOUR_BYTE_FLOAT));
		batch.addLocator(1, BaseLocator.inputStatus(1, 0));

		ModbusMaster master = getMaster(host, port);

		batch.setContiguousRequests(false);
		BatchResults<Integer> results = master.send(batch);
		System.out.println(results.getValue(0));
		System.out.println(results.getValue(1));
	}
	
	public static void main(String[] args) {
		try {
			String host="192.168.1.181";
			int port=502;
			Boolean v021 = Modbus4jUtil.readInputStatus(host, port,1, 0);
			Boolean v022 = Modbus4jUtil.readInputStatus(host, port,1, 1);
			Boolean v023 = Modbus4jUtil.readInputStatus(host, port,1, 2);
			Boolean v024 = Modbus4jUtil.readInputStatus(host, port,1, 3);
			System.out.println("v021:" + v021);
			System.out.println("v022:" + v022);
			System.out.println("v023:" + v023);
			System.out.println("v024:" + v024);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}