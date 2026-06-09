package com.htdd.habittrackerdreamdeckdemo.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AiService {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String POLLINATIONS_URL = "https://text.pollinations.ai/openai";
    private static final String OPENAI_MODEL = "gpt-3.5-turbo";
    private static final Pattern JSON_BLOCK_PATTERN = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```");

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final String openAiApiKey;

    public AiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .requestFactory(createRequestFactory())
                .build();
        this.openAiApiKey = System.getenv("OPENAI_API_KEY");
    }

    public StepsSuggestionResponse generateStepsSuggestion(StepsSuggestionRequest request) {
        try {
            String prompt = buildStepsPrompt(request);
            String rawResponse = callAiModel(prompt);
            StepsSuggestionResponse parsed = parseStepsResponse(rawResponse);
            if (parsed != null) {
                parsed.setFromAi(true);
                return parsed;
            }
        } catch (Exception e) {
            log.warn("AI steps suggestion failed, using fallback: {}", e.getMessage());
        }
        return generateStepsFallback(request);
    }

    public SmartGoalSuggestionResponse generateSmartSuggestion(SmartGoalSuggestionRequest request) {
        try {
            String prompt = buildPrompt(request);
            String rawResponse = callAiModel(prompt);
            SmartGoalSuggestionResponse parsed = parseResponse(rawResponse);
            if (parsed != null) {
                parsed.setFromAi(true);
                return parsed;
            }
        } catch (Exception e) {
            log.warn("AI suggestion failed, using fallback: {}", e.getMessage());
        }
        return generateFallback(request);
    }

    private String callAiModel(String prompt) {
        if (openAiApiKey != null && !openAiApiKey.isBlank()) {
            return callOpenAi(prompt);
        }
        log.warn("OPENAI_API_KEY missing, using Pollinations fallback.");
        return callPollinations(prompt);
    }

    private String callOpenAi(String prompt) {
        OpenAiRequest body = new OpenAiRequest(
                OPENAI_MODEL,
                List.of(Map.of("role", "user", "content", prompt)),
                0.2,
                800
        );

        return restClient.post()
                .uri(OPENAI_URL)
                .header("Authorization", "Bearer " + openAiApiKey)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    private String callPollinations(String prompt) {
        PollinationsRequest body = new PollinationsRequest(
                "openai",
                true,
                List.of(Map.of("role", "user", "content", prompt))
        );

        return restClient.post()
                .uri(POLLINATIONS_URL)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    private record OpenAiRequest(String model, List<Map<String, String>> messages, Double temperature, Integer max_tokens) {}

    private String buildPrompt(SmartGoalSuggestionRequest request) {
        String category = request.getCategoryName() != null && !request.getCategoryName().isBlank()
                ? request.getCategoryName()
                : "загальна";
        String description = request.getDescription() != null && !request.getDescription().isBlank()
                ? request.getDescription()
                : "не вказано";
        String startDate = request.getStartDate() != null ? request.getStartDate() : "не вказано";
        String deadline = request.getDeadline() != null ? request.getDeadline() : "не вказано";

        return """
                Ти експерт з постановки SMART-цілей. Твоя задача — не пояснювати правила SMART, а перетворити реальні дані користувача на готовий SMART-заголовок, опис і конкретні кроки.
                
                Використовуй саме цей текст з назви та опису. Не змінюй мету і не додавай нові напрямки, яких немає в описі.
                
                Ось дані користувача, які треба врахувати повністю:
                - Назва: %s
                - Категорія: %s
                - Опис: %s
                - Дата початку: %s
                - Дедлайн: %s
                
                Відповідь має містити:
                1. SMART-заголовок у форматі "Досягти ... до ..." або "Отримати ... до ...".
                2. Опис, який точно відображає те, що користувач вже написав, але робить його більш SMART, чітким і мотиваційним.
                3. 4-6 конкретних кроків, які прямо ведуть до цієї саме мети. Кожен крок має бути прив'язаний до реального результату і дедлайну, якщо він є.
                
                Не використовуй загальні формулювання без конкретного контексту. Повторюй контекст користувача — назву та опис — у кроках, щоб вони були конкретними.
                Якщо опис короткий, все одно згенеруй кроки, які мають чіткий зв'язок з назвою та описом.
                
                Приклад бажаної відповіді:
                {"suggestedTitle":"Досягти запуску проекту до 30 червня","description":"Запустити проект на продакшн із повним CI/CD та документацією","steps":["Налаштувати CI/CD для проекту","Опублікувати проект на хостингу та перевірити роботу"]}
                
                Відповідь має бути ТІЛЬКИ валідним JSON без markdown:
                {"suggestedTitle":"...","description":"...","steps":["крок 1","крок 2"]}
                """.formatted(
                request.getTitle(),
                category,
                description,
                startDate,
                deadline
        );
    }

    private SmartGoalSuggestionResponse parseResponse(String rawResponse) throws Exception {
        if (rawResponse == null || rawResponse.isBlank()) {
            return null;
        }

        String content = unwrapOpenAiResponse(rawResponse.trim());
        String json = extractJson(content);
        JsonNode root = objectMapper.readTree(json);

        String suggestedTitle = root.path("suggestedTitle").asText(null);
        if (suggestedTitle == null || suggestedTitle.isBlank()) {
            suggestedTitle = root.path("title").asText(null);
        }

        String description = root.path("description").asText(null);
        JsonNode stepsNode = root.path("steps");

        if (suggestedTitle == null || suggestedTitle.isBlank() || description == null || description.isBlank() || !stepsNode.isArray() || stepsNode.isEmpty()) {
            return null;
        }

        List<String> steps = new ArrayList<>();
        stepsNode.forEach(node -> {
            String step = node.asText().trim();
            if (!step.isBlank()) {
                steps.add(step);
            }
        });

        if (steps.isEmpty()) {
            return null;
        }

        return SmartGoalSuggestionResponse.builder()
                .suggestedTitle(suggestedTitle.trim())
                .description(description.trim())
                .steps(steps)
                .fromAi(true)
                .build();
    }

    private String unwrapOpenAiResponse(String rawResponse) throws Exception {
        if (rawResponse.startsWith("{") && rawResponse.contains("\"choices\"")) {
            JsonNode root = objectMapper.readTree(rawResponse);
            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
            if (!contentNode.isMissingNode()) {
                return contentNode.asText();
            }
        }
        return rawResponse;
    }

    private String extractJson(String rawResponse) {
        Matcher matcher = JSON_BLOCK_PATTERN.matcher(rawResponse);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        int start = rawResponse.indexOf('{');
        int end = rawResponse.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return rawResponse.substring(start, end + 1);
        }

        return rawResponse;
    }

    private String buildStepsPrompt(StepsSuggestionRequest request) {
        String category = request.getCategoryName() != null && !request.getCategoryName().isBlank()
                ? request.getCategoryName()
                : "загальна";
        String description = request.getDescription() != null && !request.getDescription().isBlank()
                ? request.getDescription()
                : "не вказано";
        String startDate = request.getStartDate() != null ? request.getStartDate() : "не вказано";
        String deadline = request.getDeadline() != null ? request.getDeadline() : "не вказано";
        String existing = request.getExistingSteps() == null || request.getExistingSteps().isEmpty()
                ? "немає"
                : String.join(", ", request.getExistingSteps());

        return """
                Ти помічник з планування цілей. Твоя задача — створити конкретні кроки для цієї мети, використавши усі дані користувача з назви, опису і дедлайну.
                
                Використовуй саме цей текст з назви та опису мети. Не вигадуй нову мету — працюй тільки з наданою ціллю.
                
                Ціль: %s
                Категорія: %s
                Опис: %s
                Початок: %s
                Дедлайн: %s
                Вже існуючі кроки (не повторюй їх): %s
                
                Згенеруй 4-6 практичних кроків, які прямо ведуть до цієї мети. Кроки мають:
                - бути прив'язаними до конкретної назви та опису цілі
                - бути сформульованими як реальні дії, які можна виконати
                - враховувати дедлайн або часовий контекст, якщо він є
                - якщо можливо, частково повторювати слова з назви або опису, щоб показати зв'язок
                
                Не використовуйте загальні фрази без конкретики: «оцінити стан», «спланувати час», «відстежувати прогрес», «покращити навички». Кожен крок має виглядати як конкретне завдання, яке рухає прямо до цілі.
                
                Приклад бажаної відповіді для цілі «Задеплойти свій проект»: {"steps":["Налаштувати продакшн-сервер для проекту","Розгорнути проект на хостингу та перевірити роботу" ]}
                
                Відповідай ТІЛЬКИ валідним JSON без markdown:
                {"steps":["крок 1","крок 2"]}
                """.formatted(
                request.getTitle(),
                category,
                description,
                startDate,
                deadline,
                existing
        );
    }

    private StepsSuggestionResponse parseStepsResponse(String rawResponse) throws Exception {
        if (rawResponse == null || rawResponse.isBlank()) {
            return null;
        }

        String content = unwrapOpenAiResponse(rawResponse.trim());
        String json = extractJson(content);
        JsonNode root = objectMapper.readTree(json);
        JsonNode stepsNode = root.path("steps");

        if (!stepsNode.isArray() || stepsNode.isEmpty()) {
            return null;
        }

        List<String> steps = new ArrayList<>();
        stepsNode.forEach(node -> {
            String step = node.asText().trim();
            if (!step.isBlank()) {
                steps.add(step);
            }
        });

        if (steps.isEmpty()) {
            return null;
        }

        return StepsSuggestionResponse.builder()
                .steps(steps)
                .fromAi(true)
                .build();
    }

    private StepsSuggestionResponse generateStepsFallback(StepsSuggestionRequest request) {
        String title = request.getTitle() != null ? request.getTitle().trim() : "ціль";
        String description = request.getDescription() != null && !request.getDescription().isBlank()
                ? request.getDescription().trim() : null;
        String deadlineText = request.getDeadline() != null && !request.getDeadline().isBlank()
                ? " до " + request.getDeadline() : "";

        List<String> steps = new ArrayList<>();
        steps.add("Оцінити поточний прогрес по цілі «" + title + "»" + deadlineText);
        if (description != null) {
            steps.add("Визначити найближче практичне завдання з опису: «" + description + "»");
        } else {
            steps.add("Визначити найближче міні-завдання на сьогодні");
        }
        steps.add("Запланувати цей крок у календарі та зробити його найближчим часом");
        steps.add("Після виконання перевірити результат і скоригувати план для «" + title + "»");

        return StepsSuggestionResponse.builder()
                .steps(steps)
                .fromAi(false)
                .build();
    }

    private SmartGoalSuggestionResponse generateFallback(SmartGoalSuggestionRequest request) {
        String title = request.getTitle() != null ? request.getTitle().trim() : "ціль";
        String description = request.getDescription() != null && !request.getDescription().isBlank()
                ? request.getDescription().trim() : "досягти цілі за SMART-принципом";
        String deadlineText = request.getDeadline() != null && !request.getDeadline().isBlank()
                ? " до " + request.getDeadline() : "";

        String suggestedTitle = "Досягти «" + title + "'" + deadlineText;
        String fallbackDescription = "Завершити «" + title + "» із фокусом на: " + description + ".";

        List<String> steps = List.of(
                "Визначити перший конкретний результат для «" + title + "»",
                "Розбити цей результат на практичні завдання, що відповідають опису",
                "Запланувати перший крок у календарі та виконати його до " + (request.getDeadline() != null && !request.getDeadline().isBlank() ? request.getDeadline() : "найближчих днів"),
                "Після виконання перевірити проміжний результат і скоригувати план",
                "Продовжити роботу над наступним кроком, який найбільше наближає до «" + title + "»"
        );

        return SmartGoalSuggestionResponse.builder()
                .suggestedTitle(suggestedTitle)
                .description(fallbackDescription)
                .steps(steps)
                .fromAi(false)
                .build();
    }

    private static SimpleClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(60_000);
        return factory;
    }
}
