package netty.parsers.test;

import static org.junit.Assert.*;

import java.util.regex.*;

import netty.Packet;
import netty.packets.MessagePacket;
import netty.parsers.MessageParser;

import org.junit.Test;

public class MessageParserTest {

	@Test
	public void test() {
		MessageParser parser = new MessageParser();
		Pattern pattern = parser.getPattern();
		
		String msg = "this is a msg message!";
		String eventMsg = "msg "+msg;
		Matcher matcher = pattern.matcher(eventMsg);
		assertTrue(matcher.matches());
		assertFalse(pattern.matcher("msg").matches());
		assertFalse(pattern.matcher(msg).matches());
		assertFalse(pattern.matcher("msg"+msg).matches());
	
		
		Packet packet = parser.parse(matcher, eventMsg);
		assertNotNull(packet);
		assertTrue(packet instanceof MessagePacket);
		assertEquals(msg,((MessagePacket)packet).getMessage());
		assertEquals(eventMsg, packet.toString());		
		
	}

}
