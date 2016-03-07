package duson.java.utils.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import duson.java.core.codec.BytesWapper;

public class SocketUtils {
	
	private String sendData(String ip, int port, DataPacket packet) {
		String retStr = "";
		
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out = null;

		try {
			socket = new Socket(ip, port);
			socket.setKeepAlive(true);
			socket.setSoTimeout(30 * 1000);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			out.write(packet.encodeData());
			out.flush();

			if (!socket.isInputShutdown()) {
				byte[] bHead = new byte[128];
				int nRecv = in.read(bHead, 0, 128);
				while (nRecv < 12) {
					int nContinue = in.read(bHead, nRecv, 12);
					if (nContinue >= 0) {
						nRecv += nContinue;
					} else {
						return "";
					}
				}

				byte[] bDataLen = new byte[4];
				bDataLen[0] = bHead[12];
				bDataLen[1] = bHead[13];
				bDataLen[2] = bHead[14];
				bDataLen[3] = bHead[15];

				// 数据包完整长度，开始完整接收数据 (data length + 12 bytes head length)
				int length = BytesWapper.byte2Int(bDataLen);
				if (length >= 0) {
					byte[] bData = new byte[length];
					// 先把头数据放进去
					for (int i = 0; i < nRecv; i++) {
						bData[i] = bHead[i];
					}
					while (nRecv < length) {
						int l = in.read(bData, nRecv, length - nRecv);
						if (l >= 0) {
							nRecv += l;
						} else {
							return "";
						}
					}
					retStr = DataPacket.decodeFromData(bData);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return retStr;
	}
}
