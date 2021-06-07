package controller

import exception.NotFoundException
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class SpringBoot01Application

fun main(args: Array<String>) {
    runApplication<SpringBoot01Application>(*args)
}

@RestController
@RequestMapping("messages")
class MessageResource(val service: MessageService) {
    @GetMapping("all")
    fun get(): List<MessageService.Message> = service.getMessages()

    @GetMapping("{id}")
    fun getById(@PathVariable id: String): MessageService.Message = service.getMessageById(id)

    @PostMapping
    fun post(@RequestBody message: MessageService.Message) = service.post(message)

    @DeleteMapping
    fun clear() = service.clear()

    @DeleteMapping("{id}")
    fun deleteById(@PathVariable id: String) = service.deleteMessage(id)
}

@Service
class MessageService(val db: MessageRepository) {
    fun getMessages(): List<Message> = db.getAll()
    fun getMessageById(id: String): Message = db.findById(id).orElseThrow { throw NotFoundException() }
    fun post(message: Message) = db.save(message)
    fun clear() = db.deleteAll()
    fun deleteMessage(id: String) = db.deleteById(id)


interface MessageRepository : CrudRepository<Message, String> {
    @Query("select * from messages")
    fun getAll(): List<Message>

    //todo use select
//    @Query("select msg from messages where msg.id = :id")
//    fun getById(@Param("id") id: String): Message
}

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)