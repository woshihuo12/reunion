package com.googlecode.reunion.jreunion.protocol;

import java.net.InetAddress;

import com.googlecode.reunion.jreunion.server.Client;
import com.googlecode.reunion.jreunion.server.Map;
import com.googlecode.reunion.jreunion.server.Server;

public class OtherProtocol extends Protocol {

	
	
	@Override
	public String decryptServer(Client client, byte[] data) {
		InetAddress address = client.getSocket().getLocalAddress();
		int port = client.getSocket().getLocalPort();
		System.out.println(address+" "+port);
		return decryptServer(address,port,data);
	}
	
	
	
	public String decryptClient(byte[] data, InetAddress address, int port, int version, int mapId){
		
		
		
		int magic1 = OtherProtocol.magic(address, 0);
		int magic2 = OtherProtocol.magic(address, 1);
		int magic4 = magic1 - port - mapId + version;
		
		for(int i=0;i<data.length;i++){
			
			int step1 = magic2 ^ data[i];
			int step2 = step1 - 19;			
			int step3 = step2 ^ magic4;			
			data[i] = (byte)step3;
		}		
		return new String(data);
		}
	
	
	public byte[] encryptClient(String data){
		
		
		return null;
	}
	
	
	public String decryptServer(InetAddress address,int port, byte[] data) {
		
		int magic1 = magic(address, 0);
		int magic2 = (port - 17) % 131;
		for(int i=0;i<data.length;i++){
			data[i]=(byte)(((magic1 ^ data[i]) + 49) ^ magic2);
		}		
		return new String(data);
	}

	@Override
	public byte[] encryptServer(Client client, String packet) {
	
		InetAddress address = client.getSocket().getLocalAddress();
		int port = client.getSocket().getLocalPort();
		int version = client.getVersion();
		int mapId = -1;
		for(Map map :Server.getInstance().getWorld().getMaps()){
			if(map.getAddress().getAddress().equals(address)&&map.getAddress().getPort()==port) {
				mapId = map.getId();
			}
		}
		if(mapId==-1) {
			throw new RuntimeException("Invalid Map");
		}
		return encryptServer(address, port,version, mapId, packet);
		
	}
	public byte[] encryptServer(InetAddress address, int port,int version, int mapId, String packet) {
		int magic1 = magic(address, 0);
		int magic2 = magic(address, 1);
		int magic4 = magic1 - port - mapId + version;
		
		byte [] data = packet.getBytes();
		
		for(int i = 0; i<data.length; i++) {
			
			int rstep3 = data[i] ^ magic4;
			int rstep2 = rstep3 + 19;
			int rstep1 = magic2 ^ rstep2;
			data[i] = (byte)rstep1;
		}
		return data;
	}
	
	
	public static int magic(InetAddress ip, int a2)
	{
		byte [] rip = ip.getAddress();
	    
		if ( a2 == 1 )
			return rip[0] ^ rip[1] ^ rip[2] ^ rip[3];
		else
			return rip[0] + rip[1] + rip[2] + rip[3];
	   
	 }
}