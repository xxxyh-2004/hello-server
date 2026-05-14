package com.example.helloserver.controller;

import com.example.helloserver.common.Result;
import com.example.helloserver.dto.ChatRequestDTO;
import com.example.helloserver.service.ChatService;
import com.example.helloserver.vo.ChatResponseVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Result<ChatResponseVO> chat(@RequestBody ChatRequestDTO requestDTO) {
        ChatResponseVO responseVO = chatService.chat(requestDTO);
        return Result.success(responseVO);
    }
}
