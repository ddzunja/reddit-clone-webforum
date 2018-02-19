package org.dzunja.projekat.webforum.service;

import java.util.List;


import org.dzunja.projekat.webforum.model.Subforum;


public class SubforumService extends AbstractService<Subforum> {

	public static final String dataFileName = "subforums.json";
	private ThreadService threadService;
	
	
	public SubforumService() {
		super(dataFileName, Subforum.class);
		threadService = new ThreadService();
		
	}

	@Override
	public Subforum add(Subforum item) {
		
		List<Subforum> list = get();
		
		boolean noMatch = list.stream()
							  .noneMatch(subforum -> subforum.getSubForumId().equals(item.getSubForumId()));
		if(noMatch) {
			list.add(item);
			update(list);
			return item;
		}
		
		return null;
	}

	@Override
	public Subforum update(Subforum item) {

		List<Subforum> list = get();
		
		Subforum removed = list.stream()
							   .filter(subforum -> subforum.getSubForumId().equals(item.getSubForumId()))
							   .findFirst().orElse(null);

		if(removed != null) {
			
			
			//list.remove(removed);
			//list.add(item);
			
			int index = list.indexOf(removed);
			list.set(index, item);
			update(list);

			return removed;	
		}

		return null;

	}
	
	
	public Subforum remove(Subforum item) {
		
		List<Subforum> list = get();
		
		Subforum removed = list.stream()
							   .filter(subforum -> subforum.getSubForumId().equals(item.getSubForumId()))
							   .findFirst().orElse(null);
		
		if(removed != null) {
			list.remove(removed);
			update(list);
			
			threadService.removeOnSubforum(item);

			return removed;	
		}
		
		return null;
		
	}
	
}

