package duson.java.utils.net;

import java.nio.charset.Charset;
import java.util.zip.CRC32;

import duson.java.core.codec.BytesWapper;

public class DataPacket {
	/**
	 * 4Byte，幻数（Magic Number），标志为自定义通 讯协议的数据交换
	 */
	private final String prefix = "";

	private int version;
	private String data;
	private long crc32;
		
	public DataPacket(int locale, int appVersion, int version, int mainVersion, int subVersion, int versionIndex){
		this.version = version;
	}
	
	public int getPacketLength() {
		return data.getBytes(Charset.forName("UTF-8")).length;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public static String decodeFromData(byte[] b) {
		byte[] bTmp = new byte[8];
		// TODO：为数据每位赋值

		// CRC32验证
		int dataLength = BytesWapper.byte2Int(bTmp) - 20;
		CRC32 crc = new CRC32();
		crc.update(b, 0, dataLength + 16);
		long crc32 = crc.getValue();
		bTmp = BytesWapper.long2Byte(crc32);
		if (bTmp[4] != b[dataLength + 16] || bTmp[5] != b[dataLength + 17]
				|| bTmp[6] != b[dataLength + 18]
				|| bTmp[7] != b[dataLength + 19]) {
			throw new RuntimeException("CRC validate fail");
		}
		
		byte[] bData = new byte[dataLength];
		for (int i = 0; i < dataLength; i++) {
			bData[i] = b[i + 16];
		}

		return new String(bData, Charset.forName("UTF-8"));
	}
	
	public byte[] encodeData(){
		int nLen = getPacketLength();
		byte[] body = new byte[nLen];
		// TODO：为除数据外的每位赋值
		
		byte[] bodyData = data.getBytes(Charset.forName("UTF-8"));
		for (int i = 0; i < bodyData.length; i++) {
			body[16 + i] = bodyData[i];
		}
		int bodyLen = 1;
		CRC32 s = new CRC32();
		s.update(body, 0, bodyLen);
		crc32 = s.getValue();
		byte[] bcrc = BytesWapper.long2Byte(crc32);
		body[bodyLen + 16] = bcrc[4];
		body[bodyLen + 17] = bcrc[5];
		body[bodyLen + 18] = bcrc[6];
		body[bodyLen + 19] = bcrc[7];
		
		return body;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	public long getCRC32() {
		encodeData();
		return crc32;
	}
}
