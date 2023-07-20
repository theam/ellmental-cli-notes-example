import com.aallam.openai.client.OpenAI
import com.theagilemonkeys.ellmental.embeddingsmodel.openai.OpenAIEmbeddingsModel
import com.theagilemonkeys.ellmental.semanticsearch.SearchInput
import com.theagilemonkeys.ellmental.semanticsearch.SemanticSearch
import com.theagilemonkeys.ellmental.vectorstore.pinecone.PineconeVectorStore
import kotlinx.coroutines.runBlocking

fun embeddingsModel(): OpenAIEmbeddingsModel {
    val openaiToken = System.getenv("OPEN_AI_API_KEY")
    check(openaiToken != null) { "OPEN_AI_API_KEY environment variable is not set" }
    with(OpenAI(token = openaiToken)) {
        return OpenAIEmbeddingsModel()
    }
}

fun vectorStore(): PineconeVectorStore {
    val pineconeToken = System.getenv("PINECONE_API_KEY")
    check (pineconeToken != null) { "PINECONE_API_KEY environment variable is not set" }
    val pineconeUrl = System.getenv("PINECONE_URL")
    check (pineconeUrl != null) { "PINECONE_URL environment variable is not set" }
    return PineconeVectorStore(apiKey = pineconeToken, url = pineconeUrl)
}

fun semanticSearch(): SemanticSearch {
    val embeddingsModel = embeddingsModel()
    val vectorStore = vectorStore()
    with(embeddingsModel) {
        with(vectorStore) {
            return SemanticSearch()
        }
    }
}

data class Note(
    val id: Int,
    val content: String
)

suspend fun learn(semanticSearch: SemanticSearch, note: Note) {
    semanticSearch.learn(SearchInput(listOf(note.content)))
}

suspend fun search(semanticSearch: SemanticSearch, query: String): List<String> {
    return semanticSearch.search(query).entries.map {
        it.id.value
    }
}

fun main() = runBlocking {
    val semanticSearch = semanticSearch()
    println("Available commands: learn, search, quit\n")
    var shouldQuit = false
    while (!shouldQuit) {
        print("> ")
        when (readln()) {
            "help" -> {
                println("Available commands: learn, search, quit")
            }
            "learn" -> {
                println("Enter note content:")
                val txt = readln()
                val note = Note(
                    id = 1,
                    content = txt
                )
                learn(semanticSearch, note)
            }
            "search" -> {
                println("Enter query:")
                val query = readln()
                val results = search(semanticSearch, query)
                println(results)
            }
            "quit" -> {
                println("Bye!")
                shouldQuit = true
            }
            else -> {
                println("Invalid command")
            }
        }
    }
}

