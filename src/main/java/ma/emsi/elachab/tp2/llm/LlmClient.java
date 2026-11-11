package ma.emsi.elachab.tp2.llm;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.Dependent;
import java.io.Serializable;

@Dependent
public class LlmClient implements Serializable {

    private String systemRole;
    private Assistant assistant;
    private ChatMemory chatMemory;

    public LlmClient() {
        // 1. Récupération de la clé API depuis l'environnement
        String apiKey = System.getenv("GEMINI_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("GEMINI_API_KEY");
        }

        // 2. Création du modèle Gemini
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-1.5-flash")
                .build();

        // 3. Configuration de la mémoire (fenêtre de 20 messages)
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        // 4. Création de l'assistant via AiServices
        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
    }

    public String envoyer(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "Veuillez saisir une question.";
        }
        return assistant.chat(prompt);
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
        if (systemRole != null && !systemRole.trim().isEmpty()) {
            // Réinitialise la mémoire et injecte le rôle système
            chatMemory.clear();
            chatMemory.add(new SystemMessage(systemRole));
        }
    }

    public String getSystemRole() {
        return systemRole;
    }
}