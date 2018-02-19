package org.dzunja.projekat.webforum.service;



import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.dzunja.projekat.webforum.model.Message;

public class MessageService extends AbstractService<Message> {

	public static final String dataFileName = "messages.json";
	
	public MessageService() {
		super(dataFileName, Message.class);
	}

	
	@Override
	public Message add(Message item) {
		
		List<Message> list = get();
		
		List<Integer> idsMessages = list.stream()
										.map(m -> m.getId())
										.collect(Collectors.toList());
		
		Integer maxMessageId;
		
		try {
			
			maxMessageId = Collections.max(idsMessages) + 1;
			
		}catch(NoSuchElementException e) {
			maxMessageId = 1;
		}
		
		item.setId(maxMessageId);
		
		list.add(item);
		update(list);
		
		return item;
			
	}




	@Override
	public Message update(Message item) {
		
		List<Message> list = get();
		
		Message message = list.stream()
							  .filter(m -> m.getId().equals(item.getId()))
							  .findFirst().orElse(null);
		
		int toSet = list.indexOf(message);
		list.set(toSet, item);
		
		
		update(list);
		
		return message;
	}




	
}
