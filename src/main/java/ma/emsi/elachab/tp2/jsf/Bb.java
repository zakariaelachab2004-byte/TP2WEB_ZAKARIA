package ma.emsi.elachab.tp2.jsf;

import ma.emsi.elachab.tp2.llm.LlmClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("bb")
@ViewScoped
public class Bb implements Serializable {

    private String question;
    private String reponse;
    private String conversation = "";

    private String roleSysteme;
    private List<String> rolesSysteme;
    private boolean roleSystemeChangeable = true;

    @Inject
    private LlmClient llmClient;

    public Bb() {
        // Initialisation de la liste des rôles système suggérés
        rolesSysteme = new ArrayList<>();
        rolesSysteme.add("Tu es un assistant utile et poli.");
        rolesSysteme.add("Tu es un traducteur expert en anglais et en français.");
        rolesSysteme.add("Tu es un expert en intelligence artificielle et en Java.");
    }

    public String envoyer() {
        if (question == null || question.trim().isEmpty()) {
            return null;
        }

        // Si c'est le premier message, on fixe le rôle système choisi
        if (roleSystemeChangeable && roleSysteme != null && !roleSysteme.isEmpty()) {
            llmClient.setSystemRole(roleSysteme);
        }

        // Une fois la conversation commencée, on fige le choix du rôle système
        roleSystemeChangeable = false;

        // Appel au client LLM pour obtenir la réponse
        reponse = llmClient.envoyer(question);

        // Mise à jour de l'historique de conversation
        conversation += "Moi : " + question + "\n";
        conversation += "LLM : " + reponse + "\n\n";

        // Nettoyage de la question pour la suite
        question = "";

        return null;
    }

    public String nouveauChat() {
        // Réinitialisation complète pour un nouveau chat
        llmClient = new LlmClient();
        conversation = "";
        question = "";
        reponse = "";
        roleSystemeChangeable = true;
        roleSysteme = null;
        return null;
    }

    // --- Getters et Setters ---

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    public String getConversation() { return conversation; }
    public void setConversation(String conversation) { this.conversation = conversation; }

    public String getRoleSysteme() { return roleSysteme; }
    public void setRoleSysteme(String roleSysteme) { this.roleSysteme = roleSysteme; }

    public List<String> getRolesSysteme() { return rolesSysteme; }

    public boolean isRoleSystemeChangeable() { return roleSystemeChangeable; }
    public void setRoleSystemeChangeable(boolean roleSystemeChangeable) {
        this.roleSystemeChangeable = roleSystemeChangeable;
    }
}