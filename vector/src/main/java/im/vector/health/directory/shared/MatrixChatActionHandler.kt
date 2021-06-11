package im.vector.health.directory.shared

interface MatrixChatActionHandler {
    fun call(aIsVideoCall: Boolean, matrixID: String)
    fun startChat(matrixID: String)
}