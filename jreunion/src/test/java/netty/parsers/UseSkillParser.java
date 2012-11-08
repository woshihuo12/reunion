package netty.parsers;

import java.util.regex.*;

import netty.Packet;
import netty.packets.*;
import netty.packets.UseSkillPacket.TargetType;
import netty.parsers.PacketParser.Client;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Client
public class UseSkillParser implements PacketParser {

	static final Pattern regex = Pattern.compile("^use_skill (\\d+) (?:(npc|char) (\\d+))?(?: ([^ ]+))?(?: ([^ ]+))?(?: ([^ ]+))?(?: ([^ ]+))?(?: ([^ ]+))?(?: ([^ ]+))?$"); 
	
	@Override
	public Pattern getPattern() {
		return regex;
	}

	@Override
	public Packet parse(Matcher match, String input) {
		UseSkillPacket packet = new UseSkillPacket();
		int n = 0;
		packet.setSkillId(Integer.parseInt(match.group(++n)));
		
		String targetType = match.group(++n);
		if(targetType!=null){
			if(targetType.equals("npc")){
				packet.setTargetType(TargetType.NPC);
			}else if(targetType.equals("char")){
				packet.setTargetType(TargetType.CHAR);
			}
		}
		String targetId = match.group(++n);
		if(targetId!=null){
			packet.setTargetId(Integer.parseInt(targetId));			
			
		}
		while(++n<=match.groupCount()){
			String arg = match.group(n);
			if(arg!=null){
				packet.getArguments().add(arg);
			}
		}		
		return packet;
	}
	

}
