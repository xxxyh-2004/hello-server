package com.example.helloserver.service;

import com.example.helloserver.dto.ChatRequestDTO;
import com.example.helloserver.vo.ChatResponseVO;

public interface ChatService {
    ChatResponseVO chat(ChatRequestDTO requestDTO);
}
