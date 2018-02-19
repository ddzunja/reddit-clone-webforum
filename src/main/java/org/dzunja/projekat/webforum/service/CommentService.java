package org.dzunja.projekat.webforum.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.dzunja.projekat.webforum.model.Comment;
import org.dzunja.projekat.webforum.model.Link;
import org.dzunja.projekat.webforum.model.Thread;


public class CommentService extends AbstractService<Comment> {
	
	public static final String dataFileName = "comments.json";
	
	public CommentService() {
		super(dataFileName, Comment.class);
	}
	
	public List<Comment> getByResource(Thread thread){
		
		return get().stream()
					.filter(comment -> comment.getSubforumId().equals(thread.getSubforumId()))
   					.filter(comment -> comment.getThreadId().equals(thread.getNameId()))
   					.collect(Collectors.toList());
		
	}

	
	public Comment add(Comment item) {
		
		List<Comment> list = get();
		
		List<Comment> getCommentsOfThread = list.stream()
												.filter(comment -> comment.getSubforumId().equals(item.getSubforumId()))
												.filter(comment -> comment.getThreadId().equals(item.getThreadId()))
												.collect(Collectors.toList());
		
		List<Integer> idsComments = getCommentsOfThread.stream()
													   .map(comment -> comment.getCommentId())
													   .collect(Collectors.toList());
		
		Integer maxComment;
		
		try {
		
			maxComment = Collections.max(idsComments) + 1;
		
		}catch(NoSuchElementException e) {
			maxComment = 1;
		}
		
		
		item.setCommentId(maxComment);
		String link = item.getLinks().get(0).getLink() + '/' + maxComment;
		item.getLinks().set(0, new Link(link, "self"));
		
								  
		list.add(item);
		
		update(list);
		
		return item;
	}

	
	public Comment update(Comment item) {
		
		List<Comment> list = get();
		
		Comment toDelete = list.stream()
							   .filter(comment -> comment.getSubforumId().equals(item.getSubforumId()))
							   .filter(comment -> comment.getThreadId().equals(item.getThreadId()))
							   .filter(comment -> comment.getCommentId().equals(item.getCommentId()))
							   .findFirst()
							   .orElse(null);
		
		System.out.println(toDelete);
		
		if(toDelete != null) {
			
			int index = list.indexOf(toDelete);
			
			list.set(index, item);
			update(list);
			
			return item;
			
		}
		
		
		return null;
	}

	
	/*public Comment remove(Comment item) {
		
		List<Comment> list = get();
		
		Comment toDelete = list.stream()
							   .filter(comment -> comment.getSubforumId().equals(item.getSubforumId()))
							   .filter(comment -> comment.getThreadId().equals(item.getThreadId()))
							   .filter(comment -> comment.getCommentId().equals(item.getCommentId()))
							   .findFirst()
							   .orElse(null);
		
		if(toDelete != null) {
			list.remove(toDelete);
			return toDelete;
		}
		
		return null;
	}*/
	
	public Comment logicalRemove(Comment item) {
		
		List<Comment> list = get();
		
		Comment toDelete = list.stream()
							   .filter(comment -> comment.getSubforumId().equals(item.getSubforumId()))
							   .filter(comment -> comment.getThreadId().equals(item.getThreadId()))
							   .filter(comment -> comment.getCommentId().equals(item.getCommentId()))
							   .findFirst()
							   .orElse(null);
		
		if(toDelete != null) {
			toDelete.setDeleted(true);
			return toDelete;
		}
		
		return null;
	}
	
	public boolean removeOnThread(Thread item) {
		
		List<Comment> list = get();
		
		boolean retVal = list.removeIf(comment -> {
			
			return comment.getThreadId().equals(item.getNameId()) && comment.getSubforumId().equals(item.getSubforumId());
			
		});
		
		update(list);
		
		return retVal;
		
	}

}

