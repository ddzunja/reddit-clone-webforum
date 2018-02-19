package org.dzunja.projekat.webforum.service;

import java.util.List;
import java.util.stream.Collectors;

import org.dzunja.projekat.webforum.model.Subforum;
import org.dzunja.projekat.webforum.model.Thread;

public class ThreadService extends AbstractService<Thread> {

	public static final String dataFileName = "threads.json";
	private CommentService commentService;
	
	public ThreadService() {
		super(dataFileName, Thread.class);
		commentService = new CommentService();
	}
	
	public List<Thread> getByResource(Subforum subforum){
		
		List<Thread> list = get();
		
		if(list != null) {
		
			return list.stream()
						.filter(thread -> thread.getSubforumId().equals(subforum.getSubForumId()))
						.collect(Collectors.toList());
		}
		
		return null;
	}
	@Override
	public Thread add(Thread item) {
		
		List<Thread> list = get();
		
		boolean noMatch = list.stream()
							  .filter(thread -> thread.getSubforumId().equals(item.getSubforumId()))
							  .noneMatch(thread -> thread.getNameId().equals(item.getNameId()));
		if(noMatch) {
			list.add(item);
			update(list);
			return item;
		}
		
		return null;
	}

	@Override
	public Thread update(Thread item) {
		
		List<Thread> list = get();
		
		Thread removed = list.stream()
							 .filter(thread -> thread.getSubforumId().equals(item.getSubforumId()))
							 .filter(thread -> thread.getNameId().equals(item.getNameId()))
							 .findFirst().orElse(null);
		
		if(removed != null) {
			//list.remove(removed);
			//list.add(item);
			
			int index = list.indexOf(removed);
			list.set(index, item);
			
			update(list);
			
			return item;	
		}
		
		return null;
	}
	
	
	public Thread remove(Thread item) {
		
		List<Thread> list = get();
		
		Thread removed = list.stream()
							 .filter(thread -> thread.getSubforumId().equals(item.getSubforumId()))
							 .filter(thread -> thread.getNameId().equals(item.getNameId()))
							 .findFirst().orElse(null);
		
		if(removed != null) {
			list.remove(removed);
			update(list);
			
			commentService.removeOnThread(item);
			
			return removed;	
		}
		
		return null;
	}
	
	public boolean removeOnSubforum(Subforum item) {
		
		List<Thread> list = get();
		
		boolean retVal = list.removeIf(thread -> {
			
			commentService.removeOnThread(thread);
			return thread.getSubforumId().equals(item.getSubForumId());
			
		});
		
		update(list);
		
		return retVal;
		
	}


	
}
