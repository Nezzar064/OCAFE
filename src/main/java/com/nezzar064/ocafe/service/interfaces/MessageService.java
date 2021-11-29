package com.nezzar064.ocafe.service.interfaces;

import java.util.List;

//Unused but keeping for later use..
public interface MessageService<T, X, ID> {

    public T addMessage(T message, ID parentId);

    public T addReply(T message, X reply, ID parentId);

    public List<T> getMessages(ID parentId);

}
