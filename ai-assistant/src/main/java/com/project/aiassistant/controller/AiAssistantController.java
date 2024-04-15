package com.project.aiassistant.controller;

import com.project.aiassistant.dto.MessageDTO;
import com.project.aiassistant.factory.AiAssistantFactory;
import com.project.aiassistant.factory.ContentRetrieverFactory;
import com.project.aiassistant.factory.DocumentAssistantFactory;
import com.project.aiassistant.factory.EmbeddingFactory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class AiAssistantController {

    @Value("${langchain.huggingface.accessToken}")
    private String token;

    @PostMapping
    public ResponseEntity chat(@RequestBody MessageDTO messageDTO) {
        ChatLanguageModel chatModel = AiAssistantFactory.createLocalChatModel();
        var embeddingModel = EmbeddingFactory.createEmbeddingModel();
        var embeddingStore = EmbeddingFactory.createEmbeddingStore();
        var fileContentRetriever = ContentRetrieverFactory.createFileContentRetriever(
                embeddingModel,
                embeddingStore,
                "movies.txt");

        var documentAssistant = new DocumentAssistantFactory(chatModel, fileContentRetriever);
        String response = documentAssistant.chat(messageDTO.message());
        return ResponseEntity.ok().body(response);
    }
}
